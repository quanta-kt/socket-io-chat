package com.example.socketiochat.ui.activities.chat

import androidx.lifecycle.ViewModel
import com.example.socketiochat.data.ChatHandle
import com.example.socketiochat.data.models.Message
import com.example.socketiochat.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ChatActivityViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private var chatHandle: ChatHandle? = null
    private val messages = ArrayList<Message>()

    fun connect(username: String, uri: String) {
        chatHandle?.close()
        chatHandle = repository.getChatHandle(username, uri)
        chatHandle?.connect()
    }

    suspend fun sendMessage(content: String) {
        chatHandle?.sendMessage(content)
    }

    fun messagesFlow(): Flow<List<Message>>? {
        return flow {
            emit(messages)
            chatHandle
                ?.chatFlow()
                ?.onEach { messages.add(it) }
                ?.map { messages }
                ?.let {
                    emitAll(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatHandle?.close()
    }
}