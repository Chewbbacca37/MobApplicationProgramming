package com.example.lab3

import kotlinx.serialization.Serializable

//описуємо наші адреси екранів

@Serializable // Це каже програмі: "Цей клас можна перетворити в набір байтів/JSON, щоб передати між екранами".
object ListDest // Екран списку, ми використовуємо object,
                // бо цей екран не приймає жодних даних. Це просто "адреса".

@Serializable
data class DetailDest(
    val name: String,
    val description: String,
) // Екран деталей, клас даних data class, який зберігає аргументи, що передаються на другий екран.