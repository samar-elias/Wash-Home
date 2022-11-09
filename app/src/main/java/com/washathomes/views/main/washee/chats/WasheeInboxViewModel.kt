package com.washathomes.views.main.washee.chats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.washathomes.apputils.base.BaseViewModel

import com.washathomes.AppUtils.Modules.Chatmodel.ChatRoom
import com.washathomes.AppUtils.Modules.chatnew.OrderListResponse
import com.washathomes.AppUtils.Modules.chatnew.OrderModel
import com.washathomes.retrofit.Resource


import com.washathomes.retrofit.remote.APIManager
import dagger.hilt.android.lifecycle.HiltViewModel


import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class WasheeInboxViewModel @Inject constructor(private val apiManager: APIManager) :
    BaseViewModel() {

    var chatList: ArrayList<ChatRoom> = ArrayList()
    var orders: List<OrderModel> = ArrayList()

    val getBuyerOrderChatStatus = MutableLiveData<Resource<OrderListResponse>?>()
  /*  val getSellerOrdersChatStatus = MutableLiveData<Resource<OrderListResponse>?>()
    val getDriverOrdersChatStatus = MutableLiveData<Resource<OrderListResponse>?>()*/
    val database = FirebaseDatabase.getInstance()
    val myRef = database.reference
    fun getBuyerOrdersChat(token: String) {
        viewModelScope.launch {
            val response = apiManager.getBuyerOrdersChat(token)
            getBuyerOrderChatStatus.postValue(response)
        }
    }

    fun getSellerOrdersChat(token: String) {
        viewModelScope.launch {
            val response = apiManager.getSellerOrdersChat(token)
            getBuyerOrderChatStatus.postValue(response)
        }
    }
    fun getDriverOrdersChat(token: String) {
        viewModelScope.launch {
            val response = apiManager.getDriverOrdersChat(token)
            getBuyerOrderChatStatus.postValue(response)
        }
    }





}