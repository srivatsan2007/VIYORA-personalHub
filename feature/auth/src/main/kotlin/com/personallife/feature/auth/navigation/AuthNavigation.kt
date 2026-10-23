package com.personallife.feature.auth.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.personallife.core.navigation.Screen
import com.personallife.feature.auth.presentation.AuthViewModel
import com.personallife.feature.auth.presentation.LoginScreen
import com.personallife.feature.auth.presentation.RegisterScreen

fun NavController.navigateToLogin() {
    navigate(Screen.AuthLogin.route) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavController.navigateToRegister() {
    navigate(Screen.AuthRegister.route)
}

fun NavGraphBuilder.authGraph(
    onNavigateToRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    composable(Screen.AuthLogin.route) {
        val viewModel: AuthViewModel = hiltViewModel()
        LoginScreen(
            viewModel = viewModel,
            onNavigateToRegister = onNavigateToRegister,
            onLoginSuccess = onAuthSuccess
        )
    }

    composable(Screen.AuthRegister.route) {
        val viewModel: AuthViewModel = hiltViewModel()
        RegisterScreen(
            viewModel = viewModel,
            onNavigateToLogin = onNavigateToLogin,
            onRegisterSuccess = onAuthSuccess
        )
    }
}
