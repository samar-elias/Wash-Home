package com.washathomes.AppUtils.Modules

data class Status(val massage: String?, val code: String?)

data class ErrorResponse(val status: Status)

data class BooleanResponse(val status: Status, val results: Boolean)

data class ErrorResponse2(val status: Status2)

data class Status2(val message: String?, val code: String?)
