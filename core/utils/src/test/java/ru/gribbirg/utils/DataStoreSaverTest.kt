package ru.gribbirg.utils

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class DataStoreSaverTest {

    @TempDir
    lateinit var tempDir: File

    private val testContext: Context
        get() = mockk {
            every { applicationContext } returns this@mockk
            every { filesDir } returns tempDir
        }

    private val testCoroutineDispatcher = StandardTestDispatcher()

    private val dataStore
        get() = DataStoreSaver(
            name = "test",
            context = testContext,
            coroutineDispatcher = testCoroutineDispatcher
        )

    @Test
    fun `save and get item first time`() = runTest(testCoroutineDispatcher) {
        val testDataStore = dataStore
        val testKey = "test_key"
        val testValue = "test_value"

        testDataStore.save(testKey, testValue)

        assertEquals(testValue, testDataStore.get(testKey))
    }

    @Test
    fun `save and get item if key already exists`() = runTest(testCoroutineDispatcher) {
        val testDataStore = dataStore
        val testKey = "test_key"
        val testValueInit = "test_value_init"
        val testValueFinal = "test_value_final"
        testDataStore.save(testKey, testValueInit)

        testDataStore.save(testKey, testValueFinal)

        assertEquals(testValueFinal, testDataStore.get(testKey))
    }

    @Test
    fun `get item by key which not exists`() = runTest(testCoroutineDispatcher) {
        val testDataStore = dataStore
        val testKey = "test_key"

        assertNull(testDataStore.get(testKey))
    }

    @Test
    fun `remove item by key which exists`() = runTest(testCoroutineDispatcher) {
        val testDataStore = dataStore
        val testKey = "test_key"
        val testValue = "test_value"
        testDataStore.save(testKey, testValue)

        testDataStore.remove(testKey)

        assertNull(testDataStore.get(testKey))
    }

    @Test
    fun `remove item by key which not exists`() = runTest(testCoroutineDispatcher) {
        val testDataStore = dataStore
        val testKey = "test_key"

        testDataStore.remove(testKey)

        assertNull(testDataStore.get(testKey))
    }
}