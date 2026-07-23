package com.fahmicode.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val title: String,
    val note: String,
    val category: String,
    val isIncome: Boolean,
    val dateMillis: Long
)

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val amountLimit: Double
)

data class ChecklistItem(
    val text: String,
    var isChecked: Boolean = false
)

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val checklistItems: List<ChecklistItem> = emptyList(),
    val amount: Double? = null,
    val expenseCategory: String? = null,
    val dateMillis: Long = System.currentTimeMillis(),
    val colorHex: Long = 0xFFFFFFFF
)
