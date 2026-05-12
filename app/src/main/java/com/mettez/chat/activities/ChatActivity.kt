package com.mettez.chat.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mettez.chat.R
import com.mettez.chat.adapters.MessageAdapter
import com.mettez.chat.models.Message
import java.util.UUID

class ChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var messageRecycler: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendBtn: Button
    private lateinit var titleText: TextView
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()
    private var chatId = ""
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        chatId = intent.getStringExtra("chatId") ?: ""
        userName = intent.getStringExtra("userName") ?: ""

        initViews()
        setupRecycler()
        loadMessages()
    }

    private fun initViews() {
        messageRecycler = findViewById(R.id.messageRecycler)
        messageInput = findViewById(R.id.messageInput)
        sendBtn = findViewById(R.id.sendBtn)
        titleText = findViewById(R.id.titleText)

        titleText.text = userName

        sendBtn.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupRecycler() {
        messageAdapter = MessageAdapter(messageList)
        messageRecycler.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        messageRecycler.adapter = messageAdapter
    }

    private fun sendMessage() {
        val text = messageInput.text.toString().trim()
        if (text.isEmpty()) return

        val userId = auth.currentUser?.uid ?: return
        val messageId = UUID.randomUUID().toString()

        val message = Message(
            messageId = messageId,
            senderUid = userId,
            senderName = "You",
            message = text,
            timestamp = System.currentTimeMillis(),
            isSent = true
        )

        database.reference.child("messages").child(chatId).child(messageId).setValue(message)
        messageInput.text.clear()
    }

    private fun loadMessages() {
        database.reference.child("messages").child(chatId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (data in snapshot.children) {
                        val message = data.getValue(Message::class.java)
                        if (message != null) {
                            messageList.add(message)
                        }
                    }
                    messageList.sortBy { it.timestamp }
                    messageAdapter.notifyDataSetChanged()
                    if (messageList.isNotEmpty()) {
                        messageRecycler.scrollToPosition(messageList.size - 1)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}