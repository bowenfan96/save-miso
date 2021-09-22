package com.example.snapcycle

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ToMapsDialogFragment : DialogFragment() {
    internal lateinit var listener: NoticeDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it, R.style.AlertDialogCustom)

            builder.setMessage(
                "Blue bin location set to current location!\n" +
                        "Navigate to a Maps app to see your registered blue bin location?"
            )
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(this)
                    }
                )
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(this)
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}