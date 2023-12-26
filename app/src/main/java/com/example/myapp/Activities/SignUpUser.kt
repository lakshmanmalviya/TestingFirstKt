package com.example.myapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp.databinding.ActivitySignUpUserBinding

class SignUpUser : AppCompatActivity() {
    private lateinit var bnd:ActivitySignUpUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivitySignUpUserBinding.inflate(layoutInflater)
        setContentView(bnd.root)

    }
}