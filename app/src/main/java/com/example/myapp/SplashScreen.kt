package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.delay

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        Thread(){
            run {
                try {
                   Thread.sleep(2000)
                }catch (e:Exception){
                    Toast.makeText(applicationContext,e.message,Toast.LENGTH_SHORT).show()
                }
                finally {
                    var intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }
}