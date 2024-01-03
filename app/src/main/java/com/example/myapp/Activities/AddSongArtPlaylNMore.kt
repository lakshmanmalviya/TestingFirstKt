package com.example.myapp.Activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ActionMenuView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.myapp.Classes.ReuseThings
import com.example.myapp.Classes.STATICVAR
import com.example.myapp.databinding.ActivityAddSongArtPlaylNmoreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.collections.HashMap

class AddSongArtPlaylNMore : AppCompatActivity() {
    private lateinit var bnd: ActivityAddSongArtPlaylNmoreBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var progressDialog: ProgressDialog? = null
    private var currentPhotoPath: String? = null
    private var key: String? = null
    private var imageUrl: String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var myref: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityAddSongArtPlaylNmoreBinding.inflate(layoutInflater)
        setContentView(bnd.root)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        progressDialog = ProgressDialog(this)
        key = intent.getStringExtra("key")
        when (key) {
            "song" -> {
                bnd.sapnMoreName.setHint("Enter song name ")
                bnd.desSingNameMore.setHint("Enter singer name ")
                bnd.desSingNameMore.visibility = View.VISIBLE
            }
            "playlist" -> {
                bnd.sapnMoreName.setHint("Enter playlist name ")
                bnd.desSingNameMore.setHint("Enter playlist description name ")
                bnd.desSingNameMore.visibility = View.VISIBLE
            }
            "artist" -> {
                bnd.sapnMoreName.setHint("Enter artist name ")
                bnd.desSingNameMore.visibility = View.GONE
            }
        }
        bnd.submitSAPButton.setOnClickListener {
            if (checkInput()) {
                storePlaylistIntoRTFirebase();
            } else {
                ReuseThings.customToast(applicationContext, "Enter valid details ")
            }
        }
        prepareBox("Uploading...", "   " + 0 + "% Uploaded 🏍");
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri = result.data?.data
                    imageUri?.let {
                        uploadImageToFirebase(it)
                        showBox()
                        bnd.browseSAPImage.setImageURI(imageUri)
                    }
                }
            }
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    currentPhotoPath?.let {
                        val imageUri = Uri.fromFile(File(it))
                        uploadImageToFirebase(Uri.parse(imageUri.toString()))
                        showBox()
                        bnd.browseSAPImage.setImageURI(imageUri)
                    }
                }
            }
        bnd.gallary.setOnClickListener {
            openGallery()
        }
        bnd.camera.setOnClickListener {
            openCamera()
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }

    private fun openCamera() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }

        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.myapp.fileprovider",
                it
            )

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraLauncher.launch(photoURI)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageReference =
            FirebaseStorage.getInstance().reference.child(auth.currentUser?.uid + "/" + key + "/${ReuseThings.getTimeStamp()}")
        val uploadTask = storageReference.putFile(imageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                imageUrl = uri.toString()
            }.addOnFailureListener { exception ->
                // Handle failure to upload the image
            }
        }.addOnFailureListener { exception ->
            // Handle failures
        }.addOnProgressListener { taskSnapshot ->
            val progress =
                (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            prepareBox("Uploading...", "   " + progress + "% Uploaded 🏍");
            if (progress >= 99)
                dismissBox()
        }
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

    private fun checkInput(): Boolean {
        if (key.contentEquals("artist"))
            return if (bnd.sapnMoreName.text.toString().trim().isEmpty()) false else true;
        else
            return if (bnd.sapnMoreName.text.toString().trim()
                    .isEmpty() or bnd.desSingNameMore.text.toString().trim().isEmpty()
            ) false else true
    }

    private fun storePlaylistIntoRTFirebase() {
        prepareBox("uploading...", "uploading playlist detail")
        showBox()
        myref = firebaseDatabase.getReference().child(STATICVAR.userPlaylistInfo)
            .child(auth.currentUser?.uid!!).push()
        val map: MutableMap<String, Any> = HashMap()
        map[STATICVAR.playlistId] = myref.key.toString()
        map[STATICVAR.playlistName] = bnd.sapnMoreName.text.toString().trim()
        map[STATICVAR.playlistDesc] = bnd.desSingNameMore.text.toString().trim()
        map[STATICVAR.playlistImage] = imageUrl
        myref.setValue(map)
            .addOnSuccessListener {
                dismissBox()
                ReuseThings.customToast(applicationContext, "Playlist Added Successfully");
                bnd.sapnMoreName.setText("")
                bnd.desSingNameMore.setText("")
            }
            .addOnFailureListener { e ->
                dismissBox()
                ReuseThings.customToast(applicationContext, "Error while saving " + e.message)
            }
    }
}