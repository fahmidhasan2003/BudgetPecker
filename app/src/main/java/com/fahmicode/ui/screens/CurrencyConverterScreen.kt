package com.fahmicode.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fahmicode.ui.MainViewModel
import java.text.DecimalFormat

@Composable
fun CurrencyConverterScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
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
        (amountD / fromRate) * toRate
    }

    val onAction: (String) -> Unit = { action ->
        when (action) {
            "C" -> amount = "0"
            "⌫" -> {
                amount = if (amount.length <= 1) "0" else amount.dropLast(1)
            }
            "00" -> {
                if (amount != "0") amount += "00"
            }
            "." -> {
                if (!amount.contains(".")) amount += "."
            }
            else -> {
                if (amount == "0") amount = action else amount += action
            }
        }
    }

    val commonCurrencies = listOf("USD", "BDT", "EUR", "GBP", "INR", "SAR", "AED", "CAD", "AUD", "JPY", "CNY", "SGD")
    val getFullDisplay = { code: String ->
        val name = when (code) {
            "USD" -> "US Dollar"
            "BDT" -> "Bangladeshi Taka"
            "EUR" -> "Euro"
            "GBP" -> "British Pound"
            "INR" -> "Indian Rupee"
            "SAR" -> "Saudi Riyal"
            "AED" -> "UAE Dirham"
            "CAD" -> "Canadian Dollar"
            "AUD" -> "Australian Dollar"
            "JPY" -> "Japanese Yen"
            "CNY" -> "Chinese Yuan"
            "SGD" -> "Singapore Dollar"
            else -> ""
        }
        val symbol = getCurrencySymbol(code)
        "$code - $name ($symbol)"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                "Currency Converter",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(48.dp)) // To balance the back button
        }

        // Display Section (Top Half)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Row 1 (From)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Row(
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            fromCurrency,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(Icons.Default.ArrowDropDown, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        commonCurrencies.forEach { code ->
                            DropdownMenuItem(
                                text = { Text(getFullDisplay(code)) },
                                onClick = { fromCurrency = code; expanded = false }
                            )
                        }
                    }
                }
                Text(
                    text = "${getCurrencySymbol(fromCurrency)} $amount",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Row 2 (To)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Row(
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            toCurrency,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(Icons.Default.ArrowDropDown, null, tint = MaterialTheme.colorScheme.primary)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        commonCurrencies.forEach { code ->
                            DropdownMenuItem(
                                text = { Text(getFullDisplay(code)) },
                                onClick = { toCurrency = code; expanded = false }
                            )
                        }
                    }
                }
                Text(
                    text = "${DecimalFormat("#,##,###.##").format(result)} ${getCurrencySymbol(toCurrency)}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }

            // Rate Summary Subtext
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            if (viewModel.isLiveRates.value) Color(0xFF4CAF50) else Color.Gray
                        )
                )
                Spacer(Modifier.width(6.dp))
                val rate = (rates[toCurrency] ?: 1.0) / (rates[fromCurrency] ?: 1.0)
                Text(
                    text = "1 $fromCurrency = ${DecimalFormat("#.###").format(rate)} $toCurrency • ${if (viewModel.isLiveRates.value) "Live Rates" else "Offline"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }

        // Keypad Section (Bottom Half)
        val buttons = listOf(
            listOf("7", "8", "9", "C"),
            listOf("4", "5", "6", "⌫"),
            listOf("1", "2", "3", "."),
            listOf("00", "0", "", "")
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { label ->
                        if (label.isNotBlank()) {
                            CalculatorButton(
                                label = label,
                                onClick = { onAction(label) },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                isOperation = "C⌫.".contains(label)
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
