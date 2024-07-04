package ru.gribbirg.todoapp.network

const val BASE_URL = "https://hive.mrdekk.ru/todo"
const val LIST_URL = "$BASE_URL/list"
fun getItemUrl(id: String) = "$LIST_URL/$id"

const val REVISION_HEADER = "X-Last-Known-Revision"