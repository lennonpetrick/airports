package com.example.airports.presentation.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.airports.presentation.NavigationItem

@Preview
@Composable
fun TopBarPreview() {
    TopBar("TopBar")
}

@Composable
fun TopBar(title: String, onBackClick: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = onBackClick?.let {
            {
                IconButton(onClick = it) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                }
            }
        }
    )
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