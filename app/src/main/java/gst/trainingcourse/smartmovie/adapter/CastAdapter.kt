package gst.trainingcourse.smartmovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.model.Cast
import kotlinx.android.synthetic.main.item_cast.view.*

class CastAdapter: RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Cast>() {

        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.cast_id == newItem.cast_id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    var onItemClick: (Cast) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder =
        CastViewHolder(
            onItemClick,
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_cast, parent, false
            )
        )

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val result = differ.currentList[position]
        holder.bindItemCategory(result)
    }

    override fun getItemCount(): Int = differ.currentList.size


    inner class CastViewHolder(onClickItem: (Cast) -> Unit, itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onClickItem(differ.currentList[adapterPosition])
            }
        }

        fun bindItemCategory(cast: Cast) {
            Glide.with(itemView).load(
                "https://image.tmdb.org/t/p/w342${cast.profile_path}"
            ).into(itemView.ivCast)
            itemView.tvName.text = cast.name
        }
    }
}