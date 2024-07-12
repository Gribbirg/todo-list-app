package ru.gribbirg.list.di

import android.content.Context
import ru.gribbirg.data.di.DataFactory

interface ListFeatureDependencies {
    val context: Context
    val dataFactory: DataFactory
}