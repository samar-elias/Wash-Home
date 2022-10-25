package com.washathomes.Views.Main.Courier.More.Profile.Language

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.washathomes.AppUtils.AppDefs.AppDefs
import com.washathomes.AppUtils.AppDefs.Urls
import com.washathomes.AppUtils.Helpers.LocaleHelper
import com.washathomes.AppUtils.Modules.Languages
import com.washathomes.AppUtils.Modules.UpdateLanguage
import com.washathomes.AppUtils.Modules.UserData
import com.washathomes.AppUtils.Remote.RetrofitAPIs
import com.washathomes.R
import com.washathomes.Views.Introduction.LanguagesAdapter
import com.washathomes.Views.Main.Courier.CourierMainActivity
import com.washathomes.Views.Splash.SplashActivity
import com.washathomes.databinding.FragmentCourierSwitchLanguageBinding
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CourierSwitchLanguageFragment : Fragment() {

    lateinit var binding: FragmentCourierSwitchLanguageBinding
    lateinit var courierMainActivity: CourierMainActivity
    lateinit var navController: NavController
    var lang = ""
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_courier_switch_language, container, false)
        binding = FragmentCourierSwitchLanguageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CourierMainActivity) {
            courierMainActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        getLanguages()
        onClick()
    }

    private fun initViews(view: View){
        navController = Navigation.findNavController(view)
    }

    private fun onClick(){
        binding.toolbarBackIcon.setOnClickListener {  navController.popBackStack()}
        binding.languagesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 1) {
                    lang = "es"
                } else if (position == 0){
                    lang = "en"
                }
            }

        }
        binding.nextBtn.setOnClickListener { updateLanguage() }
    }

    private fun updateLanguage(){
        binding.progressBar.visibility = View.VISIBLE
        val language = UpdateLanguage(lang)
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
        val languagesCall: Call<UserData> =
            retrofit.create(RetrofitAPIs::class.java).updateLanguage(language)
        languagesCall.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful){
                    AppDefs.user = response.body()!!
                    LocaleHelper.setAppLocale(lang, courierMainActivity)
                    saveUserToSharedPreferences()

                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun saveUserToSharedPreferences() {
        val sharedPreferences =
            courierMainActivity.getSharedPreferences(AppDefs.SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(AppDefs.ID_KEY, AppDefs.user.results!!.id)

        val gson = Gson()
        val json = gson.toJson(AppDefs.user)
        editor.putString(AppDefs.USER_KEY, json)
        editor.putString(AppDefs.TYPE_KEY, "2")
        editor.apply()
        val mainIntent = Intent(courierMainActivity, SplashActivity::class.java)
        startActivity(mainIntent)
        courierMainActivity.finish()
    }

    private fun getLanguages(){
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(Urls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val languagesCall: Call<Languages> =
            retrofit.create(RetrofitAPIs::class.java).getLanguages()
        languagesCall.enqueue(object : Callback<Languages> {
            override fun onResponse(call: Call<Languages>, response: Response<Languages>) {
                if (response.isSuccessful){
                    for (i in response.body()?.results!!){
                        AppDefs.languages.add(i)
                    }
                    setSpinner()
                }
            }

            override fun onFailure(call: Call<Languages>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setSpinner() {
        val adapter = LanguagesAdapter(courierMainActivity, AppDefs.languages)
        binding.languagesSpinner.adapter = adapter

        if (AppDefs.lang == "es"){
            binding.languagesSpinner.setSelection(1)
            lang = "es"
        }else{
            binding.languagesSpinner.setSelection(0)
            lang = "en"
        }
    }

}