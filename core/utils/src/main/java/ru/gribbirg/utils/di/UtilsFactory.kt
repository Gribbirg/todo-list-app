package ru.gribbirg.utils.di

import ru.gribbirg.domain.model.TodoItem
import ru.gribbirg.domain.utils.ItemsListsMerger
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.domain.utils.SystemDataProvider
import ru.gribbirg.utils.listMergerImpl
import ru.gribbirg.utils.todoItemComparatorForUi

class UtilsFactory(
    deps: UtilsDependencies,
) {
    private val component: UtilsComponent = DaggerUtilsComponent.factory().create(deps)

    fun createKeyValueSaver(name: String): KeyValueDataSaver =
        component.keyValueSaverFactory.create(name)

    fun createSystemDataProvider(): SystemDataProvider =
        component.systemDataProviderImpl

    fun createListMerger(): ItemsListsMerger =
        listMergerImpl

    fun createTodoItemComparator(): Comparator<TodoItem> = todoItemComparatorForUi
}