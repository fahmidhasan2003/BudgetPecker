package com.fahmicode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import com.fahmicode.data.model.FabAction
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.fahmicode.ui.MainViewModel
import com.fahmicode.ui.screens.*
import com.fahmicode.ui.theme.BudgetPeckerTheme

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.AutoMirrored.Filled.TrendingUp)
    object History : BottomNavItem("history", "History", Icons.AutoMirrored.Filled.List)
    object Add : BottomNavItem("add", "Add", Icons.Default.Add)
    object Budget : BottomNavItem("budget", "Budgets", Icons.Default.PieChart)
    object More : BottomNavItem("settings", "More", Icons.Default.GridView)
    object Notes : BottomNavItem("notes", "Notes", Icons.AutoMirrored.Filled.Notes)
    object Calculator : BottomNavItem("calculator", "Calculator", Icons.Default.Calculate)
    object Converter : BottomNavItem("converter", "Converter", Icons.Default.CurrencyExchange)
}

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDark by mainViewModel.isDarkMode
            BudgetPeckerTheme(darkTheme = isDark) {
                val navController = rememberNavController()
                var showMoreMenu by remember { mutableStateOf(false) }
                var showNoteSpeedDial by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxSize()) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val hideBottomBar = currentRoute?.startsWith("note_edit/") == true

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (!hideBottomBar) {
                                BottomNavigationBar(
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    onShowMore = { showMoreMenu = !showMoreMenu },
                                    onNoteFabClick = { showNoteSpeedDial = !showNoteSpeedDial }
                                )
                            }
                        }
                    ) { innerPadding: PaddingValues ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            NavigationHost(
                                navController = navController,
                                viewModel = mainViewModel,
                                modifier = Modifier.padding(innerPadding)
                            )

                            if (showNoteSpeedDial) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.5f))
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) { showNoteSpeedDial = false }
                                )
                                
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier
                                            .padding(bottom = 80.dp) // Pushed closer down
                                            .wrapContentSize()
                                    ) {
                                        SpeedDialItem(
                                            label = "Audio",
                                            icon = Icons.Default.Mic,
                                            onClick = { 
                                                mainViewModel.triggerNoteAction(FabAction.Audio)
                                                showNoteSpeedDial = false 
                                            }
                                        )
                                        SpeedDialItem(
                                            label = "Image",
                                            icon = Icons.Default.Image,
                                            onClick = { 
                                                mainViewModel.triggerNoteAction(FabAction.Image)
                                                showNoteSpeedDial = false 
                                            }
                                        )
                                        SpeedDialItem(
                                            label = "Calculation Table",
                                            icon = Icons.Default.TableChart,
                                            onClick = { 
                                                mainViewModel.triggerNoteAction(FabAction.CalculationTable)
                                                showNoteSpeedDial = false 
                                            }
                                        )
                                        SpeedDialItem(
                                            label = "Text Note",
                                            icon = Icons.Default.EditNote,
                                            onClick = { 
                                                mainViewModel.triggerNoteAction(FabAction.TextNote)
                                                showNoteSpeedDial = false 
                                            }
                                        )
                                    }
                                }
                            }

                            if (showMoreMenu) {
                                // Semi-transparent background that dismisses the menu
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.3f))
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) { showMoreMenu = false }
                                )

                                // Custom Floating Overlay Menu
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth(0.66f)
                                            .fillMaxHeight(0.5f)
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) { /* Block clicks from dismissing */ },
                                        shape = RoundedCornerShape(28.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        )
                                    ) {
                                        SettingsScreen(
                                            viewModel = mainViewModel,
                                            onDismiss = { showMoreMenu = false },
                                            onNotepadClick = {
                                                showMoreMenu = false
                                                navController.navigate(BottomNavItem.Notes.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                            onCalculatorClick = {
                                                showMoreMenu = false
                                                navController.navigate(BottomNavItem.Calculator.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                            onConverterClick = {
                                                showMoreMenu = false
                                                navController.navigate(BottomNavItem.Converter.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    val showAddDialog by mainViewModel.showAddTransactionDialog
                    if (showAddDialog) {
                        AddTransactionDialog(
                            viewModel = mainViewModel,
                            onDismiss = { mainViewModel.showAddTransactionDialog.value = false }
                        )
                    }


                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    onShowMore: () -> Unit,
    onNoteFabClick: () -> Unit
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.History,
        BottomNavItem.Add,
        BottomNavItem.Budget,
        BottomNavItem.More
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val isAdd = item == BottomNavItem.Add
            val isMore = item == BottomNavItem.More
            
            val isSelected = when (item) {
                BottomNavItem.More -> currentRoute == item.route || currentRoute == BottomNavItem.Notes.route || currentRoute == BottomNavItem.Calculator.route
                else -> !isAdd && currentRoute == item.route
            }

            NavigationBarItem(
                icon = {
                    if (isAdd) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.title)
                    }
                },
                label = {
                    if (!isAdd) {
                        Text(item.title)
                    }
                },
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (isAdd) Color.Transparent else MaterialTheme.colorScheme.secondaryContainer
                ),
                onClick = {
                    if (isAdd) {
                        if (currentRoute == BottomNavItem.Notes.route) {
                            onNoteFabClick()
                        } else {
                            navController.navigate(BottomNavItem.History.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            mainViewModel.showAddTransactionDialog.value = true
                        }
                    } else if (isMore) {
                        onShowMore()
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SpeedDialItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            shape = CircleShape,
            modifier = Modifier.size(44.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 2.dp,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Dashboard.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Dashboard.route) {
            DashboardScreen(viewModel, onSeeMore = {
                navController.navigate(BottomNavItem.History.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }
        composable(BottomNavItem.History.route) { HistoryScreen(viewModel) }
        composable(BottomNavItem.Budget.route) { BudgetScreen(viewModel) }
        composable(BottomNavItem.More.route) { SettingsScreen(viewModel) }
        composable(BottomNavItem.Notes.route) {
            NotesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToEditor = { noteId ->
                    navController.navigate("note_edit/$noteId")
                }
            )
        }
        composable(
            route = "note_edit/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1L
            NoteEditScreen(
                noteId = noteId,
                repository = viewModel.repository,
                onBack = { navController.popBackStack() }
            )
        }
        composable(BottomNavItem.Calculator.route) {
            CalculatorScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(BottomNavItem.Converter.route) {
            CurrencyConverterScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
