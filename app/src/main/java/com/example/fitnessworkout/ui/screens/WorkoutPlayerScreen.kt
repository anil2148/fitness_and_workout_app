package com.example.fitnessworkout.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import kotlinx.coroutines.delay

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun GuidedWorkoutPlayerScreen(vm: FitnessViewModel, back: () -> Unit, finished: () -> Unit) {
    val plan by vm.selectedPlan.collectAsState()
    val exercises by vm.selectedExercises.collectAsState()
    var index by remember(plan?.id) { mutableIntStateOf(0) }
    var remainingSeconds by remember(plan?.id) { mutableIntStateOf(0) }
    var running by remember(plan?.id) { mutableStateOf(false) }
    var showExitWarning by remember { mutableStateOf(false) }
    val exercise = exercises.getOrNull(index)

    LaunchedEffect(exercise?.id) {
        remainingSeconds = exercise?.playerSeconds() ?: 0
    }
    LaunchedEffect(running, remainingSeconds, index, exercises.size) {
        if (running && remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds--
        } else if (running && remainingSeconds == 0 && exercises.isNotEmpty()) {
            if (index < exercises.lastIndex) index++ else running = false
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(plan?.title ?: "Guided workout") },
            navigationIcon = { IconButton({ showExitWarning = true }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
        )
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (exercise == null) {
                Text("Workout exercises are loading. Go back and try again if this message remains visible.")
            } else {
                Text("Exercise ${index + 1} of ${exercises.size}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                LinearProgressIndicator({ (index + 1) / exercises.size.toFloat() }, Modifier.fillMaxWidth())
                Icon(Icons.Default.FitnessCenter, null)
                Text(exercise.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                Text(exercise.repsOrDuration)
                Text(exercise.instruction)
                Text("Safety: ${exercise.safetyTips}", color = MaterialTheme.colorScheme.error)
                Text("$remainingSeconds sec", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button({ running = !running }, Modifier.weight(1f)) {
                        Icon(if (running) Icons.Default.Pause else Icons.Default.PlayArrow, null)
                        Text(if (running) " Pause" else if (remainingSeconds < exercise.playerSeconds()) " Resume" else " Start")
                    }
                    OutlinedButton({
                        running = false
                        if (index < exercises.lastIndex) index++ else {
                            vm.completeSelectedWorkout()
                            finished()
                        }
                    }, Modifier.weight(1f)) { Text(if (index < exercises.lastIndex) "Next" else "Finish") }
                }
                OutlinedButton({
                    running = false
                    vm.completeSelectedWorkout()
                    finished()
                }, Modifier.fillMaxWidth()) { Text("Finish workout") }
            }
        }
    }
    if (showExitWarning) AlertDialog(
        onDismissRequest = { showExitWarning = false },
        title = { Text("Stop this workout?") },
        text = { Text("Stop if you feel pain, dizziness, chest pain, or severe discomfort. Ending now records a skipped workout so future recommendations can adapt.") },
        confirmButton = { TextButton({ vm.skipSelectedWorkout(); showExitWarning = false; back() }) { Text("End workout") } },
        dismissButton = { TextButton({ showExitWarning = false }) { Text("Continue safely") } }
    )
}

private fun Exercise.playerSeconds(): Int = durationSeconds ?: 45
