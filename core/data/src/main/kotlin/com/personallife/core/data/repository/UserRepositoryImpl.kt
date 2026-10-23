package com.personallife.core.data.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.personallife.core.common.result.Result
import com.personallife.core.data.firebase.FirebaseDiagnostics
import com.personallife.core.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserRepository {

    private val firestore: FirebaseFirestore?
        get() = if (FirebaseDiagnostics.isConfigured(context)) {
            try {
                FirebaseFirestore.getInstance()
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }

    // Local cached profile for quick offline access
    private var cachedProfile: UserProfile? = null

    override fun getUserProfile(uid: String): Flow<Result<UserProfile>> = flow {
        emit(Result.Loading)
        val currentDb = firestore
        if (currentDb == null) {
            val fallback = cachedProfile ?: UserProfile(uid = uid, displayName = "User", email = "")
            emit(Result.Success(fallback))
            return@flow
        }

        try {
            val doc = currentDb.collection("users").document(uid).get().await()
            if (doc.exists()) {
                val profile = UserProfile(
                    uid = uid,
                    email = doc.getString("email").orEmpty(),
                    displayName = doc.getString("displayName").orEmpty(),
                    photoUrl = doc.getString("photoUrl"),
                    bio = doc.getString("bio").orEmpty(),
                    createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis(),
                    updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis()
                )
                cachedProfile = profile
                emit(Result.Success(profile))
            } else {
                val fallback = cachedProfile ?: UserProfile(uid = uid)
                emit(Result.Success(fallback))
            }
        } catch (e: Exception) {
            val fallback = cachedProfile ?: UserProfile(uid = uid)
            emit(Result.Success(fallback))
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile): Result<Unit> {
        cachedProfile = profile
        val currentDb = firestore ?: return Result.Success(Unit)

        return try {
            val data = hashMapOf(
                "uid" to profile.uid,
                "email" to profile.email,
                "displayName" to profile.displayName,
                "photoUrl" to profile.photoUrl,
                "bio" to profile.bio,
                "updatedAt" to System.currentTimeMillis()
            )
            currentDb.collection("users").document(profile.uid).set(data).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, e.localizedMessage ?: "Failed to update profile")
        }
    }
}
