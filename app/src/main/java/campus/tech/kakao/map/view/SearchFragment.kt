package campus.tech.kakao.map.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.viewModel.MainViewModel
import campus.tech.kakao.map.view.adapter.AdapterCallback
import campus.tech.kakao.map.view.adapter.DocumentAdapter
import campus.tech.kakao.map.view.adapter.WordAdapter
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.data.document.Document
import campus.tech.kakao.map.data.searchWord.SearchWord
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), AdapterCallback {

	private val model: MainViewModel by activityViewModels()
	private lateinit var documentAdapter: DocumentAdapter
	private lateinit var wordAdapter: WordAdapter
	private lateinit var searchBinding: ActivitySearchBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		observeData()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		searchBinding = ActivitySearchBinding.inflate(inflater, container, false)
		setupUI()
		setRecyclerAdapter()
		return searchBinding.root
	}

	private fun observeData(){
		model.documentList.observe(this, Observer {documents ->
			model.documentListObserved(documents, searchBinding, documentAdapter)
		})
		model.wordList.observe(this, Observer {searchWords ->
			model.wordListObserved(searchWords, searchBinding, wordAdapter)
		})
	}
	private fun setRecyclerAdapter(){
		searchBinding.searchResultRecyclerView.layoutManager = LinearLayoutManager(activity)
		searchBinding.searchWordRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
		documentAdapter = DocumentAdapter(this)
		wordAdapter = WordAdapter(this)
	}
	private fun setupUI(){
		searchBinding.searchClear.setOnClickListener {
			searchBinding.search.setText("")
		}
		searchBinding.search.doOnTextChanged { text, _, _, _ ->
			val query = text.toString()
			model.doOnTextChanged(query, searchBinding)
		}
	}

	override fun onPlaceClicked(document: Document) {
		model.placeClicked(document)
		parentFragmentManager.popBackStack()
	}

	override fun onWordDeleted(searchWord: SearchWord) {
		lifecycleScope.launch{
			model.deleteWord(searchWord)
		}
	}

	override fun onWordSearched(searchWord: SearchWord) {
		model.searchLocalAPI(searchWord.name)
	}


}