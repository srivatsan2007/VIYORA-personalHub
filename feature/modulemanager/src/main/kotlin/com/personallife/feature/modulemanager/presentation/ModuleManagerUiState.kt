package com.personallife.feature.modulemanager.presentation

import com.personallife.core.model.AppModule

data class ModuleManagerUiState(
    val modules: List<AppModule> = emptyList(),
    val isLoading: Boolean = false,
    val selectedInfoModule: AppModule? = null
)
