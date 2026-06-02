package com.example.fitnessworkout.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.ui.components.ExerciseIllustration
import com.example.fitnessworkout.ui.components.ExerciseVideoPlayer
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import kotlinx.coroutines.delay

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun GuidedWorkoutPlayerScreen(vm: FitnessViewModel, back: () -> Unit, onPremium: () -> Unit, finished: () -> Unit) {
    val plan by vm.selectedPlan.collectAsState()
    val exercises by vm.selectedExercises.collectAsState()
    val state by vm.uiState.collectAsState()
    var index by remember(plan?.id) { mutableIntStateOf(0) }
    var remainingSeconds by remember(plan?.id) { mutableIntStateOf(0) }
    var running by remember(plan?.id) { mutableStateOf(false) }
    var resting by remember(plan?.id) { mutableStateOf(false) }
    var showExitWarning by remember { mutableStateOf(false) }
    val exercise = exercises.getOrNull(index)

    LaunchedEffect(exercise?.id) {
        remainingSeconds = exercise?.playerSeconds() ?: 0
    }
    LaunchedEffect(running, remainingSeconds, index, exercises.size, resting) {
        if (running && remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds--
        } else if (running && remainingSeconds == 0 && exercises.isNotEmpty()) {
            if (resting) {
                resting = false
                if (index < exercises.lastIndex) index++ else running = false
            } else if (index < exercises.lastIndex) {
                resting = true
                remainingSeconds = exercise?.restSeconds?.coerceAtLeast(0) ?: 0
            } else {
                running = false
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(plan?.title ?: stringResource(R.string.guided_workout)) },
            navigationIcon = { IconButton({ showExitWarning = true }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) } }
        )
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (exercise == null) {
                Text(stringResource(R.string.loading_exercises))
            } else {
                Text(stringResource(R.string.exercise_of, index + 1, exercises.size), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                LinearProgressIndicator({ (index + 1) / exercises.size.toFloat() }, Modifier.fillMaxWidth())
                ExerciseIllustration(exercise, Modifier.fillMaxWidth().height(190.dp))
                ExerciseVideoPlayer(exercise, state.isPremiumUser, onPremium)
                Text(if (resting) stringResource(R.string.rest) else exercise.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                if (resting) {
                    Text(stringResource(R.string.rest_help))
                } else {
                    Text(exercise.repsOrDuration)
                    Text(exercise.instruction)
                    Text(stringResource(R.string.safety_value, exercise.safetyTips), color = MaterialTheme.colorScheme.error)
                }
                Text(stringResource(R.string.seconds_value, remainingSeconds), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button({ running = !running }, Modifier.weight(1f)) {
                        Icon(if (running) Icons.Default.Pause else Icons.Default.PlayArrow, null)
                        Text(" " + if (running) stringResource(R.string.pause) else if (remainingSeconds < exercise.playerSeconds()) stringResource(R.string.resume) else stringResource(R.string.start))
                    }
                    OutlinedButton({
                        running = false
                        resting = false
                        if (index < exercises.lastIndex) index++ else {
                            vm.completeSelectedWorkout()
                            finished()
                        }
                    }, Modifier.weight(1f)) { Text(if (index < exercises.lastIndex) stringResource(R.string.next) else stringResource(R.string.finish)) }
                }
                OutlinedButton({
                    running = false
                    resting = false
                    if (index < exercises.lastIndex) index++ else {
                        vm.completeSelectedWorkout()
                        finished()
                    }
                }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.skip_exercise)) }
                OutlinedButton({
                    running = false
                    vm.completeSelectedWorkout()
                    finished()
                }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.finish_workout)) }
                exercises.getOrNull(index + 1)?.let { next ->
                    Text(stringResource(R.string.next_exercise), fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        ExerciseIllustration(next, Modifier.size(width = 92.dp, height = 58.dp))
                        Text(next.name)
                    }
                }
            }
        }
    }
    if (showExitWarning) AlertDialog(
        onDismissRequest = { showExitWarning = false },
        title = { Text(stringResource(R.string.stop_question)) },
        text = { Text(stringResource(R.string.stop_message)) },
        confirmButton = { TextButton({ vm.skipSelectedWorkout(); showExitWarning = false; back() }) { Text(stringResource(R.string.end_workout)) } },
        dismissButton = { TextButton({ showExitWarning = false }) { Text(stringResource(R.string.continue_safely)) } }
    )
}

private fun Exercise.playerSeconds(): Int = durationSeconds ?: 45
