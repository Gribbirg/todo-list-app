package ru.gribbirg.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.utils.di.modules.DataStoreQualifier

/**
 * Implementation using data store
 *
 * @see KeyValueDataSaver
 */
internal class DataStoreSaver @AssistedInject constructor(
    private val context: Context,
    @Assisted name: String,
    @DataStoreQualifier coroutineDispatcher: CoroutineDispatcher
) : KeyValueDataSaver {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = name,
        scope = CoroutineScope(coroutineDispatcher),
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

    @AssistedFactory
    interface Factory {
        fun create(name: String): DataStoreSaver
    }
}