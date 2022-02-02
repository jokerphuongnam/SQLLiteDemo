package com.pnam.sqllitedemo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip

class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
) {
    private val idTextView: TextView by lazy { itemView.findViewById(R.id.id) }
    private val nameTextView: TextView by lazy { itemView.findViewById(R.id.name) }
    private val deleteChip: Chip by lazy { itemView.findViewById(R.id.delete) }
    fun bind(item: TestModel, deleteClick: () -> Unit, itemClick: () -> Unit) {
        idTextView.text = item.id.toString()
        nameTextView.text = item.name
        deleteChip.setOnClickListener {
            AlertDialog.Builder(itemView.context)
                .setMessage("Bạn có muốn xóa phần tử này?")
                .setPositiveButton("OK") { dialog, id ->
                    deleteClick()
                }
                .setNegativeButton("Cancel") { dialog, id ->

                }
                .create()
                .show()
        }
        itemView.setOnClickListener {
            itemClick()
        }
    }
}