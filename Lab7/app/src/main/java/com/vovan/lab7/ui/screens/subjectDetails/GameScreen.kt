package com.vovan.lab7.ui.screens.subjectDetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen(
    topic: String, // Приймаємо тему
    viewModel: GameScreenViewModel = koinViewModel(),
) {
    val isLoadingState = viewModel.isLoadingStateFlow.collectAsState()
    val textPairListState = viewModel.textPairListStateFlow.collectAsState()

    // Завантажуємо дані при вході на екран
    LaunchedEffect(topic) {
        viewModel.requestGameData(topic)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            when {
                isLoadingState.value -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(60.dp))
                        Text(
                            text = "Generating quiz for: $topic...",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }

                else -> {
                    textPairListState.value?.let { textPairList ->
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // Заголовок теми
                            item {
                                Text(
                                    text = "Topic: $topic",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }

                            items(items = textPairList) { textPair ->
                                val isVisibleText2State = remember { mutableStateOf(false) }
                                Card(
                                    onClick = { isVisibleText2State.value = !isVisibleText2State.value },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = textPair.text1, // Питання
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )

                                        AnimatedVisibility(
                                            visible = isVisibleText2State.value,
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            Text(
                                                text = textPair.text2, // Відповідь
                                                fontSize = 18.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }

                                        if (!isVisibleText2State.value) {
                                            Text(
                                                text = "Tap to see answer",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } ?: Text("Error loading questions. Try again.")
                }
            }
        }
    }
}