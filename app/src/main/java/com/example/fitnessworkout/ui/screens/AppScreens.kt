package com.example.fitnessworkout.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Switch
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import com.example.fitnessworkout.R
import com.example.fitnessworkout.data.model.AppSettings
import com.example.fitnessworkout.utils.Units
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.data.model.PremiumComparisonFeature
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
    LaunchedEffect(state.user, state.safetyAcknowledgement) {
        if (state.user != null && state.safetyAcknowledgement.medicalDisclaimerAccepted) onFinished()
    }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf(goals.first()) }
    var level by remember { mutableStateOf("Beginner") }
    var minutes by remember { mutableIntStateOf(20) }
    var equipment by remember { mutableStateOf("No Equipment") }
    var style by remember { mutableStateOf("Balanced") }
    var country by remember { mutableStateOf("United States") }
    var language by remember { mutableStateOf("English") }
    var unitSystem by remember { mutableStateOf("Imperial") }
    var diet by remember { mutableStateOf("Balanced") }
    var location by remember { mutableStateOf("Home") }
    var injurySafeMode by remember { mutableStateOf(false) }
    var acceptedDisclaimer by remember { mutableStateOf(false) }

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
        item { OnboardingChoices("Fitness level", listOf("Beginner", "Intermediate", "Advanced"), level) { level = it } }
        item { OnboardingChoices("Available time", listOf(10, 20, 30, 45).map { "$it min" }, "$minutes min") { minutes = it.substringBefore(" ").toInt() } }
        item { OnboardingChoices("Equipment", listOf("No Equipment", "Dumbbells", "Resistance Band", "Gym"), equipment) { equipment = it } }
        item { OnboardingChoices("Workout style", listOf("Balanced", "Strength", "Cardio", "Mobility", "HIIT"), style) { style = it } }
        item { OnboardingChoices("Country / region", listOf("United States", "India", "Spain", "France", "Brazil"), country) { country = it; unitSystem = Units.defaultSystem(it) } }
        item { OnboardingChoices("Preferred language", listOf("English", "Hindi", "Spanish", "French", "Arabic"), language) { language = it } }
        item { OnboardingChoices("Unit system", listOf("Metric", "Imperial"), unitSystem) { unitSystem = it } }
        item { OnboardingChoices("Diet preference", listOf("Balanced", "Vegetarian", "Vegan", "Halal-friendly"), diet) { diet = it } }
        item { OnboardingChoices("Workout location", listOf("Home", "Gym", "Office", "Outdoor", "Apartment / no jumping"), location) { location = it } }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(injurySafeMode, { injurySafeMode = it })
                Text("Prefer beginner-paced, injury-safe recommendations")
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(acceptedDisclaimer, { acceptedDisclaimer = it })
                Text("I understand this app provides general wellness guidance, not medical advice. I will consult a healthcare professional for pregnancy, injury, or medical concerns.")
            }
        }
        item {
            Button(
                onClick = {
                    viewModel.saveUser(name.trim(), age.toInt(), weight.toFloat(), height.toFloat(), selectedGoal, level = level, minutes = minutes, equipment = equipment, style = style)
                    viewModel.saveSettings(AppSettings(country = country, language = language, unitSystem = unitSystem, dietPreference = diet, workoutLocation = location, injurySafeMode = injurySafeMode))
                    viewModel.acknowledgeSafety()
                },
                enabled = acceptedDisclaimer && name.isNotBlank() && (age.toIntOrNull() ?: 0) > 0 && (weight.toFloatOrNull() ?: 0f) > 0f && (height.toFloatOrNull() ?: 0f) > 0f,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Create my fitness plan") }
        }
    }
}

@Composable private fun OnboardingChoices(title: String, options: List<String>, selected: String, choose: (String) -> Unit) {
    Column { Text(title, fontWeight = FontWeight.Bold); Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { options.forEach { FilterChip(selected == it, { choose(it) }, label = { Text(it) }) } } }
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
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("BMI", "${"%.1f".format(state.user?.bmi ?: 0f)}", Modifier.weight(1f))
                StatCard("Water", "${state.water.amountMl} / ${state.water.goalMl} ml", Modifier.weight(1f))
            }
        }
        if (state.isPremiumUser) item { Text("★ ${stringResource(R.string.premium_member)}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold) }
        item {
            ChallengeProgress(challengeCompleted)
        }
        item {
            SectionTitle("Recommended for You", "Local suggestions adapt to your goal, recent activity, available time, and workout location.")
        }
        state.recommendation?.let { recommendation ->
            item {
                Card(onClick = { onPlan(recommendation.planId) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(recommendation.title, fontWeight = FontWeight.Bold)
                        Text(recommendation.reason, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        item {
            Card(onClick = { onNavigate("fitness-score") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Fitness score", Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("${state.fitnessScore.value} / 100", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
        item {
            Card(onClick = { onNavigate("daily-habits") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Today's habits: ${state.dailyHabit.completionPercentage}%", fontWeight = FontWeight.Bold)
                    LinearProgressIndicator({ state.dailyHabit.completionPercentage / 100f }, Modifier.fillMaxWidth())
                }
            }
        }
        state.recentPlans.firstOrNull()?.let { recent ->
            item {
                SectionTitle("Continue last workout")
                WorkoutPlanCard(recent, onClick = { onPlan(recent.id) })
            }
        }
        item {
            SectionTitle("Today's workout", "A simple session to keep your momentum.")
        }
        state.todayWorkout?.let { plan ->
            item { WorkoutPlanCard(plan, onClick = { onPlan(plan.id) }) }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickButton("Quick Start", Modifier.weight(1f)) { onNavigate("quick-workout") }
                QuickButton("My Plan", Modifier.weight(1f)) { onNavigate("workouts") }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickButton("Progress", Modifier.weight(1f)) { onNavigate("progress") }
                QuickButton("Reports", Modifier.weight(1f)) { onNavigate("fitness-reports") }
            }
        }
        if (!state.isPremiumUser) item { AdBannerPlaceholder() }
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
fun WorkoutsScreen(viewModel: FitnessViewModel, padding: PaddingValues, onPlan: (Int) -> Unit, onPremium: () -> Unit, onNavigate: (String) -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedLevel by remember { mutableStateOf("Beginner") }
    var search by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(padding)) {
        Column(Modifier.padding(18.dp)) {
            SectionTitle("Workout plans", "Pick a routine that meets you where you are.")
            OutlinedButton({ onNavigate("favorites") }, Modifier.fillMaxWidth()) { Text("Favorite workouts") }
        }
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selectedTab == 0, { selectedTab = 0 }, text = { Text("Plans") })
            Tab(selectedTab == 1, { selectedTab = 1 }, text = { Text("30-Day Challenge") })
        }
        if (selectedTab == 0) {
            LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (state.recentPlans.isNotEmpty()) {
                    item { Text("Recently viewed", fontWeight = FontWeight.Bold) }
                    items(state.recentPlans.take(3), key = { "recent-${it.id}" }) { WorkoutPlanCard(it, { onPlan(it.id) }) }
                }
                item {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Search workouts") },
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Beginner", "Intermediate", "Advanced").forEach {
                            FilterChip(selected = selectedLevel == it, onClick = { selectedLevel = it }, label = { Text(it) })
                        }
                    }
                }
                val filteredPlans = state.standardPlans.filter { it.level == selectedLevel && it.title.contains(search.trim(), ignoreCase = true) }
                if (filteredPlans.isEmpty()) item { Text("No workout plans match this search.") }
                items(filteredPlans, key = { it.id }) {
                    val locked = it.premiumOnly && !state.isPremiumUser
                    WorkoutPlanCard(it, onClick = { if (locked) onPremium() else onPlan(it.id) }, locked = locked)
                }
            }
        } else {
            val completedIds = state.completedPlanIds
            LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item { ChallengeProgress(state.challengePlans.count { it.id in completedIds }) }
                items(state.challengePlans, key = { it.id }) { plan ->
                    val locked = plan.premiumOnly && !state.isPremiumUser
                    WorkoutPlanCard(plan, onClick = { if (locked) onPremium() else onPlan(plan.id) }, locked = locked)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(viewModel: FitnessViewModel, onBack: () -> Unit, onStartPlayer: () -> Unit, onFinished: () -> Unit = onBack) {
    val plan by viewModel.selectedPlan.collectAsState()
    val exercises by viewModel.selectedExercises.collectAsState()
    val state by viewModel.uiState.collectAsState()
    val completed = remember(plan?.id) { mutableStateListOf<Int>() }
    var activeTimerExercise by remember { mutableStateOf<Int?>(null) }
    var remainingSeconds by remember { mutableIntStateOf(0) }
    var showStopWarning by remember { mutableStateOf(false) }

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
            IconButton(onClick = { showStopWarning = true }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
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
                item {
                    Button(onStartPlayer, Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.PlayArrow, null)
                        Text(" Start guided workout")
                    }
                }
                item {
                    OutlinedButton({ viewModel.toggleFavorite(it.id) }, Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Star, null)
                        Text(if (it.id in state.favoritePlanIds) " Remove from favorites" else " Add to favorites")
                    }
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
                OutlinedButton(onClick = { showStopWarning = true }, modifier = Modifier.fillMaxWidth()) { Text("Stop workout") }
            }
            item {
                Button(
                    onClick = {
                        viewModel.completeSelectedWorkout()
                        onFinished()
                    },
                    enabled = exercises.isNotEmpty() && completed.size == exercises.size,
                    modifier = Modifier.fillMaxWidth().height(54.dp)
                ) { Text(if (completed.size == exercises.size) "Finish workout" else "Complete each exercise") }
            }
        }
    }
    if (showStopWarning) AlertDialog(
        onDismissRequest = { showStopWarning = false },
        title = { Text("Stop this workout?") },
        text = { Text("Stop immediately if you feel pain, dizziness, chest pain, unusual shortness of breath, or severe discomfort. Ending now will record a skipped workout so future recommendations can adapt.") },
        confirmButton = { TextButton({ viewModel.skipSelectedWorkout(); showStopWarning = false; onBack() }) { Text("End workout") } },
        dismissButton = { TextButton({ showStopWarning = false }) { Text("Continue safely") } }
    )
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
            ExerciseVisual(exercise)
            ExerciseVideoPlayer(exercise)
            Text("${exercise.muscleGroup} | ${exercise.difficulty} | ${exercise.equipment}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
            Text("${exercise.sets} sets | ${exercise.repsOrDuration} | ${exercise.restSeconds}s rest | ${exercise.caloriesPerMinute} kcal/min", style = MaterialTheme.typography.bodySmall)
            Text(exercise.instruction, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Safety: ${exercise.safetyTips}", style = MaterialTheme.typography.bodySmall)
            Text("Avoid: ${exercise.commonMistakes}", style = MaterialTheme.typography.bodySmall)
            if (exercise.durationSeconds != null) {
                OutlinedButton(onClick = onTimer) { Text(timerText ?: "Start timer") }
            }
        }
    }
}

@Composable
fun ProgressScreen(viewModel: FitnessViewModel, padding: PaddingValues, onPremium: () -> Unit, onNavigate: (String) -> Unit) {
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
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickButton("Reports", Modifier.weight(1f)) { onNavigate("fitness-reports") }
                QuickButton("PDF preview", Modifier.weight(1f)) { onNavigate("progress-report") }
            }
        }
        item {
            if (state.isPremiumUser) {
                Text("Advanced analytics: consistency ${(state.weeklyCount / 7f * 100).toInt()}% • average ${state.history.map { it.durationMinutes }.average().takeIf { !it.isNaN() }?.toInt() ?: 0} min", fontWeight = FontWeight.Bold)
            } else OutlinedButton(onPremium, Modifier.fillMaxWidth()) { Icon(Icons.Default.Lock, null); Text(" Unlock advanced analytics") }
        }
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
fun ProfileScreen(viewModel: FitnessViewModel, padding: PaddingValues, onPremium: () -> Unit, onNavigate: (String) -> Unit) {
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
        item { ProfileSummary(user, state.settings.unitSystem) }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Dark mode", Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Switch(user.darkMode, viewModel::setDarkMode)
            }
        }
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
            listOf("water" to "Water tracker", "calculators" to "Health calculators", "diet" to "Diet guidance",
                "custom" to "AI-style custom plan", "reminders" to "Smart reminders", "measurements" to "Body measurements",
                "photos" to "Progress photos", "achievements" to "Achievements", "fitness-test" to "Fitness level test",
                "share" to "Workout share card", "privacy" to "Privacy policy", "terms" to "Terms",
                "medical" to "Medical disclaimer", "feedback" to "Feedback", "delete-data" to "Delete all data",
                "settings" to "Global settings", "regional-pricing" to "Regional pricing preview", "quick-workout" to "Quick workouts",
                "fitness-score" to "Fitness score", "ai-workout" to "AI-ready workout coach", "ai-meal" to "AI-ready meal suggestions",
                "ai-progress" to "AI-ready progress analysis", "ai-chat" to "AI-ready motivation chat", "progress-report" to "PDF report preview",
                "safety" to "Safety and trust", "about" to "About app", "contact-support" to "Contact support",
                "rate-app" to "Rate app", "share-app" to "Share app", "data-safety" to "Data safety",
                "announcements" to "Announcements", "content-categories" to "Workout and diet categories",
                "daily-habits" to "Daily habit checklist", "fitness-reports" to "Weekly and monthly reports",
                "favorites" to "Favorite workouts").forEach { (route, label) ->
                OutlinedButton({ onNavigate(route) }, Modifier.fillMaxWidth()) { Text(label) }
            }
        }
        item {
            TextButton(onClick = { showReset = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Refresh, null)
                Text(" Reset progress")
            }
        }
        if (!state.isPremiumUser) item { AdBannerPlaceholder() }
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
private fun ProfileSummary(user: UserProfile, unitSystem: String) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FitnessBlack)) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(user.name, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("${user.age} years | ${Units.weight(user.weightKg, unitSystem)} | ${Units.length(user.heightCm, unitSystem)}", color = Color.LightGray)
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
                enabled = name.isNotBlank() && (age.toIntOrNull() ?: 0) > 0 && (weight.toFloatOrNull() ?: 0f) > 0f && (height.toFloatOrNull() ?: 0f) > 0f
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(viewModel: FitnessViewModel, onBack: () -> Unit, onNavigate: (String) -> Unit) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = { Text("Fitness Premium") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
        })
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(54.dp)) }
            item { Text("Unlock your next level", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold) }
            item { Text("7-day free trial • Cancel anytime", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
            item { Text(if (state.isPremiumUser) "Premium is active on this device." else "Choose a plan and unlock the complete experience.") }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("$2.99\nMonthly", "$19.99\nYearly\nSave 44%", "$29.99\nLifetime\nLimited offer").forEach {
                        Text(it, Modifier.weight(1f).background(FitnessGreen.copy(alpha = .16f)).padding(10.dp), fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Text("Free vs Premium", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(premiumComparison) { feature -> Text("${feature.title}: ${feature.freeValue} | ${feature.premiumValue}") }
            item { Text("Testimonial placeholder: “The short workout options help me stay consistent.”") }
            item { Text("FAQ: Premium unlocks AI-ready previews, reports, advanced analytics, and unlimited tracking. Billing remains a placeholder until Play Billing is connected.") }
            item {
                // TODO: Replace the local toggle with Google Play Billing purchase verification.
                Button(onClick = { viewModel.setPremium(true) }, modifier = Modifier.fillMaxWidth()) { Text("Start 7-day free trial") }
            }
            item { OutlinedButton(onClick = { viewModel.setPremium(!state.isPremiumUser) }, modifier = Modifier.fillMaxWidth()) { Text(if (state.isPremiumUser) "Disable mock premium" else "Unlock Premium") } }
            item { OutlinedButton({}, Modifier.fillMaxWidth(), enabled = false) { Text("Coming soon: Restore purchase") } }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton({ onNavigate("terms") }) { Text("Terms") }
                    TextButton({ onNavigate("privacy") }) { Text("Privacy") }
                }
            }
        }
    }
}

private val premiumComparison = listOf(
    PremiumComparisonFeature("Progress tracking", "Limited", "Unlimited history"),
    PremiumComparisonFeature("Workout plans", "Beginner plans", "Full custom plan builder"),
    PremiumComparisonFeature("Analytics", "Basic score", "Reports and AI-ready analysis"),
    PremiumComparisonFeature("Progress photos", "2 local photos", "Unlimited local photos"),
    PremiumComparisonFeature("Ads", "Placeholder visible", "No ads")
)

@Composable
private fun ExerciseVisual(exercise: Exercise) {
    Box(Modifier.fillMaxWidth().height(96.dp).clip(RoundedCornerShape(14.dp)).background(FitnessGreen.copy(alpha = .12f)), contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Image(painterResource(R.drawable.exercise_placeholder), contentDescription = "${exercise.name} fallback visual", Modifier.size(72.dp))
            Text("${exercise.name} • ${exercise.muscleGroup}")
        }
    }
}

@Composable
private fun ExerciseVideoPlayer(exercise: Exercise) {
    // TODO: Replace this offline-safe placeholder with packaged MP4 files or Media3 playback.
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(FitnessBlack).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.PlayArrow, null, tint = FitnessGreen)
        Text(" Video placeholder • Coming soon", color = Color.White, style = MaterialTheme.typography.bodySmall)
    }
}
