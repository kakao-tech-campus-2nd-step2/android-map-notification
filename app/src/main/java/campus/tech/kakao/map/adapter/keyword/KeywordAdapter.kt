package campus.tech.kakao.map.adapter.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ListKeywordBinding
import campus.tech.kakao.map.viewmodel.OnKeywordItemClickListener

class KeywordAdapter(private val onKeywordItemClickListener: OnKeywordItemClickListener) :
    ListAdapter<String, KeywordAdapter.KeywordViewHolder>(
        KeywordDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListKeywordBinding.inflate(inflater, parent, false)
        return KeywordViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bindViewHolder(getItem(position), onKeywordItemClickListener)
    }

    class KeywordViewHolder(private val binding: ListKeywordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViewHolder(
            keyword: String,
            onKeywordItemClickListener: OnKeywordItemClickListener
        ) {
            binding.keyword = keyword
            binding.listener = onKeywordItemClickListener
            binding.executePendingBindings()
        }
    }
}

class KeywordDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}