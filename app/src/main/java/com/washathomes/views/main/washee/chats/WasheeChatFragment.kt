package com.washathomes.views.main.washee.chats

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.washathomes.R
import com.washathomes.views.main.washee.WasheeMainActivity
import com.washathomes.databinding.FragmentWasheeChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WasheeChatFragment : Fragment() {

    lateinit var binding: FragmentWasheeChatBinding
    lateinit var washeeMainActivity: WasheeMainActivity
    lateinit var navController: NavController
//    var chatRoom: ChatRoom? = null
    lateinit var firebaseUser: FirebaseUser
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var chatDataReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_washee_chat, container, false)
        binding = FragmentWasheeChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is WasheeMainActivity) {
            washeeMainActivity = context
        }
    }

//    private fun listenData() {
//        val listener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                try {
//                    chatRoom = dataSnapshot.getValue(ChatRoom::class.java)!!
//                    chatRoom?.roomKey = dataSnapshot.key ?: ""
//                    chatRoom?.let {
//                        it.messages = ArrayList()
//                        val messageRef = dataSnapshot.child("messages")
//                        for (msgSnapshot: DataSnapshot in messageRef.children) {
//                            it.messages.add(msgSnapshot.getValue(ChatMessage::class.java)!!)
//                        }
//                    }
//                } catch (e: Exception) {
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//            }
//        }
//
//        chatDataReference.addValueEventListener(listener)
//    }
//
//    fun sendMessage(msg: String) {
//        chatRoom?.let {
//            it.messages.add(
//                ChatMessage(
//                    message = msg,
//                    senderId = AppDefs.user.results!!.id!!.toInt(),
//                    createTime = System.currentTimeMillis()
//                )
//            )
//            val childUpdates = HashMap<String, Any?>()
//            childUpdates["${AppDefs.INBOX_PATH}/${it.roomKey}"] = it.toMap()
//            database.updateChildren(childUpdates)
//        }
//    }

}