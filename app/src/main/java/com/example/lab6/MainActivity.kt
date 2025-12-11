package com.example.lab6

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.ui.theme.Lab6Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherScreen() {
    // --- Змінні стану (пам'ять екрану) ---
    var city by remember { mutableStateOf("") }
    var weatherList by remember { mutableStateOf<List<ForecastItem>>(emptyList()) }
    var cityNameDisplay by remember { mutableStateOf("") }

    // Для запуску асинхронних запитів
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Твій ключ API
    val apiKey = "e81f4bb4bcc7912af0585b31fe1a1f9b"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Прогноз Погоди",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Поле вводу
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Введіть місто (напр. Kyiv)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка
        Button(
            onClick = {
                if (city.isNotEmpty()) {
                    scope.launch {
                        try {
                            val response = RetrofitClient.api.getForecast(city, apiKey)
                            if (response.isSuccessful && response.body() != null) {
                                val data = response.body()!!
                                cityNameDisplay = "Погода у: ${data.city.name}, ${data.city.country}"
                                weatherList = data.list
                            } else {
                                Toast.makeText(context, "Помилка! Код: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Помилка мережі: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Отримати прогноз")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Заголовок результату
        if (cityNameDisplay.isNotEmpty()) {
            Text(
                text = cityNameDisplay,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Список погоди
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(weatherList) { item ->
                WeatherItemCard(item)
            }
        }
    }
}

// Окрема картка для кожного елементу погоди
@Composable
fun WeatherItemCard(item: ForecastItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "📅 ${item.dateText}", fontWeight = FontWeight.Bold)
            Text(text = "🌡 Температура: ${item.main.temp}°C")
            Text(text = "☁ ${item.weather.firstOrNull()?.description ?: ""}")
            Text(text = "💧 Вологість: ${item.main.humidity}%")
        }
    }
}