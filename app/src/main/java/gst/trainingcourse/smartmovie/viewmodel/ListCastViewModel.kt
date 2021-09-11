package gst.trainingcourse.smartmovie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.smartmovie.model.ListCast
import gst.trainingcourse.smartmovie.network.Resource
import gst.trainingcourse.smartmovie.repository.MovieRepository
import kotlinx.coroutines.launch

class ListCastViewModel(private val repository: MovieRepository) : ViewModel() {
    val castById: MutableLiveData<Resource<ListCast>> = MutableLiveData()
    private var castByIdResponse: ListCast? = null

    fun getCastByID(movie_id: Int, apiKey: String) = viewModelScope.launch {
        castById.postValue(Resource.Loading())
        val response = repository.getCastByID(movie_id, apiKey)
        castById.postValue(handleCastByIdResponse(response))
    }

    private fun handleCastByIdResponse(response: Resource<ListCast>): Resource<ListCast> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                castByIdResponse = resultResponse
            }
            return Resource.Success(castByIdResponse)
        }
        return Resource.Failure(true, null, null)
    }
}