package com.example.socketiochat.ui.activities.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.socketiochat.data.PreferenceManager

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val prefManager = PreferenceManager(application)

    var savedUsername: String? by prefManager::savedUsername
    var savedServerUri: String? by prefManager::savedServerUri
}