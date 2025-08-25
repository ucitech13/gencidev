package com.ayokerjo.demogenci.api.model

data class ProductResponse (
    val products: List<Product>,
    val total: Int
)