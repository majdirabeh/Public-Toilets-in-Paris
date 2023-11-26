package fr.dev.majdi.publictoilets

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import fr.dev.majdi.presentation.BuildConfig
import timber.log.Timber

/**
 * Created by Majdi RABEH on 23/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@HiltAndroidApp
class AppToilet : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        }
    }

}