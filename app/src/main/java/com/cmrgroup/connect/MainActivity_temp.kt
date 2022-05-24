package com.cmrgroup.connect

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity_temp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        login_email.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                login_email.setBackgroundResource(R.drawable.login_input_fields)
                login_email.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.custom_email_icon,
                    0,
                    0,
                    0
                )
            } else {
                val email = login_email.text.toString()
                if (email.isEmpty()) {
                    login_email.setBackgroundResource(R.drawable.login_input_fields_error)
                    login_email.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.custom_email_icon_err,
                        0,
                        0,
                        0
                    )

                    Toast.makeText(
                        applicationContext,
                        "Please enter your Email ID",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!email.endsWith("@cmr.edu.in")) {
                    login_email.setBackgroundResource(R.drawable.login_input_fields_error)
                    login_email.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.custom_email_icon_err,
                        0,
                        0,
                        0
                    )

                    Toast.makeText(applicationContext, "Invalid Email ID", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    login_email.setBackgroundResource(R.drawable.login_input_fields)
                    login_email.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.custom_email_icon,
                        0,
                        0,
                        0
                    )
                }
            }
        }


//        login_email.setOnKeyListener(View.OnKeyListener{v, keyCode, event ->
//            if(event.action == KeyEvent.ACTION_DOWN) {
//                login_email.setBackgroundResource(R.drawable.login_input_fields)
//                login_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email_icon, 0, 0, 0)
//            }
//            Log.d("Main", "Works?" )
////            login_email.setBackgroundResource(R.drawable.login_input_fields)
////            login_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email_icon, 0, 0, 0)
//            false
//        })

        login_email.setOnClickListener(){

            login_email.setBackgroundResource(R.drawable.login_input_fields)
            login_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email_icon, 0, 0, 0)

            login_password.setBackgroundResource(R.drawable.login_input_fields)
            login_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_lock_icon, 0, 0, 0)
        }

        login_password.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                login_password.setBackgroundResource(R.drawable.login_input_fields)
                login_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_lock_icon, 0, 0, 0)
            } else{
                login_password.setBackgroundResource(R.drawable.login_input_fields)
                login_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_lock_icon, 0, 0, 0)
            }
        }

        login_password.setOnClickListener(){
            login_password.setBackgroundResource(R.drawable.login_input_fields)
            login_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_lock_icon, 0, 0, 0)

            login_email.setBackgroundResource(R.drawable.login_input_fields)
            login_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email_icon, 0, 0, 0)
        }


        login_button.setOnClickListener(){
            val email = login_email.text.toString()
            val pass = login_password.text.toString()

            val inputManager:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

            if(email.isEmpty() && pass.isEmpty()){
                login_email.setBackgroundResource(R.drawable.login_input_fields_error)
                login_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email_icon_err, 0, 0, 0)

                login_password.setBackgroundResource(R.drawable.login_input_fields_error)
                login_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_lock_icon_err, 0, 0,0)

                Toast.makeText(applicationContext, "Please enter your credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if(email.isEmpty()) {
                login_email.setBackgroundResource(R.drawable.login_input_fields_error)
                login_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email_icon_err, 0, 0, 0)

                Toast.makeText(applicationContext, "Please enter your Email ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pass.isEmpty()) {
                login_password.setBackgroundResource(R.drawable.login_input_fields_error)
                login_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_lock_icon_err, 0, 0,0)

                Toast.makeText(applicationContext, "Please enter your Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!email.endsWith("@cmr.edu.in")){
                login_email.setBackgroundResource(R.drawable.login_input_fields_error)
                login_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email_icon_err, 0, 0, 0)

                Toast.makeText(applicationContext, "Invalid Email ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if(it.isSuccessful) {
                    val sh = getSharedPreferences("CMRU", MODE_PRIVATE)
                    sh.edit().putBoolean("login", true).apply()
                    sh.edit().putString("email", email).apply()

                    val db = Firebase.firestore.collection("user")

                    db.document(email).get()
                        .addOnSuccessListener { document ->
                            if(document.exists()){
                                val profileComplete = document["profile_complete"].toString()

                                when(profileComplete){
                                    "1"->{
                                        val i = Intent(this, HomeActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    }
                                    "0"-> {
                                        val i = Intent(this, SetUpActivity1::class.java)
                                        startActivity(i)
                                        finish()
                                    }
                                }
                                Toast.makeText(applicationContext, "Successfully logged in" , Toast.LENGTH_SHORT).show()
                            }
                            else{
                                val data = hashMapOf(
                                    "email" to email,
                                    "profile_complete" to "0"
                                )
                                db.document(email).set(data)

                                val i = Intent(this, SetUpActivity1::class.java)
                                startActivity(i)
                                finish()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, it.message.toString() + "\nContact Developer", Toast.LENGTH_SHORT).show()
                        }
                }
                else{

                    if(it.exception?.message?.lowercase()!!.contains("network")){
                        Toast.makeText(applicationContext, "Please check your network and try again", Toast.LENGTH_SHORT).show()
                    } else if(it.exception?.message?.lowercase()!!.contains("invalid")){
                        login_email.setBackgroundResource(R.drawable.login_input_fields_error)
                        login_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email_icon_err, 0, 0, 0)

                        login_password.setBackgroundResource(R.drawable.login_input_fields_error)
                        login_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_lock_icon_err, 0, 0,0)

                        Toast.makeText(applicationContext, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                    } else if(it.exception?.message?.lowercase()!!.contains("blocked")){
                        Toast.makeText(applicationContext, "Too many invalid attempts.\nPlease try again later", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext, it.exception!!.message.toString() + "\nPlease Contact Developer", Toast.LENGTH_SHORT).show()
                    }

                    Log.d("xxx", it.exception?.javaClass.toString())
                }
            }
        }
    }
}