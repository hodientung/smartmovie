package gst.trainingcourse.smartmovie.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.smartmovie.model.DetailedMovie
import gst.trainingcourse.smartmovie.model.Movie
import gst.trainingcourse.smartmovie.model.Result
import gst.trainingcourse.smartmovie.network.Resource
import gst.trainingcourse.smartmovie.repository.MovieRepository
import gst.trainingcourse.smartmovie.util.Constant
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    val movie1: MutableLiveData<Resource<Movie>> = MutableLiveData()
    var moviePage1 = 1
    private var movie1Response: Movie? = null

    val movie2: MutableLiveData<Resource<Movie>> = MutableLiveData()
    var moviePage2 = 1
    private var movie2Response: Movie? = null

    val movie3: MutableLiveData<Resource<Movie>> = MutableLiveData()
    var moviePage3 = 1
    private var movie3Response: Movie? = null

    val movie4: MutableLiveData<Resource<Movie>> = MutableLiveData()
    var moviePage4 = 1
    private var movie4Response: Movie? = null

    val searchMovies: MutableLiveData<Resource<Movie>> = MutableLiveData()
    var searchMoviesPage = 1
    var searchMovieResponse: Movie? = null

    val genresMovies: MutableLiveData<Resource<Result>> = MutableLiveData()
    private var genresResponse: Result? = null

    val movie5: MutableLiveData<Resource<Movie>> = MutableLiveData()
    var moviePage5 = 1
    private var movie5Response: Movie? = null

    var handleResult = MediatorLiveData<Boolean>().apply {
        addSource(movie1) {
            this.postValue(movie1.value is Resource.Success && movie2.value is Resource.Success && movie3.value is Resource.Success && movie4.value is Resource.Success)
        }
        addSource(movie2) {
            this.postValue(movie1.value is Resource.Success && movie2.value is Resource.Success && movie3.value is Resource.Success && movie4.value is Resource.Success)
        }
        addSource(movie3) {
            this.postValue(movie1.value is Resource.Success && movie2.value is Resource.Success && movie3.value is Resource.Success && movie4.value is Resource.Success)
        }
        addSource(movie4) {
            this.postValue(movie1.value is Resource.Success && movie2.value is Resource.Success && movie3.value is Resource.Success && movie4.value is Resource.Success)
        }
    }
    var handleResult1 = MediatorLiveData<Boolean>().apply {
        addSource(movie1) {
            this.postValue(movie1.value is Resource.Failure && movie2.value is Resource.Failure && movie3.value is Resource.Failure && movie4.value is Resource.Failure)
        }
        addSource(movie2) {
            this.postValue(movie1.value is Resource.Failure && movie2.value is Resource.Failure && movie3.value is Resource.Failure && movie4.value is Resource.Failure)
        }
        addSource(movie3) {
            this.postValue(movie1.value is Resource.Failure && movie2.value is Resource.Failure && movie3.value is Resource.Failure && movie4.value is Resource.Failure)
        }
        addSource(movie4) {
            this.postValue(movie1.value is Resource.Failure && movie2.value is Resource.Failure && movie3.value is Resource.Failure && movie4.value is Resource.Failure)
        }
    }
    var handleResult2 = MediatorLiveData<Boolean>().apply {
        addSource(movie1) {
            this.postValue(movie1.value is Resource.Loading && movie2.value is Resource.Loading && movie3.value is Resource.Loading && movie4.value is Resource.Loading)
        }
        addSource(movie2) {
            this.postValue(movie1.value is Resource.Loading && movie2.value is Resource.Loading && movie3.value is Resource.Loading && movie4.value is Resource.Loading)
        }
        addSource(movie3) {
            this.postValue(movie1.value is Resource.Loading && movie2.value is Resource.Loading && movie3.value is Resource.Loading && movie4.value is Resource.Loading)
        }
        addSource(movie4) {
            this.postValue(movie1.value is Resource.Loading && movie2.value is Resource.Loading && movie3.value is Resource.Loading && movie4.value is Resource.Loading)
        }
    }

    init {
        getListGenres(Constant.API_KEY)
    }

    fun getListMovieByGenres(apiKey: String, withGenre: Int) = viewModelScope.launch {
        movie5.postValue(Resource.Loading())
        val response = repository.getListMovieByGenres(
            apiKey,
            withGenre,
            moviePage5
        )
        movie5.postValue(handleListMoviesByGenresResponse(response))
    }

    private fun handleListMoviesByGenresResponse(response: Resource<Movie>): Resource<Movie> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                moviePage5++
                if (movie5Response == null) {
                    movie5Response = resultResponse
                } else {
                    val oldMovies = movie5Response?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(movie5Response ?: resultResponse)
            }
        }
        return Resource.Failure(true, null, null)
    }


    fun getPopularMovieFromApi() = viewModelScope.launch {
        movie1.postValue(Resource.Loading())
        val response = repository.getPopularMovieFromApi( moviePage1)
        movie1.postValue(handlePopularMovieResponse(response))

    }
    fun getMoviePage1(){
        moviePage1 = 1
    }
    fun getMoviePage2(){
        moviePage2 = 1
    }
    fun getMoviePage3(){
        moviePage3 = 1
    }
    fun getMoviePage4(){
        moviePage4= 1
    }

    private fun handlePopularMovieResponse(response: Resource<Movie>): Resource<Movie> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                moviePage1++
                if (movie1Response == null) {
                    movie1Response = resultResponse
                } else {
                    val oldMovies = movie1Response?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(movie1Response ?: resultResponse)
            }
        }
        return Resource.Failure(true, null, null)
    }


    fun getTopRatedMovieFromApi() = viewModelScope.launch {
        movie2.postValue(Resource.Loading())
        val response = repository.getTopRatedMovieFromApi(moviePage2)
        movie2.postValue(handleTopRatedMovieResponse(response))
    }

    private fun handleTopRatedMovieResponse(response: Resource<Movie>): Resource<Movie> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                moviePage2++
                if (movie2Response == null) {
                    movie2Response = resultResponse
                } else {
                    val oldMovies = movie2Response?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(movie2Response ?: resultResponse)
            }
        }
        return Resource.Failure(true, null, null)
    }


    fun getUpcomingMovieFromApi() = viewModelScope.launch {
        movie3.postValue(Resource.Loading())
        val response = repository.getUpcomingMovieFromApi( moviePage3)
        movie3.postValue(handleUpcomingMovieResponse(response))
    }

    private fun handleUpcomingMovieResponse(response: Resource<Movie>): Resource<Movie> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                moviePage3++
                if (movie3Response == null) {
                    movie3Response = resultResponse
                } else {
                    val oldMovies = movie3Response?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(movie3Response ?: resultResponse)
            }
        }
        return Resource.Failure(true, null, null)
    }


    fun getNowPlayingMovieFromApi() = viewModelScope.launch {
        movie4.postValue(Resource.Loading())
        val response = repository.getNowPlayingMovieFromApi(moviePage4)
        movie4.postValue(handleNowPlayingMovieResponse(response))
    }

    private fun handleNowPlayingMovieResponse(response: Resource<Movie>): Resource<Movie> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                moviePage4++
                if (movie4Response == null) {
                    movie4Response = resultResponse
                } else {
                    val oldMovies = movie4Response?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(movie4Response ?: resultResponse)
            }
        }
        return Resource.Failure(true, null, null)
    }


    fun searchForMovies(api: String, searchQuery: String) = viewModelScope.launch {
        searchMovies.postValue(Resource.Loading())
        val response = repository.searchForMovies(api, searchQuery, searchMoviesPage)
        searchMovies.postValue(handleSearchMoviesResponse(response))
    }

    private fun handleSearchMoviesResponse(response: Resource<Movie>): Resource<Movie> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                searchMoviesPage++
                if (searchMovieResponse == null) {
                    searchMovieResponse = resultResponse
                } else {
                    val oldMovies = searchMovieResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)

                }
                return Resource.Success(searchMovieResponse ?: resultResponse)
            }
        }
        return Resource.Failure(true, null, null)
    }

    private fun getListGenres(apiKey: String) = viewModelScope.launch {
        genresMovies.postValue(Resource.Loading())
        val response = repository.getListGenres(apiKey)
        genresMovies.postValue(handleGenresMovieResponse(response))
    }

    private fun handleGenresMovieResponse(response: Resource<Result>): Resource<Result> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                genresResponse = resultResponse
            }
            return Resource.Success(genresResponse)
        }
        return Resource.Failure(true, null, null)
    }

    val movieById: MutableLiveData<Resource<DetailedMovie>> = MutableLiveData()
    private var movieByIdResponse: DetailedMovie? = null

    fun getMovieByID(movie_id: Int, apiKey: String) = viewModelScope.launch {
        movieById.postValue(Resource.Loading())
        val response = repository.getMovieByID(movie_id, apiKey)
        movieById.postValue(handleMovieByIdResponse(response))
    }

    private fun handleMovieByIdResponse(response: Resource<DetailedMovie>): Resource<DetailedMovie> {
        if (response is Resource.Success) {
            response.value?.let { resultResponse ->
                movieByIdResponse = resultResponse
            }
            return Resource.Success(movieByIdResponse)
        }
        return Resource.Failure(true, null, null)
    }

}