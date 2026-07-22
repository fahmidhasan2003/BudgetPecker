@file:OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package com.fahmicode.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fahmicode.data.model.Budget
import com.fahmicode.data.model.Transaction
import com.fahmicode.ui.MainViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Preset Categories with visual stylings matching SS 1 / SS 6
data class CategoryPreset(
    val name: String,
    val emoji: String,
    val color: Color,
    val bgLightColor: Color,
    val bgDarkColor: Color
)

val ExpenseCategoryPresets = listOf(
    CategoryPreset("Food", "🍚", Color(0xFFF44336), Color(0xFFFFECEB), Color(0xFF3E1D1B)),
    CategoryPreset("Transport", "🚗", Color(0xFF1E88E5), Color(0xFFE3F2FD), Color(0xFF16273A)),
    CategoryPreset("Shopping", "🛍️", Color(0xFF9C27B0), Color(0xFFF3E5F5), Color(0xFF2E1A31)),
    CategoryPreset("Bills", "📄", Color(0xFFFF9800), Color(0xFFFFF3E0), Color(0xFF352011)),
    CategoryPreset("Entertainment", "🎮", Color(0xFF4CAF50), Color(0xFFE8F5E9), Color(0xFF122C15)),
    CategoryPreset("Health", "🏥", Color(0xFFE91E63), Color(0xFFFCE4EC), Color(0xFF311520)),
    CategoryPreset("Others", "💰", Color(0xFF757575), Color(0xFFECEFF1), Color(0xFF252C30))
)

val IncomeCategoryPresets = listOf(
    CategoryPreset("Salary", "💼", Color(0xFF2E7D32), Color(0xFFE8F5E9), Color(0xFF122C15)),
    CategoryPreset("Freelance", "💻", Color(0xFF0097A7), Color(0xFFE0F7FA), Color(0xFF102D30)),
    CategoryPreset("Tuition", "🎓", Color(0xFF1565C0), Color(0xFFE3F2FD), Color(0xFF142436)),
    CategoryPreset("Investment", "📈", Color(0xFFEF6C00), Color(0xFFFFF3E0), Color(0xFF352011)),
    CategoryPreset("Gift", "🎁", Color(0xFFAD1457), Color(0xFFFCE4EC), Color(0xFF311520)),
    CategoryPreset("Others", "💰", Color(0xFF558B2F), Color(0xFFF1F8E9), Color(0xFF1C2D13))
)

val CustomExpenseCategoryPresets = androidx.compose.runtime.mutableStateListOf<CategoryPreset>()
val CustomIncomeCategoryPresets = androidx.compose.runtime.mutableStateListOf<CategoryPreset>()

val ExpenseCategoryPresetsAll: List<CategoryPreset>
    get() = ExpenseCategoryPresets + CustomExpenseCategoryPresets

val IncomeCategoryPresetsAll: List<CategoryPreset>
    get() = IncomeCategoryPresets + CustomIncomeCategoryPresets

val CategoryPresets: List<CategoryPreset>
    get() = ExpenseCategoryPresetsAll + IncomeCategoryPresetsAll

fun getCategoryPreset(name: String): CategoryPreset {
    val searchName = name.trim().lowercase()
    val preset = CategoryPresets.find { it.name.trim().lowercase() == searchName }
    if (preset != null) return preset

    // Default distinctive color for unknown categories
    val defaultColors = listOf(
        Color(0xFF9C27B0), Color(0xFF673AB7), Color(0xFF3F51B5),
        Color(0xFF009688), Color(0xFFCDDC39), Color(0xFFFF9800)
    )
    val hash = name.hashCode().let { if (it < 0) -it else it }
    val color = defaultColors[hash % defaultColors.size]

    return CategoryPreset(name, "📦", color, color.copy(alpha = 0.1f), color.copy(alpha = 0.2f))
}

// Global currency formatter standard
private val df = DecimalFormat("#,##,###.00")
fun formatCurrency(amount: Double, symbol: String = "৳"): String {
    return symbol + df.format(amount)
}

fun formatDate(dateMillis: Long): String {
    val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return sdf.format(Date(dateMillis))
}

fun isSameMonth(millis: Long, cal: Calendar): Boolean {
    val tCal = Calendar.getInstance().apply { timeInMillis = millis }
    return tCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
            tCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
}

@Composable
fun MonthNavigator(
    selectedDate: Calendar,
    onMonthChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val format = remember { SimpleDateFormat("MMMM yyyy", Locale.US) }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onMonthChange(-1) }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
            }
            Text(
                text = format.format(selectedDate.time),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = { onMonthChange(1) }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onSeeMore: () -> Unit = {}
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    val isDark by viewModel.isDarkMode
    val currencySymbol = viewModel.currencySymbol.value
    val selectedDate by viewModel.selectedDate

    var searchQuery by remember { mutableStateOf("") }

    val filteredTransactions = remember(searchQuery, transactions) {
        if (searchQuery.isNotBlank()) {
            transactions.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.category.contains(searchQuery, ignoreCase = true) ||
                        it.note.contains(searchQuery, ignoreCase = true) ||
                        it.amount.toString().contains(searchQuery) ||
                        df.format(it.amount).contains(searchQuery)
            }
        } else {
            transactions.take(10)
        }
    }

    val totalIncome = remember(transactions, selectedDate) {
        transactions.filter { it.isIncome && isSameMonth(it.dateMillis, selectedDate) }
            .sumOf { it.amount }
    }
    val totalExpense = remember(transactions, selectedDate) {
        transactions.filter { !it.isIncome && isSameMonth(it.dateMillis, selectedDate) }
            .sumOf { it.amount }
    }
    val balance = remember(totalIncome, totalExpense) { totalIncome - totalExpense }

    // Dialog State
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }
    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Custom Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "BudgetPecker",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Your Private Expense Companion",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Search Input Block - elegant
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search...", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .width(180.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.6f
                    )
                ),
                singleLine = true
            )
        }

        // Month Selector - Moved here
        MonthNavigator(
            selectedDate = selectedDate,
            onMonthChange = { viewModel.changeMonth(it) }
        )

        if (searchQuery.isBlank()) {

            // 1. Core Total Balance Slate Card (White/light-gray high contrast premium card)
            val formattedBalance = if (balance < 0) {
                "-$currencySymbol${DecimalFormat("#,##,###").format(-balance.toInt())}"
            } else {
                "$currencySymbol${DecimalFormat("#,##,###").format(balance.toInt())}"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Total Balance",
                            color = Color(0xFF5F6368),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            text = formattedBalance,
                            color = Color(0xFF0F172A),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = null,
                        tint = Color(0xFFBDC1C6),
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }

            // 2. Total Income Card (premium dark/tinted styling layered vertical stack item)
            val formattedIncome =
                "${currencySymbol}${DecimalFormat("#,##,###").format(totalIncome.toInt())}"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF080D16) else Color(0xFFF1FBF4)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isDark) Color(0xFF131F33) else Color(0xFFC2E8CE)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Total Income",
                            color = if (isDark) Color(0xFF7D8C9E) else Color(0xFF2E7D32),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            text = "+$formattedIncome",
                            color = if (isDark) Color(0xFF2E7D32) else Color(0xFF1B5E20),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        tint = if (isDark) Color(0xFF2E7D32).copy(alpha = 0.5f) else Color(
                            0xFF81C784
                        ),
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }

            // 3. Total Expenses Card (premium dark/tinted styling layered vertical stack item)
            val formattedExpense =
                "${currencySymbol}${DecimalFormat("#,##,###").format(totalExpense.toInt())}"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF080D16) else Color(0xFFFDF2F2)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isDark) Color(0xFF131F33) else Color(0xFFF9D5D5)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Total Expenses",
                            color = if (isDark) Color(0xFF7D8C9E) else Color(0xFFC62828),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            text = "-$formattedExpense",
                            color = if (isDark) Color(0xFFC62828) else Color(0xFFB71C1C),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                        contentDescription = null,
                        tint = if (isDark) Color(0xFFC62828).copy(alpha = 0.5f) else Color(
                            0xFFE57373
                        ),
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }

            // 3. Budgets summary progress overview matching SS 1
            Text(
                "Budget Progress",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            if (budgets.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No budgets set. Go to Budgets tab to set your monthly budgets.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                val dashboardBudgetsUnit = remember(budgets) { budgets.take(2) }
                val dashboardBudgetsSpendMap = remember(transactions, dashboardBudgetsUnit, selectedDate) {
                dashboardBudgetsUnit.associateWith { budget ->
                    transactions.filter {
                        !it.isIncome &&
                                it.category.lowercase() == budget.category.lowercase() &&
                                isSameMonth(it.dateMillis, selectedDate)
                    }.sumOf { it.amount }
                }
            }
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    dashboardBudgetsUnit.forEach { budget ->
                        val spentCat = dashboardBudgetsSpendMap[budget] ?: 0.0
                        val ratio =
                            if (budget.amountLimit > 0) (spentCat / budget.amountLimit).toFloat() else 0f
                        val clampedRatio = ratio.coerceIn(0f, 1f)

                        val preset = getCategoryPreset(budget.category)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(preset.emoji, fontSize = 16.sp)
                                        Text(
                                            budget.category,
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Text(
                                        "${(ratio * 100).toInt()}% of ${currencySymbol}${budget.amountLimit.toInt()}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { clampedRatio },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape),
                                    color = if (ratio > (viewModel.alertThreshold.value / 100f)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary,
                                    strokeCap = StrokeCap.Round
                                )
                            }
                        }
                    }
                }
            }

            // 4. Expense Categories donut chart mapping - Selected Month
        val expenses = remember(transactions, selectedDate) {
            transactions.filter { !it.isIncome && isSameMonth(it.dateMillis, selectedDate) }
        }
            val catGroup = remember(expenses) {
                expenses.groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }
            }

            if (expenses.isNotEmpty()) {
                Text(
                    "Expense Categories",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val totalForPie = catGroup.values.sum()
                        
                        val segments = remember(catGroup, CustomExpenseCategoryPresets.size) {
                            catGroup.map { (name, sum) ->
                                val preset = getCategoryPreset(name)
                                // Fallback to a distinctive default color if preset color is essentially empty/null (logic handled by getCategoryPreset)
                                Triple(name, sum, preset.color)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                var cumulativeStart = -90f
                                segments.forEach { (_, sum, color) ->
                                    val proportion = if (totalForPie > 0) (sum / totalForPie).toFloat() else 0f
                                    val sweep = proportion * 360f
                                    
                                    if (sweep > 0) {
                                        drawArc(
                                            color = color,
                                            startAngle = cumulativeStart,
                                            sweepAngle = sweep,
                                            useCenter = true,
                                            style = Fill
                                        )
                                    }
                                    cumulativeStart += sweep
                                }
                            }
                        }

                        // Legend markers
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                segments.forEach { (catName, sum, color) ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                        )
                                        val percentage = if (totalForPie > 0) (sum / totalForPie * 100).toInt() else 0
                                        Text(
                                            text = "$catName ($percentage%)",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. Income vs Expense Bar chart
            Text(
                "Income vs Expense",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Compile 6 past months bar graphics
                val monthData = remember(transactions, selectedDate) {
                    val data = mutableListOf<MonthBarData>()
                    val format = SimpleDateFormat("MMM", Locale.US)
                    for (i in 5 downTo 0) {
                        val cal = (selectedDate.clone() as Calendar).apply { add(Calendar.MONTH, -i) }
                        val mName = format.format(cal.time)
                        val mNum = cal.get(Calendar.MONTH)
                        val mYear = cal.get(Calendar.YEAR)

                        val monthTransactions = transactions.filter { tx ->
                            val tCal =
                                Calendar.getInstance().apply { timeInMillis = tx.dateMillis }
                            tCal.get(Calendar.MONTH) == mNum && tCal.get(Calendar.YEAR) == mYear
                        }
                            val incSum =
                                monthTransactions.filter { it.isIncome }.sumOf { it.amount }
                            val expSum =
                                monthTransactions.filter { !it.isIncome }.sumOf { it.amount }
                            data.add(MonthBarData(mName, incSum, expSum))
                        }
                        data
                    }

                    val maxVal = (monthData.flatMap { listOf(it.income, it.expense) }.maxOrNull()
                        ?: 100.0).coerceAtLeast(100.0)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        monthData.forEach { data ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.Bottom,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp)
                                ) {
                                    // Income bar in Green
                                    val incHeight =
                                        (data.income / maxVal * 120.0).coerceIn(2.0, 120.0).dp
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(incHeight)
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 4.dp,
                                                    topEnd = 4.dp
                                                )
                                            )
                                            .background(Color(0xFF4CAF50))
                                    )
                                    // Expense bar in Red
                                    val expHeight =
                                        (data.expense / maxVal * 120.0).coerceIn(2.0, 120.0).dp
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(expHeight)
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 4.dp,
                                                    topEnd = 4.dp
                                                )
                                            )
                                            .background(Color(0xFFE53935))
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    data.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // 6. Recent transactions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                TextButton(
                    onClick = onSeeMore,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "See More",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "See More",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No transactions yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    transactions.take(10).forEach { tx ->
                        TransactionTile(
                            tx = tx,
                            isDark = isDark,
                            onEdit = {},
                            onDelete = {},
                            currencySymbol = currencySymbol,
                            enableActions = false
                        )
                    }
                }
            }
        } else {
            // ACTIVE SEARCH RESULTS MODE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Search Results",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${filteredTransactions.size} found",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No matching transactions",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    filteredTransactions.forEach { tx ->
                        TransactionTile(
                            tx = tx,
                            isDark = isDark,
                            onEdit = {},
                            onDelete = {},
                            currencySymbol = currencySymbol,
                            enableActions = false
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation prompt dialog matching SS 2
    if (transactionToDelete != null) {
        val tx = transactionToDelete!!
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Delete Transaction?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This will permanently remove this transaction (${tx.title}). This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTransaction(tx)
                        transactionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { transactionToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit prompt dialog matching SS 3
    if (transactionToEdit != null) {
        val tx = transactionToEdit!!
        var showEditFieldsDialog by remember { mutableStateOf(false) }

        if (!showEditFieldsDialog) {
            AlertDialog(
                onDismissRequest = { transactionToEdit = null },
                title = { Text("Edit Transaction?", fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "Do you want to modify this transaction?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showEditFieldsDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Yes, Edit")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { transactionToEdit = null }) {
                        Text("No")
                    }
                }
            )
        } else {
            AddOrEditTransactionDialog(
                tx = tx,
                onDismiss = { transactionToEdit = null },
                onSave = { updated ->
                    viewModel.addTransaction(updated) // Saves replacement
                    transactionToEdit = null
                },
                currencySymbol = currencySymbol
            )
        }
    }
}

class MonthBarData(val name: String, val income: Double, val expense: Double)

@Composable
fun SummaryCard(
    title: String,
    amount: Double,
    color: Color,
    bgColor: Color,
    iconColor: Color,
    isIncome: Boolean,
    isDark: Boolean,
    modifier: Modifier = Modifier,
    currencySymbol: String = "৳"
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    color = color.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    text = (if (isIncome) "+" else "-") + currencySymbol + DecimalFormat("#,##,###").format(amount.toInt()),
                    color = color,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )
                )
            }
            Icon(
                imageVector = if (isIncome) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun TransactionTile(
    tx: Transaction,
    isDark: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    currencySymbol: String = "৳",
    enableActions: Boolean = true,
    showDeleteButton: Boolean = false
) {
    val preset = getCategoryPreset(tx.category)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enableActions) { onEdit() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) preset.bgDarkColor else preset.bgLightColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(preset.emoji, fontSize = 24.sp)
                }
                Column {
                    Text(
                        text = tx.title,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${formatDate(tx.dateMillis)} • ${tx.category}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = (if (tx.isIncome) "+" else "-") + currencySymbol + DecimalFormat("#,##,###").format(tx.amount.toInt()),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (tx.isIncome) Color(0xFF4CAF50) else Color(0xFFE53935)
                    )
                )
                if (showDeleteButton) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

// History Screen complete with filters
@Composable
fun HistoryScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val isDark by viewModel.isDarkMode
    val currencySymbol = viewModel.currencySymbol.value
    val selectedDate by viewModel.selectedDate

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterIncome by remember { mutableStateOf<Boolean?>(null) } // null = All, true = Income, false = Expense

    val filteredList = remember(transactions, searchQuery, selectedFilterIncome, selectedDate) {
        transactions.filter { tx ->
            val matchesSearch = searchQuery.isBlank() ||
                    tx.title.contains(searchQuery, ignoreCase = true) ||
                    tx.category.contains(searchQuery, ignoreCase = true) ||
                    tx.note.contains(searchQuery, ignoreCase = true) ||
                    tx.amount.toString().contains(searchQuery) ||
                    df.format(tx.amount).contains(searchQuery)
            val matchesType = selectedFilterIncome == null || tx.isIncome == selectedFilterIncome
            val matchesMonth = isSameMonth(tx.dateMillis, selectedDate)
            matchesSearch && matchesType && matchesMonth
        }
    }

    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }
    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Transaction History",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Full search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search transactions...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Month Selector - Moved below search bar
        MonthNavigator(
            selectedDate = selectedDate,
            onMonthChange = { viewModel.changeMonth(it) }
        )

        // Filters pills bar
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = selectedFilterIncome == null,
                onClick = { selectedFilterIncome = null },
                label = { Text("All") }
            )
            FilterChip(
                selected = selectedFilterIncome == false,
                onClick = { selectedFilterIncome = false },
                label = { Text("Expenses") }
            )
            FilterChip(
                selected = selectedFilterIncome == true,
                onClick = { selectedFilterIncome = true },
                label = { Text("Income") }
            )
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Inbox,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    )
                    Text(
                        text = if (transactions.isEmpty()) "Your transaction history is empty" else "No matching transactions found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList) { tx ->
                    TransactionTile(
                        tx = tx,
                        isDark = isDark,
                        onEdit = { transactionToEdit = tx },
                        onDelete = { transactionToDelete = tx },
                        currencySymbol = currencySymbol,
                        showDeleteButton = true
                    )
                }
            }
        }
    }

    if (transactionToDelete != null) {
        val tx = transactionToDelete!!
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Delete Transaction?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This will permanently remove this transaction (${tx.title}). This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTransaction(tx)
                        transactionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { transactionToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (transactionToEdit != null) {
        val tx = transactionToEdit!!
        var showEditFieldsDialog by remember { mutableStateOf(false) }

        if (!showEditFieldsDialog) {
            AlertDialog(
                onDismissRequest = { transactionToEdit = null },
                title = { Text("Edit Transaction?", fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "Do you want to modify this transaction?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showEditFieldsDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Yes, Edit")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { transactionToEdit = null }) {
                        Text("No")
                    }
                }
            )
        } else {
            AddOrEditTransactionDialog(
                tx = tx,
                onDismiss = { transactionToEdit = null },
                onSave = { updated ->
                    viewModel.addTransaction(updated)
                    transactionToEdit = null
                },
                currencySymbol = currencySymbol
            )
        }
    }
}

@Composable
fun AddTransactionDialog(viewModel: MainViewModel, onDismiss: () -> Unit) {
    val currencySymbol = viewModel.currencySymbol.value
    var amount by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Food") }
    var dateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val categories = if (isIncome) IncomeCategoryPresetsAll else ExpenseCategoryPresetsAll

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dateMillis = it }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Add Transaction",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                // Type selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = !isIncome,
                        onClick = {
                            isIncome = false
                            selectedCategory = "Food"
                        },
                        label = { Text("Expense") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = isIncome,
                        onClick = {
                            isIncome = true
                            selectedCategory = "Salary"
                        },
                        label = { Text("Income") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount ($currencySymbol)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Category dropdown
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.emoji} ${cat.name}") },
                                onClick = {
                                    selectedCategory = cat.name
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Date Picker trigger
                OutlinedTextField(
                    value = formatDate(dateMillis),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Date") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null)
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val amountD = amount.toDoubleOrNull() ?: 0.0
                            if (amountD > 0 && title.isNotBlank()) {
                                viewModel.addTransaction(
                                    Transaction(
                                        amount = amountD,
                                        title = title,
                                        note = note,
                                        category = selectedCategory,
                                        isIncome = isIncome,
                                        dateMillis = dateMillis
                                    )
                                )
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun AddOrEditTransactionDialog(
    tx: Transaction,
    onDismiss: () -> Unit,
    onSave: (Transaction) -> Unit,
    currencySymbol: String = "৳"
) {
    var amount by remember { mutableStateOf(tx.amount.toInt().toString()) }
    var title by remember { mutableStateOf(tx.title) }
    var note by remember { mutableStateOf(tx.note) }
    var isIncome by remember { mutableStateOf(tx.isIncome) }
    var selectedCategory by remember { mutableStateOf(tx.category) }
    var dateMillis by remember { mutableStateOf(tx.dateMillis) }
    var showDatePicker by remember { mutableStateOf(false) }

    val categories = if (isIncome) IncomeCategoryPresetsAll else ExpenseCategoryPresetsAll

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dateMillis = it }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Edit Transaction",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = !isIncome,
                        onClick = { isIncome = false },
                        label = { Text("Expense") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = isIncome,
                        onClick = { isIncome = true },
                        label = { Text("Income") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount ($currencySymbol)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.emoji} ${cat.name}") },
                                onClick = {
                                    selectedCategory = cat.name
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Date Picker trigger
                OutlinedTextField(
                    value = formatDate(dateMillis),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Date") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null)
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val amountD = amount.toDoubleOrNull() ?: 0.0
                            if (amountD > 0 && title.isNotBlank()) {
                                onSave(
                                    tx.copy(
                                        amount = amountD,
                                        title = title,
                                        note = note,
                                        category = selectedCategory,
                                        isIncome = isIncome,
                                        dateMillis = dateMillis
                                    )
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save Changes")
                    }
                }
            }
        }
    }
}

// Monthly Budgets limits screen matching SS 6
@Composable
fun BudgetScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val isDark by viewModel.isDarkMode
    val currencySymbol = viewModel.currencySymbol.value
    val selectedDate by viewModel.selectedDate

    var showDialogSettingForCategory by remember { mutableStateOf<String?>(null) }
    var limitInput by remember { mutableStateOf("") }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmCategory by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    if (showAddCategoryDialog) {
        var newCategoryName by remember { mutableStateOf("") }
        var newCategoryEmoji by remember { mutableStateOf("") }
        var isNewCategoryIncome by remember { mutableStateOf(false) }
        var selectedColor by remember { mutableStateOf(Color(0xFF2196F3)) }

        AlertDialog(
            onDismissRequest = { showAddCategoryDialog = false },
            title = {
                Text(
                    text = "Add Custom Category",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Category Name") },
                        placeholder = { Text("e.g. Rent, Subscriptions") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = newCategoryEmoji,
                        onValueChange = { newCategoryEmoji = it },
                        label = { Text("Emoji") },
                        placeholder = { Text("e.g. 🏠, 🎙️ (Default 📁)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Category Type",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { isNewCategoryIncome = false }
                            ) {
                                RadioButton(
                                    selected = !isNewCategoryIncome,
                                    onClick = { isNewCategoryIncome = false }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Expense", style = MaterialTheme.typography.bodyLarge)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { isNewCategoryIncome = true }
                            ) {
                                RadioButton(
                                    selected = isNewCategoryIncome,
                                    onClick = { isNewCategoryIncome = true }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Income", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Choose Accent Color",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        CustomColorPicker(
                            initialColor = selectedColor,
                            onColorChange = { selectedColor = it }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nameInput = newCategoryName.trim()
                        if (nameInput.isNotEmpty()) {
                            val success = viewModel.addCustomCategory(
                                name = nameInput,
                                emoji = newCategoryEmoji.trim().ifEmpty { "📁" },
                                colorValue = selectedColor.toArgb().toLong(),
                                isIncome = isNewCategoryIncome
                            )
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Category added successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showAddCategoryDialog = false
                            } else {
                                Toast.makeText(
                                    context,
                                    "Category already exists!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please enter a category name!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showAddCategoryDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Monthly Budgets",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            TextButton(
                onClick = { showAddCategoryDialog = true },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Categories",
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Add Categories",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        // Month Selector - Moved below header
        MonthNavigator(
            selectedDate = selectedDate,
            onMonthChange = { viewModel.changeMonth(it) }
        )

        Text(
            "Categories & Budgets",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        // Preset Categories budget listing cards matching SS 6
        ExpenseCategoryPresetsAll.forEach { preset ->
            val matchingBudget = budgets.find { it.category.lowercase() == preset.name.lowercase() }
            val limitDouble = matchingBudget?.amountLimit ?: 0.0
            
            var showCardMenu by remember { mutableStateOf(false) }
            var showEditDetailsDialog by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Column: Emoji & Hex
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDark) preset.bgDarkColor else preset.bgLightColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(preset.emoji, fontSize = 24.sp)
                        }
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(preset.color)
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), CircleShape)
                        )
                    }

                    // Middle Column: Budget Name, Label, Amount
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = preset.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                        )
                        Text(
                            text = "Monthly Budget",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        val formattedLimit = if (limitDouble > 0) {
                            DecimalFormat("#,##,###").format(limitDouble.toInt())
                        } else {
                            "0"
                        }
                        Text(
                            text = "${currencySymbol}$formattedLimit",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp
                            )
                        )
                    }

                    // Right Column: Pencil & 3-Dot
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = {
                                showDialogSettingForCategory = preset.name
                                limitInput = if (limitDouble > 0.0) limitDouble.toInt().toString() else ""
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Set Limit",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box {
                            IconButton(
                                onClick = { showCardMenu = true },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Options",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            DropdownMenu(
                                expanded = showCardMenu,
                                onDismissRequest = { showCardMenu = false }
                            ) {
                                val isCustom = CustomExpenseCategoryPresets.any { it.name.lowercase() == preset.name.lowercase() } ||
                                        CustomIncomeCategoryPresets.any { it.name.lowercase() == preset.name.lowercase() }
                                
                                if (isCustom) {
                                    DropdownMenuItem(
                                        text = { Text("Edit Details") },
                                        leadingIcon = { Icon(Icons.Default.Build, null, modifier = Modifier.size(18.dp)) },
                                        onClick = {
                                            showCardMenu = false
                                            showEditDetailsDialog = true
                                        }
                                    )
                                }
                                
                                DropdownMenuItem(
                                    text = { Text("Delete Budget", color = MaterialTheme.colorScheme.error) },
                                    leadingIcon = { Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.error) },
                                    onClick = {
                                        showCardMenu = false
                                        if (isCustom) {
                                            showDeleteConfirmCategory = preset.name
                                        } else {
                                            matchingBudget?.let { viewModel.deleteBudget(it) }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (showEditDetailsDialog) {
                EditCategoryDetailsDialog(
                    preset = preset,
                    isIncome = IncomeCategoryPresetsAll.any { it.name == preset.name },
                    onDismiss = { showEditDetailsDialog = false },
                    viewModel = viewModel
                )
            }
        }

        // Budget overview summary progress container card matching SS 6
        Text(
            "Budget Overview",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        val activeBudgets = remember(budgets) { budgets.filter { it.amountLimit > 0.0 } }
        val budgetSpendMap = remember(transactions, activeBudgets, selectedDate) {
            activeBudgets.associateWith { budget ->
                transactions.filter {
                    !it.isIncome &&
                            it.category.lowercase() == budget.category.lowercase() &&
                            isSameMonth(it.dateMillis, selectedDate)
                }.sumOf { it.amount }
            }
        }

        if (activeBudgets.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
            ) {
                Box(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No active budgets. Click 'Set' on a category to start tracking.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            activeBudgets.forEach { budget ->
                val spentCat = budgetSpendMap[budget] ?: 0.0
                val ratio =
                    if (budget.amountLimit > 0.0) (spentCat / budget.amountLimit).toFloat() else 0f
                val clampedRatio = ratio.coerceIn(0f, 1f)
                val preset = getCategoryPreset(budget.category)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(preset.emoji, fontSize = 16.sp)
                                Text(
                                    text = budget.category,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Text(
                                text = "${currencySymbol}${DecimalFormat("#,##,###").format(spentCat.toInt())} / ${currencySymbol}${DecimalFormat("#,##,###").format(budget.amountLimit.toInt())}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = if (ratio > 1f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        LinearProgressIndicator(
                            progress = { clampedRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = if (ratio > (viewModel.alertThreshold.value / 100f)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                        )

                        Text(
                            text = if (ratio > 1f) "⚠️ Exceeded budget by ${currencySymbol}${(spentCat - budget.amountLimit).toInt()}!"
                            else "${(ratio * 100).toInt()}% used of monthly target",
                            fontSize = 11.sp,
                            color = if (ratio > 1f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (ratio > 1f) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Smart Savings Banner matching SS 6 perfectly
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121C2B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Smart Saving",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Users who set monthly budgets save up to 20% more on average. Start small and adjust as you go!",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    // Modal budget setter Dialog
    if (showDialogSettingForCategory != null) {
        val categoryName = showDialogSettingForCategory!!
        val matchingB = budgets.find { it.category.lowercase() == categoryName.lowercase() }

        AlertDialog(
            onDismissRequest = { showDialogSettingForCategory = null },
            title = { Text("Set monthly limit for $categoryName", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = limitInput,
                    onValueChange = { limitInput = it },
                    placeholder = { Text("0") },
                    label = { Text("Amount limit ($currencySymbol)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val limitD = limitInput.toDoubleOrNull()
                        if (limitD != null && limitD >= 0.0) {
                            if (matchingB != null) {
                                if (limitD == 0.0) {
                                    viewModel.deleteBudget(matchingB)
                                } else {
                                    viewModel.addBudget(matchingB.copy(amountLimit = limitD))
                                }
                            } else {
                                if (limitD > 0.0) {
                                    viewModel.addBudget(
                                        Budget(
                                            category = categoryName,
                                            amountLimit = limitD
                                        )
                                    )
                                }
                            }
                            showDialogSettingForCategory = null
                            Toast.makeText(
                                context,
                                "Budget target updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Invalid amount limit entered",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Save Limit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogSettingForCategory = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteConfirmCategory != null) {
        val categoryName = showDeleteConfirmCategory!!
        AlertDialog(
            onDismissRequest = { showDeleteConfirmCategory = null },
            title = { Text("Delete Custom Category", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete the custom category '$categoryName' and its budget limit?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteCustomCategory(categoryName)
                        showDeleteConfirmCategory = null
                        Toast.makeText(
                            context,
                            "Deleted custom category: $categoryName",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmCategory = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Settings Screen complete with backup capabilities
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    val isDark by viewModel.isDarkMode
    val remindersOn by viewModel.isRemindersEnabled
    val thresholdVal by viewModel.alertThreshold

    var showResetDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var jsonPasteContent by remember { mutableStateOf("") }

    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Drag handle decoration
        Box(
            modifier = Modifier
                .size(width = 32.dp, height = 4.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f), CircleShape)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Tools & Settings",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 3x3 Grid Menu
        val items = listOf(
            Triple("Calculator", Icons.Default.Calculate, { viewModel.showCalculator.value = true }),
            Triple("Notepad", Icons.AutoMirrored.Filled.Notes, { Toast.makeText(context, "Notepad coming soon!", Toast.LENGTH_SHORT).show() }),
            Triple("Converter", Icons.Default.CurrencyExchange, { viewModel.showConverter.value = true }),
            Triple("Reports", Icons.Default.BarChart, { Toast.makeText(context, "Reports coming soon!", Toast.LENGTH_SHORT).show() }),
            Triple("Backup", Icons.Default.Backup, { showExportDialog = true }),
            Triple("Goals", Icons.Default.Flag, { Toast.makeText(context, "Goals coming soon!", Toast.LENGTH_SHORT).show() }),
            Triple("Settings", Icons.Default.Settings, { showThemeDialog = true }),
            Triple("More Tools", Icons.Default.Build, { Toast.makeText(context, "More tools coming soon!", Toast.LENGTH_SHORT).show() }),
            Triple("About", Icons.Default.Info, { showAboutDialog = true })
        )

        // Using a simple Column + Rows instead of LazyVerticalGrid to avoid nested scrolling issues in the overlay
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { (label, icon, onClick) ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable { onClick() },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                    // Add empty boxes if row is not full
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Elegant footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "BudgetPecker v1.0.1",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f)
            )
            Text(
                "Built By FAHMID HASAN",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.9f)
            )
        }
    }

    // Theme & Preferences Dialog
    if (showThemeDialog) {
        Dialog(onDismissRequest = { showThemeDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Settings & Preferences", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    
                    // Dark Mode Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Dark Mode", fontWeight = FontWeight.SemiBold)
                        Switch(checked = isDark, onCheckedChange = { viewModel.setDarkMode(it) })
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Currency Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Currency Symbol", fontWeight = FontWeight.SemiBold)
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            TextButton(onClick = { expanded = true }) {
                                Text(viewModel.currencySymbol.value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                listOf("৳", "$", "€", "£", "₹", "¥").forEach { symbol ->
                                    DropdownMenuItem(text = { Text(symbol) }, onClick = {
                                        viewModel.setCurrencySymbol(symbol)
                                        expanded = false
                                    })
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Reset Data Button
                    Button(
                        onClick = { showResetDialog = true; showThemeDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Reset All Data")
                    }

                    TextButton(onClick = { showThemeDialog = false }, modifier = Modifier.align(Alignment.End)) {
                        Text("Close")
                    }
                }
            }
        }
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About BudgetPecker", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("BudgetPecker is your private expense companion designed to help you track your spending and stay within budget.")
                    Text("Version: 1.0.1", fontWeight = FontWeight.Bold)
                    Text("Developer: FAHMID HASAN", fontWeight = FontWeight.Bold)
                    Text("Built with Kotlin & Jetpack Compose", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Reset confirmations modal
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Data?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This will permanently delete all your transactions and settings. This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllData()
                        showResetDialog = false
                        Toast.makeText(context, "All local app data cleared!", Toast.LENGTH_SHORT)
                            .show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // JSON Export dialog
    if (showExportDialog) {
        val backupString = remember { viewModel.exportBackupToJson() }
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Data & Backup", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Export your data as JSON for backup or import a previous backup.")
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                clipboard.setText(AnnotatedString(backupString))
                                Toast.makeText(context, "Backup copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2A38))
                        ) {
                            Text("Copy JSON", fontSize = 11.sp)
                        }
                        
                        OutlinedButton(
                            onClick = { showImportDialog = true; showExportDialog = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Import JSON", fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // JSON Import Backup recovery dialog
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("Import Backup", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Paste your backup JSON code below. This will replace all current data!")
                    OutlinedTextField(
                        value = jsonPasteContent,
                        onValueChange = { jsonPasteContent = it },
                        placeholder = { Text("Paste JSON here...") },
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        maxLines = 10
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (jsonPasteContent.isNotBlank()) {
                            val success = viewModel.importBackupFromJson(jsonPasteContent)
                            if (success) {
                                Toast.makeText(context, "Import successful!", Toast.LENGTH_SHORT).show()
                                showImportDialog = false
                                jsonPasteContent = ""
                            } else {
                                Toast.makeText(context, "Invalid format!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2A38))
                ) {
                    Text("Restore")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EditCategoryDetailsDialog(
    preset: CategoryPreset,
    isIncome: Boolean,
    onDismiss: () -> Unit,
    viewModel: MainViewModel
) {
    var name by remember { mutableStateOf(preset.name) }
    var emoji by remember { mutableStateOf(preset.emoji) }
    var selectedColor by remember { mutableStateOf(preset.color) }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Category Details", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("Emoji") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Text("Accent Color", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                
                CustomColorPicker(
                    initialColor = selectedColor,
                    onColorChange = { selectedColor = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val success = viewModel.updateCustomCategory(
                            oldName = preset.name,
                            newName = name.trim(),
                            newEmoji = emoji.trim().ifEmpty { "📁" },
                            newColor = selectedColor,
                            isIncome = isIncome
                        )
                        if (success) {
                            Toast.makeText(context, "Category updated!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        } else {
                            Toast.makeText(context, "Error updating category (maybe duplicate name)", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CustomColorPicker(
    initialColor: Color,
    onColorChange: (Color) -> Unit
) {
    val hsv = remember(initialColor) {
        val hsvArray = FloatArray(3)
        android.graphics.Color.colorToHSV(initialColor.toArgb(), hsvArray)
        mutableStateOf(hsvArray)
    }

    val hue = hsv.value[0]
    val saturation = hsv.value[1]
    val brightness = hsv.value[2]

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.hsv(hue, saturation, brightness))
                .align(Alignment.CenterHorizontally)
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
        )

        // Hue Slider
        Text("Hue", style = MaterialTheme.typography.labelSmall)
        val hueBrush = Brush.linearGradient(
            colors = listOf(
                Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red
            )
        )
        GradientSlider(
            value = hue,
            onValueChange = { h ->
                hsv.value = floatArrayOf(h, saturation, brightness)
                onColorChange(Color.hsv(h, saturation, brightness))
            },
            valueRange = 0f..360f,
            brush = hueBrush
        )

        // Saturation Slider
        Text("Saturation", style = MaterialTheme.typography.labelSmall)
        val saturationBrush = Brush.linearGradient(
            colors = listOf(
                Color.White,
                Color.hsv(hue, 1f, brightness)
            )
        )
        GradientSlider(
            value = saturation,
            onValueChange = { s ->
                hsv.value = floatArrayOf(hue, s, brightness)
                onColorChange(Color.hsv(hue, s, brightness))
            },
            valueRange = 0f..1f,
            brush = saturationBrush
        )

        // Brightness Slider
        Text("Brightness", style = MaterialTheme.typography.labelSmall)
        val brightnessBrush = Brush.linearGradient(
            colors = listOf(
                Color.Black,
                Color.hsv(hue, saturation, 1f)
            )
        )
        GradientSlider(
            value = brightness,
            onValueChange = { v ->
                hsv.value = floatArrayOf(hue, saturation, v)
                onColorChange(Color.hsv(hue, saturation, v))
            },
            valueRange = 0f..1f,
            brush = brightnessBrush
        )
    }
}

@Composable
fun CalculatorContent(
    viewModel: MainViewModel,
    isFullscreen: Boolean,
    onToggleFullscreen: () -> Unit,
    onDismiss: () -> Unit
) {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var showHistory by remember { mutableStateOf(false) }

    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val onAction: (String) -> Unit = { action ->
        when (action) {
            "AC" -> { expression = ""; result = "" }
            "⌫" -> { if (expression.isNotEmpty()) expression = expression.dropLast(1) }
            "00" -> { if (expression.isNotEmpty() && expression.last().isDigit()) expression += "00" }
            "=" -> {
                if (expression.isNotBlank()) {
                    val evaluated = evaluateExpression(expression)
                    result = evaluated
                    viewModel.addToCalculatorHistory("$expression = $evaluated")
                }
            }
            "copy" -> {
                if (result.isNotBlank()) {
                    clipboard.setText(AnnotatedString(result))
                    Toast.makeText(context, "Result copied!", Toast.LENGTH_SHORT).show()
                }
            }
            else -> expression += action
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .then(if (isFullscreen) Modifier.statusBarsPadding().navigationBarsPadding() else Modifier)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showHistory = !showHistory }) {
                Icon(Icons.Default.History, contentDescription = "History", tint = if (showHistory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
            }
            Text(
                "Calculator",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Row {
                IconButton(onClick = onToggleFullscreen) {
                    Icon(
                        if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        contentDescription = "Toggle Fullscreen",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (showHistory) {
                // History View
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("History", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        TextButton(onClick = { viewModel.clearCalculatorHistory() }) { Text("Clear", color = MaterialTheme.colorScheme.error) }
                    }
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(viewModel.calculatorHistory) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    val parts = item.split(" = ")
                                    if (parts.size > 1) expression = parts[1]
                                    showHistory = false
                                },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Text(item, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            } else {
                // Calculator Main View
                Column(modifier = Modifier.fillMaxSize()) {
                    // Display
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(if (isFullscreen) 0.3f else 0.25f)
                            .padding(horizontal = 24.dp, vertical = if (isFullscreen) 24.dp else 12.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = expression.ifEmpty { "0" },
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = MaterialTheme.typography.headlineSmall.fontFamily
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = result.ifEmpty { "" },
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MaterialTheme.typography.headlineLarge.fontFamily
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.End
                            )
                            if (result.isNotEmpty()) {
                                IconButton(onClick = { onAction("copy") }) {
                                    Icon(
                                        Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    // Keypad
                    val buttons = listOf(
                        listOf("AC", "%", "⌫", "÷"),
                        listOf("7", "8", "9", "×"),
                        listOf("4", "5", "6", "-"),
                        listOf("1", "2", "3", "+"),
                        listOf("00", "0", ".", "=")
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        buttons.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { label ->
                                    if (label.isNotBlank()) {
                                        CalculatorButton(
                                            label = label,
                                            onClick = { onAction(label) },
                                            modifier = Modifier.weight(1f).fillMaxHeight(),
                                            isOperation = "+-×÷=%AC⌫".contains(label)
                                        )
                                    } else {
                                        Spacer(Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOperation: Boolean = false
) {
    val containerColor = if (label == "=") {
        MaterialTheme.colorScheme.primary
    } else if (label == "AC" || label == "⌫") {
        MaterialTheme.colorScheme.errorContainer
    } else if (isOperation) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (label == "=") {
        MaterialTheme.colorScheme.onPrimary
    } else if (label == "AC" || label == "⌫") {
        MaterialTheme.colorScheme.onErrorContainer
    } else if (isOperation) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val isOperator = "+-×÷=%AC⌫".contains(label)
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    fontFamily = if (isOperator) FontFamily.Default else MaterialTheme.typography.titleLarge.fontFamily
                ),
                color = contentColor
            )
        }
    }
}

private fun evaluateExpression(expr: String): String {
    return try {
        // Simple manual parsing for basic math
        val cleanExpr = expr.replace("%", "/100.0")
            .replace("×", "*")
            .replace("÷", "/")
        
        // This is a very basic evaluator for a demo app. 
        // For production, consider using an exp evaluator library or a proper shunting-yard impl.
        // We'll use a script engine approach if possible, or a simpler fallback.
        val res = object : Any() {
            fun eval(str: String): Double {
                return object : Any() {
                    var pos = -1
                    var ch = 0
                    fun nextChar() {
                        ch = if (++pos < str.length) str[pos].toInt() else -1
                    }
                    fun eat(charToEat: Int): Boolean {
                        while (ch == ' '.toInt()) nextChar()
                        if (ch == charToEat) {
                            nextChar()
                            return true
                        }
                        return false
                    }
                    fun parse(): Double {
                        nextChar()
                        val x = parseExpression()
                        if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
                        return x
                    }
                    fun parseExpression(): Double {
                        var x = parseTerm()
                        while (true) {
                            if (eat('+'.toInt())) x += parseTerm()
                            else if (eat('-'.toInt())) x -= parseTerm()
                            else return x
                        }
                    }
                    fun parseTerm(): Double {
                        var x = parseFactor()
                        while (true) {
                            if (eat('*'.toInt())) x *= parseFactor()
                            else if (eat('/'.toInt())) x /= parseFactor()
                            else return x
                        }
                    }
                    fun parseFactor(): Double {
                        if (eat('+'.toInt())) return parseFactor()
                        if (eat('-'.toInt())) return -parseFactor()
                        var x: Double
                        val startPos = pos
                        if (eat('('.toInt())) {
                            x = parseExpression()
                            eat(')'.toInt())
                        } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) {
                            while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                            x = java.lang.Double.parseDouble(str.substring(startPos, pos))
                        } else {
                            throw RuntimeException("Unexpected: " + ch.toChar())
                        }
                        return x
                    }
                }.parse()
            }
        }.eval(cleanExpr)
        
        if (res % 1.0 == 0.0) res.toInt().toString() else DecimalFormat("#.####").format(res)
    } catch (e: Exception) {
        "Error"
    }
}

@Composable
fun CurrencyConverterContent(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf("1") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("BDT") }

    // Re-fetch rates if not live yet
    LaunchedEffect(Unit) {
        if (!viewModel.isLiveRates.value) {
            viewModel.fetchLiveRates()
        }
    }

    val rates = viewModel.exchangeRates
    val result = remember(amount, fromCurrency, toCurrency, rates.size) {
        val amountD = amount.toDoubleOrNull() ?: 0.0
        val fromRate = rates[fromCurrency] ?: 1.0
        val toRate = rates[toCurrency] ?: 1.0
        // Cross conversion: (Amount / FromRate) * ToRate
        (amountD / fromRate) * toRate
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Currency Converter",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (viewModel.isLiveRates.value) Color(0xFF4CAF50) else Color.Gray)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (viewModel.isLiveRates.value) "Live Rates" else "Offline Rates",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Text(getCurrencySymbol(fromCurrency), fontWeight = FontWeight.Bold) }
        )

        // Selectors Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CurrencyPicker(
                label = "From",
                selected = fromCurrency,
                onSelected = { fromCurrency = it },
                rates = rates,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    val temp = fromCurrency
                    fromCurrency = toCurrency
                    toCurrency = temp
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
            ) {
                Icon(Icons.Default.CurrencyExchange, contentDescription = "Swap", modifier = Modifier.size(20.dp))
            }

            CurrencyPicker(
                label = "To",
                selected = toCurrency,
                onSelected = { toCurrency = it },
                rates = rates,
                modifier = Modifier.weight(1f)
            )
        }

        // Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${amount.ifEmpty { "0" }} $fromCurrency =",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${DecimalFormat("#,##,###.##").format(result)} $toCurrency",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (viewModel.isLiveRates.value) "Live rates updated" else "Using offline fallback",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }

        // Quick Amount Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val quickAmounts = listOf("10", "50", "100", "500")
            quickAmounts.forEach { qAmount ->
                OutlinedButton(
                    onClick = { amount = qAmount },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("${getCurrencySymbol(fromCurrency)}$qAmount", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        if (viewModel.isLiveRates.value) {
            Text(
                "Last Updated: ${viewModel.lastRatesUpdate.value}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CurrencyPicker(
    label: String,
    selected: String,
    onSelected: (String) -> Unit,
    rates: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val commonCurrencies = listOf("USD", "BDT", "EUR", "GBP", "INR", "SAR", "AED", "CAD", "AUD", "JPY", "CNY", "SGD")
    
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(selected, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                commonCurrencies.forEach { code ->
                    DropdownMenuItem(
                        text = { Text(code) },
                        onClick = {
                            onSelected(code)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

fun getCurrencySymbol(code: String): String {
    return when (code) {
        "USD" -> "$"
        "BDT" -> "৳"
        "EUR" -> "€"
        "GBP" -> "£"
        "INR" -> "₹"
        "SAR" -> "﷼"
        "JPY" -> "¥"
        "CNY" -> "¥"
        else -> code
    }
}
@Composable
fun GradientSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    brush: Brush
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        contentAlignment = Alignment.Center
    ) {
        // Custom Track with Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(brush)
        )
        // Slider on top with transparent track
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            )
        )
    }
}
