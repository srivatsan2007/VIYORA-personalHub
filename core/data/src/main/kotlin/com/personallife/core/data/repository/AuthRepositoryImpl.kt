package com.personallife.core.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.personallife.core.common.result.Result
import com.personallife.core.data.firebase.FirebaseDiagnostics
import com.personallife.core.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val auth: FirebaseAuth?
        get() = if (FirebaseDiagnostics.isConfigured(context)) {
            try {
                FirebaseAuth.getInstance()
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }

    override fun isFirebaseConfigured(): Boolean = FirebaseDiagnostics.isConfigured(context)

    override val authState: Flow<AuthState> = callbackFlow {
        val currentAuth = auth
        if (currentAuth == null) {
            trySend(AuthState.Unauthenticated)
            awaitClose { }
            return@callbackFlow
        }

        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                trySend(
                    AuthState.Authenticated(
                        UserProfile(
                            uid = user.uid,
                            email = user.email.orEmpty(),
                            displayName = user.displayName.orEmpty(),
                            photoUrl = user.photoUrl?.toString()
                        )
                    )
                )
            } else {
                trySend(AuthState.Unauthenticated)
            }
        }

        currentAuth.addAuthStateListener(listener)
        awaitClose { currentAuth.removeAuthStateListener(listener) }
    }

    override val currentUser: Flow<UserProfile?> = callbackFlow {
        val currentAuth = auth
        if (currentAuth == null) {
            trySend(null)
            awaitClose { }
            return@callbackFlow
        }

        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                trySend(
                    UserProfile(
                        uid = user.uid,
                        email = user.email.orEmpty(),
                        displayName = user.displayName.orEmpty(),
                        photoUrl = user.photoUrl?.toString()
                    )
                )
            } else {
                trySend(null)
            }
        }

        currentAuth.addAuthStateListener(listener)
        awaitClose { currentAuth.removeAuthStateListener(listener) }
    }

    override suspend fun login(email: String, password: String): Flow<Result<UserProfile>> = flow {
        emit(Result.Loading)
        val currentAuth = auth
        if (currentAuth == null) {
            emit(Result.Error(IllegalStateException(FirebaseDiagnostics.MISSING_CONFIG_MESSAGE)))
            return@flow
        }

        try {
            val authResult = currentAuth.signInWithEmailAndPassword(email.trim(), password).await()
            val user = authResult.user
            if (user != null) {
                emit(
                    Result.Success(
                        UserProfile(
                            uid = user.uid,
                            email = user.email.orEmpty(),
                            displayName = user.displayName.orEmpty(),
                            photoUrl = user.photoUrl?.toString()
                        )
                    )
                )
            } else {
                emit(Result.Error(IllegalStateException("Login succeeded but user was null")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e, e.localizedMessage ?: "Failed to log in"))
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String
    ): Flow<Result<UserProfile>> = flow {
        emit(Result.Loading)
        val currentAuth = auth
        if (currentAuth == null) {
            emit(Result.Error(IllegalStateException(FirebaseDiagnostics.MISSING_CONFIG_MESSAGE)))
            return@flow
        }

        try {
            val authResult = currentAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = authResult.user
            if (user != null) {
                // Set the display name in the user profile
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName.trim())
                    .build()
                user.updateProfile(profileUpdates).await()

                emit(
                    Result.Success(
                        UserProfile(
                            uid = user.uid,
                            email = user.email.orEmpty(),
                            displayName = displayName.trim(),
                            photoUrl = user.photoUrl?.toString()
                        )
                    )
                )
            } else {
                emit(Result.Error(IllegalStateException("Registration succeeded but user was null")))
            }
        } catch (e: Exception) {
            emit(Result.Error(e, e.localizedMessage ?: "Failed to register account"))
        }
    }

    override suspend fun logout(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        val currentAuth = auth
        if (currentAuth != null) {
            try {
                currentAuth.signOut()
                emit(Result.Success(Unit))
            } catch (e: Exception) {
                emit(Result.Error(e, e.localizedMessage ?: "Failed to sign out"))
            }
        } else {
            emit(Result.Success(Unit))
        }
    }
}
