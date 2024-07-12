package ru.gribbirg.edit.di

import android.content.Context
import ru.gribbirg.data.di.DataFactory

interface EditFeatureDependencies {
    val context: Context
    val dataFactory: DataFactory
}