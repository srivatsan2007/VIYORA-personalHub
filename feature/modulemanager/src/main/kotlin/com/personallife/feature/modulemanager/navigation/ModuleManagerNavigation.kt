package com.personallife.feature.modulemanager.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personallife.core.navigation.Screen
import com.personallife.feature.modulemanager.presentation.ModuleManagerScreen
import com.personallife.feature.modulemanager.presentation.ModuleManagerViewModel

fun NavController.navigateToModuleManager() {
    navigate(Screen.ModuleManager.route)
}

fun NavGraphBuilder.moduleManagerScreen(
    onNavigateBack: () -> Unit
) {
    composable(Screen.ModuleManager.route) {
        val viewModel: ModuleManagerViewModel = hiltViewModel()
        ModuleManagerScreen(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack
        )
    }
}
