package com.example.snapcycle.itemModel

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class ItemList {

    // This class stores an ArrayList of ItemEntry items and enforces a limit on number of items
    // Adds to front of list

    var capacity: Int = 10

    var list: ArrayList<ItemEntry> = arrayListOf()


    fun add(item: ItemEntry) {
        if (list.size == capacity) {
            list.removeAt(capacity - 1)
        }
        list.add(0, item)
    }

    fun removeAt(index: Int): ItemEntry {
        return list.removeAt(index)
    }


    fun addLineAsEntry(line: String) {
        val item = Json.decodeFromString(ItemEntry.serializer(), line)
        add(item)
    }

    fun storeToFile(file: File) {
        val stringBuilder = StringBuilder()
        if (list.isNotEmpty()) {
            for (i in list.size - 1 downTo 1 step 1) {
                stringBuilder.append(Json.encodeToString(ItemEntry.serializer(), list[i]))
                stringBuilder.append("\n")
            }
            stringBuilder.append(Json.encodeToString(list[0]))
        }
        file.writeText(stringBuilder.toString())
    }

    fun readFromFile(file: File) {
        file.forEachLine {
            addLineAsEntry(it)
        }
    }

}