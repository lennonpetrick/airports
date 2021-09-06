package com.example.airports.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.airports.R
import com.example.airports.presentation.components.BottomBar
import com.example.airports.presentation.components.TopBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appName = stringResource(R.string.app_name)
            MainScreen(appName)
        }
    }

    @Composable
    fun MainScreen(topBarTitle: String) {
        val navController = rememberNavController()
        val navItems = NavigationItem.getList()

        Scaffold(
            topBar = { TopBar(topBarTitle) },
            bottomBar = { BottomBar(navController, items = navItems) }
        ) {
            Navigation(navController)
        }
    }

    @Composable
    @Preview
    fun Preview() {
        MainScreen("Airports")
    }

}