package ru.gribbirg.todoapp.data.systemdata

import android.content.Context
import android.provider.Settings

class SystemDataProviderImpl(
    private val context: Context,
) : SystemDataProvider {

    override fun getDeviceId(): String =
        Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
}