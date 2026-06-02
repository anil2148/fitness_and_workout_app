package com.example.fitnessworkout.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitnessworkout.ui.components.ExerciseIllustration
import com.example.fitnessworkout.ui.components.ExerciseVideoPlayer
import com.example.fitnessworkout.viewmodel.FitnessViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    vm: FitnessViewModel,
    exerciseId: Int?,
    back: () -> Unit,
    onPremium: () -> Unit,
    onStart: (Int) -> Unit,
) {
    val state by vm.uiState.collectAsState()
    val exercise = state.allExercises.firstOrNull { it.id == exerciseId }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(exercise?.name ?: "Exercise guide") },
                navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (exercise == null) {
                item { Text("This exercise guide is unavailable. Return to the workout and choose another exercise.") }
            } else {
                item { ExerciseIllustration(exercise, Modifier.height(210.dp)) }
                item { ExerciseVideoPlayer(exercise, state.isPremiumUser, onPremium) }
                item {
                    Text("Exercise details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${exercise.muscleGroup} | ${exercise.difficulty} | ${exercise.equipment}")
                    Text("${exercise.sets} sets | ${exercise.repsOrDuration} | ${exercise.restSeconds}s rest | ${exercise.caloriesPerMinute} kcal/min")
                }
                item {
                    Text("Step-by-step", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    exercise.instruction.split(".").filter { it.isNotBlank() }.forEachIndexed { index, step ->
                        Text("${index + 1}. ${step.trim()}.")
                    }
                }
                item {
                    Text("Safety tips", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(exercise.safetyTips)
                }
                item {
                    Text("Common mistakes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(exercise.commonMistakes)
                }
                item {
                    Button({ onStart(exercise.planId) }, Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.PlayArrow, null)
                        Text(" Start exercise")
                    }
                }
            }
        }
    }
}
