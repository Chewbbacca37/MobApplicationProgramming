package com.example.lab3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Модель даних без картинки
data class TouristPlace(
    val name: String,
    val description: String
)

// Список місць (тільки назва і опис)
val places = listOf(
    TouristPlace("Київ", "Столиця України. Багато церков, Хрещатик та багата історія."),
    TouristPlace("Львів", "Місто кави, шоколаду, старовинних вуличок та площі Ринок."),
    TouristPlace("Одеса", "Перлина біля моря, відома гумором, Оперним театром та Потьомкінськими сходами."),
    TouristPlace("Карпати", "Гори, свіже повітря, Говерла, озеро Синевир та густі ліси.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristListScreen(
    onPlaceClick: (TouristPlace) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Туристичний Гід") }) }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(places) { place ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onPlaceClick(place) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = place.name, style = MaterialTheme.typography.headlineSmall)
                        Text(text = "Натисніть, щоб прочитати опис", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouristDetailScreen(
    name: String,
    description: String
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(name) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.displaySmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}