package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemMymyBinding
import com.example.myapplication.entity.Mymy

class Mymyadapter (
    private val dataset: MutableList<Mymy>
) : RecyclerView.Adapter<Mymyadapter.CustomViewHolder>() {

    inner class CustomViewHolder(val view: ItemMymyBinding)
        :RecyclerView.ViewHolder(view.root) {

            fun bindData(item: Mymy) {
                view.title.text = item.title
                view.description.text = item.description
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val binding = ItemMymyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CustomViewHolder,
        position: Int
    ) {
        val data = dataset[position]
        holder.bindData(data)
    }

    override fun getItemCount() = dataset.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Mymy>) {
        dataset.clear()
        dataset.addAll(newData)
        notifyDataSetChanged()
    }

}