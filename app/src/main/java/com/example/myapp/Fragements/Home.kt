package com.example.myapp.Fragements

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.myapp.R
import com.example.myapp.databinding.FragmentHomeBinding

class Home : Fragment() {
    private lateinit var bnd:FragmentHomeBinding
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bnd = FragmentHomeBinding.inflate(inflater, container, false)
        val textViews = listOf(bnd.all, bnd.music,bnd.playlist,bnd.artist,bnd.podcast)

        bnd.all.setOnClickListener{
            setColor(it,textViews)
        }
        bnd.music.setOnClickListener{
            setColor(it,textViews)
        }
        bnd.playlist.setOnClickListener{
            setColor(it,textViews)
        }
        bnd.podcast.setOnClickListener{
            setColor(it,textViews)
        }
        bnd.artist.setOnClickListener{
            setColor(it,textViews)
        }

        return bnd.root;
    }
    fun setColor(item:View,list:List<TextView>){
        for(i  in list){
            if(i==item){
//                i.setBackgroundColor(getResources().getColor(R.color.green))
                i.setTextColor(getResources().getColor(R.color.black))
                i.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.green))
//              i.setBackgroundResource(R.drawable.greenbg)
            }
            else{
                i.backgroundTintList = ColorStateList.valueOf(getResources().getColor(R.color.sky))
                i.setTextColor(getResources().getColor(R.color.white))
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Now you can access your views using the binding object
//        bnd.textView6 = "Hello, Fragment!"
    }
}