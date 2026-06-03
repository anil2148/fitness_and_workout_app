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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R

@Composable
fun DailyHabitScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    var habit by remember(state.dailyHabit) { mutableStateOf(state.dailyHabit) }
    ExpansionPage(stringResource(R.string.daily_habits), back) {
        item { Text(stringResource(R.string.percent_complete, habit.completionPercentage), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
        item { LinearProgressIndicator({ habit.completionPercentage / 100f }, Modifier.fillMaxWidth()) }
        item { HabitToggle(stringResource(R.string.workout_completed), habit.workoutCompleted) { habit = habit.copy(workoutCompleted = it) } }
        item { HabitToggle(stringResource(R.string.water_goal_completed), habit.waterGoalCompleted) { habit = habit.copy(waterGoalCompleted = it) } }
        item { HabitToggle(stringResource(R.string.steps_placeholder), habit.stepsCompleted) { habit = habit.copy(stepsCompleted = it) } }
        item { HabitToggle(stringResource(R.string.meal_plan_followed), habit.mealPlanFollowed) { habit = habit.copy(mealPlanFollowed = it) } }
        item { HabitToggle(stringResource(R.string.sleep_logged), habit.sleepLogged) { habit = habit.copy(sleepLogged = it) } }
        item { HabitToggle(stringResource(R.string.stretching_completed), habit.stretchingCompleted) { habit = habit.copy(stretchingCompleted = it) } }
        item { Button({ vm.saveDailyHabit(habit) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.save_checklist)) } }
    }
}

@Composable
fun FitnessReportsScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    val weekly = state.weeklyReport
    val monthly = state.monthlyReport
    ExpansionPage(stringResource(R.string.weekly_monthly_reports), back) {
        item {
            ReportCard(stringResource(R.string.weekly_fitness_report)) {
                Text(stringResource(R.string.workouts_missed_value, weekly.workoutsCompleted, weekly.missedWorkouts))
                Text(stringResource(R.string.calories_kcal_value, weekly.caloriesBurned))
                Text(stringResource(R.string.best_week_value, weekly.bestWorkoutWeek))
                Text(stringResource(R.string.suggested_improvement_value, weekly.improvementPlan))
            }
        }
        item {
            ReportCard(stringResource(R.string.monthly_transformation_report)) {
                Text(stringResource(R.string.workouts_calories_value, monthly.workoutsCompleted, monthly.caloriesBurned))
                Text(monthly.transformationSummary)
                Text(stringResource(R.string.suggested_improvement_value, monthly.improvementPlan))
            }
        }
        item { Text(stringResource(R.string.workout_history_timeline), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        if (state.history.isEmpty()) item { Text(stringResource(R.string.timeline_empty)) }
        items(state.history) { workout ->
            Text(stringResource(R.string.timeline_row, date(workout.completedAt), workout.planTitle, workout.caloriesBurned))
        }
    }
}

@Composable
fun FavoriteWorkoutsScreen(vm: FitnessViewModel, back: () -> Unit, openPlan: (Int) -> Unit) {
    val state by vm.uiState.collectAsState()
    val plans = state.plans.filter { it.id in state.favoritePlanIds }
    ExpansionPage(stringResource(R.string.favorite_workouts), back) {
        if (plans.isEmpty()) item { Text(stringResource(R.string.favorites_empty)) }
        items(plans) { plan -> WorkoutPlanCard(plan, { openPlan(plan.id) }) }
    }
}

@Composable
fun AppFeedbackScreen(vm: FitnessViewModel, back: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val feedbackError = stringResource(R.string.feedback_error)
    val feedbackSent = stringResource(R.string.feedback_sent_successfully)
    ExpansionPage(stringResource(R.string.app_feedback), back, snackbarHostState) {
        item { Text(stringResource(R.string.feedback_body)) }
        item { OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.email)) }) }
        item { OutlinedTextField(message, { message = it }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.feedback)) }) }
        item { error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        item {
            Button({
                if (!email.contains("@") || message.isBlank()) error = feedbackError
                else { vm.sendSupportMessage(email, message); saved = true; error = null; message = ""; scope.launch { snackbarHostState.showSnackbar(feedbackSent) } }
            }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.save_feedback)) }
        }
        if (saved) item { Text(feedbackSent) }
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
private fun ExpansionPage(title: String, back: () -> Unit, snackbarHostState: SnackbarHostState? = null, content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) =
    Scaffold(snackbarHost = { snackbarHostState?.let { SnackbarHost(it) } }, topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) } }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }

private fun date(timestamp: Long) = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault()))
