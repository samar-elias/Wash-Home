package com.washathomes.AppUtils.Modules.chatnew

import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("massage")
    val massage: String?
)