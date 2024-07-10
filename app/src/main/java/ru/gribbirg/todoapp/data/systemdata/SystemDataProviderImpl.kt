package ru.gribbirg.todoapp.data.systemdata

import ru.gribbirg.todoapp.data.keyvaluesaver.KeyValueDataSaver
import java.util.UUID

class SystemDataProviderImpl(
    private val keyValueDataSaver: KeyValueDataSaver,
) : SystemDataProvider {

    override suspend fun getDeviceId(): String =
        keyValueDataSaver.get(SAVER_KEY) ?: UUID.randomUUID().toString().also { saveId(it) }

    private suspend fun saveId(id: String) {
        keyValueDataSaver.save(SAVER_KEY, id)
    }


    companion object {
        private const val SAVER_KEY = "device_id"

        const val SAVER_NAME = "system_data"
    }
}