package com.mettez.chat.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mettez.chat.R
import com.mettez.chat.adapters.ChatAdapter
import com.mettez.chat.models.Chat

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var chatRecycler: RecyclerView
    private lateinit var logoutBtn: Button
    private lateinit var chatAdapter: ChatAdapter
    private val chatList = mutableListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        initViews()
        setupRecycler()
        loadChats()
    }

    private fun initViews() {
        chatRecycler = findViewById(R.id.chatRecycler)
        logoutBtn = findViewById(R.id.logoutBtn)

        logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupRecycler() {
        chatAdapter = ChatAdapter(chatList) { chat ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chatId", chat.chatId)
            intent.putExtra("userName", chat.userName)
            startActivity(intent)
        }
        chatRecycler.layoutManager = LinearLayoutManager(this)
        chatRecycler.adapter = chatAdapter
    }

    private fun loadChats() {
        val userId = auth.currentUser?.uid ?: return
        database.reference.child("chats").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()
                    for (data in snapshot.children) {
                        val chat = data.getValue(Chat::class.java)
                        if (chat != null) {
                            chatList.add(chat)
                        }
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}