package com.example.myapp.Fragements

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.Adapters.PlaylistAdapter
import com.example.myapp.Classes.RecyclerItemClickListener
import com.example.myapp.Classes.ReuseThings
import com.example.myapp.Classes.STATICVAR
import com.example.myapp.Modals.PlaylistModal
import com.example.myapp.R
import com.example.myapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : Fragment() {
    private lateinit var bnd: FragmentHomeBinding
    private lateinit var adapter: PlaylistAdapter
    private lateinit var playlistModal: PlaylistModal
    private val plist = ArrayList<PlaylistModal>()
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var myDatabase: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentHomeBinding.inflate(inflater, container, false)
        return bnd.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDatabase = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        adapter = PlaylistAdapter(requireContext(), plist)
        bnd.playlistRec.adapter = adapter
        layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager.reverseLayout = false
        bnd.playlistRec.layoutManager = layoutManager
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

        val textViews = listOf(bnd.all, bnd.music, bnd.playlist, bnd.artist, bnd.podcast)
        bnd.all.setOnClickListener {
            setColor(it, textViews)
        }
        bnd.music.setOnClickListener {
            setColor(it, textViews)
        }
        bnd.playlist.setOnClickListener {
            setColor(it, textViews)
        }
        bnd.podcast.setOnClickListener {
            setColor(it, textViews)
        }
        bnd.artist.setOnClickListener {
            setColor(it, textViews)
        }
        bnd.playlistRec.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                bnd.playlistRec,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val playlistModal = plist[position]
                        val bundle = Bundle()
                        bundle.putString(STATICVAR.playlistImage, playlistModal.playlistImage)
                        bundle.putString(STATICVAR.playlistId, playlistModal.playlistId)
                        bundle.putString(STATICVAR.playlistDesc, playlistModal.playlistDesc)
                        bundle.putString(STATICVAR.playlistName, playlistModal.playlistName)
                        val listenPlaylist = ListenPlaylist()
                        listenPlaylist.arguments = bundle
                        replaceFragment(listenPlaylist)
                    }
                    override fun onLongItemClick(view: View, position: Int) {}
                }
            )
        )
    }

    fun setColor(selectedView: View, textViews: List<TextView>) {
        val selectedColor = requireContext().getColor(R.color.green)
        val defaultColor = requireContext().getColor(R.color.sky)
        val selectedTextColor = requireContext().getColor(R.color.black)
        val defaultTextColor = requireContext().getColor(R.color.white)

        textViews.forEach { textView ->
            if (textView == selectedView) {
                textView.setTextColor(selectedTextColor)
                textView.backgroundTintList = ColorStateList.valueOf(selectedColor)
            } else {
                textView.setTextColor(defaultTextColor)
                textView.backgroundTintList = ColorStateList.valueOf(defaultColor)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
// Create an instance of ListenPlaylistFragment
        val tag = fragment.javaClass.simpleName
        val fragmentManager = requireActivity().supportFragmentManager
        val currentFragment = fragmentManager.findFragmentByTag(tag)
        if (currentFragment == null) {
            fragmentManager.beginTransaction()
                .replace(R.id.homeFragment, fragment, tag)
                .addToBackStack(tag)
                .commit()
        }
    }
}


//    private fun replaceFragment(fragment: Fragment) {
//        val tag = fragment.javaClass.simpleName
//        val fragmentManager = requireActivity().supportFragmentManager // For fragments
//        val currentFragment = fragmentManager.findFragmentByTag(tag)
//          val existingFragment = fragmentManager.findFragmentById(R.id.homeFragment)
//           fragmentManager.beginTransaction()
//                    .replace(R.id.homeFragment, fragment, tag)
//                    .addToBackStack(tag)
//                    .commit()
//    }
