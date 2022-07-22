package com.example.socketiochat.data

import android.content.Context

class PreferenceManager(context: Context) {

    companion object {
        private const val PREF_FILE = "preferences"
        private const val KEY_USERNAME = "username"
        private const val KEY_SERVER_URI = "server_uri"
    }

    private val pref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    var savedUsername: String?
    get() = pref.getString(KEY_USERNAME, null)
    set(value) {
        pref.edit()
            .putString(KEY_USERNAME, value)
            .apply()
    }

    var savedServerUri: String?
        get() = pref.getString(KEY_SERVER_URI, null)
        set(value) {
            pref.edit()
                .putString(KEY_SERVER_URI, value)
                .apply()
        }
}