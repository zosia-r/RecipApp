package com.example.recipapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hierarchy

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Favourites : Screen("favourites", "Favourites", Icons.Default.Favorite)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object New : Screen("new", "New", Icons.Default.Add)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // pobieramy aktualną trasę by wiedzieć, który kontroler podświetlić
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFFE91E63) // różowy
            ) {
                val items = listOf(Screen.Favourites, Screen.Search, Screen.New)

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { dest ->
                            dest.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFE91E63),
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color(0xFFFCE4EC) // podświetlenie wybranej ikonki
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // pasek nawigacji
        NavHost(
            navController = navController,
            startDestination = Screen.Favourites.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Favourites.route) { FavouritesScreen() }
            composable(Screen.Search.route) { SearchScreen() }
            composable(Screen.New.route) { NewRecipeScreen() }
        }
    }
}