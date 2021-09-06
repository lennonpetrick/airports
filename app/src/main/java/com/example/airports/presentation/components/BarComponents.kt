package com.example.airports.presentation.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.airports.presentation.NavigationItem

@Composable
fun TopBar(title: String) {
    TopAppBar(title = { Text(text = title) })
}

@Composable
fun BottomBar(navController: NavController, items: List<NavigationItem>) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach {
            val isSelected = currentRoute == it.route
            BottomNavigationItem(
                selected = isSelected,
                icon = { Icon(it.icon, contentDescription = it.label) },
                label = { Text(text = it.label) },
                onClick = {
                    if (!isSelected) {
                        navController.navigate(it.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }
    }
}