package com.example.exrevhive

import android.content.Context
import android.content.SharedPreferences
import com.example.exrevhive.ViewModel.AuthViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class AuthViewModelTest {
    private val context = Mockito.mock(Context::class.java)
    private val sharedPreferences = Mockito.mock(SharedPreferences::class.java)
    private val editor = Mockito.mock(SharedPreferences.Editor::class.java)
    private val authViewModel = AuthViewModel()

    init {
        // Simuler le comportement de SharedPreferences
        Mockito.`when`(context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        Mockito.`when`(sharedPreferences.edit()).thenReturn(editor)
        Mockito.`when`(editor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(editor)
        Mockito.`when`(editor.apply()).then {}
    }

    // Test pour la fonction authenticate
    @Test
    fun authenticateTest() {
        // Arrange
        val username = "User123"
        val password = "password123"
        val incorrectPassword = "wrongPassword"

        // Act & Assert (Cas correct)
        authViewModel.authenticate(username, password, context)
        assertEquals(true, authViewModel.isLoggedIn)
        assertEquals("User123", authViewModel.username.value)
        assertEquals("", authViewModel.errorMessage)

        // Act & Assert (Cas incorrect)
        authViewModel.authenticate(username, incorrectPassword, context)
        assertEquals(false, authViewModel.isLoggedIn)
        assertEquals("Nom d'utilisateur ou mot de passe incorrect", authViewModel.errorMessage)
    }

    // Test pour la fonction logout
    @Test
    fun logoutTest() {
        // Arrange
        authViewModel.isLoggedIn = true

        // Act
        authViewModel.logout(context, "loginKey")

        // Assert
        assertEquals(false, authViewModel.isLoggedIn)
    }

    // Test pour la fonction saveLoginState
    @Test
    fun saveLoginStateMockTest() {
        // Arrange
        val loginKey = "loginKey"
        val isLoggedIn = true

        // Act
        authViewModel.saveLoginState(context, loginKey, isLoggedIn)

        // Assert
        Mockito.verify(editor).putString(loginKey, "true")
        Mockito.verify(editor).apply()
    }

    // Test pour la fonction getLoginState
    @Test
    fun getLoginStateMockTest() {
        // Arrange
        val loginKey = "loginKey"
        Mockito.`when`(sharedPreferences.getString(loginKey, "false")).thenReturn("true")

        // Act
        val result = authViewModel.getLoginState(context, loginKey)

        // Assert
        assertEquals(true, result)
    }

    // Test pour la fonction saveUsername
    @Test
    fun saveUsernameMockTest() {
        // Arrange
        val userKey = "userKey"
        val username = "User123"

        // Act
        authViewModel.saveUsername(context, userKey, username)

        // Assert
        Mockito.verify(editor).putString(userKey, username)
        Mockito.verify(editor).apply()
    }

    // Test pour la fonction getUsername
    @Test
    fun getUsernameMockTest() {
        // Arrange
        val userKey = "userKey"
        Mockito.`when`(sharedPreferences.getString(userKey, "")).thenReturn("User123")

        // Act
        val result = authViewModel.getUsername(context, userKey)

        // Assert
        assertEquals("User123", result)
    }
}