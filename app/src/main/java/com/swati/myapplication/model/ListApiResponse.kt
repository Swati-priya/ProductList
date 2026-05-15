package com.swati.myapplication.model

import com.google.gson.internal.NumberLimits

data class ListApiResponse(
    val limit: Int? = null,
    val products: List<ListData>? = null,
    val skip: Int?= null,
    val total: Int?= null,
)

data class ListData(
    val id: Int? = null,
    val title: String? = null,
    val price: Double? = null,
    val discountPercentage: Double? = null,
    val rating: Double? = null,
    val thumbnail: String? = null,
    val images: List<String>? = null,
    )
