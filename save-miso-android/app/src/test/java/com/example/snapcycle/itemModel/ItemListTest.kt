package com.example.snapcycle.itemModel

import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.rules.TemporaryFolder

internal class ItemListTest {

    private var itemList = ItemList()
    private val item1 = ItemEntry("Test", 5)
    private val item2 = ItemEntry("Test", 10)

    @Rule
    @JvmField
    val folder = TemporaryFolder()

    @Test
    fun addAndRemoveItem() {
        itemList.add(item1)
        itemList.add(item2)

        val firstItem: ItemEntry = itemList.removeAt(0)
        assertEquals(firstItem, item2)

        val secondItem: ItemEntry = itemList.removeAt(0)
        assertEquals(secondItem, item1)
    }

    @Test
    fun storeAndRetrieveEmptyList() {
        var file = folder.newFile("testfile2")

        itemList.storeToFile(file)
        var listFromFile = ItemList()
        listFromFile.readFromFile(file)

        assertEquals(listFromFile.list.size, 0)
    }

    @Test
    fun storeAndRetrieveFromFile() {

        var file = folder.newFile("testfile1")

        itemList.add(item1)
        itemList.add(item2)
        itemList.storeToFile(file)

        var listFromFile = ItemList()
        listFromFile.readFromFile(file)

        val firstItem: ItemEntry = itemList.removeAt(0)
        assertEquals(firstItem, item2)

        val secondItem: ItemEntry = itemList.removeAt(0)
        assertEquals(secondItem, item1)

        file.delete()
    }

    @Test
    fun listCapacityWorks() {
        for (i in 1..itemList.capacity) {
            itemList.add(item1)
        }

        itemList.add(item2)

        assertEquals(itemList.list.size, itemList.capacity)
    }

}