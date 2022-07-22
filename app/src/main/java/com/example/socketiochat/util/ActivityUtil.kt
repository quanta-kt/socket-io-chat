package com.example.socketiochat.util

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

/**
 * Supports lazy `findViewById` for use in Activities
 */
fun <T: View> Activity.findViewLazy(@IdRes id: Int): Lazy<T> = lazy { findViewById(id) }