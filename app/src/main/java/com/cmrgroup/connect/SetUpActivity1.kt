package com.cmrgroup.connect

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.transition.Scene
import androidx.transition.TransitionManager
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_set_up1.*
import kotlinx.android.synthetic.main.setup_frame1.*
import kotlinx.android.synthetic.main.setup_frame1.blurView_setup
import kotlinx.android.synthetic.main.setup_frame2.*
import kotlinx.android.synthetic.main.setup_frame3.*
import kotlinx.android.synthetic.main.setup_frame3.change_button_setup
import kotlinx.android.synthetic.main.setup_frame3.fact_text_setup
import kotlinx.android.synthetic.main.setup_frame3.profile_image_setup
import kotlinx.android.synthetic.main.setup_frame3.remove_button_setup
import kotlinx.android.synthetic.main.setup_frame3.setup_continue
import kotlinx.android.synthetic.main.setup_frame3.setup_skip
import kotlinx.android.synthetic.main.setup_frame3.blurView_setup1
import kotlinx.android.synthetic.main.setup_frame4.*
import kotlinx.android.synthetic.main.setup_frame4.setup_back
import kotlinx.android.synthetic.main.setup_frame5.*
import java.util.*
import kotlinx.android.synthetic.main.setup_frame3.blurView_setup1 as blurView_setup11

class SetUpActivity1 : AppCompatActivity() {

    var image : Uri? = null

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up1)

        val sh = getSharedPreferences("CMRU", MODE_PRIVATE)

        sh.edit().clear().apply()

//        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
//        supportActionBar?.setDisplayShowCustomEnabled(true)
//        supportActionBar?.setCustomView(R.layout.custom_action_bar_set_up)
//        supportActionBar?.setBackgroundDrawable(
//            ColorDrawable(
//                ContextCompat.getColor(
//                    this,
//                    R.color.cmr_green
//                )
//            )
//        )
//
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        val frame0 = Scene.getSceneForLayout(setup_frame_layout, R.layout.setup_frame0, this)
        val frame01 = Scene.getSceneForLayout(setup_frame_layout, R.layout.setup_frame01, this)
        val frame1 = Scene.getSceneForLayout(setup_frame_layout, R.layout.setup_frame1, this)
        //val frame2 = Scene.getSceneForLayout(setup_frame_layout, R.layout.setup_frame2, this)
        val frame3 = Scene.getSceneForLayout(setup_frame_layout, R.layout.setup_frame3, this)
        val frame4 = Scene.getSceneForLayout(setup_frame_layout, R.layout.setup_frame4, this)
        val frame5 = Scene.getSceneForLayout(setup_frame_layout, R.layout.setup_frame5, this)
        var currentFrame: Scene

        frame0.enter()

        currentFrame = frame0

        var frame3_flag = false
        var frame4_flag = false
        var frame5_flag = false

        setup_frame_layout.addOnLayoutChangeListener(View.OnLayoutChangeListener{ v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            when (currentFrame){

                frame0 -> {
                    blurView_setup.setupWith(window.decorView.findViewById(android.R.id.content))
                        .setFrameClearDrawable(window.decorView.background)
                        .setBlurAlgorithm(RenderScriptBlur(this))
                        .setBlurRadius(4f)
                        .setBlurAutoUpdate(true)
                        .setHasFixedTransformationMatrix(false)

                    Handler().postDelayed({
                        currentFrame = frame1
                        TransitionManager.go(frame1)
                    }, 1000)
                }

                frame01 -> {
                    blurView_setup.setupWith(window.decorView.findViewById(android.R.id.content))
                        .setFrameClearDrawable(window.decorView.background)
                        .setBlurAlgorithm(RenderScriptBlur(this))
                        .setBlurRadius(4f)
                        .setBlurAutoUpdate(true)
                        .setHasFixedTransformationMatrix(false)

                    Handler().postDelayed({
                        frame3_flag = false
                        currentFrame = frame3
                        TransitionManager.go(frame3)
                    }, 2000)
                }

                frame1 -> {
                    blurView_setup.setupWith(window.decorView.findViewById(android.R.id.content))
                        .setFrameClearDrawable(window.decorView.background)
                        .setBlurAlgorithm(RenderScriptBlur(this))
                        .setBlurRadius(4f)
                        .setBlurAutoUpdate(true)
                        .setHasFixedTransformationMatrix(false)

                    setup_backdrop.visibility = View.VISIBLE

                    Handler().postDelayed({
                        currentFrame = frame01
                        TransitionManager.go(frame01)
                    }, 800)
                }

                frame3 -> {

                    if(!frame3_flag){

                        blurView_setup.setupWith(window.decorView.findViewById(android.R.id.content))
                            .setFrameClearDrawable(window.decorView.background)
                            .setBlurAlgorithm(RenderScriptBlur(this))
                            .setBlurRadius(4f)
                            .setBlurAutoUpdate(true)
                            .setHasFixedTransformationMatrix(false)

                        blurView_setup1.setupWith(window.decorView.findViewById(android.R.id.content))
                            .setFrameClearDrawable(window.decorView.background)
                            .setBlurAlgorithm(RenderScriptBlur(this))
                            .setBlurRadius(4f)
                            .setBlurAutoUpdate(true)
                            .setHasFixedTransformationMatrix(false)

                        image = sh.getString("image", null)?.toUri()

                        if(image != null){
                            Glide.with(this)
                                .load(image)
                                .centerCrop()
                                .into(profile_image_setup)

                            setup_skip.visibility = TextView.INVISIBLE
                            setup_continue.visibility = TextView.VISIBLE
                        }

                        val x = (1..9).random()

                        Firebase.firestore.collection("fun_facts").document(x.toString()).get()
                            .addOnSuccessListener {
                                val fact = it["fact"].toString()
                                fact_text_setup.text = fact
                            }

                        frame3_flag = true
                    }

                    change_button_setup.setOnClickListener() {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

                        startActivityForResult(intent, 0)
                    }

                    remove_button_setup.setOnClickListener(){
                        val draw = resources.getDrawable(R.drawable.ic_profile_default)
                        profile_image_setup.setImageDrawable(draw)
                        image = null

                        sh.edit().remove("image").apply()
                    }

                    setup_continue.setOnClickListener() {
                        if (image != null) {
//                            val storage =
//                                FirebaseStorage.getInstance().getReference("/profileImages/$email")
//                            storage.putFile(image!!)
                            sh.edit().putString("image", image.toString()).apply()

                        }
                        frame3_flag = false
                        currentFrame = frame4
                        androidx.transition.TransitionManager.go(frame4)
                    }

                    setup_skip.setOnClickListener() {
                        frame3_flag = false
                        currentFrame = frame4
                        androidx.transition.TransitionManager.go(frame4)
                    }
                }

                frame4 -> {
                    if(!frame4_flag){

                        blurView_setup.setupWith(window.decorView.findViewById(android.R.id.content))
                            .setFrameClearDrawable(window.decorView.background)
                            .setBlurAlgorithm(RenderScriptBlur(this))
                            .setBlurRadius(4f)
                            .setBlurAutoUpdate(true)
                            .setHasFixedTransformationMatrix(false)

                        blurView_setup1.setupWith(window.decorView.findViewById(android.R.id.content))
                            .setFrameClearDrawable(window.decorView.background)
                            .setBlurAlgorithm(RenderScriptBlur(this))
                            .setBlurRadius(4f)
                            .setBlurAutoUpdate(true)
                            .setHasFixedTransformationMatrix(false)

                        val name = sh.getString("name", "")
                        val contact = sh.getString("contact", "")

                        name_setup_EditText.setText(name)
                        contact_setup_EditText.setText(contact)

                        val x = (1..9).random()

                        Firebase.firestore.collection("fun_facts").document(x.toString()).get()
                            .addOnSuccessListener {
                                val fact = it["fact"].toString()
                                fact_text_setup.text = fact
                            }
                        frame4_flag = true
                    }

                    name_setup_EditText.doOnTextChanged{text, start, before, count ->
                        if(text.isNullOrEmpty()){
                            name_setup_textInputLayout.error = "Please enter your name"
                        }else{
                            name_setup_textInputLayout.error = null
                        }
                    }


                    contact_setup_EditText.doOnTextChanged{text, start, before, count ->
                        if(text?.length == 10){
                            contact_setup_textInputLayout.error = null
                        }else{
                            contact_setup_textInputLayout.error = "Please enter a valid phone number"
                        }
                    }

                    setup_continue.setOnClickListener(){
                        val name = name_setup_EditText.text.toString()
                        val contact = contact_setup_EditText.text.toString()

                        if(name.isEmpty()){
                            name_setup_textInputLayout.error = "Please enter your name"
                        }else if(contact.length!= 10){
                            contact_setup_textInputLayout.error = "Please enter a valid phone number"
                        }else{
                            sh.edit().putString("name", name).apply()
                            sh.edit().putString("contact", contact).apply()

                            frame4_flag = false
                            currentFrame = frame5
                            androidx.transition.TransitionManager.go(frame5)
                        }
                    }

                    setup_back.setOnClickListener(){
                        val name = name_setup_EditText.text.toString()
                        val contact = contact_setup_EditText.text.toString()

                        sh.edit().putString("name", name).apply()
                        sh.edit().putString("contact", contact).apply()

                        frame4_flag = false
                        currentFrame = frame3
                        androidx.transition.TransitionManager.go(frame3)
                    }

                }

                frame5 -> {

                    if(!frame5_flag){
                        blurView_setup.setupWith(window.decorView.findViewById(android.R.id.content))
                            .setFrameClearDrawable(window.decorView.background)
                            .setBlurAlgorithm(RenderScriptBlur(this))
                            .setBlurRadius(4f)
                            .setBlurAutoUpdate(true)
                            .setHasFixedTransformationMatrix(false)

                        blurView_setup1.setupWith(window.decorView.findViewById(android.R.id.content))
                            .setFrameClearDrawable(window.decorView.background)
                            .setBlurAlgorithm(RenderScriptBlur(this))
                            .setBlurRadius(4f)
                            .setBlurAutoUpdate(true)
                            .setHasFixedTransformationMatrix(false)

                        school_setup_autoCompleteTextView.inputType = InputType.TYPE_NULL
                        val arr = resources.getStringArray(R.array.schools)
                        school_setup_autoCompleteTextView.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arr))

                        val school = sh.getString("school", "")

                        school_setup_autoCompleteTextView.setText(school, false)

                        val x = (1..9).random()

                        Firebase.firestore.collection("fun_facts").document(x.toString()).get()
                            .addOnSuccessListener {
                                val fact = it["fact"].toString()
                                fact_text_setup.text = fact
                            }
                        frame5_flag = true
                    }
                    school_setup_autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                        val arr = resources.getStringArray(R.array.schools)
                        sh.edit().putString("school", arr[position]).apply()
                    }

                    setup_back.setOnClickListener(){
                        frame5_flag = false
                        currentFrame = frame4
                        TransitionManager.go(frame4)
                    }

                    setup_continue.setOnClickListener(){

                        school_setup_textInputLayout.error = null

                        val email = FirebaseAuth.getInstance().currentUser?.email.toString()
                        val school = sh.getString("school", "")

                        if(!school.isNullOrEmpty()){

                            if(image != null){
                                FirebaseStorage.getInstance().getReference("/profileImages/$email").putFile(image!!)
                                    .addOnSuccessListener {
                                        FirebaseStorage.getInstance().getReference("/profileImages/$email").downloadUrl
                                            .addOnSuccessListener {
                                                sh.edit().putString("image", it.toString()).apply()
                                                val db = Firebase.firestore.collection("user")

                                                db.document(email.toString()).get()
                                                    .addOnSuccessListener { document ->

                                                        val data = hashMapOf(
                                                            "name" to sh.getString("name", ""),
                                                            "contact" to sh.getString("contact", ""),
                                                            "school" to sh.getString("school", ""),
                                                            "image" to it.toString(),
                                                            "profile_complete" to 1,
                                                        )
                                                        db.document(email.toString()).set(data)
                                                            .addOnSuccessListener {
                                                                getSharedPreferences("CMRU", MODE_PRIVATE).edit().clear().apply()
                                                                val intent = Intent(this, HomeActivity::class.java)
                                                                startActivity(intent)
                                                            }
                                                    }
                                            }
                                    }
                            }

                            else{
                                val db = Firebase.firestore.collection("user")

                                db.document(email.toString()).get()
                                    .addOnSuccessListener { document ->

                                        val data = hashMapOf(
                                            "name" to sh.getString("name", ""),
                                            "contact" to sh.getString("contact", ""),
                                            "school" to sh.getString("school", ""),
                                            "image" to "",
                                            "profile_complete" to 1
                                        )
                                        db.document(email.toString()).set(data)
                                            .addOnSuccessListener {
                                                val intent = Intent(this, HomeActivity::class.java)
                                                startActivity(intent)
                                            }
                                    }
                            }
                        }else{
                            school_setup_textInputLayout.error = "Please Select a School"
                        }
                    }

                }
            }
        })

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            0 -> {
                if(resultCode == Activity.RESULT_OK){
                    if(data != null){
                        image = data.data
                        //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image)
                        Glide.with(this)
                            .load(image)
                            .centerCrop()
                            .into(profile_image_setup)

                        setup_skip.visibility = TextView.INVISIBLE
                        setup_continue.visibility = TextView.VISIBLE
                    }else{
                        Toast.makeText(applicationContext, "Unable to load image", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

//    override fun onSaveInstanceState(savedInstanceState: Bundle) {
//        super.onSaveInstanceState(savedInstanceState)
//        savedInstanceState.putString("name", name)
//        savedInstanceState.putString("contact", contact)
//    }
}
