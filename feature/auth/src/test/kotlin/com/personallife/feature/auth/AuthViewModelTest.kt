package com.personallife.feature.auth

import com.personallife.core.testing.repository.FakeAuthRepository
import com.personallife.core.testing.util.MainDispatcherRule
import com.personallife.feature.auth.presentation.AuthViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        fakeAuthRepository = FakeAuthRepository()
        viewModel = AuthViewModel(fakeAuthRepository)
    }

    @Test
    fun login_withEmptyEmail_setsEmailError() = runTest {
        viewModel.onEmailChanged("")
        viewModel.onPasswordChanged("password123")

        viewModel.login()

        val state = viewModel.uiState.value
        assertNotNull(state.emailError)
        assertFalse(state.isSuccess)
    }

    @Test
    fun login_withValidCredentials_succeeds() = runTest {
        viewModel.onEmailChanged("user@example.com")
        viewModel.onPasswordChanged("password123")

        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isSuccess)
        assertNotNull(state.authenticatedUser)
        assertEquals("user@example.com", state.authenticatedUser?.email)
        assertNull(state.generalError)
    }

    @Test
    fun login_whenRepositoryFails_setsGeneralError() = runTest {
        fakeAuthRepository.shouldFail = true
        fakeAuthRepository.failureMessage = "Invalid credentials"

        viewModel.onEmailChanged("user@example.com")
        viewModel.onPasswordChanged("password123")

        viewModel.login()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isSuccess)
        assertEquals("Invalid credentials", state.generalError)
    }

    @Test
    fun register_withShortPassword_setsPasswordError() = runTest {
        viewModel.onDisplayNameChanged("Test User")
        viewModel.onEmailChanged("test@example.com")
        viewModel.onPasswordChanged("short")

        viewModel.register()

        val state = viewModel.uiState.value
        assertNotNull(state.passwordError)
        assertFalse(state.isSuccess)
    }

    @Test
    fun logout_clearsAuthenticatedUser() = runTest {
        viewModel.onEmailChanged("user@example.com")
        viewModel.onPasswordChanged("password123")
        viewModel.login()
        advanceUntilIdle()

        viewModel.logout()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.authenticatedUser)
        assertFalse(state.isSuccess)
    }
}
