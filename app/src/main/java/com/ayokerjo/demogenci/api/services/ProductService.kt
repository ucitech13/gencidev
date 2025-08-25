package com.ayokerjo.demogenci.api.services

import com.ayokerjo.demogenci.api.model.Product
import com.ayokerjo.demogenci.api.model.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    @GET("products")
    fun getAll(): Call<ProductResponse>

    @GET("products/{id}")
    fun getProductById(@Path("id") id: Int?): Call<Product>
}