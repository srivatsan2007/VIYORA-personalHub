package com.personallife.core.data.repository

import com.personallife.core.common.result.Result
import com.personallife.core.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(uid: String): Flow<Result<UserProfile>>
    suspend fun updateUserProfile(profile: UserProfile): Result<Unit>
}
