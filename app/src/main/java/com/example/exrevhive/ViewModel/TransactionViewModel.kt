package com.example.exrevhive.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.exrevhive.Model.Category
import com.example.exrevhive.Model.Transaction
import com.example.exrevhive.Model.TransactionType
import com.example.exrevhive.Model.toDate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TransactionViewModel: ViewModel() {
    // Liste mutable des transaction
    val _transactions = MutableStateFlow<List<Transaction>>(emptyList()) // Initialisation avec une liste vide
    val transactions = _transactions.asStateFlow() // Exposition en StateFlow immuable

    // Filtre par type : "All" par défaut
    var filter by mutableStateOf("All")
        private set

    // Tri par date : true = ascendant, false = descendant
    var sortByDateAscending by mutableStateOf(true)
        private set

    // Retourne les transactions filtrées et triées
    fun getFilteredAndSortedTransactions(searchQuery: String): List<Transaction> {
        var filteredTransactions = _transactions.value

        // Filtrage par recherche (description)
        if (searchQuery.isNotEmpty()) {
            filteredTransactions = filteredTransactions.filter {
                it.description.contains(searchQuery, ignoreCase = true)
            }
        }

        // Filtrage par type (DEPENSE, REVENU, All)
        filteredTransactions = when (filter) {
            "DEPENSE" -> filteredTransactions.filter { it.type == TransactionType.DEPENSE }
            "REVENU" -> filteredTransactions.filter { it.type == TransactionType.REVENU }
            else -> filteredTransactions
        }

        // Tri par date
        filteredTransactions = if (sortByDateAscending) {
            filteredTransactions.sortedBy { it.date.toDate() }
        } else {
            filteredTransactions.sortedByDescending { it.date.toDate() }
        }

        return filteredTransactions
    }

    // Modifier filtre par type
    fun setTransactionFilter(newFilter: String) {
        filter = newFilter
    }

    // Modifier l'ordre de tri par date
    fun setDateSortOrder(ascending: Boolean) {
        sortByDateAscending = ascending
    }

    // Fonction pour mettre à jour la liste des transactions
    fun updateTransactionsList(loadedTransactions: List<Transaction>) {
        _transactions.value = loadedTransactions
    }

    // Fonction pour sauvegarde le prochain ID
    fun saveTransactionId(context: Context, transactionIdKey: String, transactionId: Int) {
        val gson = Gson()
        val json = gson.toJson(transactionId) // Convertir transactionId en JSON
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(transactionIdKey, json).apply() // Sauvegarder, - .apply() pour une sauvegarde asynchrone
    }

    // Fonction pour récupérer les ID
    fun getTransactionId(context: Context, transactionIdKey: String): Int {
        val gson = Gson()
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(transactionIdKey, null) ?: return 1
        return gson.fromJson(json, Int::class.java) // Désérialiser le JSON en entier
    }

    // Fonction pour ajouter une nouvelle tâche
    fun addTransaction(amount: Double, description: String, category: Category, type: TransactionType, date: String, context: Context, transactionIdKey: String, transactionKey: String) {
        val nextId = getTransactionId(context, transactionIdKey)
        val newTransaction = Transaction(nextId, amount, description, category, type, date)
        _transactions.value += newTransaction // Ajouter une nouvelle transaction à la liste existante
        saveTransactionId(context, transactionIdKey, nextId + 1)
        saveTransaction(context, transactionKey, _transactions.value)
    }

    // Fonction pour supprimer une tâche par l'ID
    fun deleteTransaction(transactionId: Int, context: Context, transactionKey: String) {
        _transactions.value = _transactions.value.filterNot { it.id == transactionId } // Filtrer la tâche à supprimer
        saveTransaction(context, transactionKey, _transactions.value)
    }

    // Fonction pour modifier une tâche par l'ID
    fun modifyTransaction(transactionId: Int, newAmount: Double, newDescription: String, newCategory: Category, newType: TransactionType, newDate: String, context: Context, transactionKey: String) {
        // Trouver l'index de la tâche à modifier
        val transactionIndex = _transactions.value.indexOfFirst { it.id == transactionId }
        if (transactionIndex != -1) {
            // Modifier la tâche en créant une copie avec les nouveaux champs
            _transactions.value = _transactions.value.toMutableList().apply {
                this[transactionIndex] = this[transactionIndex].copy(
                    amount = newAmount,
                    description = newDescription,
                    category = newCategory,
                    type = newType,
                    date = newDate
                )
            }
        }
        // Sauvegarder la liste mise à jour
        saveTransaction(context, transactionKey, _transactions.value)
    }

    // Fonction pour sauvegarde les tâches
    fun saveTransaction(context: Context, transactionKey: String, transactions: List<Transaction>) {
        val gson = Gson()
        val json = gson.toJson(transactions) // Convertir liste d'objets transactions en JSON
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(transactionKey, json).apply() // Sauvegarder, - .apply() pour une sauvegarde asynchrone
    }

    // Fonction pour récupérer les tâches
    fun getTransactions(context: Context, transactionKey: String) {
        val gson = Gson()
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(transactionKey, null)
        val listType = object : TypeToken<List<Transaction>>() {}.type
        val loadedTransactions = gson.fromJson<List<Transaction>>(json, listType) ?: emptyList()
        _transactions.value = loadedTransactions
    }
}