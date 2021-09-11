package gst.trainingcourse.smartmovie.network

import gst.trainingcourse.smartmovie.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    fun <Api> buildApi(api: Class<Api>): Api {
        return Retrofit.Builder().baseUrl(BASE_URL).client(OkHttpClient.Builder().also { client ->
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }
        }.build())
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }

    fun <Api> buildSearchApi(api: Class<Api>): Api {
        return Retrofit.Builder().baseUrl(BASE_URL_SEARCH)
            .client(OkHttpClient.Builder().also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }

    fun <Api> buildGenresApi(api: Class<Api>): Api {
        return Retrofit.Builder().baseUrl(BASE_URL_GENRES)
            .client(OkHttpClient.Builder().also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }

    fun <Api> buildListMovieByGenresApi(api: Class<Api>): Api {
        return Retrofit.Builder().baseUrl(BASE_URL_MOVIE_BY_GENRES)
            .client(OkHttpClient.Builder().also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }

    fun <Api> buildListMovieByID(api: Class<Api>): Api {
        return Retrofit.Builder().baseUrl(BASE_URL_MOVIE_BY_ID)
            .client(OkHttpClient.Builder().also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }

    fun <Api> buildCastByID(api: Class<Api>): Api {
        return Retrofit.Builder().baseUrl(BASE_URL_MOVIE_BY_ID)
            .client(OkHttpClient.Builder().also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }

    fun <Api> buildSimilarMovieByID(api: Class<Api>): Api {
        return Retrofit.Builder().baseUrl(BASE_URL_MOVIE_BY_ID)
            .client(OkHttpClient.Builder().also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create()).build().create(api)
    }

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/movie/"
        private const val BASE_URL_SEARCH = "https://api.themoviedb.org/3/search/"
        private const val BASE_URL_GENRES = "https://api.themoviedb.org/3/genre/movie/"
        private const val BASE_URL_MOVIE_BY_GENRES = "https://api.themoviedb.org/3/discover/"
        private const val BASE_URL_MOVIE_BY_ID = "https://api.themoviedb.org/"

    }
}