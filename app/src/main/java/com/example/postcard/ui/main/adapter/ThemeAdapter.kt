package com.example.postcard.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.postcard.databinding.CardItemBinding
import com.example.postcard.ui.main.model.ExampleModel

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
            itemBinding.cardItemTitle.text = item.title
            itemBinding.cardItemDescription.text = item.text
            itemBinding.cardItemImage.setImageResource(item.imageId)
        }
    }
}