package com.example.postcard.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postcard.databinding.CardItemBinding

class ThemeAdapter(private val itemsList: List<ExampleModel>) :
    RecyclerView.Adapter<ThemeAdapter.ThemeItemHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ThemeItemHolder {
        val itemBinding =
            CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemeItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ThemeItemHolder, position: Int) {
        val paymentBean: ExampleModel = itemsList[position]
        holder.bind(paymentBean)
    }

    override fun getItemCount(): Int = itemsList.size

    class ThemeItemHolder(private val itemBinding: CardItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: ExampleModel) {
            itemBinding.ItemTitle.text = item.title
            itemBinding.ItemDescription.text = item.text
            itemBinding.ItemImage.setImageResource(item.imageId)
        }
    }
}