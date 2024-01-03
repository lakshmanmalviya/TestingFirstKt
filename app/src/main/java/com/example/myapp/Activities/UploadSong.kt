package com.example.myapp.Activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.TextUtils
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapp.Classes.ReuseThings
import com.example.myapp.Classes.STATICVAR
import com.example.myapp.databinding.ActivityUploadSongBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class UploadSong : AppCompatActivity() {
    private lateinit var bnd: ActivityUploadSongBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var pickAudioFile: ActivityResultLauncher<String?>
    private lateinit var imageLauncher: ActivityResultLauncher<Intent>
    private var progressDialog: ProgressDialog? = null
    private lateinit var myref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var key: String? = null
    private var audioUrl: String = ""
    private var imageUrl: String = ""
    private var audioFileName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityUploadSongBinding.inflate(layoutInflater)
        setContentView(bnd.root)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        progressDialog = ProgressDialog(this)
        key = intent.getStringExtra("key")
        bnd.browseSong.setOnClickListener {
            selectAudioFile()
        }
        bnd.browseSongImage.setOnClickListener {
            pickImage()
        }
        bnd.addFromFile.setOnClickListener {
            bnd.songName.setText(bnd.songName.text.toString() + audioFileName)
        }
        bnd.uploadSongButton.setOnClickListener {
            if(checkInput())
            uploadSongInfoToFirebase()
            else
                ReuseThings.customToast(applicationContext,"Select Files")
        }
        imageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri = result.data?.data
                    imageUri?.let {
                        uploadImageToFirebase(it)
                        showBox()
                        bnd.setSongImg.setImageURI(imageUri)
                    }
                }
            }
        pickAudioFile =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    showBox()
                    audioUrl = uri.toString()
                    audioFileName = getFileNameFromUri(uri)
                    bnd.fileName.text = audioFileName
                    bnd.fileName.maxLines = 1
                    bnd.fileName.ellipsize = TextUtils.TruncateAt.END
                    uploadAudioToFirebase(uri)
                }
            }
    }


    private fun selectAudioFile() {
        pickAudioFile.launch("audio/*")
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        imageLauncher.launch(intent)
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        if (key.contentEquals("addSongToPlaylist")) {
            val storageReference = FirebaseStorage.getInstance().reference
                .child(auth.currentUser?.uid + "/" + STATICVAR.playlist + "/" + intent.getStringExtra("playlistId") + "/" + STATICVAR.playlistSongImg + "/" + "/${ReuseThings.getTimeStamp()}")
            uploadImageFile(storageReference, imageUri)
        }
    }

    private fun uploadImageFile(storageReference: StorageReference, imageUri: Uri) {
        val uploadTask = storageReference.putFile(imageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                imageUrl = uri.toString()
            }.addOnFailureListener { exception ->
            }
        }.addOnFailureListener { exception ->
            dismissBox()
        }.addOnProgressListener { taskSnapshot ->
            val progress =
                (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            prepareBox("Uploading...", "   $progress% Uploaded 🏍")
            if (progress >= 99)
                dismissBox()
        }
    }

    private fun uploadAudioToFirebase(audioUri: Uri) {
        if (key.contentEquals("addSongToPlaylist")) {
            val storageReference = FirebaseStorage.getInstance().reference
                .child(auth.currentUser?.uid + "/" + STATICVAR.playlist + "/" + intent.getStringExtra("playlistId") + "/" + STATICVAR.playlistSongFiles + "/" + "/${ReuseThings.getTimeStamp()}")
//            val storageReference2 = FirebaseStorage.getInstance().reference
//                .child(auth.currentUser?.uid + "/" + key + "/"+intent.getStringExtra("playlistId")+"/"+STATICVAR.playlistSongFiles+"/"+"/${ReuseThings.getTimeStamp()}")
//            uploadImageFile(storageReference, audioUri)
            uploadAudioFile(storageReference, audioUri)
        }
//        if (key.contentEquals("song")) {
//            val storageReference = FirebaseStorage.getInstance().reference
//                .child(auth.currentUser?.uid + "/" + key + "/${UUID.randomUUID()}")
//        }
//        if (key.contentEquals("Artist")) {
//            val storageReference = FirebaseStorage.getInstance().reference
//                .child(auth.currentUser?.uid + "/" + key + "/${UUID.randomUUID()}")
//        }
    }

    private fun prepareBox(title: String, msg: String) {
        progressDialog?.setCancelable(false)
        progressDialog?.setTitle(title)
        progressDialog?.setMessage(msg)
    }

    private fun dismissBox() {
        progressDialog?.dismiss()
    }

    private fun showBox() {
        progressDialog?.show()
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }
    }

    private fun uploadAudioFile(storageReference: StorageReference, audioUri: Uri) {
        val uploadTask = storageReference.putFile(audioUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            storageReference.downloadUrl.addOnSuccessListener { uri ->
               audioUrl = uri.toString()
            }.addOnFailureListener { exception ->
            }
        }.addOnFailureListener { exception ->
            dismissBox()
        }.addOnProgressListener { taskSnapshot ->
            val progress =
                (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            prepareBox("Uploading...", "   $progress% Uploaded 🏍")
            if (progress >= 99)
                dismissBox()
        }
    }
    private fun uploadSongInfoToFirebase(){
        prepareBox("uploading...", "uploading song detail")
        showBox()
        myref = firebaseDatabase.getReference().child(STATICVAR.userPlaylistInfo)
            .child(auth.currentUser?.uid!!).child(intent.getStringExtra(STATICVAR.playlistId)+"").child(STATICVAR.songs).push()
        val map: MutableMap<String, Any> = HashMap()
        map[STATICVAR.songId] = myref.key.toString()
        map[STATICVAR.songName] = bnd.songName.text.toString().trim()
        map[STATICVAR.singerName] = bnd.singerName.text.toString().trim()
        map[STATICVAR.songImage] = imageUrl
        map[STATICVAR.songUrl] = audioUrl
        myref.setValue(map)
            .addOnSuccessListener {
                dismissBox()
                ReuseThings.customToast(applicationContext, "Song Uploaded Successfully");
                bnd.songName.setText("")
                bnd.singerName.setText("")
            }
            .addOnFailureListener { e ->
                dismissBox()
                ReuseThings.customToast(applicationContext, "Error while saving " + e.message)
            }
    }
    private fun checkInput():Boolean{
        return if(imageUrl.isEmpty()|| audioUrl.isEmpty()||bnd.songName.text.toString().isEmpty())false else true;
    }
}