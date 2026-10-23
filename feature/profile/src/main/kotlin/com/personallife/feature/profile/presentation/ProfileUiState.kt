package com.personallife.feature.profile.presentation

import com.personallife.core.model.UserProfile

data class ProfileUiState(
    val profile: UserProfile = UserProfile(),
    val editDisplayName: String = "",
    val editBio: String = "",
    val displayNameError: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
