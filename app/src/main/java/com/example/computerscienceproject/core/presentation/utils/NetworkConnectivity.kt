package com.example.computerscienceproject.core.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


fun isInternetConnected(context: Context) = callbackFlow{
    val connectivityManager = context.getSystemService<ConnectivityManager>()!!

    // We use to check if internet is not connected at all, because the network callback to make it work we should connect to it first if internet is available.
    val isConnectedFirstTime = isInternetConnectedForFirstTime(connectivityManager)
    trySend(isConnectedFirstTime)

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            trySend(true)
            Log.d("MainActivity", "onAvailable: ")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.d("MainActivity", "onUnavailable: ")
            trySend(false)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d("MainActivity", "onLost: ")
            trySend(false)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ){
            super.onCapabilitiesChanged(network, networkCapabilities)
            val connected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            Log.d("MainActivity", "onCapabilitiesChanged: connected=$connected")
            trySend(connected)
        }
    }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    connectivityManager.requestNetwork(networkRequest, networkCallback)
    awaitClose {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

private fun isInternetConnectedForFirstTime(connectivityManager: ConnectivityManager) : Boolean{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}