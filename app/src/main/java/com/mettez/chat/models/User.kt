package com.mettez.chat.models

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val profileImage: String = "",
    val lastSeen: Long = 0
) {
    constructor() : this("", "", "", "", 0)
}