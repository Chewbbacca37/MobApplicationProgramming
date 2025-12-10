package com.example.lab5

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Створюємо базу даних
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "study_db_lab5" // Нова назва бази
        ).build()
    }

    // Створюємо DAO
    single {
        get<AppDatabase>().dao()
    }

    // Створюємо ViewModel
    viewModel {
        StudyViewModel(get())
    }
}