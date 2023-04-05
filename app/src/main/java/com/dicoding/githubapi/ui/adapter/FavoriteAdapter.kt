package com.dicoding.githubapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubapi.R
import com.dicoding.githubapi.data.local.entity.FavoriteEntity

class FavoriteAdapter(private val listUser: List<FavoriteEntity>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private lateinit var onClickCallback: OnClickCallback

    interface OnClickCallback {
        fun onClicked(data: FavoriteEntity)
    }

    fun setOnClickCallback(onClickCallback: OnClickCallback) {
        this.onClickCallback = onClickCallback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.findViewById(R.id.tvItem)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvItem.text = listUser[position].username
        Glide.with(viewHolder.itemView.context)
            .load(listUser[position].avatar)
            .into(viewHolder.itemView.findViewById(R.id.ivAvatar))

        viewHolder.itemView.setOnClickListener { onClickCallback.onClicked(listUser[viewHolder.adapterPosition]) }
    }

    override fun getItemCount() = listUser.size
}