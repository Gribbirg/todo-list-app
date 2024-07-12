package ru.gribbirg.list.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.domain.model.TodoItem
import ru.gribbirg.list.di.ListFeatureComponent
import ru.gribbirg.utils.di.UtilsFactory

@Module
interface UtilsModule {
    companion object {
        @Provides
        fun utilsFactory(depsImpl: ListFeatureComponent) = UtilsFactory(depsImpl)

        @Provides
        fun comparator(factory: UtilsFactory): Comparator<TodoItem> =
            factory.createTodoItemComparator()
    }
}