package com.example.socketiochat.data

import com.example.socketiochat.data.models.Message
import io.socket.client.AckWithTimeout
import io.socket.client.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import java.util.concurrent.TimeoutException

class ChatHandle(
    private val username: String,
    uri: String,
) {

    private val io = IO.socket(uri)

    fun connect() {
        io.connect()
    }

    fun close() {
        io.close()
    }

    fun chatFlow(): Flow<Message> = callbackFlow {
        io.on("message") { args ->
            val json = args[0] as JSONObject
            val message = Message(
                username = json.getString("username"),
                message = json.getString("message"),
                uid = json.getString("uid")
            )

            trySendBlocking(message)
        }

        awaitClose {
            io.off("message")
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun sendMessage(message: String) = suspendCancellableCoroutine {
        val messageJson = JSONObject().apply {
            put("message", message)
            put("username", username)
        }

        io.emit("message", messageJson, object : AckWithTimeout(10000) {
            override fun onSuccess(vararg args: Any?) {
                it.resume(Unit) {}
            }
            override fun onTimeout() {
                it.cancel(TimeoutException())
            }
        })
    }
}