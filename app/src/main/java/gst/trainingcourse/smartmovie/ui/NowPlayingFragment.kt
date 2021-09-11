package gst.trainingcourse.smartmovie.ui

import android.os.Bundle
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
import gst.trainingcourse.smartmovie.adapter.CategoryAdapter
import gst.trainingcourse.smartmovie.network.MovieApi
import gst.trainingcourse.smartmovie.network.RemoteDataSource
import gst.trainingcourse.smartmovie.network.Resource
import gst.trainingcourse.smartmovie.repository.MovieRepository
import gst.trainingcourse.smartmovie.util.Constant
import gst.trainingcourse.smartmovie.viewmodel.MovieViewModel
import gst.trainingcourse.smartmovie.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.coroutines.*


class NowPlayingFragment : Fragment() {

    private val remoteDataSource = RemoteDataSource()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_popular, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(getFragmentRepository())
        movieViewModel = ViewModelProvider(this, factory).get(getViewModel())
        setupRecyclerView()
        getData()
        swipeContainer.setOnRefreshListener {
            movieViewModel.getMoviePage4()
            getData()
            swipeContainer.isRefreshing = false
        }
        categoryAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("result", it)
            }
            //val direction = HomeFragmentDirections.actionHomeFragmentToDetailedMovieFragment(it)
            findNavController().navigate(R.id.action_homeFragment_to_detailedMovieFragment, bundle)
        }

    }

    private fun getData() {
        movieViewModel.getNowPlayingMovieFromApi()
        movieViewModel.movie4.observe(viewLifecycleOwner, {

            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.value?.let { newsResponse ->
                        categoryAdapter.differ.submitList(newsResponse.results.toList())
                        isLastPage = movieViewModel.moviePage4 == newsResponse.total_pages
                    }
                }
                is Resource.Failure -> {

                    hideProgressBar()
                }
                else -> {
                    showProgressBar()
                }
            }
        })
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
                movieViewModel.getNowPlayingMovieFromApi()
                isScrolling = false
            } else {
                rvPopularMovie.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter()
        rvPopularMovie.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NowPlayingFragment.scrollListener)
        }
    }

    private fun getViewModel() = MovieViewModel::class.java

    private fun getFragmentRepository() =
        MovieRepository(remoteDataSource.buildApi(MovieApi::class.java))

    companion object {

        @JvmStatic
        fun newInstance() = NowPlayingFragment()
    }
}