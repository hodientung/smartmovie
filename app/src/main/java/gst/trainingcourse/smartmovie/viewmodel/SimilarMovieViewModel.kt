package gst.trainingcourse.smartmovie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.smartmovie.model.Movie
import gst.trainingcourse.smartmovie.network.Resource
import gst.trainingcourse.smartmovie.repository.MovieRepository
import kotlinx.coroutines.launch

class SimilarMovieViewModel(private val repository: MovieRepository) : ViewModel() {
    val similarMovieById: MutableLiveData<Resource<Movie>> = MutableLiveData()
    var similarMoviePage = 1
    private var similarMovieByIdResponse: Movie? = null

    fun getSimilarMovieByID(movie_id: Int, apiKey: String) = viewModelScope.launch {
        similarMovieById.postValue(Resource.Loading())
        val response = repository.getSimilarMovieByID(movie_id, apiKey, similarMoviePage)
        similarMovieById.postValue(handleSimilarMovieByIdResponse(response))
    }

    private fun handleSimilarMovieByIdResponse(response: Resource<Movie>): Resource<Movie> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                similarMoviePage++
                if (similarMovieByIdResponse == null) {
                    similarMovieByIdResponse = resultResponse
                } else {
                    val oldMovies = similarMovieByIdResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)

                }
                return Resource.Success(similarMovieByIdResponse ?: resultResponse)
            }
        }
        return Resource.Failure(true, null, null)
    }
}