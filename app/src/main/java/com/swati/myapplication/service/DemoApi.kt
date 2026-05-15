package com.swati.myapplication.service

import com.swati.myapplication.model.ListApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DemoApi {
    @GET("/products")
    suspend fun getList(
        @Query("limit") limits : Int,
        @Query("skip") skip: Int
    ) : Response<ListApiResponse>
}