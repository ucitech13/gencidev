package com.ayokerjo.demogenci.api.model

data class Product (
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val brand: String,
    val thumbnail: String
)