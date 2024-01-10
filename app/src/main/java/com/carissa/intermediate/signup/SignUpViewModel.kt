package com.carissa.intermediate.signup

import androidx.lifecycle.ViewModel
import com.carissa.intermediate.data.response.RegisterResponse
import com.carissa.intermediate.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()

    fun register(
        name: String,
        email: String,
        password: String,
        onRegistrationResult: (Boolean) -> Unit
    ) {
        apiService.register(name, email, password).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    onRegistrationResult(true)
                } else {
                    onRegistrationResult(false)
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                onRegistrationResult(false)
            }
        })
    }
}