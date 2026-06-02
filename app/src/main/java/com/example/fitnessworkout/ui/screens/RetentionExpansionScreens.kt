package com.example.fitnessworkout.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitnessworkout.data.model.DailyHabit
import com.example.fitnessworkout.ui.components.WorkoutPlanCard
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import java.time.Instant
import java.time.ZoneId

@Composable
fun DailyHabitScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    var habit by remember(state.dailyHabit) { mutableStateOf(state.dailyHabit) }
    ExpansionPage("Daily habits", back) {
        item { Text("${habit.completionPercentage}% complete", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
        item { LinearProgressIndicator({ habit.completionPercentage / 100f }, Modifier.fillMaxWidth()) }
        item { HabitToggle("Workout completed", habit.workoutCompleted) { habit = habit.copy(workoutCompleted = it) } }
        item { HabitToggle("Water goal completed", habit.waterGoalCompleted) { habit = habit.copy(waterGoalCompleted = it) } }
        item { HabitToggle("Steps placeholder", habit.stepsCompleted) { habit = habit.copy(stepsCompleted = it) } }
        item { HabitToggle("Meal plan followed", habit.mealPlanFollowed) { habit = habit.copy(mealPlanFollowed = it) } }
        item { HabitToggle("Sleep logged", habit.sleepLogged) { habit = habit.copy(sleepLogged = it) } }
        item { HabitToggle("Stretching completed", habit.stretchingCompleted) { habit = habit.copy(stretchingCompleted = it) } }
        item { Button({ vm.saveDailyHabit(habit) }, Modifier.fillMaxWidth()) { Text("Save today's checklist") } }
    }
}

@Composable
fun FitnessReportsScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    val weekly = state.weeklyReport
    val monthly = state.monthlyReport
    ExpansionPage("Weekly and monthly reports", back) {
        item {
            ReportCard("Weekly fitness report") {
                Text("Workouts: ${weekly.workoutsCompleted} | Missed: ${weekly.missedWorkouts}")
                Text("Calories: ${weekly.caloriesBurned} kcal")
                Text("Best workout week: ${weekly.bestWorkoutWeek}")
                Text("Suggested improvement: ${weekly.improvementPlan}")
            }
        }
        item {
            ReportCard("Monthly transformation report") {
                Text("Workouts: ${monthly.workoutsCompleted} | Calories: ${monthly.caloriesBurned} kcal")
                Text(monthly.transformationSummary)
                Text("Suggested improvement: ${monthly.improvementPlan}")
            }
        }
        item { Text("Workout history timeline", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        if (state.history.isEmpty()) item { Text("Complete a workout to begin your timeline.") }
        items(state.history) { workout ->
            Text("${date(workout.completedAt)} | ${workout.planTitle} | ${workout.caloriesBurned} kcal")
        }
    }
}

@Composable
fun FavoriteWorkoutsScreen(vm: FitnessViewModel, back: () -> Unit, openPlan: (Int) -> Unit) {
    val state by vm.uiState.collectAsState()
    val plans = state.plans.filter { it.id in state.favoritePlanIds }
    ExpansionPage("Favorite workouts", back) {
        if (plans.isEmpty()) item { Text("Save a workout from its detail screen to find it here.") }
        items(plans) { plan -> WorkoutPlanCard(plan, { openPlan(plan.id) }) }
    }
}

@Composable
fun AppFeedbackScreen(vm: FitnessViewModel, back: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    ExpansionPage("App feedback", back) {
        item { Text("Share ideas or report a problem. This offline placeholder stores your message locally.") }
        item { OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("Email") }) }
        item { OutlinedTextField(message, { message = it }, Modifier.fillMaxWidth(), label = { Text("Feedback") }) }
        item { error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        item {
            Button({
                if (!email.contains("@") || message.isBlank()) error = "Enter a valid email and a feedback message."
                else { vm.sendSupportMessage(email, message); saved = true; error = null }
            }, Modifier.fillMaxWidth()) { Text("Save feedback locally") }
        }
        if (saved) item { Text("Feedback saved for the future support integration.") }
    }
}

@Composable
private fun HabitToggle(label: String, checked: Boolean, change: (Boolean) -> Unit) =
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked, change)
        Text(label)
    }

@Composable
private fun ReportCard(title: String, content: @Composable ColumnScope.() -> Unit) =
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            content()
        }
    }

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun ExpansionPage(title: String, back: () -> Unit, content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) =
    Scaffold(topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }

private fun date(timestamp: Long) = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate().toString()
