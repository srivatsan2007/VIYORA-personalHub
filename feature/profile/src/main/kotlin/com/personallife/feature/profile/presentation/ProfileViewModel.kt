package com.personallife.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personallife.core.common.result.Result
import com.personallife.core.common.util.ValidationUtil
import com.personallife.core.data.repository.AuthRepository
import com.personallife.core.data.repository.UserRepository
import com.personallife.core.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        authRepository.currentUser
            .onEach { user ->
                if (user != null) {
                    userRepository.getUserProfile(user.uid).collect { result ->
                        when (result) {
                            is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                            is Result.Success -> {
                                val current = result.data.copy(
                                    email = user.email.ifEmpty { result.data.email },
                                    displayName = if (result.data.displayName.isNotBlank()) result.data.displayName else user.displayName
                                )
                                _uiState.update {
                                    it.copy(
                                        profile = current,
                                        editDisplayName = current.displayName,
                                        editBio = current.bio,
                                        isLoading = false
                                    )
                                }
                            }
                            is Result.Error -> {
                                _uiState.update {
                                    it.copy(
                                        profile = user,
                                        editDisplayName = user.displayName,
                                        isLoading = false,
                                        errorMessage = result.message
                                    )
                                }
                            }
                        }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onDisplayNameChanged(name: String) {
        _uiState.update {
            it.copy(
                editDisplayName = name,
                displayNameError = null,
                errorMessage = null
            )
        }
    }

    fun onBioChanged(bio: String) {
        _uiState.update { it.copy(editBio = bio) }
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val validation = ValidationUtil.validateDisplayName(_uiState.value.editDisplayName)
        if (!validation.isValid) {
            _uiState.update { it.copy(displayNameError = validation.errorMessage) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val current = _uiState.value.profile
            val updated = current.copy(
                displayName = _uiState.value.editDisplayName.trim(),
                bio = _uiState.value.editBio.trim(),
                updatedAt = System.currentTimeMillis()
            )

            when (val result = userRepository.updateUserProfile(updated)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            profile = updated,
                            isSaving = false,
                            successMessage = "Profile updated successfully"
                        )
                    }
                    onSuccess()
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = result.message
                        )
                    }
                }
                is Result.Loading -> Unit
            }
        }
    }
}
