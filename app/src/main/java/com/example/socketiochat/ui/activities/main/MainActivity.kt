package com.example.socketiochat.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.socketiochat.R
import com.example.socketiochat.ui.activities.chat.ChatActivity
import com.example.socketiochat.util.findViewLazy

class MainActivity : AppCompatActivity() {

    private val toolbar: Toolbar by findViewLazy(R.id.toolbar)
    private val usernameInput: EditText by findViewLazy(R.id.input_username)
    private val serverAddressInput: EditText by findViewLazy(R.id.input_server_address)
    private val loginButton: Button by findViewLazy(R.id.button_login)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val viewModel: MainActivityViewModel by viewModels()

        usernameInput.setText(viewModel.savedUsername ?: "")
        serverAddressInput.setText(viewModel.savedServerUri ?: "")

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val serverAddress = serverAddressInput.text.toString()

            viewModel.savedUsername = username
            viewModel.savedServerUri = serverAddress

            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra(ChatActivity.EXTRA_USERNAME, username)
                putExtra(ChatActivity.EXTRA_SERVER_URI, serverAddress)
            }

            startActivity(intent)
            finish()
        }
    }
}