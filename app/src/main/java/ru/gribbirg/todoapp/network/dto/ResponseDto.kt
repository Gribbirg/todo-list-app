package ru.gribbirg.todoapp.network.dto

abstract class ResponseDto {
    abstract val status: String
    abstract val revision: Int
}