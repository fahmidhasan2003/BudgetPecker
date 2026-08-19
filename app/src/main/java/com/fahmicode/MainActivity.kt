package com.fahmicode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
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
                var showNotesPopup by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            BottomNavigationBar(
                                navController = navController,
                                mainViewModel = mainViewModel,
                                onShowMore = { showMoreMenu = !showMoreMenu }
                            )
                        }
                    ) { innerPadding: PaddingValues ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            NavigationHost(
                                navController = navController,
                                viewModel = mainViewModel,
                                modifier = Modifier.padding(innerPadding)
                            )

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

                    if (showNotesPopup) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { showNotesPopup = false },
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            NotesPopup(
                                viewModel = mainViewModel,
                                onExpand = {
                                    showNotesPopup = false
                                    navController.navigate(BottomNavItem.Notes.route)
                                },
                                onDismiss = { showNotesPopup = false }
                            )
                        }
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
    onShowMore: () -> Unit
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
                        navController.navigate(BottomNavItem.History.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        mainViewModel.showAddTransactionDialog.value = true
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
