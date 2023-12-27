package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapp.Activities.EmailPwdSignUp
import com.example.myapp.Activities.MasterAllPage
import com.example.myapp.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    private lateinit var bnd: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd?.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance();
        if(auth.currentUser!=null){
           //send to home activity/master
        }
        bnd.manualSign.setOnClickListener {
            var intent = Intent(applicationContext, EmailPwdSignUp::class.java)
            intent.putExtra("signup","signup");
            startActivity(intent)
        }
        bnd.mobileNumber.setOnClickListener {
            var intent = Intent(applicationContext, MasterAllPage::class.java)
            startActivity(intent)
        }
        bnd.login.setOnClickListener {
            var intent = Intent(applicationContext, EmailPwdSignUp::class.java)
            intent.putExtra("login","login");
            startActivity(intent)
        }
    }
}