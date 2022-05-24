package com.cmrgroup.connect

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    var image : Uri? = null
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setDisplayShowCustomEnabled(true);
        supportActionBar?.setCustomView(R.layout.custom_action_bar_set_up);
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF02b7ac")))

        window.statusBarColor = ContextCompat.getColor(this, R.color.cmr_green)

        school_profile1_autoCompleteTextView.inputType = InputType.TYPE_NULL
        val arr = resources.getStringArray(R.array.schools)
        school_profile1_autoCompleteTextView.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arr))

        val shEmail = getSharedPreferences("CMRU", MODE_PRIVATE).getString("email", "")

        if(!shEmail.equals("")){
            email_profile1_EditText.setText(shEmail)
        }else{
            Toast.makeText(applicationContext, "User not found", Toast.LENGTH_SHORT).show()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        //name_profile1_textInputLayout.error = "WWWWW"
        var name = ""
        val email = shEmail
        var contact = ""
        var usn = ""
        var school = ""
        var uid = ""

//        1) Working code for image selector
//        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//            if(it.resultCode == Activity.RESULT_OK && it.data != null){
//                image = it.data!!.data
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image)
//                profile_image_profile1.setImageBitmap(bitmap)
//            }
//        }

        change_button_profile1.setOnClickListener(){
//            1) Working code for image selector
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//
//            startActivityForResult(intent, 12345)
//
//            getContent.launch(intent)

            CropImage.startPickImageActivity(this)

//            CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setCropShape(CropImageView.CropShape.OVAL)
//                .setAspectRatio(1, 1)
//                .start(this)

        }

        remove_button_profile1.setOnClickListener(){
            val draw = resources.getDrawable(R.drawable.ic_profile_default, theme)
            profile_image_profile1.setImageDrawable(draw)
            image = null
        }

        name_profile1_EditText.doOnTextChanged{text, start, before, count ->
            name_profile1_textInputLayout.error = null
        }
        contact_profile1_EditText.doOnTextChanged{text, start, before, count ->
            contact_profile1_textInputLayout.error = null
        }
        usn_profile1_EditText.doOnTextChanged{text, start, before, count ->
            usn_profile1_textInputLayout.error = null
        }
        school_profile1_autoCompleteTextView.doOnTextChanged{text, start, before, count ->
            school_profile1_textInputLayout.error = null
        }

        submit_button_profile1.setOnClickListener(){
            name = name_profile1_EditText.text.toString()
            contact = contact_profile1_EditText.text.toString()
            usn = usn_profile1_EditText.text.toString()
            school = school_profile1_autoCompleteTextView.text.toString()

            var flag = true

            if(name.equals("")){
                name_profile1_textInputLayout.error = "Please enter your name"
                flag = false
            }
            if(contact.equals("")){
                contact_profile1_textInputLayout.error = "Please enter your contact number"
                flag = false
            }
            if(usn.equals("")){
                usn_profile1_textInputLayout.error = "Please enter your USN Number"
                flag = false
            }
            if(school.equals("")){
                school_profile1_textInputLayout.error = "Please select your School"
                flag = false
            }

            if(flag){

                if(image!=null){
                    val storage = FirebaseStorage.getInstance().getReference("/profileImages/$email")
                    storage.putFile(image!!)
                }
                else{
                    Log.d("xxx", "From failure")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            12345 ->{
                if(resultCode == Activity.RESULT_OK && data!=null){
                    val uri = data.data!!

                    CropImage.activity(image)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        //.setCropShape(CropImageView.CropShape.OVAL)
                        //.setAspectRatio(1, 1)
                        .start(this)
                }
            }

            CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE ->{
                if(resultCode == Activity.RESULT_OK && data!=null){
                    image = data.data!!

                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, image)
                    profile_image_profile1.setImageBitmap(bitmap)

//                    val cc = CropImage.activity(res)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setCropShape(CropImageView.CropShape.OVAL)
//                        .setAspectRatio(1, 1)

//                    if(CropImage.isReadExternalStoragePermissionsRequired(this, res)){
//                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 7894)
//                        Log.d("xxx", "REQ PERMISSION")
//                    }else{
//                        Log.d("xxx", "ERROR IN CROP IMAGE")
//                        val intent = CropImage.activity()
//                            .getIntent(this)
//                        startActivityForResult(intent, 0)
//                        Log.d("xxx", "ERROR IN CROP IMAGE !!!!!!!!!!")
                    }
                }
            }

//            0 ->{
//                if(resultCode == Activity.RESULT_OK){
//                    val res = CropImage.getActivityResult(data)
//                    res.uri?.let {
//                        image = res.uri
//                    }
//                }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
//                    Toast.makeText(applicationContext, "Unable to fetch image", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }
}

//CropImage.activity(res)
//.setGuidelines(CropImageView.Guidelines.ON)
//.setCropShape(CropImageView.CropShape.OVAL)
//.setAspectRatio(1, 1)
//.getIntent(this)