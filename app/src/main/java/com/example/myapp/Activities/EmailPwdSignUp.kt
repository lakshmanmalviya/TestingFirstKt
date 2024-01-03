package com.example.myapp.Activities
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.Classes.MyColor
import com.example.myapp.Classes.ReuseThings
import com.example.myapp.Classes.STATICVAR
import com.example.myapp.R
import com.example.myapp.databinding.ActivityEmailPwdSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EmailPwdSignUp : AppCompatActivity() {
    private lateinit var bnd: ActivityEmailPwdSignUpBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var myref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityEmailPwdSignUpBinding.inflate(layoutInflater)
        setContentView(bnd?.root)
        supportActionBar?.hide()
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        if(intent.getStringExtra("signup").contentEquals("signup")){
            prepareBox("Creating Account","We are creating your account");
        }
       if(intent.getStringExtra("login").contentEquals("login")){
           bnd.signUpButton.setText("Login")
           prepareBox("Login","We are logging your account");
       }
        bnd.signUpButton.setOnClickListener {
            if(intent.getStringExtra("signup").contentEquals("signup")){
                progressDialog?.show()
                if (checkInput())
                    creatAccount()
                else{
                   ReuseThings.customToast(applicationContext,R.drawable.spotify,"Please Enter valid details");
                    dismissBox()
                }
            }
            if(intent.getStringExtra("login").contentEquals("login")){
                showBox()
                if (checkInput())
                loginAccount()
                else{
                    ReuseThings.customToast(applicationContext,R.drawable.spotify,"Please Enter valid details");
                  dismissBox()
                }
            }
        }
    }

    private fun checkInput(): Boolean {
        return if (bnd.userName.text.toString().trim().isEmpty() or bnd.emailInput.text.isEmpty() or bnd.pwdInput.text.toString().trim().isEmpty()) false else true;
    }
    private fun dismissBox(){
        progressDialog?.dismiss()
    }
    private fun showBox(){
        progressDialog?.show()
    }
    private fun prepareBox(title:String,msg:String){
        progressDialog = ProgressDialog(this)
        progressDialog?.setCancelable(false)
        progressDialog?.setTitle(title)
        progressDialog?.setMessage(msg)
    }
    private fun creatAccount() {
        auth.createUserWithEmailAndPassword(
            bnd.emailInput.text.toString(),
            bnd.emailInput.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val id = task.result?.user?.uid
                    Log.d("ID ======>>>>", id!!)
                    storeUserDetail(id)
                }
            }
            .addOnFailureListener { e ->
                progressDialog?.dismiss()
                ReuseThings.customToast(applicationContext,R.drawable.spotify,"Account creation is failed "+e.message);
            }
    }
    private fun loginAccount(){
        auth.signInWithEmailAndPassword(bnd.emailInput.text.toString().trim(), bnd.pwdInput.text.toString().trim())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressDialog?.dismiss()
                    startActivity(Intent(this, MasterAllPage::class.java))
                    finish()
                } else {
                    progressDialog?.dismiss()
                    ReuseThings.customToast(applicationContext,R.drawable.spotify,"Authentication failed: ${task.exception?.message}")
                }
            }
    }
    private fun resetPossword(){
        auth.sendPasswordResetEmail(bnd.emailInput.text.toString().trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    ReuseThings.customToast(applicationContext,R.drawable.spotify,"Password reset email sent.");
                } else {
                    ReuseThings.customToast(applicationContext,R.drawable.spotify,"Failed to send password reset email.");
                }
            }
    }
    private fun storeUserDetail(id: String?) {
        myref = firebaseDatabase.getReference().child(STATICVAR.signedUpUsersData).child(id!!)
        val map: MutableMap<String, Any> = HashMap()
        map[STATICVAR.userId] = id
        map[STATICVAR.userEmail] = bnd.emailInput.text.toString().trim()
        map[STATICVAR.possword] = bnd.pwdInput.text.toString().trim()
        map[STATICVAR.userName] = bnd.userName.text.toString().trim()
        myref.setValue(map)
            .addOnSuccessListener {
                dismissBox()
                ReuseThings.customToast(applicationContext,R.drawable.spotify,"Account created Successfully");
                bnd.userName.setText("")
                bnd.emailInput.setText("")
                bnd.pwdInput.setText("")
                val intent = Intent(this,MasterAllPage::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                dismissBox()
             ReuseThings.customToast(applicationContext,"Error while saving " + e.message)
            }
    }

//    private fun customToast(image:Int,message: String){
//        val layoutInflater = layoutInflater
//        val toast = Toast(applicationContext)
//        val layout: View = layoutInflater.inflate(R.layout.custom_toast, findViewById(R.id.customeToast))
//        layout.findViewById<ImageView>(R.id.toastImage).setImageResource(image)
//        val text = layout.findViewById<TextView>(R.id.toastMessage)
//        text.text=message
//        text.setTextColor(MyColor.RED);
////        toast.setGravity(Gravity.CENTER, 0, 0)
//        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
//        toast.duration = Toast.LENGTH_SHORT
//        toast.view = layout
//        toast.show()
//    }
}