package com.ayokerjo.demogenci.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val stok: Int,
    val brand: String?,
    val thumbnail: String
)