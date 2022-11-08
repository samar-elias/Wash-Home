package com.washathomes.AppUtils.AppDefs

import com.google.firebase.auth.FirebaseUser
import com.washathomes.AppUtils.Modules.*
import java.util.*
import kotlin.collections.ArrayList

public class AppDefs {
    companion object{

        var SHARED_PREF_KEY = "SHARED_PREF"
        var ID_KEY = "ID"
        var USER_KEY = "USER"
        var TYPE_KEY = "TYPE"
        var lang: String? = "en"
        const val INBOX_PATH = "chatroom"
        const val USER_PATH = "users"
        val languages: ArrayList<Language?> = ArrayList()
        var splashBackground: String? = ""
        var blueLogo: String? = ""
        var whiteLogo: String? = ""
        var background: String? = ""
        lateinit var user: UserData
        var subTotal = ""
        lateinit var firebaseUser: FirebaseUser
        lateinit var deliveryInfo: DeliveryInfoObj
        lateinit var deliveryInfoPrices: ArrayList<DeliveryInfo>
        lateinit var cartData: CartData
        lateinit var chatUser: ChatUser
        lateinit var order: Order
        lateinit var orderHistory: OrderHistoryObject
        lateinit var activeOrder: ActiveOrderObj
        lateinit var pendingOrder: PendingOrderObj
        lateinit var washeeActiveOrder: WasheeActiveOrder
        var times: ArrayList<String> = ArrayList()



        @JvmName("getLanguage1")
        fun getLanguage(): String? {
            return if (Locale.getDefault().displayLanguage == "Spanish") {
                "es"
            } else {
                "en"
            }
        }
    }

}