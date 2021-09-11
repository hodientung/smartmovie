package gst.trainingcourse.smartmovie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gst.trainingcourse.smartmovie.repository.BaseRepository
import gst.trainingcourse.smartmovie.repository.MovieRepository

class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> MovieViewModel(repository as MovieRepository) as T
            modelClass.isAssignableFrom(ListCastViewModel::class.java) -> ListCastViewModel(
                repository as MovieRepository
            ) as T
            modelClass.isAssignableFrom(SimilarMovieViewModel::class.java) -> SimilarMovieViewModel(
                repository as MovieRepository
            ) as T
            else -> throw IllegalArgumentException("ViewModelClass not found")
        }

    }
}