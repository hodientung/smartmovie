package gst.trainingcourse.smartmovie.ui

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dmax.dialog.SpotsDialog
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.adapter.MovieAdapter
import gst.trainingcourse.smartmovie.network.MovieApi
import gst.trainingcourse.smartmovie.network.RemoteDataSource
import gst.trainingcourse.smartmovie.network.Resource
import gst.trainingcourse.smartmovie.repository.MovieRepository
import gst.trainingcourse.smartmovie.viewmodel.MovieViewModel
import gst.trainingcourse.smartmovie.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_movie.*


class MovieFragment : Fragment() {

    private val remoteDataSource = RemoteDataSource()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var popularAdapter: MovieAdapter
    private lateinit var topRatedAdapter: MovieAdapter
    private lateinit var upcomingAdapter: MovieAdapter
    private lateinit var nowPlayingAdapter: MovieAdapter

    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog: android.app.AlertDialog? = SpotsDialog.Builder().setContext(context).build()
        val factory = ViewModelFactory(getFragmentRepository())
        movieViewModel = ViewModelProvider(this, factory).get(getViewModel())
        setupRecyclerView()
        getData()
        swipeContainer1.setOnRefreshListener {
            movieViewModel.getMoviePage1()
            movieViewModel.getMoviePage2()
            movieViewModel.getMoviePage3()
            movieViewModel.getMoviePage4()
            getData()
            swipeContainer1.isRefreshing = false

        }

        movieViewModel.handleResult2.observe(viewLifecycleOwner, {
            if (it == true) {
                dialog?.show()
            }
        })

        movieViewModel.handleResult.observe(viewLifecycleOwner, {
            if (it == true) {
                dialog?.dismiss()
            }
        })
        movieViewModel.handleResult1.observe(viewLifecycleOwner, {
            if (it == true) {
                dialog?.dismiss()
                handleApiError(Resource.Failure(true, null, null), context)
            }
        })

        tvPopularMovie.setOnClickListener {
            navigateToPopularPage()
        }
        tvTopRatedMovie.setOnClickListener {
            navigateToTopRatedPage()
        }
        tvUpcomingMovie.setOnClickListener {
            navigateToUpcoming()
        }
        tvNowPlayingMovie.setOnClickListener {
            navigateToNowPlaying()
        }
        popularAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("result", it)
            }
            //val direction = HomeFragmentDirections.actionHomeFragmentToDetailedMovieFragment(it)
            findNavController().navigate(R.id.action_homeFragment_to_detailedMovieFragment, bundle)
        }
        topRatedAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("result", it)
            }
            //val direction = HomeFragmentDirections.actionHomeFragmentToDetailedMovieFragment(it)
            findNavController().navigate(R.id.action_homeFragment_to_detailedMovieFragment, bundle)
        }
        nowPlayingAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("result", it)
            }
            //val direction = HomeFragmentDirections.actionHomeFragmentToDetailedMovieFragment(it)
            findNavController().navigate(R.id.action_homeFragment_to_detailedMovieFragment, bundle)
        }
        upcomingAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("result", it)
            }
            //val direction = HomeFragmentDirections.actionHomeFragmentToDetailedMovieFragment(it)
            findNavController().navigate(R.id.action_homeFragment_to_detailedMovieFragment, bundle)
        }
    }

    private fun getData() {
        movieViewModel.getPopularMovieFromApi()
        movieViewModel.getTopRatedMovieFromApi()
        movieViewModel.getUpcomingMovieFromApi()
        movieViewModel.getNowPlayingMovieFromApi()
        movieViewModel.movie1.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    //dialog?.dismiss()
                    it.value?.let { newsResponse ->
                        popularAdapter.differ.submitList(
                            newsResponse.results.subList(
                                fromIndex = 0,
                                toIndex = 4
                            ).toList()
                        )
                    }
                }
                is Resource.Failure -> {
                    hideProgressBar()
                    //handleApiError(it, context)
                }
                else -> {
                    //dialog?.show()

                    hideProgressBar()
                }
            }
        })

        movieViewModel.movie2.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    it.value?.let { newsResponse ->
                        topRatedAdapter.differ.submitList(
                            newsResponse.results.subList(
                                fromIndex = 0,
                                toIndex = 4
                            ).toList()
                        )
                    }
                }
                is Resource.Failure -> {
                    hideProgressBar()
                    //handleApiError(it, context)
                }
                else -> {
                    hideProgressBar()
                }
            }
        })

        movieViewModel.movie3.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {

                    it.value?.let { newsResponse ->
                        upcomingAdapter.differ.submitList(
                            newsResponse.results.subList(
                                fromIndex = 0,
                                toIndex = 4
                            ).toList()
                        )
                    }
                }
                is Resource.Failure -> {
                    hideProgressBar()

                    //handleApiError(it, context)
                }
                else -> {
                    hideProgressBar()
                }
            }
        })

        movieViewModel.movie4.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    it.value?.let { newsResponse ->
                        nowPlayingAdapter.differ.submitList(
                            newsResponse.results.subList(
                                fromIndex = 0,
                                toIndex = 4
                            ).toList()
                        )
                    }
                }
                is Resource.Failure -> {
                    hideProgressBar()
                    //handleApiError(it, context)
                }
                else -> {
                    hideProgressBar()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exchange_layout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch -> {
                switchLayout()
                switchIcon(item)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun switchLayout() {
        if (t && u && n && g) {
            rvPopularMovie1.layoutManager = GridLayoutManager(activity, 1)
            t = false
            rvTopRatedMovie1.layoutManager = GridLayoutManager(activity, 1)
            u = false
            rvUpcomingMovie1.layoutManager = GridLayoutManager(activity, 1)
            n = false
            rvNowPlayingMovie1.layoutManager = GridLayoutManager(activity, 1)
            g = false
        } else {
            rvPopularMovie1.layoutManager = GridLayoutManager(activity, 2)
            t = true
            rvTopRatedMovie1.layoutManager = GridLayoutManager(activity, 2)
            u = true
            rvUpcomingMovie1.layoutManager = GridLayoutManager(activity, 2)
            n = true
            rvNowPlayingMovie1.layoutManager = GridLayoutManager(activity, 2)
            g = true
        }

        popularAdapter.notifyItemRangeChanged(0, popularAdapter.itemCount)
        topRatedAdapter.notifyItemChanged(0, topRatedAdapter.itemCount)
        upcomingAdapter.notifyItemChanged(0, upcomingAdapter.itemCount)
        nowPlayingAdapter.notifyItemChanged(0, nowPlayingAdapter.itemCount)
    }

    private fun switchIcon(item: MenuItem) {
        if (!t && !u && !n && !g) {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.home, null)
        } else {
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.change_layout, null)
        }
    }

    private fun navigateToNowPlaying() {
        requireActivity().vpHome.currentItem = 4
    }

    private fun navigateToUpcoming() {
        requireActivity().vpHome.currentItem = 3
    }

    private fun navigateToTopRatedPage() {
        requireActivity().vpHome.currentItem = 2
    }

    private fun navigateToPopularPage() {
        requireActivity().vpHome.currentItem = 1
    }

    private fun hideProgressBar() {
        paginationProgressBar1.visibility = View.INVISIBLE
        isLoading = false
    }

//        private fun showProgressBar() {
//        paginationProgressBar1.visibility = View.VISIBLE
//        isLoading = true
//    }

    private var t = true
    private var u = true
    private var n = true
    private var g = true
    private fun setupRecyclerView() {
        popularAdapter = MovieAdapter()
        rvPopularMovie1.apply {
            adapter = popularAdapter
            layoutManager = GridLayoutManager(activity, 2)
            t = true

        }

        topRatedAdapter = MovieAdapter()
        rvTopRatedMovie1.apply {
            adapter = topRatedAdapter
            layoutManager = GridLayoutManager(activity, 2)
            u = true
        }

        upcomingAdapter = MovieAdapter()
        rvUpcomingMovie1.apply {
            adapter = upcomingAdapter
            layoutManager = GridLayoutManager(activity, 2)
            n = true
        }

        nowPlayingAdapter = MovieAdapter()
        rvNowPlayingMovie1.apply {
            adapter = upcomingAdapter
            layoutManager = GridLayoutManager(activity, 2)
            g = true
        }


    }


    private fun getViewModel() = MovieViewModel::class.java

    private fun getFragmentRepository() =
        MovieRepository(remoteDataSource.buildApi(MovieApi::class.java))

    companion object {

        @JvmStatic
        fun newInstance() = MovieFragment()
    }
}