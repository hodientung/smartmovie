package gst.trainingcourse.smartmovie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.adapter.GenresAdapter
import gst.trainingcourse.smartmovie.network.MovieApi
import gst.trainingcourse.smartmovie.network.RemoteDataSource
import gst.trainingcourse.smartmovie.network.Resource
import gst.trainingcourse.smartmovie.repository.MovieRepository
import gst.trainingcourse.smartmovie.viewmodel.MovieViewModel
import gst.trainingcourse.smartmovie.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_genres.*


class GenresFragment : Fragment() {
    private val remoteDataSource = RemoteDataSource()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var genresAdapter: GenresAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_genres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory(getFragmentRepository())
        movieViewModel = ViewModelProvider(this, factory).get(getViewModel())
        setupRecyclerView()
        genresAdapter.onItemClick = {
            val bundle = Bundle().apply {
                putSerializable("genre", it)
            }
            findNavController().navigate(
                R.id.action_genresFragment_to_listMovieByGenreFragment,
                bundle
            )
        }


        movieViewModel.genresMovies.observe(viewLifecycleOwner, {

            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.value?.let { newsResponse ->
                        genresAdapter.differ.submitList(newsResponse.genres.toList())
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
        isLoading = false
    }

    private fun showProgressBar() {
        isLoading = true
    }

    private var isLoading = false

    private fun getViewModel() = MovieViewModel::class.java

    private fun getFragmentRepository() =
        MovieRepository(remoteDataSource.buildGenresApi(MovieApi::class.java))

    private fun setupRecyclerView() {
        genresAdapter = GenresAdapter()
        rvGenres.apply {
            adapter = genresAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
