package com.example.facefitar.ui.camera

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.facefitar.data.model.FilterModel
import com.example.facefitar.databinding.ItemFilterBinding

class FilterAdapter(
    private val filters: List<FilterModel>,
    private val onClick: (FilterModel) -> Unit
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {


    private var selectedPosition = 0

    inner class FilterViewHolder(val binding: ItemFilterBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {

        val binding = ItemFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {

        val filter = filters[position]

        holder.binding.filterImage.setImageResource(filter.icon)
        holder.binding.filterName.text = filter.name

        // highlight selected filter
        holder.binding.root.alpha =
            if (position == selectedPosition) 1f else 0.5f

        holder.binding.root.setOnClickListener {

            val previous = selectedPosition
            selectedPosition = position

            notifyItemChanged(previous)
            notifyItemChanged(position)

            onClick(filter)
        }
    }

    override fun getItemCount(): Int = filters.size


}
