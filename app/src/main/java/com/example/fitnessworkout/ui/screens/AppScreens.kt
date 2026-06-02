package com.example.fitnessworkout.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.ui.components.AdBannerPlaceholder
import com.example.fitnessworkout.ui.components.ChallengeProgress
import com.example.fitnessworkout.ui.components.SectionTitle
import com.example.fitnessworkout.ui.components.StatCard
import com.example.fitnessworkout.ui.components.WorkoutPlanCard
import com.example.fitnessworkout.ui.theme.FitnessBlack
import com.example.fitnessworkout.ui.theme.FitnessGreen
import com.example.fitnessworkout.viewmodel.FitnessUiState
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import kotlinx.coroutines.delay

private val goals = listOf("Lose Weight", "Build Muscle", "Stay Fit", "Improve Stamina")

@Composable
fun OnboardingScreen(viewModel: FitnessViewModel, onFinished: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.user) { if (state.user != null) onFinished() }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf(goals.first()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Build your strongest routine", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
            Text("Tell us a little about yourself to personalize your fitness dashboard.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item { ProfileFields(name, age, weight, height, { name = it }, { age = it }, { weight = it }, { height = it }) }
        item {
            Text("Your fitness goal", fontWeight = FontWeight.Bold)
            Column {
                goals.forEach { goal ->
                    FilterChip(selected = selectedGoal == goal, onClick = { selectedGoal = goal }, label = { Text(goal) })
                }
            }
        }
        item {
            Button(
                onClick = {
                    viewModel.saveUser(name.trim(), age.toInt(), weight.toFloat(), height.toFloat(), selectedGoal)
                },
                enabled = name.isNotBlank() && age.toIntOrNull() != null && weight.toFloatOrNull() != null && height.toFloatOrNull() != null,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Create my fitness plan") }
        }
    }
}

@Composable
private fun ProfileFields(
    name: String, age: String, weight: String, height: String,
    onName: (String) -> Unit, onAge: (String) -> Unit, onWeight: (String) -> Unit, onHeight: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(name, onName, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(age, onAge, label = { Text("Age") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(weight, onWeight, label = { Text("Weight (kg)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(height, onHeight, label = { Text("Height (cm)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun HomeScreen(viewModel: FitnessViewModel, padding: PaddingValues, onNavigate: (String) -> Unit, onPlan: (Int) -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val challengeCompleted = state.challengePlans.count { it.id in state.completedPlanIds }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AnimatedContent(state.user?.name.orEmpty(), label = "greeting") { name ->
                Column {
                    Text("Hello, $name", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                    Text(state.quote, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item { DashboardStats(state) }
        item {
            ChallengeProgress(challengeCompleted)
        }
        item {
            SectionTitle("Today's workout", "A simple session to keep your momentum.")
        }
        state.todayWorkout?.let { plan ->
            item { WorkoutPlanCard(plan, onClick = { onPlan(plan.id) }) }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickButton("Start Workout", Modifier.weight(1f)) { state.todayWorkout?.let { onPlan(it.id) } }
                QuickButton("My Plan", Modifier.weight(1f)) { onNavigate("workouts") }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickButton("Progress", Modifier.weight(1f)) { onNavigate("progress") }
                QuickButton("Profile", Modifier.weight(1f)) { onNavigate("profile") }
            }
        }
        item { AdBannerPlaceholder() }
    }
}

@Composable
private fun DashboardStats(state: FitnessUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard("Calories burned", "${state.totalCalories} kcal", Modifier.weight(1f))
            StatCard("Weekly workouts", "${state.weeklyCount} / 7", Modifier.weight(1f))
        }
        LinearProgressIndicator(progress = { (state.weeklyCount / 7f).coerceAtMost(1f) }, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)))
    }
}

@Composable
private fun QuickButton(label: String, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = modifier.height(52.dp)) { Text(label) }
}

@Composable
fun WorkoutsScreen(viewModel: FitnessViewModel, padding: PaddingValues, onPlan: (Int) -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedLevel by remember { mutableStateOf("Beginner") }
    Column(Modifier.fillMaxSize().padding(padding)) {
        Column(Modifier.padding(18.dp)) {
            SectionTitle("Workout plans", "Pick a routine that meets you where you are.")
        }
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selectedTab == 0, { selectedTab = 0 }, text = { Text("Plans") })
            Tab(selectedTab == 1, { selectedTab = 1 }, text = { Text("30-Day Challenge") })
        }
        if (selectedTab == 0) {
            LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Beginner", "Intermediate", "Advanced").forEach {
                            FilterChip(selected = selectedLevel == it, onClick = { selectedLevel = it }, label = { Text(it) })
                        }
                    }
                }
                items(state.standardPlans.filter { it.level == selectedLevel }, key = { it.id }) {
                    WorkoutPlanCard(it, onClick = { onPlan(it.id) })
                }
            }
        } else {
            val completedIds = state.completedPlanIds
            LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item { ChallengeProgress(state.challengePlans.count { it.id in completedIds }) }
                items(state.challengePlans, key = { it.id }) { plan ->
                    WorkoutPlanCard(plan, onClick = { onPlan(plan.id) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(viewModel: FitnessViewModel, onBack: () -> Unit) {
    val plan by viewModel.selectedPlan.collectAsState()
    val exercises by viewModel.selectedExercises.collectAsState()
    val completed = remember(plan?.id) { mutableStateListOf<Int>() }
    var activeTimerExercise by remember { mutableStateOf<Int?>(null) }
    var remainingSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(activeTimerExercise, remainingSeconds) {
        if (activeTimerExercise != null && remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds--
        } else if (activeTimerExercise != null && remainingSeconds == 0) {
            activeTimerExercise?.let { if (it !in completed) completed.add(it) }
            activeTimerExercise = null
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(plan?.title ?: "Workout") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
        })
    }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            plan?.let {
                item {
                    Text(it.description)
                    Spacer(Modifier.height(6.dp))
                    Text("${it.durationMinutes} min | ${it.estimatedCalories} kcal", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            itemsIndexed(exercises, key = { _, item -> item.id }) { index, exercise ->
                ExerciseCard(
                    index = index + 1,
                    exercise = exercise,
                    isComplete = exercise.id in completed,
                    timerText = if (activeTimerExercise == exercise.id) "$remainingSeconds sec" else null,
                    onTimer = {
                        activeTimerExercise = exercise.id
                        remainingSeconds = exercise.durationSeconds ?: 0
                    },
                    onComplete = {
                        if (exercise.id in completed) completed.remove(exercise.id) else completed.add(exercise.id)
                    }
                )
            }
            item {
                Button(
                    onClick = {
                        viewModel.completeSelectedWorkout()
                        onBack()
                    },
                    enabled = exercises.isNotEmpty() && completed.size == exercises.size,
                    modifier = Modifier.fillMaxWidth().height(54.dp)
                ) { Text(if (completed.size == exercises.size) "Finish workout" else "Complete each exercise") }
            }
        }
    }
}

@Composable
private fun ExerciseCard(
    index: Int, exercise: Exercise, isComplete: Boolean, timerText: String?, onTimer: () -> Unit, onComplete: () -> Unit
) {
    val scale by animateFloatAsState(if (isComplete) 1.02f else 1f, tween(250), label = "exercise")
    Card(
        modifier = Modifier.fillMaxWidth().scale(scale),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = if (isComplete) FitnessGreen.copy(alpha = .15f) else MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("$index.", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(exercise.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                IconButton(onClick = onComplete) { Icon(Icons.Default.CheckCircle, "Mark complete", tint = if (isComplete) MaterialTheme.colorScheme.primary else Color.Gray) }
            }
            Text("${exercise.sets} sets | ${exercise.repsOrDuration} | ${exercise.restSeconds}s rest", style = MaterialTheme.typography.bodySmall)
            Text(exercise.instruction, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (exercise.durationSeconds != null) {
                OutlinedButton(onClick = onTimer) { Text(timerText ?: "Start timer") }
            }
        }
    }
}

@Composable
fun ProgressScreen(viewModel: FitnessViewModel, padding: PaddingValues) {
    val state by viewModel.uiState.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { SectionTitle("Your progress", "Every completed workout counts.") }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Total workouts", "${state.history.size}", Modifier.weight(1f))
                StatCard("This week", "${state.weeklyCount}", Modifier.weight(1f))
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Calories", "${state.totalCalories}", Modifier.weight(1f))
                StatCard("Day streak", "${state.streak}", Modifier.weight(1f))
            }
        }
        item { SectionTitle("Recent workouts") }
        if (state.history.isEmpty()) item { Text("Complete your first workout to start building your history.") }
        items(state.history, key = { it.id }) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(it.planTitle, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("${it.caloriesBurned} kcal", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: FitnessViewModel, padding: PaddingValues, onPremium: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val user = state.user ?: return
    var showEditor by remember { mutableStateOf(false) }
    var showReset by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { SectionTitle("Profile", "Your health details and fitness goal.") }
        item { ProfileSummary(user) }
        item {
            Button(onClick = { showEditor = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Edit, null)
                Text(" Edit profile")
            }
        }
        item {
            OutlinedButton(onClick = onPremium, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Star, null)
                Text(" Explore Premium")
            }
        }
        item {
            TextButton(onClick = { showReset = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Refresh, null)
                Text(" Reset progress")
            }
        }
        item { AdBannerPlaceholder() }
    }
    if (showEditor) EditProfileDialog(user, onDismiss = { showEditor = false }) { name, age, weight, height, goal ->
        viewModel.saveUser(name, age, weight, height, goal)
        showEditor = false
    }
    if (showReset) AlertDialog(
        onDismissRequest = { showReset = false },
        title = { Text("Reset progress?") },
        text = { Text("This removes your completed workout history, calories, and streak.") },
        confirmButton = { TextButton(onClick = { viewModel.resetProgress(); showReset = false }) { Text("Reset") } },
        dismissButton = { TextButton(onClick = { showReset = false }) { Text("Cancel") } }
    )
}

@Composable
private fun ProfileSummary(user: UserProfile) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FitnessBlack)) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(user.name, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("${user.age} years | ${user.weightKg} kg | ${user.heightCm} cm", color = Color.LightGray)
            Text("Goal: ${user.fitnessGoal}", color = FitnessGreen, fontWeight = FontWeight.Bold)
            Text("BMI: ${"%.1f".format(user.bmi)} (${bmiLabel(user.bmi)})", color = Color.White)
        }
    }
}

private fun bmiLabel(bmi: Float) = when {
    bmi < 18.5f -> "Below healthy range"
    bmi < 25f -> "Healthy range"
    bmi < 30f -> "Above healthy range"
    else -> "High range"
}

@Composable
private fun EditProfileDialog(user: UserProfile, onDismiss: () -> Unit, onSave: (String, Int, Float, Float, String) -> Unit) {
    var name by remember { mutableStateOf(user.name) }
    var age by remember { mutableStateOf(user.age.toString()) }
    var weight by remember { mutableStateOf(user.weightKg.toString()) }
    var height by remember { mutableStateOf(user.heightCm.toString()) }
    var goal by remember { mutableStateOf(user.fitnessGoal) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit profile") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { ProfileFields(name, age, weight, height, { name = it }, { age = it }, { weight = it }, { height = it }) }
                item {
                    goals.forEach { option ->
                        FilterChip(selected = goal == option, onClick = { goal = option }, label = { Text(option) })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name.trim(), age.toInt(), weight.toFloat(), height.toFloat(), goal) },
                enabled = name.isNotBlank() && age.toIntOrNull() != null && weight.toFloatOrNull() != null && height.toFloatOrNull() != null
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(onBack: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Fitness Premium") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
        })
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(54.dp))
            Text("Unlock your next level", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Text("Premium is a placeholder ready for future billing integration.")
            listOf("Custom workout plans", "No ads", "Advanced progress analytics", "Diet plan").forEach {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                    Text(it, fontWeight = FontWeight.Bold)
                }
            }
            Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) { Text("Coming soon") }
        }
    }
}
