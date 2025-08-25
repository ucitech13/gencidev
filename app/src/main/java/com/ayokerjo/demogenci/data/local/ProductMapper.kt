package com.ayokerjo.demogenci.data.local

import com.ayokerjo.demogenci.api.model.Product

object ProductMapper {
    fun fromApiToEntity(apiProduct: Product): ProductEntity {
        return ProductEntity(
            id = apiProduct.id,
            title = apiProduct.title,
            description = apiProduct.description,
            price = apiProduct.price.toDouble(),
            stok = apiProduct.stock,
            brand = apiProduct.brand,
            thumbnail = apiProduct.thumbnail
        )
    }

    fun fromApiListToEntityList(apiProducts: List<Product>): List<ProductEntity> {
        return apiProducts.map { fromApiToEntity(it) }
    }
}