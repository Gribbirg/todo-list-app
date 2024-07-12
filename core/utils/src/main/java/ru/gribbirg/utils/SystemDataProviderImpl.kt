package ru.gribbirg.utils

import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.domain.utils.SystemDataProvider
import ru.gribbirg.utils.di.modules.SystemDataQualifier
import java.util.UUID
import javax.inject.Inject

internal class SystemDataProviderImpl @Inject constructor(
    @SystemDataQualifier private val keyValueDataSaver: KeyValueDataSaver,
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