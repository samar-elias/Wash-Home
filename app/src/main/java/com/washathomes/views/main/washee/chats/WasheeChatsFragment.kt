package com.washathomes.views.main.washee.chats

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.washathomes.apputils.appdefs.AppDefs
import com.washathomes.apputils.appdefs.Urls
import com.washathomes.apputils.modules.ErrorResponse
import com.washathomes.apputils.modules.Inbox
import com.washathomes.apputils.modules.InboxMessages
import com.washathomes.apputils.remote.RetrofitAPIs
import com.washathomes.R
import com.washathomes.views.main.washee.WasheeMainActivity
import com.washathomes.databinding.FragmentWasheeChatsBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class WasheeChatsFragment : Fragment() {

    lateinit var binding: FragmentWasheeChatsBinding
    lateinit var washeeMainActivity: WasheeMainActivity
    lateinit var navController: NavController
    var inboxMessages: ArrayList<Inbox> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_washee_chats, container, false)
        binding = FragmentWasheeChatsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is WasheeMainActivity) {
            washeeMainActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        onClick()
        getInbox()
    }

    private fun initViews(view: View){
        navController = Navigation.findNavController(view)

    }

    private fun onClick(){
        binding.toolbarLayout.clLeft.setOnClickListener { navController.navigate(
            WasheeChatsFragmentDirections.actionWasheeChatsFragmentToWasheeNotificationsFragment()) }
        binding.toolbarLayout.clRight.setOnClickListener { navController.navigate(
            WasheeChatsFragmentDirections.actionWasheeChatsFragmentToBasketFragment()) }
    }

    private fun getInbox(){
        binding.progressBar.visibility = View.VISIBLE
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("Content-Type", "application/json; charset=UTF-8")
                    builder.header("Authorization", AppDefs.user.token!!)
                    return@Interceptor chain.proceed(builder.build())
                }
            )
        }.build()
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(Urls.BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val languagesCall: Call<InboxMessages> =
            retrofit.create(RetrofitAPIs::class.java).getWasheeInbox()
        languagesCall.enqueue(object : Callback<InboxMessages> {
            override fun onResponse(call: Call<InboxMessages>, response: Response<InboxMessages>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body()!!.results != null) {
                        inboxMessages = response.body()!!.results
                    }
                    setInboxRV()
                }else{
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type //ErrorResponse is the data class that matches the error response
                    val errorResponse = gson.fromJson<ErrorResponse>(response.errorBody()!!.charStream(), type) // errorResponse is an instance of ErrorResponse that will contain details about the error
                    Toast.makeText(
                        washeeMainActivity,
                        errorResponse.status.massage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<InboxMessages>, t: Throwable) {
                Toast.makeText(washeeMainActivity, resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setInboxRV(){
        if (inboxMessages.size == 0){
            binding.noInboxLayout.visibility = View.VISIBLE
        }else{
            binding.noInboxLayout.visibility = View.GONE
        }
        val adapter = WasheeInboxAdapter(this, inboxMessages)
        binding.inboxList.adapter = adapter
        binding.inboxList.layoutManager = LinearLayoutManager(washeeMainActivity)
    }

}