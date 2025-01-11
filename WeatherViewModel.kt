package eu.tutorials.wheatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.tutorials.wheatherapp.api.Constant
import eu.tutorials.wheatherapp.api.NetworkResponse
import eu.tutorials.wheatherapp.api.Retrofitinstance
import eu.tutorials.wheatherapp.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {

    private val weatherApi = Retrofitinstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()

    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String) {
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.Apikey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                }
            }
            catch (e:Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to load data")
            }
        }
    }
}