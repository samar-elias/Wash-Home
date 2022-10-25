package com.washathomes.Views.Main.Washee.Orders.Confirmed

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.washathomes.AppUtils.AppDefs.AppDefs
import com.washathomes.AppUtils.AppDefs.Urls
import com.washathomes.AppUtils.Modules.BooleanResponse
import com.washathomes.AppUtils.Modules.ChatUser
import com.washathomes.AppUtils.Modules.ErrorResponse
import com.washathomes.AppUtils.Modules.OrderObj
import com.washathomes.AppUtils.Remote.RetrofitAPIs
import com.washathomes.R
import com.washathomes.Views.Main.Washee.WasheeMainActivity
import com.washathomes.databinding.FragmentOrderConfirmedBinding
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.String
import java.util.*

class OrderConfirmedFragment : Fragment() {

    lateinit var binding: FragmentOrderConfirmedBinding
    lateinit var washeeMainActivity: WasheeMainActivity
    lateinit var navController: NavController
    lateinit var firebaseUser: FirebaseUser
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_order_confirmed, container, false)
        binding = FragmentOrderConfirmedBinding.inflate(layoutInflater)
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
        setData()
    }

    private fun initViews(view: View){
        navController = Navigation.findNavController(view)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference(AppDefs.USER_PATH).child(firebaseUser.uid)
    }

    private fun onClick(){
        binding.toolbarBackIcon.setOnClickListener { navController.popBackStack() }
        binding.directionsBtn.setOnClickListener { openGoogleMaps() }
        binding.directionsActionBtn.setOnClickListener { droppedOffOrderPopUp() }
        binding.messageBtn.setOnClickListener { sendMessage() }
    }

    private fun setData(){
        binding.orderHeader.text = resources.getString(R.string.order) + "#" + AppDefs.washeeActiveOrder.id
        binding.orderConfirmedDate.text = resources.getString(R.string.order_confirmed_on)+" "+AppDefs.washeeActiveOrder.time+" "+AppDefs.washeeActiveOrder.date
        if (AppDefs.washeeActiveOrder.is_delivery_pickup == "0"){
            binding.headerSubDescription.visibility = View.VISIBLE
            binding.driverLabel.visibility = View.GONE
            binding.driverName.visibility = View.GONE
            binding.directionsBtn.visibility = View.VISIBLE
            binding.directionsActionBtn.visibility = View.VISIBLE
        }else{
            binding.headerSubDescription.visibility = View.GONE
//            binding.driverLabel.visibility = View.VISIBLE
//            binding.driverName.visibility = View.VISIBLE
            binding.directionsBtn.visibility = View.GONE
            binding.directionsActionBtn.visibility = View.GONE
        }
        binding.pickupDate.text = AppDefs.washeeActiveOrder.pickup_date
        binding.pickupTime.text = AppDefs.washeeActiveOrder.pickup_time
    }

    private fun openGoogleMaps(){
        val uri = String.format(Locale.ENGLISH, "geo:%f,%f", AppDefs.washeeActiveOrder.lat.toFloat(), AppDefs.washeeActiveOrder.long.toFloat())
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private fun droppedOffOrderPopUp(){
        val alertView: View =
            LayoutInflater.from(context).inflate(R.layout.dropped_off_order_to_washer_popup, null)
        val alertBuilder = AlertDialog.Builder(context).setView(alertView).show()
        alertBuilder.show()
        alertBuilder.setCancelable(false)

        alertBuilder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val cancel: TextView = alertView.findViewById(R.id.cancel)
        val droppedOff: TextView = alertView.findViewById(R.id.dropped_off)

        cancel.setOnClickListener { alertBuilder.dismiss() }
        droppedOff.setOnClickListener {
            droppedOffOrder()
            alertBuilder.dismiss()
        }
    }

    private fun droppedOffOrder(){
        binding.progressBar.visibility = View.VISIBLE
        val orderObj = OrderObj(AppDefs.washeeActiveOrder.id)
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
        val ordersCall: Call<BooleanResponse> =
            retrofit.create(RetrofitAPIs::class.java).washeeDropOffOrder(orderObj)
        ordersCall.enqueue(object : Callback<BooleanResponse> {
            override fun onResponse(call: Call<BooleanResponse>, response: Response<BooleanResponse>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful){
                    Toast.makeText(washeeMainActivity, resources.getString(R.string.order_dropped_off), Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }else{
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type //ErrorResponse is the data class that matches the error response
                    val errorResponse = gson.fromJson<ErrorResponse>(response.errorBody()!!.charStream(), type) // errorResponse is an instance of ErrorResponse that will contain details about the error
                    Toast.makeText(washeeMainActivity, errorResponse.status.massage.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BooleanResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(washeeMainActivity, resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun sendMessage(){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(ChatUser:: class.java)
                navController.navigate(OrderConfirmedFragmentDirections.actionOrderConfirmedFragmentToWasheeChatFragment2(AppDefs.user.results!!.fcm_token!!))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}