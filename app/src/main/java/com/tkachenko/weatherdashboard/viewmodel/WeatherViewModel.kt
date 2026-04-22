package com.tkachenko.weatherdashboard.viewmodel

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.tkachenko.weatherdashboard.data.WeatherData
import com.tkachenko.weatherdashboard.data.WeatherRepository
import kotlinx.coroutines.async

class WeatherViewModel : ViewModel(){
    private val repository = WeatherRepository()
    private val _weatherState = MutableStateFlow(WeatherData())
    val weatherState: StateFlow<WeatherData> = _weatherState.asStateFlow()
    init{
        loadWeatherData()
    }

    fun loadWeatherData() {
        viewModelScope.launch {
            _weatherState.value = _weatherState.value.copy(
                isLoading = true,
                error = null
            )
            try{
                val temperatureDeferrred = async { repository.fetchTemperature() }
                val humidityDeferrred  =async { repository.fetchHumidity()  }
                val windSpeedDeferrred  = async { repository.fetchWindSpeed() }
                val temperature = temperatureDeferrred.await()
                val humidity = temperatureDeferrred.await()
                val windSpeed = temperatureDeferrred.await()
                _weatherState.value = WeatherData(
                    temperature = temperature,
                    humidity = humidity,
                    windSpeed = windSpeed,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception){
                _weatherState.value = _weatherState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки: ${e.message}"
                )
            }

        }
    }
}
