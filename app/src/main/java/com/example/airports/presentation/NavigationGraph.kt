package com.example.airports.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.airports.presentation.airportlist.AirportListScreen
import com.example.airports.presentation.airportmap.AirportMapScreen
import com.example.airports.presentation.settings.SettingsScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.AirportMap.route) {
        composable(NavigationItem.AirportMap.route) { AirportMapScreen() }
        composable(NavigationItem.AirportList.route) { AirportListScreen() }
        composable(NavigationItem.Settings.route) { SettingsScreen() }
    }
}

sealed class NavigationItem(val route: String,
                            val label: String,
                            val icon: ImageVector) {

    companion object {
        fun getList(): List<NavigationItem> {
            return listOf(AirportMap, AirportList, Settings)
        }
    }

    object AirportMap: NavigationItem(route = "airport_map", label = "Map", icon = Icons.Default.Map)
    object AirportList: NavigationItem(route = "airport_list", label = "From Schiphol", icon = Icons.Default.List)
    object Settings: NavigationItem(route = "settings", label = "Settings", icon = Icons.Default.Settings)
}