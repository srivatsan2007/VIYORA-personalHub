package com.personallife.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.personallife.app.navigation.AppNavHost
import com.personallife.core.common.security.BiometricAuthManager
import com.personallife.core.common.security.WindowSecurityHelper
import com.personallife.core.data.repository.AuthRepository
import com.personallife.core.data.repository.AuthState
import com.personallife.core.data.repository.UserPreferencesRepository
import com.personallife.core.designsystem.component.LoadingStateView
import com.personallife.core.designsystem.theme.PersonalLifeTheme
import com.personallife.core.model.ThemeMode
import com.personallife.core.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var preferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val biometricManager = BiometricAuthManager(this)

        setContent {
            val userPreferences by preferencesRepository.userPreferences.collectAsStateWithLifecycle(
                initialValue = com.personallife.core.model.UserPreferences()
            )
            val authState by authRepository.authState.collectAsStateWithLifecycle(
                initialValue = AuthState.Loading
            )

            // Dynamic Window FLAG_SECURE protection
            LaunchedEffect(userPreferences.isFlagSecureEnabled) {
                WindowSecurityHelper.applyFlagSecure(
                    activity = this@MainActivity,
                    enable = userPreferences.isFlagSecureEnabled
                )
            }

            PersonalLifeTheme(themeMode = userPreferences.themeMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var isBiometricUnlocked by remember { mutableStateOf(!userPreferences.isBiometricEnabled) }

                    // Trigger biometric unlock if enabled and not yet unlocked
                    LaunchedEffect(userPreferences.isBiometricEnabled, authState) {
                        if (userPreferences.isBiometricEnabled && authState is AuthState.Authenticated && !isBiometricUnlocked) {
                            biometricManager.authenticate(
                                activity = this@MainActivity,
                                onSuccess = { isBiometricUnlocked = true },
                                onError = { /* User can retry */ }
                            )
                        } else if (!userPreferences.isBiometricEnabled) {
                            isBiometricUnlocked = true
                        }
                    }

                    when (val state = authState) {
                        is AuthState.Loading -> {
                            LoadingStateView(message = "Initializing Personal Hub...")
                        }
                        is AuthState.Authenticated -> {
                            if (isBiometricUnlocked) {
                                val navController = rememberNavController()
                                AppNavHost(
                                    navController = navController,
                                    startDestination = Screen.Dashboard.route
                                )
                            } else {
                                LoadingStateView(message = "Awaiting Biometric Authentication...")
                            }
                        }
                        is AuthState.Unauthenticated -> {
                            val navController = rememberNavController()
                            AppNavHost(
                                navController = navController,
                                startDestination = Screen.AuthLogin.route
                            )
                        }
                    }
                }
            }
        }
    }
}
