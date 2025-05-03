package com.example.visionary_android

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.RegistrationViewModel

class RegistrationViewModelTest {

    private lateinit var viewModel: RegistrationViewModel

    @Before
    fun setUp() {
        viewModel = RegistrationViewModel()
    }

    @Test
    fun `valid credentials return true`() {
        val result = viewModel.validateCredentials(
            username = "validUser",
            password = "password123",
            confirmPassword = "password123"
        )
        assertTrue(result)
    }

    @Test
    fun `passwords do not match returns false`() {
        val result = viewModel.validateCredentials(
            username = "validUser",
            password = "password123",
            confirmPassword = "wrongpass"
        )
        assertFalse(result)
    }

    @Test
    fun `password too short returns false`() {
        val result = viewModel.validateCredentials(
            username = "validUser",
            password = "short",
            confirmPassword = "short"
        )
        assertFalse(result)
    }

    @Test
    fun `username too short returns false`() {
        val result = viewModel.validateCredentials(
            username = "abc",
            password = "password123",
            confirmPassword = "password123"
        )
        assertFalse(result)
    }

    @Test
    fun `username too long returns false`() {
        val result = viewModel.validateCredentials(
            username = "a".repeat(26),
            password = "password123",
            confirmPassword = "password123"
        )
        assertFalse(result)
    }
}
