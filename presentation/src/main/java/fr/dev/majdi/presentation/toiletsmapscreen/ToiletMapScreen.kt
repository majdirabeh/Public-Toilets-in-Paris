package fr.dev.majdi.presentation.toiletsmapscreen

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import fr.dev.majdi.presentation.ui.Screen
import fr.dev.majdi.presentation.components.AppBar

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@Composable
fun ToiletMapScreen(navController: NavController) {
    Scaffold(
        topBar = {
            AppBar(title = "Toilet Map", onBackPressed = {
                navController.navigate(Screen.ToiletListScreen.route) // Handle the back navigation
            })
        }
    ) {

    }
}