package gst.trainingcourse.smartmovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.model.Result
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
    var onItemClick: (Result) -> Unit = {}
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            onItemClick,
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie, parent, false
            )
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val result = differ.currentList[position]
        holder.bindItemCategory(result)
    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class MovieViewHolder(onClickItem: (Result) -> Unit, itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onClickItem(differ.currentList[adapterPosition])
            }
        }

        fun bindItemCategory(result: Result) {
            Glide.with(itemView).load("https://image.tmdb.org/t/p/w342" + result.poster_path)
                .into(itemView.ivPoster1)
            itemView.tvTitle1.text = result.title
            itemView.tvDuration1.text = result.release_date
            if (result.vote_average >= 8.0) {
                itemView.ivVoteAverage1.setImageResource(R.drawable.star_yellow)
            } else {
                itemView.ivVoteAverage1.setImageResource(R.drawable.star)
            }
        }
    }
}