package com.carissa.intermediate.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carissa.intermediate.data.preference.UserPreference
import com.carissa.intermediate.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiConfig.getApiService()
    private val userPreference by lazy {
        UserPreference(application)
    }

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun userLogin(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = apiService.login(email, password)
                if (response.isSuccessful) {
                    val token = response.body()?.loginResult?.token

                    if (token != null) {
                        userPreference.saveUserToken(token)
                        _loginResult.value = true
                    } else {
                        _error.value = "Login Failed"
                    }
                } else {
                    _error.value = "Login Failed"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }
}