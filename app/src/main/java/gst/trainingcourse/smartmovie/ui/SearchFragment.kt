package gst.trainingcourse.smartmovie.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.adapter.SearchAdapter
import gst.trainingcourse.smartmovie.network.MovieApi
import gst.trainingcourse.smartmovie.network.RemoteDataSource
import gst.trainingcourse.smartmovie.network.Resource
import gst.trainingcourse.smartmovie.repository.MovieRepository
import gst.trainingcourse.smartmovie.util.Constant
import gst.trainingcourse.smartmovie.viewmodel.MovieViewModel
import gst.trainingcourse.smartmovie.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private val remoteDataSource = RemoteDataSource()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var searchAdapter: SearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val factory = ViewModelFactory(getFragmentRepository())
        movieViewModel = ViewModelProvider(this, factory).get(getViewModel())


        var job: Job? = null
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                job?.cancel()
            }

            override fun afterTextChanged(s: Editable?) {
                job?.cancel()
                job = MainScope().launch {
                    delay(Constant.SEARCH_NEWS_TIME_DELAY)
                    s?.let {
                        if (s.toString().trim().isNotEmpty()) {
                            movieViewModel.searchMoviesPage = 1
                            movieViewModel.searchForMovies(Constant.API_KEY, s.toString())

                        } else {
                            movieViewModel.searchMovieResponse = null
                        }
                    }
                    movieViewModel.searchMovies.observe(viewLifecycleOwner, {
                        when (it) {
                            is Resource.Success -> {
                                hideProgressBar()
                                it.value?.let { newsResponse ->
                                    searchAdapter.differ.submitList(newsResponse.results.toList())
                                    isLastPage =
                                        movieViewModel.searchMoviesPage == newsResponse.total_pages
                                }
                            }
                            is Resource.Failure -> {
                                hideProgressBar()
                                handleApiError(it, context)
                            }
                            else -> {
                                showProgressBar()
                            }
                        }

                    })
                    searchAdapter.onItemClick = {
                        val bundle = Bundle().apply {
                            putSerializable("result", it)
                        }
                        //val direction = HomeFragmentDirections.actionHomeFragmentToDetailedMovieFragment(it)
                        findNavController().navigate(
                            R.id.action_searchFragment_to_detailedMovieFragment,
                            bundle
                        )
                    }
                }
            }


        })


        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter()
        rvSearchMovies.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constant.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                movieViewModel.searchForMovies(Constant.API_KEY, etSearch.text.toString())
                isScrolling = false
            } else {
                rvSearchMovies.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    private fun getViewModel() = MovieViewModel::class.java

    private fun getFragmentRepository() =
        MovieRepository(remoteDataSource.buildSearchApi(MovieApi::class.java))
}