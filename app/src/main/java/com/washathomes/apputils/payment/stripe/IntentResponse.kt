package com.washathomes.AppUtils.Payment.stripe

import com.washathomes.model.stripe.IntentData

/**
 * Created on 2019-11-07.
 */
data class IntentResponse(
    val messages: List<String>,
    val response: IntentData
)