package com.pnam.sqllitedemo

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val efab: ExtendedFloatingActionButton by lazy { findViewById(R.id.efab) }
    private val scroll: NestedScrollView by lazy { findViewById(R.id.scroll) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }
    private val nameEditText: EditText by lazy { findViewById(R.id.name) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.adapter = adapter
        scroll.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY < oldScrollY) {
                if (scrollY < 200) {
                    efab.extend()
                }
            } else {
                efab.shrink()
            }
        }
        efab.setOnClickListener {
            if (nameEditText.text.isNotEmpty()) {
                val selected = viewModel.selected.value
                if (selected == null) {
                    viewModel.insert(nameEditText.text.toString())
                    adapter.notifyItemInserted(viewModel.list.size)
                } else {
                    selected.name = nameEditText.text.toString()
                    viewModel.update(selected)
                    adapter.notifyItemChanged(viewModel.list.indexOfFirst { e -> e.id == selected.id })
                }
                nameEditText.setText("")
                efab.text = "Add"
                viewModel.selected.value = null
            } else {
                Toast.makeText(this, "Name need not empty", Toast.LENGTH_SHORT).show()
            }
        }
        efab.setOnLongClickListener {
            viewModel.findTestModelsByName(nameEditText.text.toString())
            adapter.notifyDataSetChanged()
            true
        }
        nameEditText.addTextChangedListener {
            if(it?.isEmpty() != false) {
                viewModel.onCreate()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.selected.observe(this) { selectedModel ->
            selectedModel?.let {
                nameEditText.setText(selectedModel.name)
            }
        }
    }

    private val adapter: RecyclerView.Adapter<MainViewHolder> by lazy {
        object : RecyclerView.Adapter<MainViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): MainViewHolder {
                return MainViewHolder(parent)
            }

            override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
                val testModels = viewModel.list[position]
                holder.bind(
                    testModels,
                    deleteClick = {
                        viewModel.delete(testModels.id)
                        adapter.notifyItemRemoved(position)
                    },
                    itemClick = {
                        efab.text = "Update"
                        viewModel.selected.postValue(testModels)
                    }
                )
            }

            override fun getItemCount(): Int {
                return viewModel.list.size
            }
        }
    }
}