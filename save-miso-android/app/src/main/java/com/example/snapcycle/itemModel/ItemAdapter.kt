package com.example.snapcycle.itemModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.snapcycle.R

class ItemAdapter(var tasks: ItemList) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox
        val carbonTextView: TextView
        val quantityTextView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            checkBox = view.findViewById(R.id.todoCheckBox)
            carbonTextView = view.findViewById(R.id.carbonUnits)
            quantityTextView = view.findViewById(R.id.recyclingItemQuantity)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_new_layout, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val layoutPosition = viewHolder.layoutPosition
        val adapterPosition = viewHolder.adapterPosition
        // Checkbox text set to description
        viewHolder.checkBox.text = tasks.list[layoutPosition].description

        // Set subscript on "CO2"
        var textCO2 = tasks.list[layoutPosition].carbon.toString() + "g of CO₂"

//        var spannableStringBuilder = SpannableStringBuilder(textCO2)
//        val subscriptSpan = SubscriptSpan()
//        spannableStringBuilder.setSpan(
//            subscriptSpan,
//            textCO2.indexOf("CO2") + "CO".length,
//            textCO2.indexOf("CO2") + "CO2".length,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//        // set superscript text smaller
//        val relativeSizeSpan = RelativeSizeSpan(.6f)
//        spannableStringBuilder.setSpan(
//            relativeSizeSpan,
//            textCO2.indexOf("CO2") + "CO".length,
//            textCO2.indexOf("CO2") + "CO2".length,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        viewHolder.carbonTextView.text = textCO2
        viewHolder.quantityTextView.text = "(${tasks.list[layoutPosition].count})"

        viewHolder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            tasks.list[viewHolder.adapterPosition].status = isChecked
            Log.d(
                "ITEMADAPTER",
                "Item adapter: checkbox at position $position is checked, value $isChecked"
            )
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = tasks.list.size

}