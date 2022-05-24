package com.cmrgroup.connect

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_set_up1.*
import kotlinx.android.synthetic.main.custom_action_bar_home.*
import kotlinx.android.synthetic.main.setup_frame1.*
import kotlinx.android.synthetic.main.setup_frame3.*
import kotlinx.android.synthetic.main.setup_frame3.blurView_setup

class HomeActivity : AppCompatActivity() {

    private var flag = false

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.custom_action_bar_home)
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_action_back))

        logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Splash::class.java)
            startActivity(intent)
            finish()
            getSharedPreferences("CMRU", MODE_PRIVATE).edit().clear().apply()
        }

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.statusBarColor = ContextCompat.getColor(this, R.color.cmr_green_gradient)

        window.statusBarColor = Color.TRANSPARENT

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        home_bottom_navigation_menu.background = null
        home_bottom_navigation_menu.menu.getItem(2).isEnabled = false

//        var image = getSharedPreferences("CMRU", MODE_PRIVATE).getString("image", "")?.toUri()
        val email = FirebaseAuth.getInstance().currentUser?.email.toString()

        FirebaseStorage.getInstance().getReference("/profileImages/$email").downloadUrl
            .addOnSuccessListener {
                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(home_floating_menu_3)
            }

        home_floating_menu_main.setOnClickListener{
            open_menu(flag)
            flag = !flag
        }

//        home_back_blurView.setupWith(window.decorView.findViewById(android.R.id.content))
//            .setFrameClearDrawable(window.decorView.background)
//            .setBlurAlgorithm(RenderScriptBlur(this))
//            .setBlurRadius(4f)
//            .setBlurAutoUpdate(true)
//            .setHasFixedTransformationMatrix(false)

        home_floating_menu_3.setOnClickListener{
            val intent = Intent(this, trial::class.java)
            startActivity(intent)
        }

        val home = Scene.getSceneForLayout(home_frames, R.layout.home_bottom_menu_frame_home, this)
        val announcements = Scene.getSceneForLayout(home_frames, R.layout.home_bottom_menu_frame_announcements, this)
        val explore = Scene.getSceneForLayout(home_frames, R.layout.home_bottom_menu_frame_explore, this)
        val messaging = Scene.getSceneForLayout(home_frames, R.layout.home_bottom_menu_frame_messaging, this)

        var currentFrame = home
        home.enter()

//        val x = home_bottom_navigation_menu.sele

//        home_bottom_navigation_menu.setOnNavigationItemReselectedListener {
//            when(it.itemId){
//                R.id.menu_home -> {
//                    TransitionManager.go(home)
//                    currentFrame = home
//                    Log.d("xxx", "HOME")
//                }
//                R.id.menu_announcements -> {
//                    TransitionManager.go(announcements)
//                    currentFrame = announcements
//                    Log.d("xxx", "announce")
//                }
//                R.id.menu_explore -> {
//                    TransitionManager.go(explore)
//                    currentFrame = explore
//                    Log.d("xxx", "explore")
//                }
//                R.id.menu_messaging -> {
//                    TransitionManager.go(messaging)
//                    currentFrame = messaging
//                    Log.d("xxx", "message")
//                }
//            }
//
//        }

        home_floating_menu_main.setOnClickListener{
            open_menu(flag)
            flag = !flag
        }

        home_bottom_navigation_menu.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.menu_home -> {
                    TransitionManager.go(home)
                    currentFrame = home
                    Log.d("xxx", "HOME")
                }
                R.id.menu_announcements -> {
                    TransitionManager.go(announcements)
                    currentFrame = announcements
                    Log.d("xxx", "announce")
                }
                R.id.menu_explore -> {
                    TransitionManager.go(explore)
                    currentFrame = explore
                    Log.d("xxx", "explore")
                }
                R.id.menu_messaging -> {
                    TransitionManager.go(messaging)
                    currentFrame = messaging
                    Log.d("xxx", "message")
                }
            }
            true
        }

//        val navController = findNavController(R.id.fragments_main_home)
//        home_bottom_navigation_menu.setupWithNavController(navController)

    }

    private fun open_menu(flag:Boolean){

        val bounce = AnimationUtils.loadAnimation(applicationContext, R.anim.bounce)
        val fadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        val rotateOpen = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_open)
        val rotateClose = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_close)

        when(flag){
            false -> {

                home_floating_menu_main.startAnimation(rotateOpen)

                home_floating_menu_1.visibility = View.VISIBLE
                home_floating_menu_1.startAnimation(bounce)
                home_floating_menu_2.visibility = View.VISIBLE
                home_floating_menu_2.startAnimation(bounce)
                home_floating_menu_3.visibility = View.VISIBLE
                home_floating_menu_3.startAnimation(bounce)
                home_floating_menu_4.visibility = View.VISIBLE
                home_floating_menu_4.startAnimation(bounce)
                home_floating_menu_5.visibility = View.VISIBLE
                home_floating_menu_5.startAnimation(bounce)

                //home_back_blurView.visibility = View.VISIBLE
            }

            true -> {

                home_floating_menu_main.startAnimation(rotateClose)

                home_floating_menu_1.visibility = View.INVISIBLE
                home_floating_menu_1.startAnimation(fadeOut)
                home_floating_menu_2.visibility = View.INVISIBLE
                home_floating_menu_2.startAnimation(fadeOut)
                home_floating_menu_3.visibility = View.INVISIBLE
                home_floating_menu_3.startAnimation(fadeOut)
                home_floating_menu_4.visibility = View.INVISIBLE
                home_floating_menu_4.startAnimation(fadeOut)
                home_floating_menu_5.visibility = View.INVISIBLE
                home_floating_menu_5.startAnimation(fadeOut)

                //home_back_blurView.visibility = View.INVISIBLE
            }
        }
    }
}