package gst.trainingcourse.smartmovie.model

data class ListCast(
    val cast: MutableList<Cast>,
    val crew: List<Crew>,
    val id: Int
)