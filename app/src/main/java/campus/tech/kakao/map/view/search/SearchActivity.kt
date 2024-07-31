package campus.tech.kakao.map.view.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.R
import campus.tech.kakao.map.adapter.keyword.KeywordAdapter
import campus.tech.kakao.map.adapter.search.SearchAdapter
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.viewmodel.DualViewModelClickListener
import campus.tech.kakao.map.viewmodel.keyword.KeywordViewModel
import campus.tech.kakao.map.viewmodel.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val keywordViewModel: KeywordViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var keywordAdapter: KeywordAdapter

    val searchResultViewVisible: LiveData<Int> get() = _searchResultViewVisible
    private val _searchResultViewVisible = MutableLiveData<Int>()
    val emptyViewVisible: LiveData<Int> get() = _emptyViewVisible
    private val _emptyViewVisible = MutableLiveData<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupSearchResultViewRecyclerView()
        setupKeywordHistoryView()
        setupEditText()
        observeSearchResult()
        observeKeywordHistory()
        observeLocalInformation()
    }

    fun clearSearchKeyword() {
        binding.etSearchTextInput.text.clear()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.searchActivity = this
        binding.lifecycleOwner = this
    }

    private fun setupSearchResultViewRecyclerView() {
        searchAdapter = SearchAdapter(DualViewModelClickListener(keywordViewModel, searchViewModel))
        binding.searchResultView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }

        val dividerItemDecoration = DividerItemDecoration(
            binding.searchResultView.context,
            (binding.searchResultView.layoutManager as LinearLayoutManager).orientation
        )
        binding.searchResultView.addItemDecoration(dividerItemDecoration)
    }

    private fun setupKeywordHistoryView() {
        keywordAdapter =
            KeywordAdapter(
                keywordViewModel
            )
        binding.keywordHistoryView.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = keywordAdapter
        }
        lifecycleScope.launch {
            keywordViewModel.readKeywordHistory()
        }
    }

    private fun setupEditText() {
        val handler = Handler(Looper.getMainLooper())
        var runnable: Runnable? = null
        binding.etSearchTextInput.setOnFocusChangeListener { _, _ ->
            lifecycleScope.launch {
                keywordViewModel.readKeywordHistory()
            }
        }

        binding.etSearchTextInput.doAfterTextChanged {
            runnable?.let { handler.removeCallbacks(it) }

            runnable = Runnable {
                binding.etSearchTextInput.text.toString().let {
                    searchViewModel.searchLocationData(it)
                }
            }

            handler.postDelayed(runnable!!, 500)
        }
    }

    private fun observeSearchResult() {
        searchViewModel.items.observe(this) {
            if (it.isEmpty()) {
                _searchResultViewVisible.value = View.GONE
                _emptyViewVisible.value = View.VISIBLE
            } else {
                searchAdapter.submitList(it)
                _searchResultViewVisible.value = View.VISIBLE
                _emptyViewVisible.value = View.GONE
            }
        }
    }

    private fun observeKeywordHistory() {
        keywordViewModel.keyword.observe(this) {
            keywordAdapter.submitList(it)
        }

        keywordViewModel.keywordClicked.observe(this) {
            binding.etSearchTextInput.setText(it)
        }
    }

    private fun observeLocalInformation() {
        searchViewModel.localInformation.observe(this) {
            val intent = Intent().apply {
                putExtra("selected_location", it)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}

