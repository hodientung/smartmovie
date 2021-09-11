package gst.trainingcourse.smartmovie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.adapter.CastAdapter
import gst.trainingcourse.smartmovie.adapter.SearchAdapter
import gst.trainingcourse.smartmovie.model.Result
import gst.trainingcourse.smartmovie.network.MovieApi
import gst.trainingcourse.smartmovie.network.RemoteDataSource
import gst.trainingcourse.smartmovie.network.Resource
import gst.trainingcourse.smartmovie.repository.MovieRepository
import gst.trainingcourse.smartmovie.util.Constant
import gst.trainingcourse.smartmovie.viewmodel.ListCastViewModel
import gst.trainingcourse.smartmovie.viewmodel.MovieViewModel
import gst.trainingcourse.smartmovie.viewmodel.SimilarMovieViewModel
import gst.trainingcourse.smartmovie.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_detailed_movie.*
import kotlinx.android.synthetic.main.fragment_popular.*


class DetailedMovieFragment : Fragment() {

    private val args: DetailedMovieFragmentArgs by navArgs()
    private val remoteDataSource = RemoteDataSource()
    private lateinit var movieViewModel: MovieViewModel
    var result: Result? = null


    private lateinit var listCastViewModel: ListCastViewModel
    private lateinit var castAdapter: CastAdapter

    private lateinit var similarMovieViewModel: SimilarMovieViewModel
    private lateinit var similarMovieAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detailed_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        btnBack.setOnClickListener { findNavController().navigateUp() } -> back screen
        val factory = ViewModelFactory(getFragmentRepository())
        movieViewModel = ViewModelProvider(this, factory).get(getViewModel())
        result = args.result
        result?.id?.let { movieViewModel.getMovieByID(it, Constant.API_KEY) }
        movieViewModel.movieById.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    tvTitleDetailedMovie.text = it.value?.title
                    var genres = ""
                    val originalList = it.value?.genres
                    val n = originalList?.size
                    for (i in 0 until n!!) {
                        genres += if (i == n - 1) {
                            originalList[i].name
                        } else {
                            originalList[i].name + " | "
                        }
                    }
                    tvGenresDetailedMovie.text = genres
                    rbDetailedMovie.rating = ((it.value.vote_average / 2).toFloat())

                    var language = ""
                    val arr = it.value.spoken_languages
                    val m = arr.size
                    for (i in 0 until m) {
                        language += if (i == m - 1) {
                            arr[i].english_name
                        } else {
                            arr[i].english_name + " | "
                        }
                    }
                    tvLanguage.text = "Language: $language"
                    tvDurationDetailedMovie.text =
                        "${it.value.release_date} ${exchangeMinuteToHourMinute(it.value.runtime)}"
                    Glide.with(view).load("https://image.tmdb.org/t/p/w200" + it.value.poster_path)
                        .into(ivPosterDetailedMovie)
                    vmtvDescription.setAnimationDuration(500)
                        .setEllipsizedText("View More")
                        .setVisibleLines(3)
                        .setIsExpanded(false)
                        .setEllipsizedTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.purple_500
                            )
                        ).text = it.value.overview
                    vmtvDescription.setOnClickListener {
                        vmtvDescription.toggle()
                    }

                }
                is Resource.Failure -> {
                    hideProgressBar()
                    handleApiError(it, context)
                }
                else -> {
                    hideProgressBar()
                }
            }
        })


        val factory1 = ViewModelFactory(getFragmentRepository1())
        listCastViewModel = ViewModelProvider(this, factory1).get(getViewModel1())
        setupRecyclerView()
        result?.id?.let { listCastViewModel.getCastByID(it, Constant.API_KEY) }
        listCastViewModel.castById.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.value?.let { newsResponse ->
                        castAdapter.differ.submitList(newsResponse.cast.toList())
                    }
                }
                is Resource.Failure -> {
                    hideProgressBar()
                    handleApiError(it, context)
                }
                else -> {
                    hideProgressBar()
                }

            }
        })


        val factory2 = ViewModelFactory(getFragmentRepository2())
        similarMovieViewModel = ViewModelProvider(this, factory2).get(getViewModel2())
        result?.id?.let { similarMovieViewModel.getSimilarMovieByID(it, Constant.API_KEY) }
        setupRecyclerView1()
        similarMovieViewModel.similarMovieById.observe(viewLifecycleOwner, {

            when (it) {
                is Resource.Success -> {
                    hideProgressBar1()
                    it.value?.let { newsResponse ->
                        similarMovieAdapter.differ.submitList(newsResponse.results.toList())
                        isLastPage =
                            similarMovieViewModel.similarMoviePage == newsResponse.total_pages
                    }
                }
                is Resource.Failure -> {
                    hideProgressBar1()
                    handleApiError(it, context)
                }
                else -> {
                    showProgressBar1()
                }
            }
        })
    }

    private fun setupRecyclerView1() {
        similarMovieAdapter = SearchAdapter()
        rvSimilarMovies.apply {
            adapter = similarMovieAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@DetailedMovieFragment.scrollListener)
        }
    }

    private fun setupRecyclerView() {
        castAdapter = CastAdapter()
        rvCast.apply {
            adapter = castAdapter
            layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun exchangeMinuteToHourMinute(minute: Int): String {
        val hour = minute / 60
        val minuteResult = minute % 60
        return "${hour}h ${minuteResult}m"
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        paginationProgressBar3.visibility = View.INVISIBLE
        isLoading = false
    }

//    private fun showProgressBar() {
//        paginationProgressBar3.visibility = View.VISIBLE
//        isLoading = true
//    }

    private fun hideProgressBar1() {
        paginationProgressBar4.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar1() {
        paginationProgressBar4.visibility = View.VISIBLE
        isLoading = true
    }

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
                result?.let { similarMovieViewModel.getSimilarMovieByID(it.id, Constant.API_KEY) }
                isScrolling = false
            } else {
                rvSimilarMovies.setPadding(0, 0, 0, 0)
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
    private fun getViewModel1() = ListCastViewModel::class.java
    private fun getViewModel2() = SimilarMovieViewModel::class.java

    private fun getFragmentRepository() =
        MovieRepository(remoteDataSource.buildListMovieByID(MovieApi::class.java))

    private fun getFragmentRepository1() =
        MovieRepository(remoteDataSource.buildCastByID(MovieApi::class.java))

    private fun getFragmentRepository2() =
        MovieRepository(remoteDataSource.buildSimilarMovieByID(MovieApi::class.java))
}