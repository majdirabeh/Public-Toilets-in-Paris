package fr.dev.majdi.presentation.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * Created by Majdi RABEH on 25/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@Suppress("DEPRECATION")
class ConnectivityReceiver(private val listener: OnConnectivityChangedListener) :
    BroadcastReceiver() {

    constructor() : this(object : OnConnectivityChangedListener {
        override fun onConnectivityChanged(isConnected: Boolean) {
            // Handle connectivity change
        }
    })

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            val isConnected = networkInfo != null && networkInfo.isConnected
            listener.onConnectivityChanged(isConnected)
        }
    }
}

interface OnConnectivityChangedListener {
    fun onConnectivityChanged(isConnected: Boolean)
}