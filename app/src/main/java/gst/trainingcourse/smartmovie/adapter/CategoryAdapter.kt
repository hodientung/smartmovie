package gst.trainingcourse.smartmovie.adapter

import android.graphics.Color
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.model.Result
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            onItemClick,
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_category, parent, false
            )
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val result = differ.currentList[position]
        holder.bindItemCategory(result)
    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class CategoryViewHolder(onClickItem: (Result) -> Unit, itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onClickItem(differ.currentList[adapterPosition])
            }
        }

        fun bindItemCategory(result: Result) {
            Glide.with(itemView).load("https://image.tmdb.org/t/p/w342"+result.poster_path)
                .into(itemView.ivPoster)
            itemView.tvOverview.text = result.overview
            itemView.tvTitle.text = result.title
            itemView.tvReleaseDate.text = result.release_date
            if (result.vote_average >= 8.0) {
                itemView.ivVoteAverage.setImageResource(R.drawable.star_yellow)
            } else {
                itemView.ivVoteAverage.setImageResource(R.drawable.star)

            }
        }
    }
}