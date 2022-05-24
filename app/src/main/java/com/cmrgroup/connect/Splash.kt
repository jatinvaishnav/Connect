package com.cmrgroup.connect

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        //window.statusBarColor = ContextCompat.getColor(this, R.color.cmr_green)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

//        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = Color.TRANSPARENT

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val user = FirebaseAuth.getInstance().currentUser


        val flag = user != null

        if(flag){
            Handler().postDelayed({
                val db = Firebase.firestore.collection("user")
                val email = user?.email

                if (email != null) {
                    db.document(email).get()
                        .addOnSuccessListener { jatin ->
                            if(jatin.exists()){
                                val profileComplete = jatin["profile_complete"].toString()

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
                            } else{
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
                }else{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }, 2000)
        }else{
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }
}