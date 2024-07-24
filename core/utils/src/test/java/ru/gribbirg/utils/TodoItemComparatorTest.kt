package ru.gribbirg.utils

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.gribbirg.domain.model.todo.TodoImportance
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.utils.extensions.toLocalDate
import ru.gribbirg.utils.extensions.toLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime

class TodoItemComparatorTest {

    private val comparator: Comparator<TodoItem> = todoItemComparatorForUi

    private fun mockItem(
        importance: TodoImportance = TodoImportance.No,
        deadline: LocalDate? = null,
        creationDate: LocalDateTime = 0L.toLocalDateTime()!!,
    ): TodoItem =
        mockk {
            every { this@mockk.importance } returns importance
            every { this@mockk.deadline } returns deadline
            every { this@mockk.creationDate } returns creationDate
            every { this@mockk.toString() } returns "TodoItem(importance: $importance, deadline: $deadline, creationDate: $creationDate)"
        }

    @Test
    fun `compare items by importance`() {
        val itemBigger = mockItem(TodoImportance.Low)
        val itemSmaller = mockItem(TodoImportance.High)

        val res = comparator.compare(itemSmaller, itemBigger)

        assert(res < 0)
    }

    @Test
    fun `compare items by deadline`() {
        val itemBigger = mockItem(deadline = 1L.toLocalDate())
        val itemSmaller = mockItem(deadline = 1000000000000L.toLocalDate())

        val res = comparator.compare(itemSmaller, itemBigger)

        assert(res < 0) { "!($itemSmaller < $itemBigger)" }
    }

    @Test
    fun `compare items by creationDate`() {
        val itemBigger = mockItem(creationDate = 1L.toLocalDateTime()!!)
        val itemSmaller = mockItem(creationDate = 1000000000000L.toLocalDateTime()!!)

        val res = comparator.compare(itemSmaller, itemBigger)

        assert(res < 0) { "!($itemSmaller < $itemBigger)" }
    }

    @Test
    fun `compare equals items`() {
        val item1 = mockItem()
        val item2 = mockItem()

        val res = comparator.compare(item1, item2)

        assert(res == 0) { "!($item1 == $item2)" }
    }
}