package com.example.myapp.Activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp.Fragements.Home
import com.example.myapp.Fragements.Library
import com.example.myapp.Fragements.Premium
import com.example.myapp.Fragements.Search
import com.example.myapp.R
import com.example.myapp.databinding.ActivityMasterAllPageBinding
import com.google.firebase.storage.FirebaseStorage

class MasterAllPage : AppCompatActivity() {
    private lateinit var bnd: ActivityMasterAllPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityMasterAllPageBinding.inflate(layoutInflater)
        setContentView(bnd.root)
        supportActionBar?.hide()
        replaceFragment(Home())
        bnd.homeIcon.setOnClickListener {
            replaceFragment(Home())
        }
        bnd.searchIcon.setOnClickListener {
            replaceFragment(Search())
        }
        bnd.libraryIcon.setOnClickListener {
            replaceFragment(Library())
        }
        bnd.premiumIcon.setOnClickListener {
            replaceFragment(Premium())
            showCustomDialog()
        }
    }

    private fun replaceFragment(frag: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.replaceFragment, frag)
        transaction.commit()
    }
    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.addanything_dialog)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        val addPlaylist = dialog.findViewById<LinearLayout>(R.id.addPlaylist)
        val addArtist = dialog.findViewById<LinearLayout>(R.id.addArtist)
        val addSongs = dialog.findViewById<LinearLayout>(R.id.addSongs)
        val createPlaylist = dialog.findViewById<LinearLayout>(R.id.createPlaylist)
        val closeDialog = dialog.findViewById<ImageView>(R.id.closeDialog)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val dialogWidth = screenWidth *1.5.toInt()
        dialog.window?.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
        addPlaylist.setOnClickListener {
            dialog.dismiss()
        }

        addArtist.setOnClickListener {
            dialog.dismiss()
        }
        addSongs.setOnClickListener {
            dialog.dismiss()
        }
        createPlaylist.setOnClickListener {
            dialog.dismiss()
        }
        closeDialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}