package com.example.fitnessworkout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessworkout.data.local.FitnessDatabase
import com.example.fitnessworkout.repository.FitnessRepository
import com.example.fitnessworkout.ui.navigation.FitnessApp
import com.example.fitnessworkout.ui.theme.FitnessTheme
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import com.example.fitnessworkout.viewmodel.FitnessViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FitnessDatabase.getInstance(applicationContext)
        val repository = FitnessRepository(database.fitnessDao())
        setContent {
            FitnessTheme {
                val fitnessViewModel: FitnessViewModel = viewModel(
                    factory = FitnessViewModelFactory(repository)
                )
                FitnessApp(fitnessViewModel)
            }
        }
    }
}
