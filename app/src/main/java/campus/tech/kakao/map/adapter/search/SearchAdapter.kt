package campus.tech.kakao.map.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ListItemBinding
import campus.tech.kakao.map.viewmodel.OnSearchItemClickListener
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel

class SearchAdapter(
    private val onSearchItemClickListener: OnSearchItemClickListener
) :
    ListAdapter<LocalUiModel, SearchAdapter.SearchViewHolder>(SearchDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindViewHolder(item, onSearchItemClickListener)
    }

    class SearchViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViewHolder(
            localUiModel: LocalUiModel,
            onSearchItemClickListener: OnSearchItemClickListener
        ) {
            binding.localUiModel = localUiModel
            binding.listener = onSearchItemClickListener
            binding.executePendingBindings()
        }
    }
}

class SearchDiffCallback : DiffUtil.ItemCallback<LocalUiModel>() {
    override fun areItemsTheSame(oldItem: LocalUiModel, newItem: LocalUiModel) =
        oldItem.place == newItem.place

    override fun areContentsTheSame(oldItem: LocalUiModel, newItem: LocalUiModel) =
        oldItem == newItem
}
