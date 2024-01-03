package com.example.myapp.Fragements

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Activities.AddSongArtPlaylNMore
import com.example.myapp.Activities.UploadSong
import com.example.myapp.Adapters.PlaylistAdapter
import com.example.myapp.Classes.RecyclerItemClickListener
import com.example.myapp.Classes.ReuseThings
import com.example.myapp.Classes.STATICVAR
import com.example.myapp.Modals.PlaylistModal
import com.example.myapp.R
import com.example.myapp.databinding.FragmentYourPlaylistsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class YourPlaylists : Fragment() {
    private lateinit var bnd: FragmentYourPlaylistsBinding
    private lateinit var adapter: PlaylistAdapter
    private lateinit var playlistModal: PlaylistModal
    private val plist = ArrayList<PlaylistModal>()
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var myDatabase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentYourPlaylistsBinding.inflate(inflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDatabase = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        adapter = PlaylistAdapter(requireContext(), plist)
        bnd.yourpRec.adapter = adapter
        layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager.reverseLayout = false
        bnd.yourpRec.layoutManager = layoutManager
        myDatabase.reference.child(STATICVAR.userPlaylistInfo).child(auth.currentUser?.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        plist.clear()
                        for (dataSnapshot in snapshot.children) {
                            playlistModal =
                                dataSnapshot.getValue(PlaylistModal::class.java) ?: PlaylistModal()
                            plist.add(playlistModal)
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
        bnd.yourpRec.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                bnd.yourpRec,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val playlistModal = plist[position]
                        val intent = Intent(requireContext(), UploadSong::class.java)
                        intent.putExtra(STATICVAR.key, "addSongToPlaylist")
                        intent.putExtra(STATICVAR.playlistId, playlistModal.playlistId)
                        intent.putExtra(STATICVAR.playlistName, playlistModal.playlistName)
                        intent.putExtra(STATICVAR.playlistImage, playlistModal.playlistImage)
                        intent.putExtra(STATICVAR.playlistDesc, playlistModal.playlistDesc)
                        startActivity(intent)
                    }

                    override fun onLongItemClick(view: View, position: Int) {}
                }
            )
        )
    }
}