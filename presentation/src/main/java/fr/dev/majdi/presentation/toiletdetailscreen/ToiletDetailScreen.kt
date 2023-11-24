package fr.dev.majdi.presentation.toiletdetailscreen

import android.os.Bundle
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.dev.majdi.presentation.components.AppBar
import fr.dev.majdi.presentation.ui.Screen

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@Composable
fun ToiletDetailScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val arguments = navBackStackEntry?.arguments

    // Extracting the bundle from the route
    val bundle = arguments?.getParcelable<Bundle>(Screen.ToiletDetailScreen.route)

    // Retrieving data from the bundle
    val data = bundle?.getString("recordid")


    Scaffold(
        topBar = {
            AppBar(title = "Toilet Detail", onBackPressed = {
                navController.navigate(Screen.ToiletListScreen.route) // Handle the back navigation
            })
        }
    ) { padding ->
        // Your UI code using the retrieved data
        // For example, display the data in a Text composable
        Text(modifier = Modifier.padding(padding), text = "Received data: $data")
    }
}