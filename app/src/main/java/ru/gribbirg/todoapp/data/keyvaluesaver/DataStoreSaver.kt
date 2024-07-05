package ru.gribbirg.todoapp.data.keyvaluesaver

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

/**
 * Implementation using data store
 *
 * @see KeyValueDataSaver
 */
class DataStoreSaver(
    private val context: Context,
    name: String,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : KeyValueDataSaver {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = name,
        scope = coroutineScope,
    )

    override suspend fun save(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { pref ->
            pref[prefKey] = value
        }
    }

    override suspend fun get(key: String): String? {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { it[prefKey] }.firstOrNull()
    }

    override suspend fun remove(key: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { pref ->
            pref.remove(prefKey)
        }
    }
}