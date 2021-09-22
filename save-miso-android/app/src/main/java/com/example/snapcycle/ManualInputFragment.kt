package com.example.snapcycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ManualInputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManualInputFragment : Fragment() {

    var navc: NavController? = null
    val dataViewModel: DataViewModel by activityViewModels()
    lateinit var typeSpinner: Spinner
    lateinit var countField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)
        var itemType: String?
        var itemCount: Int?

        typeSpinner = view.findViewById(R.id.type_spinner)
        countField = view.findViewById<EditText>(R.id.item_count)

        ArrayAdapter.createFromResource(
            view.context,
            R.array.recycleables_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            typeSpinner.adapter = adapter
        }

        val butAddItem: Button = view.findViewById(R.id.next)
        butAddItem.setOnClickListener {
            // Save values from input fields
            itemType = typeSpinner.selectedItem.toString()
            itemCount = Integer.parseInt(countField.text.toString())

            if (itemType == null || itemCount == null) {
                Toast.makeText(activity, "Please input item type and quantity!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                dataViewModel.setRecycleItemType(itemType!!)
                dataViewModel.setRecycleItemCount(itemCount!!)
                navc?.navigate(R.id.action_manualInputFragment_to_confirmAddFragment)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ManualInputFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManualInputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}