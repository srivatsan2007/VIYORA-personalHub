package com.personallife.core.data.repository

import com.personallife.core.common.result.Result
import com.personallife.core.model.UserProfile
import kotlinx.coroutines.flow.Flow

sealed interface AuthState {
    data class Authenticated(val user: UserProfile) : AuthState
    data object Unauthenticated : AuthState
    data object Loading : AuthState
}

interface AuthRepository {
    val authState: Flow<AuthState>
    val currentUser: Flow<UserProfile?>
    
    fun isFirebaseConfigured(): Boolean

    suspend fun login(email: String, password: String): Flow<Result<UserProfile>>

    suspend fun register(email: String, password: String, displayName: String): Flow<Result<UserProfile>>

    suspend fun logout(): Flow<Result<Unit>>
}
