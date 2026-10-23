package com.personallife.core.data.database

import android.content.Context
import android.util.Base64
import com.personallife.core.common.security.KeystoreHelper
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabasePassphraseProvider @Inject constructor(
    private val context: Context
) {
    private val keystoreHelper = KeystoreHelper("PersonalHubDatabaseKey")
    private val prefs = context.getSharedPreferences("personal_hub_security_prefs", Context.MODE_PRIVATE)

    @Synchronized
    fun getOrCreatePassphrase(): ByteArray {
        val encryptedKey = prefs.getString("encrypted_db_key", null)
        val ivString = prefs.getString("db_key_iv", null)

        if (encryptedKey != null && ivString != null) {
            val iv = Base64.decode(ivString, Base64.NO_WRAP)
            val ciphertext = Base64.decode(encryptedKey, Base64.NO_WRAP)
            return keystoreHelper.decrypt(iv, ciphertext)
        }

        // Generate 32 bytes (256-bit) cryptographically strong random key
        val rawKey = ByteArray(32)
        SecureRandom().nextBytes(rawKey)

        val (iv, ciphertext) = keystoreHelper.encrypt(rawKey)
        prefs.edit()
            .putString("encrypted_db_key", Base64.encodeToString(ciphertext, Base64.NO_WRAP))
            .putString("db_key_iv", Base64.encodeToString(iv, Base64.NO_WRAP))
            .apply()

        return rawKey
    }
}
