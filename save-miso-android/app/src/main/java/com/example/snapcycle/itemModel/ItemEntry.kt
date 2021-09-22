package com.example.snapcycle.itemModel

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ItemEntry(
    var description: String,
    var count: Int,
    var status: Boolean = false,
    var carbon: Int = 0
)