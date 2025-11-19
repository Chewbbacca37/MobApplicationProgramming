package com.example.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = ListDest
                ) {
                    // Екран 1: Список
                    composable<ListDest> {
                        TouristListScreen(
                            onPlaceClick = { place ->
                                // Передаємо тільки назву та опис
                                navController.navigate(
                                    DetailDest(
                                        name = place.name,
                                        description = place.description
                                    )
                                )
                            }
                        )
                    }

                    // Екран 2: Деталі
                    composable<DetailDest> { backStackEntry ->
                        val args = backStackEntry.toRoute<DetailDest>()

                        TouristDetailScreen(
                            name = args.name,
                            description = args.description
                        )
                    }
                }
            }
        }
    }
}