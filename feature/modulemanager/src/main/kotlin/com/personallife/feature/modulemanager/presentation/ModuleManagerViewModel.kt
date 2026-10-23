package com.personallife.feature.modulemanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personallife.core.data.repository.ModuleRepository
import com.personallife.core.model.AppModule
import com.personallife.core.model.ModuleId
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
class ModuleManagerViewModel @Inject constructor(
    private val moduleRepository: ModuleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModuleManagerUiState(isLoading = true))
    val uiState: StateFlow<ModuleManagerUiState> = _uiState.asStateFlow()

    init {
        observeModules()
    }

    private fun observeModules() {
        moduleRepository.modules
            .onEach { list ->
                _uiState.update {
                    it.copy(
                        modules = list,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun toggleModule(moduleId: ModuleId, isEnabled: Boolean) {
        viewModelScope.launch {
            moduleRepository.setModuleEnabled(moduleId, isEnabled)
        }
    }

    fun selectModuleInfo(module: AppModule) {
        _uiState.update { it.copy(selectedInfoModule = module) }
    }

    fun dismissModuleInfo() {
        _uiState.update { it.copy(selectedInfoModule = null) }
    }
}
