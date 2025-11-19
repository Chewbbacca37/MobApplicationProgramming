package com.example.lab3

import kotlinx.serialization.Serializable

//описуємо наші адреси екранів

@Serializable
object ListDest // Екран списку

@Serializable
data class DetailDest(
    val name: String,
    val description: String,
) // Екран деталей