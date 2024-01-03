package com.example.myapp.Adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Modals.PlaylistModal
import com.example.myapp.Modals.SongModal
import com.example.myapp.R
import com.squareup.picasso.Picasso

class SongAdapter(private val context: Context, private val list: List<SongModal>) :
    RecyclerView.Adapter<SongAdapter.SongHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_song_row, parent, false) // Replace with your item layout
        return SongHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val songModal = list[position]
        holder.songNamePlaylist.text = songModal.songName
        holder.singerNamePlaylist.text = songModal.singerName
        holder.songNamePlaylist.maxLines = 1
        holder.songNamePlaylist.ellipsize = TextUtils.TruncateAt.END
        holder.singerNamePlaylist.maxLines = 1
        holder.singerNamePlaylist.ellipsize = TextUtils.TruncateAt.END
        Picasso.get()
            .load(songModal.songImage)
            .placeholder(R.drawable.skybg)
            .into(holder.playlistSongImage)
    }

    inner class SongHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playlistSongImage: ImageView = itemView.findViewById(R.id.playlistSongImageRow)
        val songMenuInPlaylist: ImageView = itemView.findViewById(R.id.songMenuInPlaylistRow)
        val songNamePlaylist: TextView = itemView.findViewById(R.id.songNamePlaylistRow)
        val singerNamePlaylist: TextView = itemView.findViewById(R.id.singerNamePlaylistRow)
    }
}