package com.washathomes.Views.Registration.WasherRegistration

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.washathomes.R
import java.io.ByteArrayOutputStream


class WasherRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washer_registration)
    }

    fun convertToBase64(bitmap: Bitmap): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}