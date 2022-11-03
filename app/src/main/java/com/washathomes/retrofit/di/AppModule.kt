package com.washathomes.retrofit.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    fun provideGson(): Gson = GsonBuilder().create()


    @Provides
    @Singleton
    fun provideSharedPreferences(context: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}

