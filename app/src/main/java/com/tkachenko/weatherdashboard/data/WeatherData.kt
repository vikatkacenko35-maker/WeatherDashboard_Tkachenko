package com.tkachenko.weatherdashboard.data

data class WeatherData(
    val temperature: Int? = null,
    val hymidity: Int? = null,
    val windSpeed: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)