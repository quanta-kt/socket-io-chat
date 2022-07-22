package com.example.socketiochat.ui.activities.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.example.socketiochat.R
import com.example.socketiochat.data.models.Message
import com.example.socketiochat.messageItem
import com.example.socketiochat.util.findViewLazy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeoutException

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_SERVER_URI = "extra_server_uri"
    }

    private val recycleView: EpoxyRecyclerView by findViewLazy(R.id.message_recycler)
    private val toolbar: Toolbar by findViewLazy(R.id.toolbar)
    private val messageField: EditText by findViewLazy(R.id.message_field)
    private val sendButton: Button by findViewLazy(R.id.send_button)

    private val username by lazy {
        intent.getStringExtra(EXTRA_USERNAME)
            ?: error("Chat activity must not be started without EXTRA_USERNAME")
    }
    private val uri by lazy {
        intent.getStringExtra(EXTRA_SERVER_URI)
            ?: error("Chat activity must not be started without EXTRA_SERVER_URI")
    }

    private val viewModel: ChatActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setSupportActionBar(toolbar)
        setupRecycleView()

        sendButton.setOnClickListener {
            sendMessage(messageField.text.toString())
            messageField.text.clear()
        }
    }

    private fun setupRecycleView() {
        (recycleView.layoutManager as LinearLayoutManager).stackFromEnd = true

        lifecycleScope.launchWhenStarted {
            var messages = emptyList<Message>()

            viewModel.connect(username, uri)

            recycleView.withModels {
                messages.forEach { message ->
                    messageItem {
                        id(message.uid)
                        message(message)
                        isOutGoing(message.username == username)
                    }
                }
            }

            viewModel.messagesFlow()?.collect {
                messages = it
                recycleView.requestModelBuild()
            }
        }
    }

    private fun sendMessage(message: String) {
        lifecycleScope.launch {
            try {
                viewModel.sendMessage(message)
            } catch (e: TimeoutException) {
                Toast.makeText(
                    this@ChatActivity,
                    "Unable to send your message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
