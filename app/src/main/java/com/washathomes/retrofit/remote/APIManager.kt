package com.washathomes.retrofit.remote

import com.washathomes.retrofit.BaseDataSource

import javax.inject.Inject

class APIManager @Inject constructor(
    private val mApi: WashAPI,

    ) : BaseDataSource() {


    suspend fun getBuyerOrdersChat(token: String) = getResult {

        mApi.getBuyerOrdersChat(token)
    }

    suspend fun getSellerOrdersChat(token: String) = getResult {
        mApi.getSellerOrdersChat(token)
    }
    suspend fun getDriverOrdersChat(token: String) = getResult {

        mApi.getDriverOrdersChat(token)
    }


}