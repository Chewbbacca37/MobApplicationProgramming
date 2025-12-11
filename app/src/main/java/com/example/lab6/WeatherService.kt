package com.example.lab6

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

// --- 1. Моделі даних (Data Classes) ---

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City
)

data class City(
    val name: String,
    val country: String
)

data class ForecastItem(
    @SerializedName("dt_txt") val dateText: String,
    val main: MainStats,
    val weather: List<WeatherDescription>
)

data class MainStats(
    val temp: Double,
    val humidity: Int
)

data class WeatherDescription(
    val description: String
)

// --- 2. API Інтерфейс ---

interface WeatherApi {
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ua"
    ): Response<ForecastResponse>
}

// --- 3. Об'єкт для створення підключення ---

object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/"

    val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}