package com.ayokerjo.demogenci.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayokerjo.demogenci.api.model.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)   // ✅ harus pakai ProductEntity

    @Query("SELECT * FROM products")
    suspend fun getAll(): List<ProductEntity>
}