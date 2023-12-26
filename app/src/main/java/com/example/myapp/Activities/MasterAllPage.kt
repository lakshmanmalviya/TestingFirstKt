package com.example.myapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        }
//        val button = findViewById<Button>(R.id.buttonShowDialog)
//        button.setOnClickListener {
//            showCustomDialogWithAnimation()
//        }

    }

    private fun replaceFragment(frag: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.replaceFragment, frag)
        transaction.commit()
    }
}