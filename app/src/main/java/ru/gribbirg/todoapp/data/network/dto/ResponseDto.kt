package ru.gribbirg.todoapp.data.network.dto

/**
 * Basic class for response
 */
interface ResponseDto {
    val status: String
    val revision: Int
}