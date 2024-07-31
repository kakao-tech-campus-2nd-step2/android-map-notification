package campus.tech.kakao.map.presentation.search

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.presentation.adapter.SearchedPlaceAdapter
import campus.tech.kakao.map.presentation.adapter.LogAdapter
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.presentation.map.MapActivity
import campus.tech.kakao.map.util.PlaceMapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    @Inject lateinit var listener: SearchActivityListener
    private lateinit var binding: ActivityMainBinding
    private val searchedPlaceAdapter by lazy { SearchedPlaceAdapter(listener)}
    private val logAdapter by lazy { LogAdapter(listener) }
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        setStatusBarTransparent()
        initBinding()
        setupRecyclerViews()
        observeViewModel()
    }

    private fun setStatusBarTransparent() {
        this.window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupRecyclerViews() {
        setupSearchedPlaceRecyclerView()
        setupLogRecyclerView()
    }

    private fun setupSearchedPlaceRecyclerView() {
        val searchedPlaceRecyclerView = binding.recyclerPlace

        searchedPlaceRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            addItemDecoration(DividerItemDecoration(this@SearchActivity, DividerItemDecoration.VERTICAL ))
            adapter = searchedPlaceAdapter
        }
    }


    private fun setupLogRecyclerView() {
        val logRecyclerView = binding.recyclerLog

        logRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, RecyclerView.HORIZONTAL, false)
            adapter = logAdapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.searchedPlaces.collect { places ->
                updateSearchedPlaceList(places)
                binding.tvHelpMessage.visibility = if (places.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.logList.collect { logList ->
                logAdapter.submitList(PlaceMapper.mapPlaces(logList))
            }
        }
    }

    fun handlePlaceClick(place: Place) {
        lifecycleScope.launch {
            viewModel.updateLogs(place)
            val selectedPlace = viewModel.getPlaceById(place.id)
            val intent = Intent(this@SearchActivity, MapActivity::class.java).apply {
                putExtra("placeData", selectedPlace)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    fun handleLogDelBtnClick(logId: String) {
        lifecycleScope.launch {
            viewModel.removeLog(logId)
        }
    }

    private fun updateSearchedPlaceList(places: List<Place>) {
        searchedPlaceAdapter.submitList(PlaceMapper.mapPlaces(places))
    }
}
