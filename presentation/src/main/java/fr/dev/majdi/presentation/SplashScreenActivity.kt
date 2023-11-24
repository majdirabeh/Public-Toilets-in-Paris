package fr.dev.majdi.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.dev.majdi.presentation.splashscreen.SplashScreen
import fr.dev.majdi.presentation.splashscreen.SplashScreenViewModel
import fr.dev.majdi.presentation.ui.theme.PublicToiletsTheme

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PublicToiletsTheme {
                SplashScreen(
                    splashScreenViewModel = splashScreenViewModel,
                    context = this
                )
            }
        }
    }
}