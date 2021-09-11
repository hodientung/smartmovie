package gst.trainingcourse.smartmovie.model

data class Movie(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)

