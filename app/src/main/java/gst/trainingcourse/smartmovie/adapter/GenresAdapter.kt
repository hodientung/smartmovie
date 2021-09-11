package gst.trainingcourse.smartmovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.model.Genre
import gst.trainingcourse.smartmovie.util.Constant
import kotlinx.android.synthetic.main.item_genres.view.*

class GenresAdapter : RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Genre>() {

        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    var onItemClick: (Genre) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder =
        GenresViewHolder(
            onItemClick,
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_genres, parent, false
            )
        )

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        val result = differ.currentList[position]
        holder.bindItemCategory(result)
    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class GenresViewHolder(onClickItem: (Genre) -> Unit, itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onClickItem(differ.currentList[adapterPosition])
            }
        }

        fun bindItemCategory(genre: Genre) {
            Glide.with(itemView).load(
                "https://image.tmdb.org/t/p/w342${Constant.listGenre[adapterPosition]}"
            ).into(itemView.ivGenres)
            itemView.tvGenres1.text = genre.name

        }
    }
}