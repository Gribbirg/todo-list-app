package ru.gribbirg.utils

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.gribbirg.domain.utils.KeyValueDataSaver

class SystemDataProviderImplTest {

    private val dispatcher = StandardTestDispatcher()

    private val mockKeyValueSaver = object : KeyValueDataSaver {

        private val map = mutableMapOf<String, String>()

        override suspend fun save(key: String, value: String) {
            map[key] = value
        }

        override suspend fun get(key: String): String? = map[key]

        override suspend fun remove(key: String) {
            map.remove(key)
        }
    }

    private val systemDataProvider = SystemDataProviderImpl(mockKeyValueSaver)

    @Test
    fun `get device id two times with creation at first`() = runTest(dispatcher) {
        val resFirst = systemDataProvider.getDeviceId()
        val resSecond = systemDataProvider.getDeviceId()

        assertEquals(resFirst, resSecond)
    }
}