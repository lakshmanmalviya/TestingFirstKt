package com.example.myapp.Fragements

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Activities.UploadSong
import com.example.myapp.Adapters.PlaylistAdapter
import com.example.myapp.Adapters.SongAdapter
import com.example.myapp.Classes.MyColor
import com.example.myapp.Classes.RecyclerItemClickListener
import com.example.myapp.Classes.ReuseThings
import com.example.myapp.Classes.STATICVAR
import com.example.myapp.Modals.PlaylistModal
import com.example.myapp.Modals.SongModal
import com.example.myapp.R
import com.example.myapp.databinding.FragmentHomeBinding
import com.example.myapp.databinding.FragmentListenPlaylistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListenPlaylist : Fragment() {
    private lateinit var bnd: FragmentListenPlaylistBinding
    private lateinit var adapter: SongAdapter
    private lateinit var songModal: SongModal
    private val plist = ArrayList<SongModal>()
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var myDatabase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private var isPlayingPlaylist = false
    private var isPlayingShuffled = false
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentListenPlaylistBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDatabase = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        mediaPlayer = MediaPlayer()
        adapter = SongAdapter(requireContext(), plist)
        bnd.listenPlaylistRec.adapter = adapter
        layoutManager = GridLayoutManager(requireContext(), 1)
        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager.reverseLayout = false
        bnd.listenPlaylistRec.layoutManager = layoutManager
        Picasso.get().load(arguments?.getString(STATICVAR.playlistImage))
            .placeholder(R.drawable.skybg).into(bnd.playlistImage)
        bnd.playlistDesc.setText(arguments?.getString(STATICVAR.playlistDesc))

        Picasso.get().load(arguments?.getString(STATICVAR.playlistImage))
            .into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let {
                        Palette.from(it).generate { palette ->
//                        val dominantColor = palette?.dominantSwatch?.rgb ?: Color.WHITE
//                        val darkVibrantColor = palette?.darkVibrantSwatch?.rgb ?: Color.BLACK
                            val dominantColor = palette?.darkVibrantSwatch?.rgb ?: Color.WHITE
                            val darkVibrantColor = palette?.darkMutedSwatch?.rgb ?: Color.BLACK
                            val gradient = GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                intArrayOf(dominantColor, darkVibrantColor)
                            )
                            bnd.listenPlaylistFrag.background = gradient
                        }
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    // Handle image loading failure
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    // Handle image loading in progress
                }
            })


        myDatabase.reference.child(STATICVAR.userPlaylistInfo).child(auth.currentUser?.uid!!)
            .child(arguments?.getString(STATICVAR.playlistId) + "").child(STATICVAR.songs)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        plist.clear()
                        for (dataSnapshot in snapshot.children) {
                            songModal =
                                dataSnapshot.getValue(SongModal::class.java) ?: SongModal()
                            plist.add(songModal)
                        }
                        adapter.notifyDataSetChanged()
                    } else {
//                        bnd.seekLayout.visibility = View.GONE
//                        bnd.seekCntLayout.visibility = View.GONE
//                        bnd.seeText.text = "No song is Available in this section"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    ReuseThings.customToast(requireContext(), "Database load error $error")
                }
            })

        bnd.playPausePlaylist.setOnClickListener {
            if (isPlayingPlaylist) {
                bnd.playPausePlaylist.setImageResource(R.drawable.play)
                isPlayingPlaylist = false;
            } else {
                bnd.playPausePlaylist.setImageResource(R.drawable.pause)
                isPlayingPlaylist = true;

            }
        }
        bnd.shufflePlaylist.setOnClickListener {
            if (isPlayingShuffled) {
                isPlayingShuffled = false;
                bnd.shufflePlaylist.setColorFilter(MyColor.BLACK, PorterDuff.Mode.SRC_IN)
            } else {
                isPlayingShuffled = true;
                bnd.shufflePlaylist.setColorFilter(MyColor.GREEN, PorterDuff.Mode.SRC_IN)
            }
        }

        bnd.listenPlaylistRec.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                bnd.listenPlaylistRec,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val songModal = plist[position]
                        CoroutineScope(Dispatchers.IO).launch {
                            playMusic(songModal.songUrl);
                        }
                    }

                    override fun onLongItemClick(view: View, position: Int) {}
                }
            )
        )
    }

    private suspend fun playMusic(uri: String) {
        Log.d("TAG", "playMusic: ---->playling"+uri)
        withContext(Dispatchers.IO) {
            try {
                mediaPlayer?.apply {
                    reset()
                    setDataSource(uri)
                    prepare()
                    start()
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}