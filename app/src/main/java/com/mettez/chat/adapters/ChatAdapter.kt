package com.mettez.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mettez.chat.R
import com.mettez.chat.models.Chat
import java.text.SimpleDateFormat
import java.util.Date

class ChatAdapter(
    private val chats: List<Chat>,
    private val onItemClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.chatName)
        private val messageText: TextView = itemView.findViewById(R.id.lastMessage)
        private val timeText: TextView = itemView.findViewById(R.id.chatTime)

        fun bind(chat: Chat) {
            nameText.text = chat.userName
            messageText.text = chat.lastMessage
            timeText.text = formatTime(chat.lastMessageTime)
            itemView.setOnClickListener {
                onItemClick(chat)
            }
        }

        private fun formatTime(timestamp: Long): String {
            return SimpleDateFormat("HH:mm").format(Date(timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemCount(): Int = chats.size
}