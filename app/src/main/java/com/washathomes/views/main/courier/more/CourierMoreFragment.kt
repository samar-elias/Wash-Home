
    private fun getNotifications(){
        notifications.clear()
        val userTypeObj = UserTypeObj("2")
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
        val notificationsCall: Call<Notifications> =
            retrofit.create(RetrofitAPIs::class.java).getNotifications(userTypeObj)
        notificationsCall.enqueue(object : Callback<Notifications> {
            override fun onResponse(call: Call<Notifications>, response: Response<Notifications>) {
                if (response.isSuccessful){
                    notifications = response.body()!!.results.notifications
                    checkNewNotifications()
                }else{
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type //ErrorResponse is the data class that matches the error response
                    val errorResponse = gson.fromJson<ErrorResponse>(response.errorBody()!!.charStream(), type) // errorResponse is an instance of ErrorResponse that will contain details about the error
                    Toast.makeText(courierMainActivity, errorResponse.status.massage.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Notifications>, t: Throwable) {
                Toast.makeText(courierMainActivity, resources.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun checkNewNotifications(){
        var counter = 0
        for (notification in notifications){
            if (notification.is_read == "0"){
                counter++
            }
        }
        if (counter>0){
            binding.toolbarLayout.toolbarNotifyBadge.visibility = View.VISIBLE
            binding.toolbarLayout.toolbarNotifyBadge.text = counter.toString()
        }else{
            binding.toolbarLayout.toolbarNotifyBadge.visibility = View.GONE
        }
    }

}