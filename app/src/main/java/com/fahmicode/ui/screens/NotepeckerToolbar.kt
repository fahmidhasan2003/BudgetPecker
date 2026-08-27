package com.fahmicode.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
            .height(56.dp),
        color = backgroundColor,
        tonalElevation = 2.dp
    ) {
        Column {
            HorizontalDivider(color = contentColor.copy(alpha = 0.1f))
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AaTool(contentColor)
                QuickBoldTool(contentColor)
                AttachmentTool(contentColor)
                NoteColorTool(contentColor)
                ListTool(contentColor)
                MoreTool(contentColor)
            }
        }
    }
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
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AaTool(contentColor: Color) {
    var showSheet by remember { mutableStateOf(false) }
    FormattingToolButton(text = "Aa", contentColor = contentColor) { showSheet = true }

    if (showSheet) {
        EditorBottomSheet(onDismiss = { showSheet = false }) {
            Text(
                "Text Style",
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BottomSheetItem(Icons.Default.FormatBold, "Bold")
                BottomSheetItem(Icons.Default.FormatItalic, "Italic")
                BottomSheetItem(Icons.Default.FormatUnderlined, "Underline")
                BottomSheetItem(Icons.Default.FormatStrikethrough, "Strikethrough")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                BottomSheetItem(Icons.Default.FontDownload, "Font")
                BottomSheetItem(Icons.Default.FormatSize, "Font Size")
                BottomSheetItem(Icons.Default.FormatColorText, "Font Color")
                BottomSheetItem(Icons.Default.BorderColor, "Highlight")
            }
        }
    }
}

@Composable
fun QuickBoldTool(contentColor: Color) {
    var isSelected by remember { mutableStateOf(false) }
    FormattingToolButton(text = "B", isSelected = isSelected, contentColor = contentColor) { 
        isSelected = !isSelected 
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentTool(contentColor: Color) {
    var showSheet by remember { mutableStateOf(false) }
    var nestedSheet by remember { mutableStateOf<String?>(null) }

    FormattingToolButton(icon = Icons.Default.AttachFile, contentColor = contentColor) { showSheet = true }

    if (showSheet) {
        EditorBottomSheet(onDismiss = { showSheet = false }) {
            Text("Attachment", modifier = Modifier.padding(bottom = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BottomSheetItem(Icons.Default.PhotoCamera, "Photo") { 
                    nestedSheet = "Photo"
                    showSheet = false
                }
                BottomSheetItem(Icons.AutoMirrored.Filled.InsertDriveFile, "Files")
                BottomSheetItem(Icons.Default.Mic, "Audio") { 
                    nestedSheet = "Audio"
                    showSheet = false
                }
                BottomSheetItem(Icons.Default.QrCodeScanner, "Scan")
                BottomSheetItem(Icons.Default.Link, "Link")
            }
        }
    }

    if (nestedSheet == "Photo") {
        EditorBottomSheet(onDismiss = { nestedSheet = null }) {
            Text("Photo Source", modifier = Modifier.padding(bottom = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BottomSheetItem(Icons.Default.PhotoLibrary, "Gallery") { nestedSheet = null }
                BottomSheetItem(Icons.Default.PhotoCamera, "Camera") { nestedSheet = null }
            }
        }
    }

    if (nestedSheet == "Audio") {
        EditorBottomSheet(onDismiss = { nestedSheet = null }) {
            Text("Audio", modifier = Modifier.padding(bottom = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BottomSheetItem(Icons.Default.RadioButtonChecked, "Record") { nestedSheet = null }
                BottomSheetItem(Icons.Default.LibraryMusic, "Choose Audio") { nestedSheet = null }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteColorTool(contentColor: Color) {
    var showSheet by remember { mutableStateOf(false) }
    FormattingToolButton(icon = Icons.Default.Palette, contentColor = contentColor) { showSheet = true }

    if (showSheet) {
        val colors = listOf(
            0xFF121212, 0xFFF8D7DA, 0xFFD4EDDA, 0xFFD1ECF1,
            0xFFFFF3CD, 0xFFE1D5E7, 0xFFE2E3E5, 0xFFBEE5EB,
            0xFFC3E6CB, 0xFFF5C6CB, 0xFFFFEBAA, 0xFFD6D8D9
        )
        EditorBottomSheet(onDismiss = { showSheet = false }) {
            Text("Note Color", modifier = Modifier.padding(bottom = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                colors.take(5).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(color))
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape)
                            .clickable { showSheet = false }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                colors.drop(5).take(5).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(color))
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape)
                            .clickable { showSheet = false }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTool(contentColor: Color) {
    var showSheet by remember { mutableStateOf(false) }
    var nestedSheet by remember { mutableStateOf<String?>(null) }

    FormattingToolButton(icon = Icons.AutoMirrored.Filled.FormatListBulleted, contentColor = contentColor) { showSheet = true }

    if (showSheet) {
        EditorBottomSheet(onDismiss = { showSheet = false }) {
            Text("List Options", modifier = Modifier.padding(bottom = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BottomSheetItem(Icons.AutoMirrored.Filled.FormatListBulleted, "Bullet") { 
                    nestedSheet = "Bullet"
                    showSheet = false
                }
                BottomSheetItem(Icons.Default.FormatListNumbered, "Number") { 
                    nestedSheet = "Number"
                    showSheet = false
                }
                BottomSheetItem(Icons.Default.CheckBox, "Checklist") { showSheet = false }
            }
        }
    }

    if (nestedSheet == "Bullet") {
        EditorBottomSheet(onDismiss = { nestedSheet = null }) {
            Text("Bullet Styles", modifier = Modifier.padding(bottom = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Basic", style = MaterialTheme.typography.labelMedium, color = AccentColor, modifier = Modifier.padding(vertical = 4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("•", "○", "■", "□", "–").forEach { style ->
                    Box(
                        modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)).clickable { nestedSheet = null },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(style, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Symbol", style = MaterialTheme.typography.labelMedium, color = AccentColor, modifier = Modifier.padding(vertical = 4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                listOf("→", "✓", "☆").forEach { style ->
                    Box(
                        modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)).clickable { nestedSheet = null },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(style, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (nestedSheet == "Number") {
        EditorBottomSheet(onDismiss = { nestedSheet = null }) {
            Text("Number Styles", modifier = Modifier.padding(bottom = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            val styles1 = listOf("1.", "1)", "a.")
            val styles2 = listOf("A.", "i.", "I.")
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                styles1.forEach { style ->
                    Box(
                        modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(8.dp)).clickable { nestedSheet = null },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(style, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                styles2.forEach { style ->
                    Box(
                        modifier = Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(8.dp)).clickable { nestedSheet = null },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(style, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreTool(contentColor: Color) {
    var showSheet by remember { mutableStateOf(false) }
    FormattingToolButton(icon = Icons.Default.MoreVert, contentColor = contentColor) { showSheet = true }

    if (showSheet) {
        EditorBottomSheet(onDismiss = { showSheet = false }) {
            Text("More Options", modifier = Modifier.padding(bottom = 16.dp), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Section("1. Paragraph") {
                    BottomSheetItem(Icons.Default.FormatAlignLeft, "Alignment")
                    BottomSheetItem(Icons.AutoMirrored.Filled.FormatIndentIncrease, "Indent")
                    BottomSheetItem(Icons.Default.VerticalAlignBottom, "Paragraph Spacing")
                }

                Section("2. Line & Spacing") {
                    BottomSheetItem(Icons.Default.FormatLineSpacing, "Line Spacing")
                    BottomSheetItem(Icons.Default.VerticalAlignTop, "Before Paragraph")
                    BottomSheetItem(Icons.Default.VerticalAlignBottom, "After Paragraph")
                }

                Section("3. Insert") {
                    BottomSheetItem(Icons.Default.TableChart, "Table")
                    BottomSheetItem(Icons.Default.Calculate, "Calculation Table")
                }

                Section("4. Format") {
                    BottomSheetItem(Icons.Default.FormatClear, "Clear Formatting")
                }

                Section("5. Others") {
                    BottomSheetItem(Icons.Default.SelectAll, "Select All")
                }
            }
        }
    }
}

@Composable
fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelSmall, color = AccentColor, fontWeight = FontWeight.Bold)
        content()
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
    }
}

@Composable
fun BottomSheetItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f))
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorBottomSheet(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)) },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 48.dp)
        ) {
            content()
        }
    }
}
