package com.fahmicode.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
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
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expression by remember { mutableStateOf("") }
    var liveResult by remember { mutableStateOf("") }
    var showHistory by remember { mutableStateOf(false) }

    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val expressionScrollState = rememberScrollState()
    val resultScrollState = rememberScrollState()

    // Auto-scroll to end when expression or liveResult changes
    LaunchedEffect(expression) {
        expressionScrollState.animateScrollTo(expressionScrollState.maxValue)
    }
    LaunchedEffect(liveResult) {
        resultScrollState.animateScrollTo(resultScrollState.maxValue)
    }

    // Helper to check if the last number already has a decimal point
    fun canAddDecimal(expr: String): Boolean {
        if (expr.isEmpty()) return true
        val lastNumber = expr.split("+", "-", "×", "÷", "%").last()
        return !lastNumber.contains(".")
    }

    val onAction: (String) -> Unit = { action ->
        when (action) {
            "AC" -> {
                expression = ""
                liveResult = ""
            }
            "⌫" -> {
                if (expression.isNotEmpty()) {
                    expression = expression.dropLast(1)
                    liveResult = if (expression.isNotEmpty()) evaluateExpression(expression) else ""
                }
            }
            "00" -> {
                if (expression.isNotEmpty() && expression.last().isDigit()) {
                    expression += "00"
                    liveResult = evaluateExpression(expression)
                }
            }
            "." -> {
                if (canAddDecimal(expression)) {
                    expression += if (expression.isEmpty() || !expression.last().isDigit()) "0." else "."
                }
            }
            "=" -> {
                if (expression.isNotBlank()) {
                    val evaluated = evaluateExpression(expression)
                    if (evaluated != "Error" && evaluated.isNotEmpty()) {
                        viewModel.addToCalculatorHistory("$expression = $evaluated")
                        expression = evaluated
                        liveResult = ""
                    }
                }
            }
            "copy" -> {
                val toCopy = if (liveResult.isNotEmpty()) liveResult else expression
                if (toCopy.isNotBlank() && toCopy != "Error") {
                    clipboard.setText(AnnotatedString(toCopy))
                    Toast.makeText(context, "Result copied!", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // Prevent starting with operator except minus
                if (expression.isEmpty() && "+×÷%".contains(action)) {
                    // Do nothing
                } else {
                    // Prevent multiple operators in a row
                    if (expression.isNotEmpty() && "+-×÷%.".contains(expression.last().toString()) && "+-×÷%.".contains(action)) {
                        expression = expression.dropLast(1) + action
                    } else {
                        expression += action
                    }
                    liveResult = evaluateExpression(expression)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header - Improved layout for back button and title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Calculator",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        fontSize = 28.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "Quick Utility Tool",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
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
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { showHistory = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close History")
                        }
                        Text(
                            "Recent History",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (viewModel.calculatorHistory.isNotEmpty()) {
                            TextButton(onClick = { viewModel.clearCalculatorHistory() }) {
                                Text("Clear All", color = MaterialTheme.colorScheme.error)
                            }
                        } else {
                            Spacer(Modifier.width(48.dp))
                        }
                    }
                    
                    if (viewModel.calculatorHistory.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.padding(32.dp).shadow(4.dp, RoundedCornerShape(16.dp))
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.History,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    Text(
                                        "No History Yet",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            items(viewModel.calculatorHistory) { item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val parts = item.split(" = ")
                                            if (parts.size > 1) {
                                                expression = parts[1]
                                                liveResult = ""
                                            }
                                            showHistory = false
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        item,
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = FontFamily.SansSerif
                                    )
                                }
                            }
                            item {
                                Text(
                                    "Tap on any history item to recall",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp, bottom = 8.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            } else {
                // Calculator Main View
                Column(modifier = Modifier.fillMaxSize()) {
                    // Display Result Card - Expanded height and styled
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp) // Even taller for better readability
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .shadow(16.dp, RoundedCornerShape(28.dp)),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Action Icons inside Card - better padding
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = { showHistory = true },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(
                                        Icons.Default.History,
                                        contentDescription = "History",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                if (expression.isNotEmpty()) {
                                    IconButton(
                                        onClick = { onAction("copy") },
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp, vertical = 32.dp),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                // Scrollable Expression showing END
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(expressionScrollState),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = expression.ifEmpty { "0" },
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                        maxLines = 1,
                                        fontFamily = FontFamily.SansSerif,
                                        textAlign = TextAlign.End
                                    )
                                }
                                
                                Spacer(Modifier.height(20.dp))
                                
                                // Auto-scaling font size for result
                                val resultFontSize = remember(liveResult) {
                                    when {
                                        liveResult.length <= 10 -> 54.sp
                                        liveResult.length <= 14 -> 42.sp
                                        liveResult.length <= 18 -> 32.sp
                                        else -> 24.sp
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(resultScrollState),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = liveResult.ifEmpty { "" },
                                        style = MaterialTheme.typography.displayLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = resultFontSize
                                        ),
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.End,
                                        maxLines = 1,
                                        fontFamily = FontFamily.SansSerif
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(0.1f))

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
                            .weight(1.5f)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                            )
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        buttons.forEach { row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                row.forEach { label ->
                                    if (label.isNotBlank()) {
                                        CalculatorButton(
                                            label = label,
                                            onClick = { onAction(label) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight(),
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
    if (expr.isBlank()) return ""
    return try {
        // Clean expression: remove trailing operators, but keep % for live calculation
        var cleaned = expr.trim()
        while (cleaned.isNotEmpty() && "+-×÷.".contains(cleaned.last().toString())) {
            cleaned = cleaned.dropLast(1)
        }
        if (cleaned.isEmpty()) return ""

        val finalExpr = cleaned.replace("×", "*")
            .replace("÷", "/")
        
        val res = object : Any() {
            fun eval(str: String): Double {
                return object : Any() {
                    var pos = -1
                    var ch = 0
                    var lastWasPercent = false

                    fun nextChar() {
                        ch = if (++pos < str.length) str[pos].code else -1
                    }
                    fun eat(charToEat: Int): Boolean {
                        while (ch == ' '.code) nextChar()
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
                            if (eat('+'.code)) {
                                val y = parseTerm()
                                if (lastWasPercent) x += x * y else x += y
                                lastWasPercent = false
                            } else if (eat('-'.code)) {
                                val y = parseTerm()
                                if (lastWasPercent) x -= x * y else x -= y
                                lastWasPercent = false
                            } else return x
                        }
                    }
                    fun parseTerm(): Double {
                        var x = parseFactor()
                        while (true) {
                            if (eat('*'.code)) {
                                x *= parseFactor()
                                lastWasPercent = false
                            } else if (eat('/'.code)) {
                                x /= parseFactor()
                                lastWasPercent = false
                            } else return x
                        }
                    }
                    fun parseFactor(): Double {
                        if (eat('+'.code)) return parseFactor()
                        if (eat('-'.code)) return -parseFactor()
                        var x: Double
                        val startPos = pos
                        if (eat('('.code)) {
                            x = parseExpression()
                            eat(')'.code)
                            lastWasPercent = false
                        } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) {
                            while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                            x = java.lang.Double.parseDouble(str.substring(startPos, pos))
                            lastWasPercent = false
                        } else {
                            throw RuntimeException("Unexpected: " + ch.toChar())
                        }

                        if (eat('%'.code)) {
                            x /= 100.0
                            lastWasPercent = true
                        }
                        return x
                    }
                }.parse()
            }
        }.eval(finalExpr)
        
        if (res.isInfinite() || res.isNaN()) return "Error"
        
        // Use scientific notation for extreme values (above 1 quadrillion or very tiny)
        if (Math.abs(res) >= 1e15 || (Math.abs(res) < 1e-9 && res != 0.0)) {
            return java.text.DecimalFormat("0.########E0").format(res).lowercase().replace("e", "e+")
        }
        
        val df = DecimalFormat("#.##########")
        if (res % 1.0 == 0.0) {
            // For large integers, ensure they don't get scientific notation from Double.toString()
            return res.toLong().toString()
        } else {
            return df.format(res)
        }
    } catch (e: Exception) {
        "" // Silent error for live calculation
    }
}
