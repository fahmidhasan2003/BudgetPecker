package com.fahmicode.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fahmicode.data.AppDatabase
import com.fahmicode.data.AppRepository
import com.fahmicode.data.model.Transaction
import com.fahmicode.data.model.Budget
import com.fahmicode.data.model.Note
import com.fahmicode.ui.screens.CategoryPreset
import com.fahmicode.ui.screens.CustomExpenseCategoryPresets
import com.fahmicode.ui.screens.CustomIncomeCategoryPresets
import com.fahmicode.ui.screens.ExpenseCategoryPresets
import com.fahmicode.ui.screens.IncomeCategoryPresets
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import androidx.core.content.edit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository
    private val sharedPrefs: SharedPreferences = application.getSharedPreferences("budgetpecker_prefs", Context.MODE_PRIVATE)

    val transactions: StateFlow<List<Transaction>>
    val budgets: StateFlow<List<Budget>>
    val notes: StateFlow<List<Note>>

    // Preference States
    var isDarkMode = mutableStateOf(sharedPrefs.getBoolean("is_dark_mode", false))
        private set

    var isRemindersEnabled = mutableStateOf(sharedPrefs.getBoolean("enable_reminders", true))
        private set

    var alertThreshold = mutableFloatStateOf(sharedPrefs.getFloat("alert_threshold", 80f))
        private set

    var currencySymbol = mutableStateOf(sharedPrefs.getString("currency_symbol", "৳") ?: "৳")
        private set

    var selectedDate = mutableStateOf(Calendar.getInstance())
        private set

    var showAddTransactionDialog = mutableStateOf(false)

    var calculatorHistory = mutableStateListOf<String>()

    // Currency Converter States
    var exchangeRates = mutableStateMapOf<String, Double>()
    var lastRatesUpdate = mutableStateOf("N/A")
    var isLiveRates = mutableStateOf(false)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AppRepository(database.transactionDao(), database.budgetDao(), database.noteDao())

        transactions = repository.allTransactions
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

        budgets = repository.allBudgets
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

        notes = repository.allNotes
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )
        loadCustomCategories()
        loadCalculatorHistory()
        fetchLiveRates()
    }

    fun fetchLiveRates() {
        // Fallback rates in case API fails
        val fallbackRates = mapOf(
            "USD" to 1.0, "BDT" to 118.5, "EUR" to 0.92, "GBP" to 0.77,
            "INR" to 83.5, "SAR" to 3.75, "AED" to 3.67, "CAD" to 1.37, "AUD" to 1.49
        )
        exchangeRates.putAll(fallbackRates)

        viewModelScope.launch {
            try {
                val response = repository.getExchangeRates()
                if (response.result == "success") {
                    exchangeRates.clear()
                    exchangeRates.putAll(response.rates)
                    lastRatesUpdate.value = response.lastUpdateUtc
                    isLiveRates.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                isLiveRates.value = false
            }
        }
    }

    private fun loadCalculatorHistory() {
        val historyStr = sharedPrefs.getString("calculator_history", null)
        if (!historyStr.isNullOrEmpty()) {
            try {
                val array = JSONArray(historyStr)
                calculatorHistory.clear()
                for (i in 0 until array.length()) {
                    calculatorHistory.add(array.getString(i))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addToCalculatorHistory(entry: String) {
        if (calculatorHistory.contains(entry)) return
        calculatorHistory.add(0, entry)
        if (calculatorHistory.size > 20) calculatorHistory.removeAt(calculatorHistory.lastIndex)
        saveCalculatorHistory()
    }

    fun clearCalculatorHistory() {
        calculatorHistory.clear()
        saveCalculatorHistory()
    }

    private fun saveCalculatorHistory() {
        val array = JSONArray()
        calculatorHistory.forEach { array.put(it) }
        sharedPrefs.edit { putString("calculator_history", array.toString()) }
    }

    // Custom categories integration
    @OptIn(androidx.compose.ui.graphics.ExperimentalGraphicsApi::class)
    fun addCustomCategory(name: String, emoji: String, colorValue: Long, isIncome: Boolean): Boolean {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) return false

        // Check duplicates
        val exists = ExpenseCategoryPresets.any { it.name.equals(trimmedName, ignoreCase = true) } ||
                     IncomeCategoryPresets.any { it.name.equals(trimmedName, ignoreCase = true) } ||
                     CustomExpenseCategoryPresets.any { it.name.equals(trimmedName, ignoreCase = true) } ||
                     CustomIncomeCategoryPresets.any { it.name.equals(trimmedName, ignoreCase = true) }
        
        if (exists) return false

        val keyColor = Color(colorValue)
        val preset = CategoryPreset(
            name = trimmedName,
            emoji = emoji.trim().ifEmpty { "📁" },
            color = keyColor,
            bgLightColor = keyColor.copy(alpha = 0.12f),
            bgDarkColor = keyColor.copy(alpha = 0.2f)
        )

        if (isIncome) {
            CustomIncomeCategoryPresets.add(preset)
        } else {
            CustomExpenseCategoryPresets.add(preset)
        }
        
        saveCustomCategories()
        return true
    }

    fun deleteCustomCategory(name: String): Boolean {
        val removedExpense = CustomExpenseCategoryPresets.removeAll { it.name.equals(name, ignoreCase = true) }
        val removedIncome = CustomIncomeCategoryPresets.removeAll { it.name.equals(name, ignoreCase = true) }
        if (removedExpense || removedIncome) {
            saveCustomCategories()
            return true
        }
        return false
    }

    fun updateCustomCategory(oldName: String, newName: String, newEmoji: String, newColor: Color, isIncome: Boolean): Boolean {
        // If name changed, check duplicates
        if (!oldName.equals(newName, ignoreCase = true)) {
            val exists = ExpenseCategoryPresets.any { it.name.equals(newName, ignoreCase = true) } ||
                    IncomeCategoryPresets.any { it.name.equals(newName, ignoreCase = true) } ||
                    CustomExpenseCategoryPresets.any { it.name.equals(newName, ignoreCase = true) } ||
                    CustomIncomeCategoryPresets.any { it.name.equals(newName, ignoreCase = true) }
            if (exists) return false
        }

        val list = if (isIncome) CustomIncomeCategoryPresets else CustomExpenseCategoryPresets
        val index = list.indexOfFirst { it.name.equals(oldName, ignoreCase = true) }
        
        if (index != -1) {
            val updatedPreset = CategoryPreset(
                name = newName,
                emoji = newEmoji,
                color = newColor,
                bgLightColor = newColor.copy(alpha = 0.12f),
                bgDarkColor = newColor.copy(alpha = 0.2f)
            )
            list[index] = updatedPreset
            saveCustomCategories()
            
            // If name changed, update existing transactions and budgets
            if (!oldName.equals(newName, ignoreCase = true)) {
                updateCategoryInRecords(oldName, newName)
            }
            return true
        }
        return false
    }

    // Color conversion helpers for Room/Database logic as requested
    fun colorToInt(color: Color): Int = color.toArgb()
    fun intToColor(colorInt: Int): Color = Color(colorInt)
    fun colorToHex(color: Color): String = String.format("#%08X", color.toArgb())

    @OptIn(androidx.compose.ui.graphics.ExperimentalGraphicsApi::class)
    private fun loadCustomCategories() {
        try {
            val jsonStr = sharedPrefs.getString("custom_categories_json", null)
            if (!jsonStr.isNullOrEmpty()) {
                val array = JSONArray(jsonStr)
                CustomExpenseCategoryPresets.clear()
                CustomIncomeCategoryPresets.clear()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val name = obj.getString("name")
                    val emoji = obj.getString("emoji")
                    val colorValue = obj.getLong("colorValue")
                    val isInc = obj.getBoolean("isIncome")

                    val keyColor = Color(colorValue.toULong())
                    val preset = CategoryPreset(
                        name = name,
                        emoji = emoji,
                        color = keyColor,
                        bgLightColor = keyColor.copy(alpha = 0.12f),
                        bgDarkColor = keyColor.copy(alpha = 0.2f)
                    )

                    if (isInc) {
                        CustomIncomeCategoryPresets.add(preset)
                    } else {
                        CustomExpenseCategoryPresets.add(preset)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @OptIn(androidx.compose.ui.graphics.ExperimentalGraphicsApi::class)
    private fun saveCustomCategories() {
        try {
            val array = JSONArray()
            CustomExpenseCategoryPresets.forEach { preset ->
                val obj = JSONObject().apply {
                    put("name", preset.name)
                    put("emoji", preset.emoji)
                    put("colorValue", preset.color.value.toLong())
                    put("isIncome", false)
                }
                array.put(obj)
            }
            CustomIncomeCategoryPresets.forEach { preset ->
                val obj = JSONObject().apply {
                    put("name", preset.name)
                    put("emoji", preset.emoji)
                    put("colorValue", preset.color.value.toLong())
                    put("isIncome", true)
                }
                array.put(obj)
            }
            sharedPrefs.edit { putString("custom_categories_json", array.toString()) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Toggle Preferences
    fun setDarkMode(enabled: Boolean) {
        isDarkMode.value = enabled
        sharedPrefs.edit { putBoolean("is_dark_mode", enabled) }
    }

    fun setRemindersEnabled(enabled: Boolean) {
        isRemindersEnabled.value = enabled
        sharedPrefs.edit { putBoolean("enable_reminders", enabled) }
    }

    fun setAlertThreshold(threshold: Float) {
        alertThreshold.floatValue = threshold
        sharedPrefs.edit { putFloat("alert_threshold", threshold) }
    }

    fun setCurrencySymbol(symbol: String) {
        currencySymbol.value = symbol
        sharedPrefs.edit { putString("currency_symbol", symbol) }
    }

    fun changeMonth(offset: Int) {
        val newCal = selectedDate.value.clone() as Calendar
        newCal.add(Calendar.MONTH, offset)
        selectedDate.value = newCal
    }

    @Suppress("Unused")
    fun setSelectedMonth(month: Int, year: Int) {
        val newCal = selectedDate.value.clone() as Calendar
        newCal.set(Calendar.MONTH, month)
        newCal.set(Calendar.YEAR, year)
        selectedDate.value = newCal
    }

    // Transaction Actions
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun updateCategoryInRecords(oldName: String, newName: String) {
        viewModelScope.launch {
            transactions.value.filter { it.category == oldName }.forEach {
                repository.insertTransaction(it.copy(category = newName))
            }
            budgets.value.filter { it.category == oldName }.forEach {
                repository.insertBudget(it.copy(category = newName))
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    // Budget Actions
    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            repository.insertBudget(budget)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }

    // Note Actions
    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }

    // Import / Export backup (using Android standard org.json)
    fun exportBackupToJson(): String {
        val root = JSONObject()
        val txArray = JSONArray()
        transactions.value.forEach { tx ->
            val obj = JSONObject().apply {
                put("id", tx.id)
                put("amount", tx.amount)
                put("title", tx.title)
                put("note", tx.note)
                put("category", tx.category)
                put("isIncome", tx.isIncome)
                put("dateMillis", tx.dateMillis)
            }
            txArray.put(obj)
        }
        
        val bgArray = JSONArray()
        budgets.value.forEach { bg ->
            val obj = JSONObject().apply {
                put("id", bg.id)
                put("category", bg.category)
                put("amountLimit", bg.amountLimit)
            }
            bgArray.put(obj)
        }

        root.put("transactions", txArray)
        root.put("budgets", bgArray)
        return root.toString(2)
    }

    fun importBackupFromJson(jsonString: String): Boolean {
        return try {
            val root = JSONObject(jsonString)
            val txArray = root.optJSONArray("transactions")
            val bgArray = root.optJSONArray("budgets")

            viewModelScope.launch {
                if (txArray != null) {
                    for (i in 0 until txArray.length()) {
                        val obj = txArray.getJSONObject(i)
                        val tx = Transaction(
                            id = obj.optInt("id", 0),
                            amount = obj.getDouble("amount"),
                            title = obj.getString("title"),
                            note = obj.optString("note", ""),
                            category = obj.getString("category"),
                            isIncome = obj.getBoolean("isIncome"),
                            dateMillis = obj.getLong("dateMillis")
                        )
                        repository.insertTransaction(tx)
                    }
                }

                if (bgArray != null) {
                    for (i in 0 until bgArray.length()) {
                        val obj = bgArray.getJSONObject(i)
                        val bg = Budget(
                            id = obj.optInt("id", 0),
                            category = obj.getString("category"),
                            amountLimit = obj.getDouble("amountLimit")
                        )
                        repository.insertBudget(bg)
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
