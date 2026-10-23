package com.personallife.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SupportFactory

/**
 * Room Database secured with SQLCipher AES-256 database-level encryption.
 */
@Database(
    entities = [WidgetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun widgetDao(): WidgetDao

    companion object {
        const val DATABASE_NAME = "personal_hub_encrypted.db"

        fun buildEncrypted(
            context: Context,
            passphraseProvider: DatabasePassphraseProvider
        ): AppDatabase {
            val passphrase = passphraseProvider.getOrCreatePassphrase()
            val factory = SupportFactory(passphrase)

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .openHelperFactory(factory)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
