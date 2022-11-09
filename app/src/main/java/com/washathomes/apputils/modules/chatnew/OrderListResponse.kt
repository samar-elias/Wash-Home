package com.washathomes.AppUtils.Modules.chatnew

import com.google.gson.annotations.SerializedName
import com.washathomes.AppUtils.Modules.chatnew.Status

data class OrderListResponse(
    @SerializedName("results")
    val results: List<OrderModel>,
    @SerializedName("status")
    val status: Status
)