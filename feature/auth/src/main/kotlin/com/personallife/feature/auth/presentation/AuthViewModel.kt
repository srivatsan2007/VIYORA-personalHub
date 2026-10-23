package com.personallife.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personallife.core.common.result.Result
import com.personallife.core.common.util.ValidationUtil
import com.personallife.core.data.repository.AuthRepository
import com.personallife.core.data.repository.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AuthUiState(isFirebaseConfigured = authRepository.isFirebaseConfigured())
    )
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        authRepository.authState
            .onEach { state ->
                when (state) {
                    is AuthState.Authenticated -> {
                        _uiState.update {
                            it.copy(
                                authenticatedUser = state.user,
                                isSuccess = true,
                                isLoading = false,
                                generalError = null
                            )
                        }
                    }
                    is AuthState.Unauthenticated -> {
                        _uiState.update {
                            it.copy(
                                authenticatedUser = null,
                                isSuccess = false,
                                isLoading = false
                            )
                        }
                    }
                    is AuthState.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(
                emailInput = email,
                emailError = null,
                generalError = null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                passwordInput = password,
                passwordError = null,
                generalError = null
            )
        }
    }

    fun onDisplayNameChanged(displayName: String) {
        _uiState.update {
            it.copy(
                displayNameInput = displayName,
                displayNameError = null,
                generalError = null
            )
        }
    }

    fun login() {
        val emailValidation = ValidationUtil.validateEmail(_uiState.value.emailInput)
        val passwordValidation = if (_uiState.value.passwordInput.isEmpty()) {
            com.personallife.core.common.util.ValidationResult(false, "Password cannot be empty")
        } else {
            com.personallife.core.common.util.ValidationResult(true)
        }

        if (!emailValidation.isValid || !passwordValidation.isValid) {
            _uiState.update {
                it.copy(
                    emailError = emailValidation.errorMessage,
                    passwordError = passwordValidation.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            authRepository.login(_uiState.value.emailInput, _uiState.value.passwordInput)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> _uiState.update { it.copy(isLoading = true, generalError = null) }
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = true,
                                    authenticatedUser = result.data,
                                    generalError = null
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = false,
                                    generalError = result.message
                                )
                            }
                        }
                    }
                }
        }
    }

    fun register() {
        val emailValidation = ValidationUtil.validateEmail(_uiState.value.emailInput)
        val passwordValidation = ValidationUtil.validatePassword(_uiState.value.passwordInput)
        val nameValidation = ValidationUtil.validateDisplayName(_uiState.value.displayNameInput)

        if (!emailValidation.isValid || !passwordValidation.isValid || !nameValidation.isValid) {
            _uiState.update {
                it.copy(
                    emailError = emailValidation.errorMessage,
                    passwordError = passwordValidation.errorMessage,
                    displayNameError = nameValidation.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            authRepository.register(
                _uiState.value.emailInput,
                _uiState.value.passwordInput,
                _uiState.value.displayNameInput
            ).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true, generalError = null) }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true,
                                authenticatedUser = result.data,
                                generalError = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = false,
                                generalError = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout().collect { result ->
                if (result is Result.Success) {
                    _uiState.update {
                        AuthUiState(isFirebaseConfigured = authRepository.isFirebaseConfigured())
                    }
                }
            }
        }
    }
}
