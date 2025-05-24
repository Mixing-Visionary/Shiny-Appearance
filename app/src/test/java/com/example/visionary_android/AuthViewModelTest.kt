package com.example.visionary_android

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import ru.visionary.mixing.shiny_appearance.data.local.TokenStorage
import ru.visionary.mixing.shiny_appearance.data.repository.AuthRepositoryImpl
import ru.visionary.mixing.shiny_appearance.domain.model.AuthResponse
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var repository: AuthRepositoryImpl
    private lateinit var tokenStorage: TokenStorage
    private lateinit var viewModel: AuthViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        tokenStorage = mock()

        whenever(tokenStorage.accessToken).thenReturn(MutableStateFlow(""))

        viewModel = AuthViewModel(repository, tokenStorage)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login should save tokens on success`() = runTest {
        val access = "access123"
        val refresh = "refresh123"

        whenever(repository.login("email@test.com", "pass1111"))
            .thenReturn(AuthResponse(accessToken = access, refreshToken = refresh))

        viewModel.login("email@test.com", "pass1111") { success ->
        }
        testDispatcher.scheduler.advanceUntilIdle()

        verify(tokenStorage).saveTokens(access, refresh)
    }

    @Test
    fun `register should save tokens on success`() = runTest {
        val access = "access456"
        val refresh = "refresh456"

        whenever(repository.register("nick", "email@test.com", "pass1111"))
            .thenReturn(AuthResponse(accessToken = access, refreshToken = refresh))

        viewModel.register("nick", "email@test.com", "pass1111")
        testDispatcher.scheduler.advanceUntilIdle()

        verify(tokenStorage).saveTokens(access, refresh)
    }

    @Test
    fun `logout should clear tokens`() = runTest {
        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        verify(tokenStorage).clearTokens()
    }
}
