package com.example.lab5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyApp()
        }
    }
}

@Composable
fun StudyApp() {
    val navController = rememberNavController()
    val viewModel: StudyViewModel = koinViewModel()

    NavHost(navController = navController, startDestination = "subjects") {
        composable("subjects") {
            SubjectsScreen(
                viewModel = viewModel,
                onSubjectClick = { subId -> navController.navigate("labs/$subId") }
            )
        }
        composable("labs/{subId}") { entry ->
            val subId = entry.arguments?.getString("subId")?.toIntOrNull() ?: 0
            LabsScreen(viewModel, subId)
        }
    }
}

// --- ЕКРАН ПРЕДМЕТІВ ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(viewModel: StudyViewModel, onSubjectClick: (Int) -> Unit) {
    val subjects by viewModel.subjects.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        SimpleAddDialog(
            title = "Новий предмет",
            onDismiss = { showAddDialog = false },
            onSave = { name ->
                viewModel.addSubject(name)
                showAddDialog = false
            }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Мої Предмети") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(subjects) { subject ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onSubjectClick(subject.id) },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = subject.name, style = MaterialTheme.typography.headlineMedium)
                        IconButton(onClick = { viewModel.deleteSubject(subject) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// --- ЕКРАН ЛАБОРАТОРНИХ ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabsScreen(viewModel: StudyViewModel, subjectId: Int) {
    val labs by viewModel.getLabs(subjectId).collectAsState(initial = emptyList())
    var selectedLab by remember { mutableStateOf<LabWork?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    if (selectedLab != null) {
        EditLabDialog(
            lab = selectedLab!!,
            onDismiss = { selectedLab = null },
            onSave = { updatedLab ->
                viewModel.updateLab(updatedLab)
                selectedLab = null
            }
        )
    }

    if (showAddDialog) {
        SimpleAddDialog(
            title = "Нова лаба",
            onDismiss = { showAddDialog = false },
            onSave = { name ->
                viewModel.addLab(subjectId, name)
                showAddDialog = false
            }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Лабораторні роботи") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(labs) { lab ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { selectedLab = lab },
                    colors = CardDefaults.cardColors(
                        containerColor = if (lab.status == "Здано") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = lab.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Статус: ${lab.status}")
                            if (lab.comment.isNotEmpty()) Text(text = "Комент: ${lab.comment}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { viewModel.deleteLab(lab) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// --- ДІАЛОГИ ---
@Composable
fun EditLabDialog(lab: LabWork, onDismiss: () -> Unit, onSave: (LabWork) -> Unit) {
    var status by remember { mutableStateOf(lab.status) }
    var comment by remember { mutableStateOf(lab.comment) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Редагувати: ${lab.name}") },
        text = {
            Column {
                OutlinedTextField(value = status, onValueChange = { status = it }, label = { Text("Статус") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = comment, onValueChange = { comment = it }, label = { Text("Коментар") })
            }
        },
        confirmButton = { Button(onClick = { onSave(lab.copy(status = status, comment = comment)) }) { Text("Зберегти") } },
        dismissButton = { Button(onClick = onDismiss) { Text("Скасувати") } }
    )
}

@Composable
fun SimpleAddDialog(title: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Назва") }) },
        confirmButton = { Button(onClick = { if (text.isNotBlank()) onSave(text) }) { Text("Додати") } },
        dismissButton = { Button(onClick = onDismiss) { Text("Скасувати") } }
    )
}