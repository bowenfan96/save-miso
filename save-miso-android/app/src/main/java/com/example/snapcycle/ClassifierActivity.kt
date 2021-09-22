/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * MODIFICATIONS Copyright (C) 2021 The Save Miso Team
 */
package com.example.snapcycle

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ImageReader
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.NavController
import com.example.snapcycle.cameraHelpers.BorderedText
import com.example.snapcycle.cameraHelpers.ImageUtils
import com.example.snapcycle.cameraHelpers.Logger
import com.example.snapcycle.tflite.Classifier
import com.example.snapcycle.tflite.Classifier.Recognition
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class ClassifierActivity : CameraActivity(), ImageReader.OnImageAvailableListener {
    private var rgbFrameBitmap: Bitmap? = null
    private var croppedBitmap: Bitmap? = null
    private var cropCopyBitmap: Bitmap? = null
    private var lastProcessingTimeMs: Long = 0
    private var sensorOrientation: Int? = null
    private var classifier: Classifier? = null
    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null
    private var borderedText: BorderedText? = null

    // DECLARE CONFIDENCE THRESHOLD TO QUIT SCANNING AND CALL THE SERVER
    val thresholdConfidence = 0.75f

    var navc: NavController? = null

    override fun getLayoutId(): Int {
        return R.layout.tfe_camera_connection_fragment
    }

    override fun getDesiredPreviewFrameSize(): Size {
        return DESIRED_PREVIEW_SIZE
    }

    public override fun onPreviewSizeChosen(size: Size, rotation: Int) {
        val textSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, resources.displayMetrics
        )
        borderedText = BorderedText(textSizePx)
        borderedText!!.setTypeface(Typeface.MONOSPACE)
        recreateClassifier(model, device, numThreads)
        if (classifier == null) {
            LOGGER.e("No classifier on preview!")
            return
        }
        previewWidth = size.width
        previewHeight = size.height
        sensorOrientation = rotation - screenOrientation
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation)
        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight)
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
        croppedBitmap = Bitmap.createBitmap(
            classifier!!.imageSizeX, classifier!!.imageSizeY, Bitmap.Config.ARGB_8888
        )
        frameToCropTransform = ImageUtils.getTransformationMatrix(
            previewWidth,
            previewHeight,
            classifier!!.imageSizeX,
            classifier!!.imageSizeY,
            sensorOrientation!!,
            MAINTAIN_ASPECT
        )
        cropToFrameTransform = Matrix()
        frameToCropTransform?.invert(cropToFrameTransform)
    }

    var elapsedTime: Long = 0
    override fun onClick(p0: View?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun processImage() {
        rgbFrameBitmap!!.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight)
        val canvas = Canvas(croppedBitmap!!)
        canvas.drawBitmap(rgbFrameBitmap!!, frameToCropTransform!!, null)

        val start = System.currentTimeMillis()

        if (classifier != null) {
            val results = classifier!!.recognizeImage(croppedBitmap)
            LOGGER.v("Detect: %s", results)
//            cropCopyBitmap = Bitmap.createBitmap(croppedBitmap!!)

            runOnUiThread {
                showResultsInBottomSheet(results)
                // return top result when it stays stable for 1 second, and confidence above 75%
                val end = System.currentTimeMillis()
                elapsedTime += (end - start)
                if (elapsedTime > 1000) {

                    val path: String = this.cacheDir.toString()
                    Log.d("CAMERA", path)
                    var fOut: OutputStream? = null
                    val file = File(
                        path + "scannedItemImage.jpg"
                    )

                    fOut = FileOutputStream(file, false)

                    croppedBitmap!!.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        fOut
                    )

                    fOut.flush()
                    fOut.close()

                    MediaStore.Images.Media.insertImage(
                        contentResolver,
                        file.absolutePath,
                        file.name,
                        file.name
                    )

//                    FileOutputStream(this.filesDir.absolutePath + "scannedItemImage", false).use { out ->
//                        cropCopyBitmap!!.compress(
//                            Bitmap.CompressFormat.JPEG,
//                            90,
//                            out
//                        ) // bmp is your Bitmap instance
//
//                        Log.d("CAMERA", this.filesDir.absolutePath)
//                    }

                    returnTopResultToServer(results[0])
                }
            }
        }
        readyForNextImage()
    }

    private fun returnTopResultToServer(recognition: Recognition?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("FromClassifierActivity", 1)
        if (recognition != null) {
            if (recognition.confidence != null && recognition.title != null) {
                val topResult = recognition.title
                if (recognition.confidence > thresholdConfidence) {
                    if (topResult == "Non-recyclable") {
                        Toast.makeText(
                            this,
                            "Can't detect item, please input manually",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, topResult + " scanned", Toast.LENGTH_LONG).show()
                    }
                    intent.putExtra("ItemDetected", 1)
                    intent.putExtra("ObjectName", topResult)
                    this.finish()
                    startActivity(intent)
                } else {
                    // threshold less than confidence interval
                    Toast.makeText(this, "Unsure of item, please input manually", Toast.LENGTH_LONG)
                        .show()
                    intent.putExtra("ItemDetected", 0)
                    intent.putExtra("ObjectName", topResult)
                    this.finish()
                    startActivity(intent)
                }
            }
        }
    }

    override fun onInferenceConfigurationChanged() {
        if (croppedBitmap == null) {
            // Defer creation until we're getting camera frames.
            return
        }
        val device = device
        val model = model
        val numThreads = numThreads
        runInBackground { recreateClassifier(model, device, numThreads) }
    }

    private fun recreateClassifier(
        model: Classifier.Model,
        device: Classifier.Device,
        numThreads: Int
    ) {
        if (classifier != null) {
            LOGGER.d("Closing classifier.")
            classifier!!.close()
            classifier = null
        }
        if (device == Classifier.Device.GPU && model == Classifier.Model.QUANTIZED) {
            LOGGER.d("Not creating classifier: GPU doesn't support quantized models.")
            runOnUiThread {
                Toast.makeText(
                    this,
                    "GPU does not yet supported quantized models.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            return
        }
        try {
            LOGGER.d(
                "Creating classifier (model=%s, device=%s, numThreads=%d)",
                model,
                device,
                numThreads
            )
            classifier = Classifier.create(this, model, device, numThreads)
        } catch (e: IOException) {
            LOGGER.e(e, "Failed to create classifier.")
        }
    }

    companion object {
        private val LOGGER = Logger()
        private const val MAINTAIN_ASPECT = true
        private val DESIRED_PREVIEW_SIZE = Size(640, 480)
        private const val TEXT_SIZE_DIP = 10f
    }
}