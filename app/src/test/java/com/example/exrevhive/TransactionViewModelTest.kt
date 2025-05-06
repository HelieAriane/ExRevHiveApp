package com.example.exrevhive

import android.content.Context
import android.content.SharedPreferences
import com.example.exrevhive.Model.Category
import com.example.exrevhive.Model.Transaction
import com.example.exrevhive.Model.TransactionType
import com.example.exrevhive.ViewModel.TransactionViewModel
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class TransactionViewModelTest {
    private val context = Mockito.mock(Context::class.java)
    private val sharedPreferences = Mockito.mock(SharedPreferences::class.java)
    private val editor = Mockito.mock(SharedPreferences.Editor::class.java)
    private val transactionViewModel = TransactionViewModel()

    init {
        // Simuler le comportement de SharedPreferences
        Mockito.`when`(context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        Mockito.`when`(sharedPreferences.edit()).thenReturn(editor)
        Mockito.`when`(editor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(editor)
        Mockito.`when`(editor.apply()).then {}
    }

    // Test pour la fonction getFilteredAndSortedTransactions
    @Test
    fun getFilteredAndSortedTransactionsTest() {
        // Arrange
        val transactions = listOf(
            Transaction(1, 62.85, "Épicerie", Category.ALIMENTATION, TransactionType.DEPENSE, "12-02-2025"),
            Transaction(2, 1200.0, "Salaire", Category.SALAIRE, TransactionType.REVENU, "13-02-2025"),
            Transaction(3, 900.0, "Loyer", Category.LOGEMENT, TransactionType.DEPENSE, "14-02-2025")
        )
        transactionViewModel.updateTransactionsList(transactions)

        // Act & Assert (type "DEPENSE")
        transactionViewModel.setTransactionFilter("DEPENSE")
        var result = transactionViewModel.getFilteredAndSortedTransactions("")
        assertEquals(2, result.size)

        // Act & Assert (type "REVENU")
        transactionViewModel.setTransactionFilter("REVENU")
        result = transactionViewModel.getFilteredAndSortedTransactions("")
        assertEquals(1, result.size)

        // Act & Assert (description "Loyer")
        transactionViewModel.setTransactionFilter("All")
        result = transactionViewModel.getFilteredAndSortedTransactions("Loyer")
        assertEquals(1, result.size)

        // Act & Assert (Tri par date en ordre ascendant)
        transactionViewModel.setDateSortOrder(true)
        result = transactionViewModel.getFilteredAndSortedTransactions("")
        assertEquals(3, result.size)
        assertEquals("Épicerie", result[0].description)
        assertEquals("Loyer", result[2].description)

        // Act & Assert (Tri par date en ordre descendant)
        transactionViewModel.setDateSortOrder(false)
        result = transactionViewModel.getFilteredAndSortedTransactions("")
        assertEquals(3, result.size)
        assertEquals("Loyer", result[0].description)
        assertEquals("Épicerie", result[2].description)
    }

    // Test pour la fonction addTransaction
    @Test
    fun addTransactionTest() {
        // Arrange
        val transactions = listOf(
            Transaction(1, 62.85, "Épicerie", Category.ALIMENTATION, TransactionType.DEPENSE, "12-02-2025"),
            Transaction(2, 1200.0, "Salaire", Category.SALAIRE, TransactionType.REVENU, "13-02-2025"),
        )
        transactionViewModel.updateTransactionsList(transactions)
        val amount = 900.00
        val description = "Loyer"
        val category = Category.LOGEMENT
        val type = TransactionType.DEPENSE
        val date = "14-02-2025"
        val transactionKey = "transactionKey"
        val transactionIdKey = "transactionIdKey"

        // Act
        transactionViewModel.addTransaction(amount, description, category, type, date, context, transactionIdKey, transactionKey)
        val result = transactionViewModel.getFilteredAndSortedTransactions("")

        // Assert
        assertEquals(3, result.size)
        assertEquals(900.0, result[2].amount, 00.1)
        assertEquals("Loyer", result[2].description)
        assertEquals(Category.LOGEMENT, result[2].category)
        assertEquals(TransactionType.DEPENSE, result[2].type)
        assertEquals("14-02-2025", result[2].date)
    }

    // Test pour la fonction deleteTransaction
    @Test
    fun deleteTransactionTest() {
        // Arrange
        val transactions = listOf(
            Transaction(1, 62.85, "Épicerie", Category.ALIMENTATION, TransactionType.DEPENSE, "12-02-2025"),
            Transaction(2, 1200.0, "Salaire", Category.SALAIRE, TransactionType.REVENU, "13-02-2025"),
            Transaction(3, 900.0, "Loyer", Category.LOGEMENT, TransactionType.DEPENSE, "14-02-2025")
        )
        transactionViewModel.updateTransactionsList(transactions)

        // Act
        val transactionIdToDelete = 2
        transactionViewModel.deleteTransaction(transactionIdToDelete, context, "transactionKey")
        val result = transactionViewModel.getFilteredAndSortedTransactions("")

        // Assert
        assertEquals(2, result.size)
        val ids = result.map { it.id }
        assertEquals(false, ids.contains(transactionIdToDelete))
    }

    // Test pour la fonction modifyTransaction
    @Test
    fun modifyTransactionTest() {
        // Arrange
        val transactions = listOf(
            Transaction(1, 62.85, "Épicerie", Category.ALIMENTATION, TransactionType.DEPENSE, "12-02-2025"),
            Transaction(2, 1200.0, "Salaire", Category.SALAIRE, TransactionType.REVENU, "13-02-2025"),
            Transaction(3, 900.0, "Loyer", Category.LOGEMENT, TransactionType.DEPENSE, "14-02-2025")
        )
        transactionViewModel.updateTransactionsList(transactions)
        val newAmount = 1300.0
        val newDescription = "Salaire augmenté"
        val newCategory = Category.SALAIRE
        val newType = TransactionType.REVENU
        val newDate = "15-02-2025"
        val transactionIdToModify = 2

        // Act
        transactionViewModel.modifyTransaction(transactionIdToModify, newAmount, newDescription, newCategory, newType, newDate, context,"transactionKey")
        val result = transactionViewModel.getFilteredAndSortedTransactions("")

        // Assert
        assertEquals(1300.0, result[2].amount, 00.1)
        assertEquals("Salaire augmenté", result[2].description)
        assertEquals(Category.SALAIRE, result[2].category)
        assertEquals(TransactionType.REVENU, result[2].type)
        assertEquals("15-02-2025", result[2].date)
    }


    // Test pour la fonction saveTransaction
    @Test
    fun saveTransactionMockTest() {
        // Arrange
        val transactions = listOf(
            Transaction(1, 62.85, "Épicerie", Category.ALIMENTATION, TransactionType.DEPENSE, "12-02-2025"),
            Transaction(2, 1200.0, "Salaire", Category.SALAIRE, TransactionType.REVENU, "13-02-2025"),
            Transaction(3, 900.0, "Loyer", Category.LOGEMENT, TransactionType.DEPENSE, "14-02-2025")
        )
        val transactionKey = "transactionKey"
        val gson = Gson()
        val json = gson.toJson(transactions)

        // Act
        transactionViewModel.saveTransaction(context, transactionKey, transactions)

        // Assert
        Mockito.verify(editor).putString(transactionKey, json)
        Mockito.verify(editor).apply()
    }

    // Test pour la fonction getTransactions
    @Test
    fun getTransactionsMockTest() {
        // Arrange
        val transactions = listOf(
            Transaction(1, 62.85, "Épicerie", Category.ALIMENTATION, TransactionType.DEPENSE, "12-02-2025"),
            Transaction(2, 1200.0, "Salaire", Category.SALAIRE, TransactionType.REVENU, "13-02-2025"),
            Transaction(3, 900.0, "Loyer", Category.LOGEMENT, TransactionType.DEPENSE, "14-02-2025")
        )
        val transactionKey = "transactionKey"
        val gson = Gson()
        val json = gson.toJson(transactions)
        Mockito.`when`(sharedPreferences.getString(transactionKey, null)).thenReturn(json)

        // Act
        transactionViewModel.getTransactions(context, transactionKey)

        // Assert
        assertEquals(3, transactionViewModel._transactions.value.size)
        assertEquals(900.0, transactionViewModel._transactions.value[2].amount, 00.1)
        assertEquals("Loyer", transactionViewModel._transactions.value[2].description)
        assertEquals(Category.LOGEMENT, transactionViewModel._transactions.value[2].category)
        assertEquals(TransactionType.DEPENSE, transactionViewModel._transactions.value[2].type)
        assertEquals("14-02-2025", transactionViewModel._transactions.value[2].date)
    }

    // Test pour la fonction saveTransactionI
    @Test
    fun saveTransactionIdMockTest() {
        // Arrange
        val transactionIdKey = "transactionIdKey"
        val nextTransactionId = 4

        // Act
        transactionViewModel.saveTransactionId(context, transactionIdKey, nextTransactionId)

        // Assert
        Mockito.verify(editor).putString(transactionIdKey, nextTransactionId.toString())
        Mockito.verify(editor).apply()
    }

    // Test pour la fonction getTransactionI
    @Test
    fun getTransactionIdMockTest() {
        // Arrange
        val transactionIdKey = "transactionIdKey"
        Mockito.`when`(sharedPreferences.getString(transactionIdKey, null)).thenReturn("3")

        // Act
        val result = transactionViewModel.getTransactionId(context, transactionIdKey)

        // Assert
        assertEquals(3, result)
    }
}