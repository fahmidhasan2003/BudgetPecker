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
    val amountLimit: Double,
    val month: Int = -1,
    val year: Int = -1
)

data class ChecklistItem(
    val text: String,
    var isChecked: Boolean = false
)

data class TextSpan(
    val text: String,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val colorHex: Int? = null
)

data class RichTextContent(
    val spans: List<TextSpan> = emptyList()
)

data class CalculationItem(
    val description: String,
    val amount: Double
)

data class CalculationTable(
    val items: List<CalculationItem> = emptyList(),
    val total: Double = 0.0
)

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String, // Plain text for search and preview
    val contentJson: String = "", // Rich text JSON
    val category: String = "General",
    val updatedAt: Long = System.currentTimeMillis(),
    val containsTable: Boolean = false,
    val tableDataJson: String? = null,
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val colorHex: Long = 0xFF121212,
    val isPinned: Boolean = false
)

enum class FabAction {
    TextNote, CalculationTable, Image, Audio
}
