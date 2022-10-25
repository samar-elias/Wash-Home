package com.washathomes.Views.Splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.washathomes.AppUtils.AppDefs.AppDefs
import com.washathomes.AppUtils.AppDefs.Urls
import com.washathomes.AppUtils.Helpers.LocaleHelper
import com.washathomes.AppUtils.Remote.RetrofitAPIs
import com.washathomes.AppUtils.Modules.DataList
import com.washathomes.AppUtils.Modules.UserData
import com.washathomes.R
import com.washathomes.Views.Introduction.IntroductionActivity
import com.washathomes.Views.Main.Courier.CourierMainActivity
import com.washathomes.Views.Main.Washee.WasheeMainActivity
import com.washathomes.Views.Main.Washer.WasherMainActivity
import com.washathomes.databinding.ActivitySplashBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = R.layout.activity_splash
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        getAppData()
    }

    private fun getAppData(){
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(Urls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val dataCall: Call<DataList> =
            retrofit.create(RetrofitAPIs::class.java).getAppData()
        dataCall.enqueue(object : Callback<DataList>{
            override fun onResponse(call: Call<DataList>, response: Response<DataList>) {
                if (response.isSuccessful){
                    Glide.with(applicationContext).load(response.body()?.results?.get(1)?.image).into(binding.logo)
                    AppDefs.splashBackground = response.body()?.results?.get(0)?.image
                    AppDefs.blueLogo = response.body()?.results?.get(1)?.image
                    AppDefs.whiteLogo = response.body()?.results?.get(2)?.image
                    AppDefs.background = response.body()?.results?.get(3)?.image
                    if (!response.body()?.results?.get(0)?.image?.isEmpty()!!){
                        binding.videoView.visibility = View.GONE
                        binding.splashBackground.visibility = View.VISIBLE
                        Glide.with(applicationContext).load(response.body()?.results?.get(0)?.image).into(binding.splashBackground)
                        setSplash(2000)
                    }else{
                        binding.videoView.visibility = View.VISIBLE
                        binding.splashBackground.visibility = View.GONE
                        setVideo()
                    }
                }
            }

            override fun onFailure(call: Call<DataList>, t: Throwable) {
            }

        })

    }

    private fun setVideo(){
        try {
            val video: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash)
            binding.videoView.setVideoURI(video)
            binding.videoView.setOnCompletionListener { setSplash(0) }
            binding.videoView.start()
        } catch (ex: Exception) {
            setSplash(0)
        }
    }

    private fun setSplash(duration: Long) {

        Handler(Looper.myLooper()!!).postDelayed({
            getUserFromSharedPreferences() }, duration)

    }

    private fun getUserFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences(AppDefs.SHARED_PREF_KEY, MODE_PRIVATE)
        val id = sharedPreferences.getString(AppDefs.ID_KEY, null)
        val type = sharedPreferences.getString(AppDefs.TYPE_KEY, null)
        val gson = Gson()
        val user = sharedPreferences.getString(AppDefs.USER_KEY, null)
        when {
            id != null && user != null-> {
                AppDefs.user = gson.fromJson(user, UserData::class.java)
                LocaleHelper.setAppLocale(AppDefs.user.results!!.language_code, this)
                if (type == "1"){
                    val mainIntent = Intent(this, WasheeMainActivity::class.java)
                    startActivity(mainIntent)
                }else if (type == "2"){
                    val mainIntent = Intent(this, WasherMainActivity::class.java)
                    startActivity(mainIntent)
                }else if (type == "3"){
                    val mainIntent = Intent(this, CourierMainActivity::class.java)
                    startActivity(mainIntent)
                }
                finish()
            }
            else -> {
                val registrationIntent = Intent(this, IntroductionActivity::class.java)
                startActivity(registrationIntent)
                finish()
            }
        }
    }
}