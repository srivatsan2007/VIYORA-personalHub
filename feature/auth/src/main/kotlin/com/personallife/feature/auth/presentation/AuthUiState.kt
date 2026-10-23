package com.personallife.feature.auth.presentation

import com.personallife.core.model.UserProfile

data class AuthUiState(
    val emailInput: String = "",
    val passwordInput: String = "",
    val displayNameInput: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val displayNameError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val authenticatedUser: UserProfile? = null,
    val isFirebaseConfigured: Boolean = true
)
