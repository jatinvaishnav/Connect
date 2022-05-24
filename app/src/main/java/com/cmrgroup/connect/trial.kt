package com.cmrgroup.connect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cmrgroup.connect.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_trial.*

private var messageID : String = ""
private val email = FirebaseAuth.getInstance().currentUser?.email
private const val toUser = "kajal@cmr.edu.in"

class trial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trial)

        Firebase.firestore.collection("user")
            .document("$email")
            .collection("Connections")
            .document(toUser)
            .get().addOnSuccessListener {
                messageID = it["messageID"].toString()
                Log.d("xxx", "MessageId   :  $messageID")
            }

        syncMessages()

        but.setOnClickListener{
            //Log.d("xxx", "MessageID : $messageID")
            sendMessage()
        }
    }

    private fun sendMessage(){
        val message = text.text.toString()
        val ref = FirebaseDatabase.getInstance()
            .getReference("/messages")
            .child("/$messageID").push()

        //Log.d("xxx", ref.key!!)

        val newMessage = ChatMessage(ref.key!!, message, email.toString(), toUser, System.currentTimeMillis()/1000)
        ref.setValue(newMessage)
            .addOnSuccessListener {
                //Log.d("xxx", "WORKS")
            }
            .addOnFailureListener{
                //Log.d("xxx", it.message.toString())
            }
    }

    private fun syncMessages(){
        FirebaseDatabase.getInstance()
            .getReference("/messages/$messageID")
            .addChildEventListener(object:ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val chatLog = snapshot.getValue(ChatMessage::class.java)!!
                    Log.d("xxx", chatLog.text.toString())
                }
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {
                }
            })
    }
}