package com.example.myapp.Activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp.Fragements.*
import com.example.myapp.R
import com.example.myapp.databinding.ActivityMasterAllPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlin.random.Random

class MasterAllPage : AppCompatActivity() {
    private lateinit var bnd: ActivityMasterAllPageBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityMasterAllPageBinding.inflate(layoutInflater)
        setContentView(bnd.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
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

    private fun replaceFragment(fragment: Fragment) {
            val tag = fragment.javaClass.simpleName
            val fragmentManager = supportFragmentManager
            val currentFragment = fragmentManager.findFragmentByTag(tag)
            if (currentFragment == null) {
                val existingFragment = fragmentManager.findFragmentById(R.id.replaceFragment)
                if (existingFragment == null || existingFragment.javaClass != fragment.javaClass) {
                    fragmentManager.beginTransaction()
                        .replace(R.id.replaceFragment, fragment, tag)
                        .addToBackStack(tag)
                        .commit()
                }
            } else {
                // Fragment is already in the back stack, do not add it again
                fragmentManager.popBackStack(tag, 0) // Pops the back stack up to the given tag
            }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.addanything_dialog)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        val addPlaylist = dialog.findViewById<LinearLayout>(R.id.addPlaylist)
        val addArtist = dialog.findViewById<LinearLayout>(R.id.addArtist)
        val addSongs = dialog.findViewById<LinearLayout>(R.id.addSongs)
        val addSongToPlaylist = dialog.findViewById<LinearLayout>(R.id.addSongToPlaylist)
        val closeDialog = dialog.findViewById<ImageView>(R.id.closeDialog)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val dialogWidth = screenWidth * 1.5.toInt()
        dialog.window?.setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT)
        addPlaylist.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this,AddSongArtPlaylNMore::class.java);
            intent.putExtra("key","playlist")
            startActivity(intent)
        }
        addSongToPlaylist.setOnClickListener {
            dialog.dismiss()
//            val intent = Intent(this,AddSongArtPlaylNMore::class.java);
////          intent.putExtra("key","playlist")
//            startActivity(intent)
           replaceFragment(YourPlaylists());
        }

        addArtist.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this,AddSongArtPlaylNMore::class.java);
            intent.putExtra("key","artist")
            startActivity(intent)
        }
        addSongs.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this,AddSongArtPlaylNMore::class.java);
            intent.putExtra("key","song")
            startActivity(intent)
        }
        closeDialog.setOnClickListener {
            dialog.dismiss()
         //   Random.nextInt(0,100)
        }
        dialog.show()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount==1){
            finishAffinity()
        }
        if (supportFragmentManager.backStackEntryCount >1){
                supportFragmentManager.popBackStack()
        }
        else{
          super.onBackPressed()
        }

//        val currentFragment = supportFragmentManager.findFragmentById(R.id.replaceFragment)
//        // Check if the current fragment is of type Home
//        if (currentFragment is Home && auth.currentUser != null) {
//            finishAffinity()
//        }
//        else{
//            if (supportFragmentManager.backStackEntryCount >=1)
//                supportFragmentManager.popBackStack()
//            else  super.onBackPressed()
//        }
    }
}