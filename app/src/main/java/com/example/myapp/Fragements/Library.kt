package com.example.myapp.Fragements

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapp.Classes.ReuseThings
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentLibraryBinding
import com.google.firebase.auth.FirebaseAuth

class Library : Fragment() {
    private lateinit var bnd:FragmentLibraryBinding
    private lateinit var auth :FirebaseAuth
      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
          // Inflate the layout for this fragment
          bnd =  FragmentLibraryBinding.inflate(inflater, container, false)
          auth = FirebaseAuth.getInstance()
          bnd.logout.setOnClickListener {
              auth.signOut()
              var intent = Intent(context,MainActivity::class.java);
              startActivity(intent)
              activity?.finish()
          }
          return bnd.root
      }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}