package ru.gribbbirg.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.gribbirg.db.TodoDao
import ru.gribbirg.db.TodoDatabase
import ru.gribbirg.db.TodoDbEntity
import ru.gribbirg.db.toLocalDbItem
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.utils.extensions.currentLocalDateTimeAtUtc
import ru.gribbirg.utils.extensions.toLocalDateTime
import ru.gribbirg.utils.extensions.toTimestamp
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {
    private lateinit var dao: TodoDao
    private lateinit var db: TodoDatabase

    private fun getItem(): TodoItem {
        val now = currentLocalDateTimeAtUtc.toTimestamp().toLocalDateTime()!!
        return TodoItem(
            id = UUID.randomUUID().toString(),
            creationDate = now,
            editDate = now,
        )
    }

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java).build()
        dao = db.getTodoDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getEmptyList() {
        runBlocking {
            val res = dao.getAll()

            assertEquals(emptyList<TodoDbEntity>(), res)
        }
    }

    @Test
    fun getNotExistsItem() {
        runBlocking {
            val id = "1"

            val res = dao.getItem(id)

            assertNull(res)
        }
    }

    @Test
    fun addAndGetItem() {
        runBlocking {
            val item = getItem().toLocalDbItem()

            dao.addItem(item)
            val res = dao.getItem(item.id)

            assertEquals(item, res)
        }
    }

    @Test
    fun addManyAndGetAll() {
        runBlocking {
            val items = List(10) { getItem().toLocalDbItem() }

            dao.addAll(items)
            val res = dao.getAll()

            assertEquals(items, res)
        }
    }

    @Test
    fun addEmptyList() {
        runBlocking {
            val items = emptyList<TodoDbEntity>()

            dao.addAll(items)
        }
    }

    @Test
    fun updateItem() {
        runBlocking {
            val item = getItem().toLocalDbItem()
            dao.addItem(item)
            val newItem = item.copy(text = "new text")

            dao.updateItem(newItem)

            val res = dao.getItem(newItem.id)
            assertEquals(newItem, res)
        }
    }

    @Test
    fun updateNotExistsItem() {
        runBlocking {
            val item = getItem().toLocalDbItem()

            dao.updateItem(item)

            val res = dao.getItem(item.id)
            assertNull(res)
        }
    }

    @Test
    fun deleteItem() {
        runBlocking {
            val item = getItem().toLocalDbItem()
            dao.addItem(item)

            dao.deleteItemById(item.id)

            val res = dao.getItem(item.id)
            assertNull(res)
        }
    }

    @Test
    fun deleteNotExistsItem() {
        runBlocking {
            dao.deleteItemById("1")
        }
    }

    @Test
    fun deleteAllItems() {
        runBlocking {
            val items = List(10) { getItem().toLocalDbItem() }
            dao.addAll(items)

            dao.deleteAll()

            items.forEach { assertNull(dao.getItem(it.id)) }
        }
    }

    @Test
    fun refreshItems() {
        runBlocking {
            val itemsInit = List(10) { getItem().toLocalDbItem() }
            dao.addAll(itemsInit)
            val itemsNew = List(10) { getItem().toLocalDbItem() }

            dao.refreshItems(itemsNew)

            val res = dao.getAll()
            assertEquals(itemsNew, res)
        }
    }
}