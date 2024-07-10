package ru.gribbirg.todoapp.data.systemdata

interface SystemDataProvider {
    fun getDeviceId(): String
}