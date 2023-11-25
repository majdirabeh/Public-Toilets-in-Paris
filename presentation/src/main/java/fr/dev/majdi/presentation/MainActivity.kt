package fr.dev.majdi.presentation

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.dev.majdi.presentation.toiletslistscreen.ToiletListScreen
import fr.dev.majdi.presentation.toiletslistscreen.ToiletListViewModel
import fr.dev.majdi.presentation.toiletsmapscreen.ToiletMapScreen
import fr.dev.majdi.presentation.toiletsmapscreen.ToiletMapViewModel
import fr.dev.majdi.presentation.ui.Screen
import fr.dev.majdi.presentation.util.ConnectivityReceiver
import fr.dev.majdi.presentation.util.OnConnectivityChangedListener

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity(), OnConnectivityChangedListener {

    private val toiletListViewModel: ToiletListViewModel by viewModels()
    private val toiletMapViewModel: ToiletMapViewModel by viewModels()

    private val connectivityReceiver = ConnectivityReceiver(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val bottomBarScreens = listOf(
                Screen.ToiletListScreen,
                Screen.ToiletMapScreen
            )
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        bottomBarScreens.forEach { screen ->
                            BottomNavigationItem(
                                icon = { Icon(screen.icon, contentDescription = null) },
                                label = { Text(text = stringResource(id = screen.resourceId)) },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { padding ->
                NavHost(
                    modifier = Modifier.padding(padding),
                    navController = navController,
                    startDestination = Screen.ToiletListScreen.route
                ) {
                    composable(Screen.ToiletListScreen.route) {
                        ToiletListScreen(toiletListViewModel)
                    }
                    composable(Screen.ToiletMapScreen.route) {
                        ToiletMapScreen(toiletMapViewModel)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
    }


    override fun onConnectivityChanged(isConnected: Boolean) {
        toiletListViewModel._isInternetAvailable.value = isConnected
    }
}