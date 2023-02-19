package fr.melanoxy.roomsystem.ui.modulesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.melanoxy.roomsystem.databinding.ModuleItemBinding

class ModulesAdapter: ListAdapter<ModuleViewStateItem, ModulesAdapter.ViewHolder>(ModulesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ModuleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ModuleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ModuleViewStateItem) {
            Glide.with(binding.moduleItemImage)
                .load(item.moduleImageUrl)
                .fitCenter()
                .into(binding.moduleItemImage)
            binding.moduleItemText.text = item.moduleName
            binding.moduleItemImage.setOnClickListener {
                item.onModuleClicked.invoke()
            }
        }
    }

    object ModulesDiffCallback : DiffUtil.ItemCallback<ModuleViewStateItem>() {
        override fun areItemsTheSame(oldItem: ModuleViewStateItem, newItem: ModuleViewStateItem): Boolean = oldItem.moduleId == newItem.moduleId

        override fun areContentsTheSame(oldItem: ModuleViewStateItem, newItem: ModuleViewStateItem): Boolean = oldItem == newItem
    }
}