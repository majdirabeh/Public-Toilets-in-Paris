package fr.dev.majdi.presentation.splashscreen

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.dev.majdi.presentation.MainActivity
import fr.dev.majdi.presentation.SplashScreenActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@HiltViewModel
class SplashScreenViewModel @Inject constructor() : ViewModel() {

    // Navigate to the next screen after 4 second
    fun onAnimationFinish(context: Context) {
        viewModelScope.launch {
            delay(4000)
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            (context as? SplashScreenActivity)?.finish()
        }
    }

}