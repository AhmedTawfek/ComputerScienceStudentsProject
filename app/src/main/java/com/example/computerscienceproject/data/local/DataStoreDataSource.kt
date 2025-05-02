package com.example.computerscienceproject.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreDataSource (private val dataStore : DataStore<Preferences>) {

    suspend fun <T : Any> saveValueToDataStore(key : Preferences.Key<T>, value : T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun <T : Any> getValue(key : Preferences.Key<T>,default : T) = dataStore.data.first()[key] ?: default

    fun <T : Any> getFlow(key : Preferences.Key<T>,defaultValue : T) = dataStore.data.map { preferences ->
        preferences[key] ?: defaultValue
    }

    companion object{
        val USER_TOKEN = stringPreferencesKey("token")
        val LOCATION_PERMISSION_REQUEST_SKIPPED = booleanPreferencesKey("location_permission_request_skipped")
        val ON_BOARDING_SKIPPED = booleanPreferencesKey("onboarding_skipped")
        val REGISTRATION_SKIPPED = booleanPreferencesKey("registration_skipped")
//        val LOCATION_LONGITUDE = doublePreferencesKey("location_longitude")
//        val LOCATION_LATITUDE = doublePreferencesKey("location_latitude")
//        val LOCATION_PERMISSION_SKIPPED = booleanPreferencesKey(PERMISSION_LOCATION_KEY)
//        val NOTIFICATION_PERMISSION_SKIPPED = booleanPreferencesKey(PERMISSION_NOTIFICATION_KEY)
        var USER_LOGGED_IN : Boolean = false
    }
}