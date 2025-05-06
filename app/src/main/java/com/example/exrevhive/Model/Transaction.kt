package com.example.exrevhive.Model

import java.text.SimpleDateFormat
import java.text.DecimalFormat
import java.util.Date
import java.util.Locale

data class Transaction (
    val id: Int,
    val amount: Double,
    val description: String,
    val category: Category,
    val type: TransactionType,
    val date: String
)

enum class Category(val displayName: String) {
    LOGEMENT("Logement"),
    ALIMENTATION("Alimentation"),
    TRANSPORT("Transport"),
    SANTE("Santé"),
    PERSONNEL("Personnel"),
    SALAIRE("Salaire"),
    AUTRE("Autre");

    override fun toString(): String = displayName
}

enum class TransactionType {
    DEPENSE, REVENU
}

fun formatAmount(amount: Double): String {
    val decimalFormat = DecimalFormat("#,###.00")
    return decimalFormat.format(amount)
}

fun String.toDate(): Date {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormat.parse(this) ?: Date() // Retourne la date actuelle si la conversion échoue
}
