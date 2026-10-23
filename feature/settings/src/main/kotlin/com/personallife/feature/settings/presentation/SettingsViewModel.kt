package com.personallife.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personallife.core.common.result.Result
import com.personallife.core.data.repository.AuthRepository
import com.personallife.core.data.repository.UserPreferencesRepository
import com.personallife.core.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        combine(
            preferencesRepository.userPreferences,
            authRepository.currentUser
        ) { prefs, user ->
            _uiState.update {
                it.copy(
                    userPreferences = prefs,
                    userProfile = user
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
            _uiState.update { it.copy(isThemeDialogVisible = false) }
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setBiometricEnabled(enabled)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setNotificationsEnabled(enabled)
        }
    }

    fun setFlagSecureEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setFlagSecureEnabled(enabled)
        }
    }

    fun showThemeDialog() = _uiState.update { it.copy(isThemeDialogVisible = true) }
    fun hideThemeDialog() = _uiState.update { it.copy(isThemeDialogVisible = false) }

    fun showLogoutDialog() = _uiState.update { it.copy(isLogoutDialogVisible = true) }
    fun hideLogoutDialog() = _uiState.update { it.copy(isLogoutDialogVisible = false) }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout().collect { result ->
                if (result is Result.Success) {
                    hideLogoutDialog()
                    onLoggedOut()
                }
            }
        }
    }
}
