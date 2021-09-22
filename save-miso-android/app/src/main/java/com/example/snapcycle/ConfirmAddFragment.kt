package com.example.snapcycle

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.snapcycle.itemModel.ItemEntry
import com.example.snapcycle.server.ServerAPI
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmAddFragment : Fragment() {

    val dataViewModel: DataViewModel by activityViewModels()
    var navc: NavController? = null
    lateinit var updateMsg: String
    private val TAG = "ConfirmAddActivity"

    lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)
        val textDescription: TextView = view.findViewById(R.id.descriptionRec)

        var itemType = activity?.intent?.getStringExtra("ObjectName")
        var itemCount: Int = 1 // Model can only detect one item at a time

        // If this is null, it means that we were directed here from ManualInput
        if (itemType == null || itemType == "Non-recyclable") {
            itemType = dataViewModel.getRecycleItemType()
            itemCount = dataViewModel.getRecycleItemCount()
        }

        Toast.makeText(this.context, "Querying server for points, please wait", Toast.LENGTH_LONG)
            .show()

        var itemCarbon: Int = 0

        // Display image of scanned item
        val path = activity?.cacheDir.toString()
        Log.d("CONFIRMADD", path)
        val imgFile = File(path + "scannedItemImage.jpg")
        if (imgFile.exists()) {
            val imgBMP: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            val scannedItem = getView()?.findViewById(R.id.defaultRecyclable) as ImageView
            scannedItem.setImageBitmap(imgBMP)
        }

        // Update text

        // prepare to update local userscore
        prefs = requireContext().getSharedPreferences("saveMiso_settings", 0)

        // Query server for carbon value of item
        ServerAPI
            .service
            .getCarbon(itemType)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("---TTTT :: GET Throwable EXCEPTION:: " + t.message)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.string()
                        itemCarbon = msg.toString().toInt()
                        Log.e(
                            "ConfirmAddFragment",
                            "Item Carbon retrieved from server: $itemCarbon"
                        )

                        // Add to list of recycleables and update file storage
                        dataViewModel.recycleablesList.add(
                            ItemEntry(
                                itemType.toString(),
                                itemCount,
                                false,
                                itemCarbon * itemCount
                            )
                        )
                        Log.e(
                            "ConfirmAddFragment",
                            "Item added to dataviewmodel with carbon $itemCarbon"
                        )
                        textDescription.text =
                            "You will save ${itemCarbon}g CO₂ per ${itemType}, ${itemCarbon * itemCount}g in total!"

                        val filename = "datafile"
                        val file = File(requireContext().filesDir, filename)
                        try {
                            dataViewModel.recycleablesList.storeToFile(file)
                        } catch (e: Exception) {
                            Log.e("ConfirmAddFragment", "Error writing recycleables list to file")
                            e.printStackTrace()
                        }

                    } else {
                        Log.d("ConfirmAddFragment", "FAILED RESPONSE TO SERVER")
                    }
                }
            })


        // Clear the intent so that navigation doesn't cause action to be repeated
        activity?.intent?.removeExtra("ObjectName")
        activity?.intent?.removeExtra("ItemDetected")
        activity?.intent?.removeExtra("FromClassifierActivity")

        // navigation to add another item
        val butAddItemAgain: Button = view.findViewById(R.id.butAddItemAgain)
        butAddItemAgain.setOnClickListener {
            navc?.navigate(R.id.action_confirmAddFragment_to_manualInputFragment)
        }

        // navigation to revise item parameters
        val butEditItem: Button = view.findViewById(R.id.butEditItem)
        butEditItem.setOnClickListener {
            // if edit is pressed then we should remove the latest entry
            dataViewModel.recycleablesList.list.removeAt(dataViewModel.recycleablesList.list.size - 1)
            navc?.navigate(R.id.action_confirmAddFragment_to_manualInputFragment)
        }

    }
}