package com.fahmicode.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahmicode.ui.MainViewModel
import java.text.DecimalFormat

@Composable
fun CalculatorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
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
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
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
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            // Empty box to balance the header
            Box(modifier = Modifier.size(48.dp))
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
                            .weight(0.3f)
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = expression.ifEmpty { "0" },
                            style = MaterialTheme.typography.headlineSmall,
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
                                    fontWeight = FontWeight.Bold
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
