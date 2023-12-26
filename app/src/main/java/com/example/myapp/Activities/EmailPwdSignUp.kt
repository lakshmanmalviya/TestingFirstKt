package com.example.myapp.Activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.Classes.STATICVAR
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
        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Creating Account")
        progressDialog?.setMessage("We are creating your account")
        progressDialog?.setCancelable(false)
        bnd.signUpButton.setOnClickListener {
            progressDialog?.show()
            if (checkInput())
                creatAccount()
            else
                Toast.makeText(applicationContext, "Please Enter valid details", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun checkInput(): Boolean {
        return if (bnd.userName.text.isEmpty() or bnd.emailInput.text.isEmpty() or bnd.pwdInput.text.isEmpty()) false else true;
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
                Toast.makeText(
                    applicationContext,
                    "Account creation is failed " + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun storeUserDetail(id: String?) {
        myref = firebaseDatabase.getReference().child(STATICVAR.users).child(id!!)
        val map: MutableMap<String, Any> = HashMap()
        map[STATICVAR.userId] = id
        map[STATICVAR.userEmail] = bnd.emailInput.text.toString().trim()
        map[STATICVAR.possword] = bnd.pwdInput.text.toString().trim()
        map[STATICVAR.userName] = bnd.userName.text.toString().trim()
        myref.setValue(map)
            .addOnSuccessListener {
                progressDialog?.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Account created Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                bnd.userName.setText("")
                bnd.emailInput.setText("")
                bnd.pwdInput.setText("")
            }
            .addOnFailureListener { e ->
                progressDialog?.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Error while saving  " + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}


//
//
//----------------------------------------------------------------
//package com.example.attendance.TeacherActivity
//
//import android.app.ProgressDialog
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.attendance.databinding.ActivitySignUpBinding
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//
//class SignUp : AppCompatActivity() {
//    private lateinit var bnd: ActivitySignUpBinding
//    private lateinit var myAuth: FirebaseAuth
//    private lateinit var database: FirebaseDatabase
//    private lateinit var dialog: ProgressDialog
//    private lateinit var myref: DatabaseReference
//
//    private val allTeacher = "allTeacher"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        bnd = ActivitySignUpBinding.inflate(layoutInflater)
//        setContentView(bnd.root)
//
//        myAuth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance()
//        dialog = ProgressDialog(this)
//        dialog.setTitle("Creating Account")
//        dialog.setMessage("We are creating your account")
//
//        bnd.signUpSubmit.setOnClickListener {
//            if (bnd.singUpEmail.text.toString().trim().isEmpty() || bnd.signUPwd.text.toString().trim().isEmpty() || bnd.signUpTeacherName.text.toString().trim().isEmpty()) {
//                Toast.makeText(applicationContext, "please provide asked details", Toast.LENGTH_SHORT).show()
//            } else {
//                dialog.show()
//                signUpTeacher()
//            }
//        }
//    }
//
//    private fun signUpTeacher() {
//        myAuth.createUserWithEmailAndPassword(bnd.singUpEmail.text.toString().trim(), bnd.signUPwd.text.toString().trim())
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    val id = task.result?.user?.uid
//                    Log.d("ID ======>>>>", id!!)
//                    storeTeacherDetail(id)
//                    dialog.dismiss()
//                } else {
//                    Toast.makeText(applicationContext, "SignUp is not Succeeded", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(applicationContext, "Account creation is failed " + e.message, Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun storeTeacherDetail(id: String?) {
//        myref = database.getReference().child(allTeacher).child(id!!)
//        val map: MutableMap<String, Any> = HashMap()
//        map["teacherName"] = bnd.signUpTeacherName.text.toString().trim()
//        map["teacherEmpId"] = bnd.singUpTeacherEmpId.text.toString().trim()
//        map["deptName"] = bnd.singUpTeacherDeptName.text.toString().trim()
//        map["teacherSubName"] = bnd.signupTeacherSubName.text.toString().trim()
//        map["teacherId"] = myref.key!!
//        map["email"] = bnd.singUpEmail.text.toString().trim()
//        map["possword"] = bnd.signUPwd.text.toString().trim()
//
//        myref.setValue(map)
//            .addOnSuccessListener {
//                Toast.makeText(applicationContext, "Storing teacher details", Toast.LENGTH_SHORT).show()
//                bnd.singUpEmail.setText("")
//                bnd.signUPwd.setText("")
//                bnd.signUpTeacherName.setText("")
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(applicationContext, "Error while storing  " + e.message, Toast.LENGTH_SHORT).show()
//            }
//    }
//}
