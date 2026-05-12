package com.mettez.chat.models

data class Chat(
    val chatId: String = "",
    val userId: String = "",
    val userName: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = 0,
    val unreadCount: Int = 0
) {
    constructor() : this("", "", "", "", 0, 0)
}