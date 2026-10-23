package com.personallife.feature.profile.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personallife.core.navigation.Screen
import com.personallife.feature.profile.presentation.EditProfileScreen
import com.personallife.feature.profile.presentation.ProfileScreen
import com.personallife.feature.profile.presentation.ProfileViewModel

fun NavController.navigateToProfile() {
    navigate(Screen.Profile.route)
}

fun NavController.navigateToEditProfile() {
    navigate(Screen.EditProfile.route)
}

fun NavGraphBuilder.profileGraph(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit
) {
    composable(Screen.Profile.route) {
        val viewModel: ProfileViewModel = hiltViewModel()
        ProfileScreen(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack,
            onNavigateToEdit = onNavigateToEdit
        )
    }

    composable(Screen.EditProfile.route) {
        val viewModel: ProfileViewModel = hiltViewModel()
        EditProfileScreen(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack
        )
    }
}
