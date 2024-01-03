package com.example.myapp.Classes

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapp.R
import java.time.LocalDateTime
import java.util.UUID

object ReuseThings {
    fun customToast(context: Context, image: Int, message: String) {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toast = Toast(context)
        val layout: View = layoutInflater.inflate(R.layout.custom_toast, null)
        layout.findViewById<ImageView>(R.id.toastImage).setImageResource(image)
        val text = layout.findViewById<TextView>(R.id.toastMessage)
        text.text = message
        text.setTextColor(MyColor.RED);
//        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    fun customToast(context: Context, message: String) {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toast = Toast(context)
        val layout: View = layoutInflater.inflate(R.layout.custom_toast, null)
        layout.findViewById<ImageView>(R.id.toastImage).setImageResource(R.drawable.spotify)
        val text = layout.findViewById<TextView>(R.id.toastMessage)
        text.text = message
        text.setTextColor(MyColor.RED)
//        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    fun getTimeStamp(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().toString()
        } else {
            return UUID.randomUUID().toString()
        };
    }
    // same work we will do for the alertDialog with cancelable false hurray!!
}