package ru.gribbirg.data.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.data.di.DataComponent
import ru.gribbirg.domain.utils.ItemsListsMerger
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.domain.utils.SystemDataProvider
import ru.gribbirg.network.NetworkConstants
import ru.gribbirg.network.di.modules.ApiClientScope
import ru.gribbirg.utils.di.UtilsFactory

@Module
internal interface UtilsModule {
    companion object {
        @Provides
        fun utilsFactory(deps: DataComponent): UtilsFactory = UtilsFactory(deps)

        @Provides
        @ApiClientScope
        fun internetKeyValueSaver(factory: UtilsFactory): KeyValueDataSaver =
            factory.createKeyValueSaver(NetworkConstants.KEY_VALUE_SAVER_NAME)

        @Provides
        fun systemDataProvider(factory: UtilsFactory): SystemDataProvider =
            factory.createSystemDataProvider()

        @Provides
        fun listMerger(factory: UtilsFactory): ItemsListsMerger =
            factory.createListMerger()
    }
}