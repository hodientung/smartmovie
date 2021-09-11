package gst.trainingcourse.smartmovie.network

import gst.trainingcourse.smartmovie.model.DetailedMovie
import gst.trainingcourse.smartmovie.model.ListCast
import gst.trainingcourse.smartmovie.model.Movie
import gst.trainingcourse.smartmovie.model.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("popular")
    suspend fun getPopularMovieFromApi(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Movie

    @GET("top_rated")
    suspend fun getTopRatedMovieFromApi(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Movie

    @GET("upcoming")
    suspend fun getUpcomingMovieFromApi(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Movie

    @GET("now_playing")
    suspend fun getNowPlayingMovieFromApi(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Movie

    @GET("movie")
    suspend fun searchForMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Movie

    @GET("list")
    suspend fun getListGenres(
        @Query("api_key") apiKey: String,
    ): Result

    @GET("movie")
    suspend fun getListMovieByGenres(
        @Query("api_key") apiKey: String,
        @Query("with_genres") withGenre: Int,
        @Query("page") page: Int
    ): Movie


    @GET("3/movie/{movie_id}?")
    suspend fun getMovieByID(
        @Path("movie_id") movie_id:
        Int,
        @Query("api_key") apiKey: String
    ): DetailedMovie

    @GET("3/movie/{movie_id}/credits")
    suspend fun getCastByID(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String
    ): ListCast

    @GET("3/movie/{movie_id}/similar")
    suspend fun getSimilarMovieByID(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Movie
}