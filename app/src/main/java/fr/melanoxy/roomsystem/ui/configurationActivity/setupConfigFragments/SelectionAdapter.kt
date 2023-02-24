package fr.melanoxy.roomsystem.ui.configurationActivity.setupConfigFragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.melanoxy.roomsystem.databinding.ConfigurationItemBinding

class SelectionAdapter: ListAdapter<SelectionStateItem, SelectionAdapter.ViewHolder>(
    SelectionDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ConfigurationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ConfigurationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SelectionStateItem) {
            binding.configurationItemTextView.text = item.selectionField
            binding.configurationItemTextView.setOnClickListener {
                item.onSelectionClicked.invoke()
            }
        }
    }

    object SelectionDiffCallback : DiffUtil.ItemCallback<SelectionStateItem>() {
        override fun areItemsTheSame(oldItem: SelectionStateItem, newItem: SelectionStateItem): Boolean = oldItem.selectionId == newItem.selectionId

        override fun areContentsTheSame(oldItem: SelectionStateItem, newItem: SelectionStateItem): Boolean = oldItem == newItem
    }
}