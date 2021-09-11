package gst.trainingcourse.smartmovie.repository

import gst.trainingcourse.smartmovie.network.MovieApi
import gst.trainingcourse.smartmovie.util.Constant

class MovieRepository(private val api: MovieApi) : BaseRepository() {

    suspend fun getPopularMovieFromApi( page: Int) =
        safeApiCall { api.getPopularMovieFromApi(Constant.API_KEY, page) }

    suspend fun getTopRatedMovieFromApi(page: Int) =
        safeApiCall { api.getTopRatedMovieFromApi(Constant.API_KEY, page) }

    suspend fun getUpcomingMovieFromApi(page: Int) =
        safeApiCall { api.getUpcomingMovieFromApi(Constant.API_KEY, page) }

    suspend fun getNowPlayingMovieFromApi( page: Int) =
        safeApiCall { api.getNowPlayingMovieFromApi(Constant.API_KEY, page) }

    suspend fun searchForMovies(apiKey: String, query: String, page: Int) =
        safeApiCall { api.searchForMovies(apiKey, query, page) }

    suspend fun getListGenres(apiKey: String) = safeApiCall { api.getListGenres(apiKey) }

    suspend fun getListMovieByGenres(apiKey: String, withGenre: Int, page: Int) =
        safeApiCall { api.getListMovieByGenres(apiKey, withGenre, page) }

    suspend fun getMovieByID(movie_id: Int, apiKey: String) = safeApiCall {
        api.getMovieByID(movie_id, apiKey)
    }

    suspend fun getCastByID(movie_id: Int, apiKey: String) = safeApiCall {
        api.getCastByID(movie_id, apiKey)
    }

    suspend fun getSimilarMovieByID(movie_id : Int,apiKey: String,page: Int ) =safeApiCall {
        api.getSimilarMovieByID(movie_id, apiKey, page)
    }
}