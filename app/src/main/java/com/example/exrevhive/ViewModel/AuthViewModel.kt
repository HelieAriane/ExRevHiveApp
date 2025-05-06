package com.example.exrevhive.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val _username = MutableStateFlow("") // Initialisation avec une chaîne vide
    val username = _username.asStateFlow() // Exposer en tant que StateFlow immuable

    var isLoggedIn by mutableStateOf(false)
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf("")

    fun init(context: Context) {
        _username.value = getUsername(context, "usernameKey") // Charger le nom d'utilisateur au démarrage
    }

    // Fonction pour l'authentification
    fun authenticate(username: String, password: String, context: Context) {
        // Identifiants hardcodées pour la connexion
        val hardcodedUsername = "User123"
        val hardcodedPassword = "password123"

        // Vérification des informations de connexion
        if (username.isEmpty() || password.isEmpty()) {
            errorMessage = "Les champs ne peuvent pas être vides"
            return
        }

        if (username == hardcodedUsername && password == hardcodedPassword) {
            isLoggedIn = true
            _username.value = username // Mettre à jour le nom d'utilisateur
            saveLoginState(context , "loginKey", true) // Sauvegarde de l'état de connexion
            saveUsername(context, "usernameKey", username) // Sauvegarder le nom d'utilisateur
            errorMessage = "" // Réinitialisation du message d'erreur
        } else {
            isLoggedIn = false
            errorMessage = "Nom d'utilisateur ou mot de passe incorrect" // Affiche message d'erreur
        }
    }

    //Fonction pour sauvegarder l’état de connexion
    fun saveLoginState(context: Context, loginKey: String, isLoggedIn: Boolean) {
        val gson = Gson()
        val json = gson.toJson(isLoggedIn) // Convertir le booléen en JSON
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE) // Accéder aux SharedPreferences de l'application
        sharedPreferences.edit().putString(loginKey, json).apply() // Sauvegarder l'état de connexion - .apply() pour une sauvegarde asynchrone
    }

    // Fonction pour récupérer l'état de connexion
    fun getLoginState(context: Context, loginKey: String): Boolean {
        val gson = Gson()
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(loginKey, "false") // Récupérer le JSON
        return gson.fromJson(json, Boolean::class.java) // Convertir le JSON en booléen et le retourner
    }

    // Fonction pour sauvegarder le nom d'utilisateur
    fun saveUsername(context: Context, userKey: String, username: String) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE) // Accéder aux SharedPreferences de l'application
        sharedPreferences.edit().putString(userKey, username).apply() // Sauvegarder l'état de connexion - .apply() pour une sauvegarde asynchrone
    }

    // Fonction pour récupérer le nom d'utilisateur
    fun getUsername(context: Context, userKey: String): String {
        val gson = Gson()
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(userKey, "") ?: "" // Récupérer le JSON
        return gson.fromJson(json, String::class.java) // Convertir le JSON en booléen et le retourner
    }

    //Fonction de déconnexion
    fun logout(context: Context, loginKey: String) {
        isLoggedIn = false
        saveLoginState(context, loginKey, false) // Sauvegarde de l'état de déconnexion
        saveUsername(context, "usernameKey", "") // Supprimer le nom d'utilisateur
    }
}