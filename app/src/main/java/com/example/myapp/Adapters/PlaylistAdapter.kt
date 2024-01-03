package com.example.myapp.Adapters

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Modals.PlaylistModal
import com.example.myapp.R
import com.squareup.picasso.Picasso

class PlaylistAdapter(private val context: Context, private val list: List<PlaylistModal>) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_row, parent, false) // Replace with your item layout
        return PlaylistHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        val playlistModal = list[position]
        holder.playlistNameRow.text = playlistModal.playlistName
        holder.playlistNameRow.maxLines = 2
        holder.playlistNameRow.ellipsize = TextUtils.TruncateAt.END
        Picasso.get()
            .load(playlistModal.playlistImage)
            .placeholder(R.drawable.skybg)
            .into(holder.playlistImageRow)
    }

    inner class PlaylistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistNameRow: TextView = itemView.findViewById(R.id.playlistNameRow)
        val playlistImageRow: ImageView = itemView.findViewById(R.id.playlistImageRow)
    }

}