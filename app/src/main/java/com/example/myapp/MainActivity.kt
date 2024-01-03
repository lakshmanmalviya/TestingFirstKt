package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapp.Activities.EmailPwdSignUp
import com.example.myapp.Activities.MasterAllPage
import com.example.myapp.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var bnd: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd?.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance();
        connectWithFirebase()
        if(auth.currentUser!=null){
          var intent = Intent(this, MasterAllPage::class.java)
            startActivity(intent)
            finish()
        }
        bnd.manualSign.setOnClickListener {
            var intent = Intent(applicationContext, EmailPwdSignUp::class.java)
            intent.putExtra("signup","signup");
            startActivity(intent)
        }

        bnd.login.setOnClickListener {
            var intent = Intent(applicationContext, EmailPwdSignUp::class.java)
            intent.putExtra("login","login");
            startActivity(intent)
        }
    }
    private fun connectWithFirebase(){
        // hittting the request
        var rand = Random.nextInt(1,100)
        var rand2 = Random.nextInt(1,100)
        FirebaseDatabase.getInstance().getReference("request").setValue("Hitted request with rand "+rand+rand2).addOnCompleteListener(
            OnCompleteListener {
                //nothing just for smooth execution
            })
    }

    override fun onBackPressed() {
        Log.d("pressed","on backpressed");
        super.onBackPressed()
        finishAffinity()
    }
}