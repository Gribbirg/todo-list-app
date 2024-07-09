package ru.gribbirg.todoapp.data.logic

import ru.gribbirg.todoapp.data.data.TodoItem

/**
 * List merger based on simple algorithm:
 * - if item is in both lists - take last updated
 * - if item only in one - get it if it was edited after last sync
 *
 * @see ItemsListsMerger
 */
val listMergerImpl = ItemsListsMerger { local, internet, lastUpdateTime ->
    val internetMap = internet.associateBy { it.id }
    val cacheMap = local.associateBy { it.id }

    val res = mutableListOf<TodoItem>()

    cacheMap.forEach { (key, cacheValue) ->
        if (key in internetMap.keys) {
            val internetValue = internetMap[key]!!
            val lastUpdatedItem =
                if (cacheValue.editDate >= internetValue.editDate)
                    cacheValue
                else
                    internetValue
            res.add(lastUpdatedItem)
        } else if (cacheValue.editDate >= lastUpdateTime) {
            res.add(cacheValue)
        }
    }

    internetMap.forEach { (key, internetValue) ->
        if (key !in cacheMap.keys && internetValue.editDate >= lastUpdateTime) {
            res.add(internetValue)
        }
    }

    res
}