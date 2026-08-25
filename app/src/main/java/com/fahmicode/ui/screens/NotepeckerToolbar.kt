package com.fahmicode.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahmicode.ui.theme.AccentColor

@Composable
fun SingleLineFormattingToolbar(
    backgroundColor: Color,
    contentColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .height(52.dp),
        color = backgroundColor,
        tonalElevation = 2.dp
    ) {
        Column {
            HorizontalDivider(color = contentColor.copy(alpha = 0.1f))
            LazyRow(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item { AaTool(contentColor) }
                item { VerticalDivider(contentColor) }
                item { BoldTool(contentColor) }
                item { VerticalDivider(contentColor) }
                item { UnderlineTool(contentColor) }
                item { VerticalDivider(contentColor) }
                item { ItalicTool(contentColor) }
                item { VerticalDivider(contentColor) }
                item { LinkTool(contentColor) }
                item { VerticalDivider(contentColor) }
                item { ColorTool(contentColor) }
                item { VerticalDivider(contentColor) }
                item { BulletTool(contentColor) }
                item { VerticalDivider(contentColor) }
                item { NumberingTool(contentColor) }
                item { VerticalDivider(contentColor) }
                item { TableTool(contentColor) }
                item { VerticalDivider(contentColor) }
                item { MoreTool(contentColor) }
            }
        }
    }
}

@Composable
fun VerticalDivider(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .background(color.copy(alpha = 0.15f))
    )
}

@Composable
fun FormattingToolButton(
    icon: ImageVector? = null,
    text: String? = null,
    isSelected: Boolean = false,
    contentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) AccentColor.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) AccentColor else contentColor,
                modifier = Modifier.size(24.dp)
            )
        } else if (text != null) {
            Text(
                text = text,
                color = if (isSelected) AccentColor else contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun AaTool(contentColor: Color) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        FormattingToolButton(text = "Aa", contentColor = contentColor) { expanded = true }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(220.dp)
        ) {
            Text(
                "Text Style",
                modifier = Modifier.padding(16.dp, 8.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            // Font Size Selector UI
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("S", "N", "L", "T", "ST").forEach { size ->
                    TextButton(onClick = { expanded = false }, modifier = Modifier.weight(1f)) {
                        Text(size, fontSize = 12.sp)
                    }
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            
            DropdownMenuItem(
                text = { Text("Small") },
                onClick = { expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Normal") },
                onClick = { expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Large") },
                onClick = { expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Title") },
                onClick = { expanded = false }
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            
            DropdownMenuItem(
                text = { Text("Superscript") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.Superscript, null) }
            )
            DropdownMenuItem(
                text = { Text("Subscript") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.Subscript, null) }
            )
            DropdownMenuItem(
                text = { Text("Strikethrough") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.StrikethroughS, null) }
            )
            DropdownMenuItem(
                text = { Text("Clear Formatting") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.FormatClear, null) }
            )
        }
    }
}

@Composable
fun BoldTool(contentColor: Color) {
    var isSelected by remember { mutableStateOf(false) }
    FormattingToolButton(text = "B", isSelected = isSelected, contentColor = contentColor) { isSelected = !isSelected }
}

@Composable
fun UnderlineTool(contentColor: Color) {
    var isSelected by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) AccentColor.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { isSelected = !isSelected },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "U",
            color = if (isSelected) AccentColor else contentColor,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
        )
    }
}

@Composable
fun ItalicTool(contentColor: Color) {
    var isSelected by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) AccentColor.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { isSelected = !isSelected },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "I",
            color = if (isSelected) AccentColor else contentColor,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
    }
}

@Composable
fun LinkTool(contentColor: Color) {
    var expanded by remember { mutableStateOf(false) }
    var showLinkDialog by remember { mutableStateOf(false) }
    
    Box {
        FormattingToolButton(icon = Icons.Default.AttachFile, contentColor = contentColor) { expanded = true }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Web Link") },
                onClick = { expanded = false; showLinkDialog = true },
                leadingIcon = { Icon(Icons.Default.Public, null) }
            )
            DropdownMenuItem(
                text = { Text("Cross Reference") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.Shortcut, null) }
            )
            DropdownMenuItem(
                text = { Text("Telephone") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.Phone, null) }
            )
            DropdownMenuItem(
                text = { Text("Checkbox") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.CheckBox, null) }
            )
            DropdownMenuItem(
                text = { Text("Media") },
                onClick = { expanded = false },
                leadingIcon = { Icon(Icons.Default.Image, null) }
            )
        }
    }
    
    if (showLinkDialog) {
        AlertDialog(
            onDismissRequest = { showLinkDialog = false },
            title = { Text("Insert Link") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = "", onValueChange = {}, label = { Text("Text") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = "", onValueChange = {}, label = { Text("URL") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = { TextButton(onClick = { showLinkDialog = false }) { Text("Insert") } },
            dismissButton = { TextButton(onClick = { showLinkDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun ColorTool(contentColor: Color) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        FormattingToolButton(icon = Icons.Default.Palette, contentColor = contentColor) { expanded = true }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.width(280.dp)) {
            var selectedTab by remember { mutableIntStateOf(0) }
            
            // Simplified Tab Header to avoid intrinsic measurement crash
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                listOf("Font", "Highlight").forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = index }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (selectedTab == index) AccentColor else contentColor.copy(alpha = 0.6f),
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                        if (selectedTab == index) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(AccentColor)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val colors = listOf(Color.Black, Color.DarkGray, Color.Red, Color.Magenta, Color.Yellow, Color.Green, Color.Blue, Color.Cyan)
            
            // Using Column + Row instead of FlowRow to avoid intrinsic measurement crash in DropdownMenu
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                colors.chunked(4).forEach { rowColors ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        rowColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
                                    .clickable { expanded = false }
                            )
                        }
                    }
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.dp, contentColor.copy(alpha = 0.3f), CircleShape)
                            .clickable { expanded = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp), tint = contentColor)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BulletTool(contentColor: Color) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        FormattingToolButton(text = "•••", contentColor = contentColor) { expanded = true }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Text("Bullet Style", modifier = Modifier.padding(16.dp, 8.dp), style = MaterialTheme.typography.labelMedium)
            val styles = listOf("•", "■", "★", "→", "✓", "✕")
            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                styles.chunked(3).forEach { rowStyles ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        rowStyles.forEach { style ->
                            FormattingToolButton(text = style, contentColor = contentColor) { expanded = false }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberingTool(contentColor: Color) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        FormattingToolButton(text = "1…", contentColor = contentColor) { expanded = true }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Text("Numbering Style", modifier = Modifier.padding(16.dp, 8.dp), style = MaterialTheme.typography.labelMedium)
            val styles = listOf("1.", "1)", "a)", "A.", "a.", "i)")
            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                styles.chunked(3).forEach { rowStyles ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        rowStyles.forEach { style ->
                            FormattingToolButton(text = style, contentColor = contentColor) { expanded = false }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableTool(contentColor: Color) {
    var expanded by remember { mutableStateOf(false) }
    var showInsertTable by remember { mutableStateOf(false) }
    var showCalcTable by remember { mutableStateOf(false) }
    
    Box {
        FormattingToolButton(icon = Icons.Default.TableChart, contentColor = contentColor) { expanded = true }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Insert Table") },
                onClick = { expanded = false; showInsertTable = true },
                leadingIcon = { Icon(Icons.Default.GridOn, null) }
            )
            DropdownMenuItem(
                text = { Text("Calculation Table") },
                onClick = { expanded = false; showCalcTable = true },
                leadingIcon = { Icon(Icons.Default.Calculate, null) }
            )
        }
    }
    
    if (showInsertTable) {
        TableInsertSheet { showInsertTable = false }
    }
    
    if (showCalcTable) {
        CalculationTableSheet { showCalcTable = false }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableInsertSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Create Table", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Rows", modifier = Modifier.weight(1f))
                IconButton(onClick = {}) { Icon(Icons.Default.Remove, null) }
                Text("3", modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = {}) { Icon(Icons.Default.Add, null) }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Columns", modifier = Modifier.weight(1f))
                IconButton(onClick = {}) { Icon(Icons.Default.Remove, null) }
                Text("3", modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = {}) { Icon(Icons.Default.Add, null) }
            }
            
            OutlinedTextField(value = "Automatic", onValueChange = {}, label = { Text("Cell Width") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "Automatic", onValueChange = {}, label = { Text("Cell Height") }, modifier = Modifier.fillMaxWidth())
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onDismiss) { Text("Cancel") }
                Button(onClick = onDismiss, modifier = Modifier.padding(start = 8.dp)) { Text("Insert") }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationTableSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Calculation Table", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Rows", modifier = Modifier.weight(1f))
                IconButton(onClick = {}) { Icon(Icons.Default.Remove, null) }
                Text("4", modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = {}) { Icon(Icons.Default.Add, null) }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Columns", modifier = Modifier.weight(1f))
                IconButton(onClick = {}) { Icon(Icons.Default.Remove, null) }
                Text("3", modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = {}) { Icon(Icons.Default.Add, null) }
            }
            
            OutlinedTextField(value = "Sum", onValueChange = {}, label = { Text("Calculation") }, modifier = Modifier.fillMaxWidth())
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onDismiss) { Text("Cancel") }
                Button(onClick = onDismiss, modifier = Modifier.padding(start = 8.dp)) { Text("Insert") }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreTool(contentColor: Color) {
    var showSheet by remember { mutableStateOf(false) }
    Box {
        FormattingToolButton(icon = Icons.Default.MoreHoriz, contentColor = contentColor) { showSheet = true }
        if (showSheet) {
            ModalBottomSheet(onDismissRequest = { showSheet = false }) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text("More Options", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    
                    Section("Paragraph") {
                        AlignmentMenu()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Spacing", style = MaterialTheme.typography.labelLarge)
                        SpacingControls()
                    }
                    
                    Section("Formatting") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FormattingToolButton(icon = Icons.Default.Superscript, contentColor = contentColor) {}
                            FormattingToolButton(icon = Icons.Default.Subscript, contentColor = contentColor) {}
                            FormattingToolButton(icon = Icons.Default.StrikethroughS, contentColor = contentColor) {}
                            FormattingToolButton(icon = Icons.Default.FormatClear, contentColor = contentColor) {}
                        }
                    }
                    
                    Section("Insert") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FormattingToolButton(icon = Icons.Default.Category, contentColor = contentColor) {} // Shapes
                            FormattingToolButton(icon = Icons.Default.Image, contentColor = contentColor) {}
                            FormattingToolButton(icon = Icons.Default.Videocam, contentColor = contentColor) {}
                            FormattingToolButton(icon = Icons.Default.Link, contentColor = contentColor) {}
                            FormattingToolButton(icon = Icons.Default.CheckBox, contentColor = contentColor) {}
                        }
                    }
                    
                    Section("Advanced") {
                        TextButton(onClick = {}) { Text("Custom Line Spacing") }
                        TextButton(onClick = {}) { Text("Custom Colors") }
                        TextButton(onClick = {}) { Text("Shape Picker") }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = AccentColor)
        content()
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
fun AlignmentMenu() {
    var selected by remember { mutableIntStateOf(0) }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            Icons.Default.FormatAlignLeft to "Left",
            Icons.Default.FormatAlignCenter to "Center",
            Icons.Default.FormatAlignRight to "Right",
            Icons.Default.FormatAlignJustify to "Auto"
        ).forEachIndexed { index, pair ->
            FormattingToolButton(
                icon = pair.first,
                isSelected = selected == index,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) { selected = index }
        }
    }
}

@Composable
fun SpacingControls() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Before", modifier = Modifier.weight(1f))
            IconButton(onClick = {}) { Icon(Icons.Default.Remove, null) }
            Text("0", modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = {}) { Icon(Icons.Default.Add, null) }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("After", modifier = Modifier.weight(1f))
            IconButton(onClick = {}) { Icon(Icons.Default.Remove, null) }
            Text("0", modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = {}) { Icon(Icons.Default.Add, null) }
        }
        
        Text("Line Spacing", style = MaterialTheme.typography.labelSmall)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf("1.00", "1.25", "1.50", "1.75", "2.00", "Custom").forEach { spacing ->
                SuggestionChip(onClick = {}, label = { Text(spacing) })
            }
        }
    }
}
