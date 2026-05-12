package com.mettez.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mettez.chat.R
import com.mettez.chat.models.Message
import java.text.SimpleDateFormat
import java.util.Date

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.messageText)
        private val messageTime: TextView = itemView.findViewById(R.id.messageTime)
        private val messageContainer: LinearLayout = itemView.findViewById(R.id.messageContainer)

        fun bind(message: Message) {
            messageText.text = message.message
            messageTime.text = formatTime(message.timestamp)

            if (message.isSent) {
                messageContainer.gravity = android.view.Gravity.END
                messageText.setBackgroundResource(R.drawable.sent_message_bg)
            } else {
                messageContainer.gravity = android.view.Gravity.START
                messageText.setBackgroundResource(R.drawable.received_message_bg)
            }
        }

        private fun formatTime(timestamp: Long): String {
            return SimpleDateFormat("HH:mm").format(Date(timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size
}