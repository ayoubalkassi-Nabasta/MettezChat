package com.mettez.chat.models

data class Message(
    val messageId: String = "",
    val senderUid: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: Long = 0,
    val isSent: Boolean = true
) {
    constructor() : this("", "", "", "", 0, true)
}