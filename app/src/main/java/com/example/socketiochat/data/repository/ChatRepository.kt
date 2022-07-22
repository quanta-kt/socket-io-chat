package com.example.socketiochat.data.repository

import com.example.socketiochat.data.ChatHandle
import javax.inject.Inject

class ChatRepository @Inject constructor() {

    fun getChatHandle(username: String, uri: String): ChatHandle {
        return ChatHandle(username, uri)
    }
}