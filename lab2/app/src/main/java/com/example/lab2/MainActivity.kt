package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.ui.theme.Lab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
                MainActivityScreen()
            }
        }
    }
}
data class ToDoItem(
    val id: String,
    val title: String,
    val description: String
)

@Composable
fun MainActivityScreen() {
    // Список, який вміє оновлювати екран, коли в нього щось додають або видаляють
    val itemList = remember { mutableStateListOf<ToDoItem>() }

    // Змінні для полів вводу
    var idInput by remember { mutableStateOf("") }
    var titleInput by remember { mutableStateOf("") }
    var descInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Відступи від країв екрану
    ) {
        // Заголовок
        Text(
            text = "Мої завдання",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- 3. Список завдань (LazyColumn) ---
        // weight(1f) означає "займи все доступне місце, залишивши трохи для меню знизу"
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(itemList) { item ->
                // Викликаємо функцію, яка малює красиву картку
                ToDoItemRow(
                    item = item,
                    onDelete = { itemList.remove(item) } // Логіка видалення
                )
            }
        }

        // --- 4. Панель додавання (Ввід даних) ---
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Нове завдання", fontWeight = FontWeight.SemiBold)

                // Поле ID
                OutlinedTextField(
                    value = idInput,
                    onValueChange = { idInput = it },
                    label = { Text("ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Поле Назва
                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("Назва завдання") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Поле Опис
                OutlinedTextField(
                    value = descInput,
                    onValueChange = { descInput = it },
                    label = { Text("Опис") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Кнопка Додати
                Button(
                    onClick = {
                        if (titleInput.isNotEmpty()) {
                            // Додаємо новий елемент у список
                            itemList.add(ToDoItem(idInput, titleInput, descInput))
                            // Очищаємо поля
                            idInput = ""
                            titleInput = ""
                            descInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Додати")
                }
            }
        }
    }
}

// --- 5. Окремий компонент для одного рядка (Card) ---
@Composable
fun ToDoItemRow(item: ToDoItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Ліва частина: Тексти
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ID: ${item.id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Права частина: Кнопка видалення
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Видалити",
                    tint = Color.Red
                )
            }
        }
    }
}