package fr.dev.majdi.presentation.splashscreen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import fr.dev.majdi.presentation.R

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */

@Composable
fun SplashScreen(splashScreenViewModel: SplashScreenViewModel, context: Context) {
    val animationSpec = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.paris_animation)
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Lottie Animation
            val composition by animationSpec

            // render the animation
            LottieAnimation(
                modifier = Modifier.size(size = 140.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever // animate forever
            )
            // Navigate to the next screen when animation finishes
            DisposableEffect(composition) {
                onDispose {
                    splashScreenViewModel.onAnimationFinish(context = context)
                }
            }
        }
    }
}