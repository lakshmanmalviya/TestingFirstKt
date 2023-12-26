package com.example.myapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.view.isVisible
import com.example.myapp.databinding.ActivitySecondBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Second : AppCompatActivity() {
    private var isShowing: Boolean = true;
    private lateinit var mediaPlayer: MediaPlayer

    //  private  val handler=Handler()
    private lateinit var bnd: ActivitySecondBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(bnd.root)
        mediaPlayer = MediaPlayer()
        bnd.play.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                playMusic("https://firebasestorage.googleapis.com/v0/b/musicmg-9d5bf.appspot.com/o/allSong%2FallSong-11691139001181.mp3?alt=media&token=1f0758ff-2cfe-45dd-bef8-2f632fc98f4c")
            }
        }
        bnd.next.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                playMusic("https://firebasestorage.googleapis.com/v0/b/musicmg-9d5bf.appspot.com/o/allSong%2Finspiring-cinematic-ambient-116199.mp3?alt=media&token=f4ded183-a442-45ef-a98b-6cde12865dd4")
            }
        }
    }

    private suspend fun playMusic(uri: String) {
        withContext(Dispatchers.IO) {
            try {
                mediaPlayer?.apply {
                    reset()
                    setDataSource(uri)
                    prepare()
                    start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}


//        val colorChangeRunnable = object : Runnable {
//            override fun run() {
//                 if(isShowing){
//                      bnd.button.visibility = View.GONE
//                      bnd.button6.visibility = View.GONE
//                      bnd.button7.visibility = View.GONE
//                      bnd.button8.visibility = View.GONE
//                      bnd.button9.visibility = View.GONE
//                      bnd.button10.visibility = View.GONE
//                     isShowing = !isShowing
//                 }
//                else{
//                     bnd.button.visibility = View.VISIBLE
//                     bnd.button6.visibility = View.VISIBLE
//                     bnd.button7.visibility = View.VISIBLE
//                     bnd.button8.visibility = View.VISIBLE
//                     bnd.button9.visibility = View.VISIBLE
//                     bnd.button10.visibility =View.VISIBLE
//                     isShowing = !isShowing
//                 }
//                handler.postDelayed(this, 5000) // Run the Runnable again after 10 seconds
//            }
//        }
//        handler.post(colorChangeRunnable)
