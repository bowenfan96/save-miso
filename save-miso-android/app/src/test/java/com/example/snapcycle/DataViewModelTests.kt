package com.example.snapcycle

import android.location.Location
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class DataViewModelTests {

    var dataViewModel = DataViewModel()

    @Rule
    @JvmField
    val folder = TemporaryFolder()

    @Test
    fun testRecycleItemType() {
        val dataViewModel = DataViewModel()
        dataViewModel.setRecycleItemType("Testing")
        val itemType = dataViewModel.getRecycleItemType()
        assertEquals("Testing", itemType)
    }

    @Test
    fun testRecycleItemCount() {
        val dataViewModel = DataViewModel()
        dataViewModel.setRecycleItemCount(42)
        val itemCount = dataViewModel.getRecycleItemCount()
        assertEquals(42, itemCount)
    }

    @Test
    fun checkBinLocation() {
        var file = folder.newFile("testfile2")

        var location = Location("dummyprovider")
        location.longitude = 50.toDouble()
        location.longitude = 10.toDouble()
        dataViewModel.binLocation = location

        dataViewModel.saveBinLocationToFile(file)

        dataViewModel.binLocation = null
        dataViewModel.readBinLocationFromFile(file)

        assert(dataViewModel.binLocation != null)

    }

}