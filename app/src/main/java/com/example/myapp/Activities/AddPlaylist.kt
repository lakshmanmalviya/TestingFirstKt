package com.example.myapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.example.myapp.databinding.ActivityAddPlaylistBinding
import com.example.myapp.databinding.ActivityMasterAllPageBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddPlaylist : AppCompatActivity() {
    private lateinit var bnd: ActivityAddPlaylistBinding
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var myref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityAddPlaylistBinding.inflate(layoutInflater)
        setContentView(bnd.root)

    }
}