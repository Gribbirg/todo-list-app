package ru.gribbirg.utils

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.domain.utils.ItemsListsMerger
import ru.gribbirg.utils.extensions.toLocalDateTime
import java.time.LocalDateTime
import java.util.UUID

class ListMergerImplTest {

    private val listMerger: ItemsListsMerger = listMergerImpl

    private val newItem = mockItem(UUID.randomUUID().toString(), 100L.toLocalDateTime()!!)
    private val oldItem = mockItem(UUID.randomUUID().toString(), 10L.toLocalDateTime()!!)

    private fun mockItem(id: String, lastUpdateTime: LocalDateTime): TodoItem =
        mockk {
            every { this@mockk.id } returns id
            every { editDate } returns lastUpdateTime
        }

    @Test
    fun `merge empty lists without update time`() {
        val list1 = emptyList<TodoItem>()
        val list2 = emptyList<TodoItem>()
        val lastSyncTime = 0L.toLocalDateTime()!!

        val res = listMerger.mergeLists(list1, list2, lastSyncTime)

        assertEquals(emptyList<TodoItem>(), res)
    }

    @Test
    fun `merge lists if local is empty and sync time is newer then item update time`() {
        val emptyList = emptyList<TodoItem>()
        val fillList = listOf(newItem, oldItem)
        val lastSyncTime = 200L.toLocalDateTime()!!

        val res = listMerger.mergeLists(emptyList, fillList, lastSyncTime)

        assertEquals(emptyList, res)
    }

    @Test
    fun `merge lists if local is empty and sync time is older then item update time`() {
        val emptyList = emptyList<TodoItem>()
        val fillList = listOf(newItem, oldItem)
        val lastSyncTime = 0L.toLocalDateTime()!!

        val res = listMerger.mergeLists(emptyList, fillList, lastSyncTime)

        assertEquals(fillList, res)
    }

    @Test
    fun `merge lists if local is empty and sync time between updates times`() {
        val emptyList = emptyList<TodoItem>()
        val fillList = listOf(newItem, oldItem)
        val lastSyncTime = 50L.toLocalDateTime()!!

        val res = listMerger.mergeLists(emptyList, fillList, lastSyncTime)

        assertEquals(listOf(newItem), res)
    }

    @Test
    fun `merge lists if server is empty and sync time is newer then item update time`() {
        val emptyList = emptyList<TodoItem>()
        val fillList = listOf(newItem, oldItem)
        val lastSyncTime = 200L.toLocalDateTime()!!

        val res = listMerger.mergeLists(fillList, emptyList, lastSyncTime)

        assertEquals(emptyList, res)
    }

    @Test
    fun `merge lists if server is empty and sync time is older then item update time`() {
        val emptyList = emptyList<TodoItem>()
        val fillList = listOf(newItem, oldItem)
        val lastSyncTime = 0L.toLocalDateTime()!!

        val res = listMerger.mergeLists(fillList, emptyList, lastSyncTime)

        assertEquals(fillList, res)
    }

    @Test
    fun `merge lists if server is empty and sync time between updates times`() {
        val emptyList = emptyList<TodoItem>()
        val fillList = listOf(newItem, oldItem)
        val lastSyncTime = 50L.toLocalDateTime()!!

        val res = listMerger.mergeLists(fillList, emptyList, lastSyncTime)

        assertEquals(listOf(newItem), res)
    }

    @Test
    fun `merge two filled lists with local edit item`() {
        val id = UUID.randomUUID().toString()
        val listWithNew = listOf(mockItem(id, 1L.toLocalDateTime()!!))
        val listWithOld = listOf(mockItem(id, 0L.toLocalDateTime()!!))
        val lastUpdateTime = 0L.toLocalDateTime()!!

        val res = listMerger.mergeLists(listWithNew, listWithOld, lastUpdateTime)

        assertEquals(listWithNew, res)
    }

    @Test
    fun `merge two filled lists with server edit item`() {
        val id = UUID.randomUUID().toString()
        val listWithNew = listOf(mockItem(id, 1L.toLocalDateTime()!!))
        val listWithOld = listOf(mockItem(id, 0L.toLocalDateTime()!!))
        val lastUpdateTime = 0L.toLocalDateTime()!!

        val res = listMerger.mergeLists(listWithOld, listWithNew, lastUpdateTime)

        assertEquals(listWithNew, res)
    }
}