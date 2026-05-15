package com.swati.myapplication.repository

import com.swati.myapplication.model.ListApiResponse
import com.swati.myapplication.service.DemoApi
import retrofit2.Response
import javax.inject.Inject

class DemoListRepository @Inject constructor(val demoApi: DemoApi) {

    suspend fun getListData(limit: Int = 20, skip: Int): Response<ListApiResponse> {
        return demoApi.getList(limits = limit, skip = skip)
    }
}