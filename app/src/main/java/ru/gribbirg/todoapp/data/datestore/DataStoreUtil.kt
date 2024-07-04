package ru.gribbirg.todoapp.data.datestore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class DataStoreUtil(
    private val context: Context,
    name: String,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = name,
        scope = coroutineScope,
    )

    suspend fun saveItem(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { pref ->
            pref[prefKey] = value
        }
    }

    suspend fun getItem(key: String): String? {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { it[prefKey] }.firstOrNull()
    }
}