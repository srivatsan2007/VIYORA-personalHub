package com.personallife.core.data.firebase

import android.content.Context
import com.google.firebase.FirebaseApp

object FirebaseDiagnostics {

    fun isConfigured(context: Context): Boolean {
        return try {
            FirebaseApp.getApps(context).isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }

    const val MISSING_CONFIG_MESSAGE =
        "Firebase is not configured yet. Please follow the instructions in firebase/SETUP.md to add your google-services.json file to the app module."
}
