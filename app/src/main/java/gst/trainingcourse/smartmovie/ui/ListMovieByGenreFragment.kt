package gst.trainingcourse.smartmovie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.adapter.MovieByGenreAdapter
import gst.trainingcourse.smartmovie.model.Genre
import gst.trainingcourse.smartmovie.network.MovieApi
import gst.trainingcourse.smartmovie.network.RemoteDataSource
import gst.trainingcourse.smartmovie.network.Resource
import gst.trainingcourse.smartmovie.repository.MovieRepository
import gst.trainingcourse.smartmovie.util.Constant
import gst.trainingcourse.smartmovie.viewmodel.MovieViewModel
import gst.trainingcourse.smartmovie.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_list_movie_by_genre.*
import kotlinx.android.synthetic.main.fragment_popular.*


class ListMovieByGenreFragment : Fragment() {

    private val args: ListMovieByGenreFragmentArgs by navArgs()
    private val remoteDataSource = RemoteDataSource()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieByGenreAdapter: MovieByGenreAdapter



    var genre: Genre? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_movie_by_genre, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(getFragmentRepository())
        movieViewModel = ViewModelProvider(this, factory).get(getViewModel())


        genre = args.genre
        tvNameGenre.text = genre?.name
        genre?.id?.let { movieViewModel.getListMovieByGenres(Constant.API_KEY, it) }
        setupRecyclerView()
        movieViewModel.movie5.observe(viewLifecycleOwner, {

            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.value?.let { newsResponse ->
                        movieByGenreAdapter.differ.submitList(newsResponse.results.toList())
                        isLastPage = movieViewModel.moviePage5 == newsResponse.total_pages
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
    }


    private fun hideProgressBar() {
        paginationProgressBar2.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar2.visibility = View.VISIBLE
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
                genre?.let { movieViewModel.getListMovieByGenres(Constant.API_KEY, it.id) }
                isScrolling = false
            } else {
                rvListMovieByGenre.setPadding(0, 0, 0, 0)
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
        movieByGenreAdapter = MovieByGenreAdapter()
        rvListMovieByGenre.apply {
            adapter = movieByGenreAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@ListMovieByGenreFragment.scrollListener)
        }
    }

    private fun getViewModel() = MovieViewModel::class.java

    private fun getFragmentRepository() =
        MovieRepository(remoteDataSource.buildListMovieByGenresApi(MovieApi::class.java))
}