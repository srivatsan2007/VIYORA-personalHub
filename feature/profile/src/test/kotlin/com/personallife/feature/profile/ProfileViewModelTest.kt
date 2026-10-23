package com.personallife.feature.profile

import com.personallife.core.model.UserProfile
import com.personallife.core.testing.repository.FakeAuthRepository
import com.personallife.core.testing.repository.FakeUserRepository
import com.personallife.core.testing.util.MainDispatcherRule
import com.personallife.feature.profile.presentation.ProfileViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeUserRepository: FakeUserRepository
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        fakeAuthRepository = FakeAuthRepository()
        fakeUserRepository = FakeUserRepository()
        viewModel = ProfileViewModel(fakeAuthRepository, fakeUserRepository)
    }

    @Test
    fun saveProfile_withBlankDisplayName_setsError() = runTest {
        viewModel.onDisplayNameChanged("")
        var successCalled = false
        viewModel.saveProfile { successCalled = true }

        val state = viewModel.uiState.value
        assertNotNull(state.displayNameError)
        assertEquals(false, successCalled)
    }

    @Test
    fun saveProfile_withValidInput_updatesProfile() = runTest {
        val testUser = UserProfile(uid = "test-uid", displayName = "Old Name", email = "test@example.com")
        fakeUserRepository.storedProfile = testUser

        viewModel.onDisplayNameChanged("New Name")
        viewModel.onBioChanged("New Bio")

        var successCalled = false
        viewModel.saveProfile { successCalled = true }
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(successCalled)
        assertEquals("New Name", state.profile.displayName)
        assertEquals("New Bio", state.profile.bio)
    }
}
