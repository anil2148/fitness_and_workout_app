package com.example.fitnessworkout.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R
import com.example.fitnessworkout.data.model.AppSettings
import com.example.fitnessworkout.utils.Units
import com.example.fitnessworkout.utils.AppLocaleManager
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.data.model.PremiumComparisonFeature
import com.example.fitnessworkout.ui.components.AdBannerPlaceholder
import com.example.fitnessworkout.ui.components.ChallengeProgress
import com.example.fitnessworkout.ui.components.ExerciseIllustration
import com.example.fitnessworkout.ui.components.ExerciseVideoPlayer
import com.example.fitnessworkout.ui.components.SectionTitle
import com.example.fitnessworkout.ui.components.StatCard
import com.example.fitnessworkout.ui.components.WorkoutPlanIllustration
import com.example.fitnessworkout.ui.components.WorkoutPlanCard
import com.example.fitnessworkout.ui.theme.FitnessBlack
import com.example.fitnessworkout.ui.theme.FitnessGreen
import com.example.fitnessworkout.viewmodel.FitnessUiState
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import kotlinx.coroutines.delay

private val goals = listOf("Lose Weight", "Build Muscle", "Stay Fit", "Improve Stamina")

@Composable
fun OnboardingScreen(viewModel: FitnessViewModel, onFinished: () -> Unit) {
    val context = LocalContext.current
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
    var languageCode by remember { mutableStateOf(AppLocaleManager.persistedLanguageCode(context)) }
    var unitSystem by remember { mutableStateOf("Imperial") }
    var diet by remember { mutableStateOf("Balanced") }
    var location by remember { mutableStateOf("Home") }
    var injurySafeMode by remember { mutableStateOf(false) }
    var acceptedDisclaimer by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(stringResource(R.string.onboarding_title), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
            Text(stringResource(R.string.onboarding_subtitle), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item { ProfileFields(name, age, weight, height, { name = it }, { age = it }, { weight = it }, { height = it }) }
        item {
            Text(stringResource(R.string.fitness_goal), fontWeight = FontWeight.Bold)
            Column {
                goals.forEach { goal ->
                    FilterChip(selected = selectedGoal == goal, onClick = { selectedGoal = goal }, label = { Text(goal) })
                }
            }
        }
        item { OnboardingChoices(stringResource(R.string.fitness_level), listOf("Beginner", "Intermediate", "Advanced"), level) { level = it } }
        item { OnboardingChoices(stringResource(R.string.available_time), listOf(10, 20, 30, 45).map { "$it min" }, "$minutes min") { minutes = it.substringBefore(" ").toInt() } }
        item { OnboardingChoices(stringResource(R.string.equipment), listOf("No Equipment", "Dumbbells", "Resistance Band", "Gym"), equipment) { equipment = it } }
        item { OnboardingChoices(stringResource(R.string.workout_style), listOf("Balanced", "Strength", "Cardio", "Mobility", "HIIT"), style) { style = it } }
        item { OnboardingChoices(stringResource(R.string.country_region), listOf("United States", "India", "Spain", "France", "Brazil"), country) { country = it; unitSystem = Units.defaultSystem(it) } }
        item { OnboardingChoices(stringResource(R.string.preferred_language), AppLocaleManager.supportedLanguages.map { it.label }, AppLocaleManager.languageName(languageCode)) { languageCode = AppLocaleManager.languageCode(it) } }
        item { OnboardingChoices(stringResource(R.string.unit_system), listOf("Metric", "Imperial"), unitSystem) { unitSystem = it } }
        item { OnboardingChoices(stringResource(R.string.diet_preference), listOf("Balanced", "Vegetarian", "Vegan", "Halal-friendly"), diet) { diet = it } }
        item { OnboardingChoices(stringResource(R.string.workout_location), listOf("Home", "Gym", "Office", "Outdoor", "Apartment / no jumping"), location) { location = it } }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(injurySafeMode, { injurySafeMode = it })
                Text(stringResource(R.string.prefer_injury_safe))
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(acceptedDisclaimer, { acceptedDisclaimer = it })
                Text(stringResource(R.string.medical_ack))
            }
        }
        item {
            Button(
                onClick = {
                    val parsedAge = age.toIntOrNull()
                    val parsedWeight = weight.toFloatOrNull()
                    val parsedHeight = height.toFloatOrNull()
                    if (!acceptedDisclaimer || name.isBlank() || parsedAge == null || parsedAge <= 0 ||
                        parsedWeight == null || parsedWeight <= 0f || parsedHeight == null || parsedHeight <= 0f
                    ) {
                        error = context.getString(R.string.onboarding_validation_error)
                    } else {
                        error = null
                        viewModel.completeOnboarding(name.trim(), parsedAge, parsedWeight, parsedHeight, selectedGoal, level, minutes, equipment, style,
                            AppSettings(country = country, language = AppLocaleManager.languageName(languageCode), selectedLanguageCode = languageCode, unitSystem = unitSystem, dietPreference = diet, workoutLocation = location, injurySafeMode = injurySafeMode)
                        ) { AppLocaleManager.restartUi(context) }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text(stringResource(R.string.create_fitness_plan)) }
        }
        item { error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable private fun OnboardingChoices(title: String, options: List<String>, selected: String, choose: (String) -> Unit) {
    Column {
        Text(title, fontWeight = FontWeight.Bold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            options.forEach { FilterChip(selected == it, { choose(it) }, label = { Text(it) }) }
        }
    }
}

@Composable
private fun ProfileFields(
    name: String, age: String, weight: String, height: String,
    onName: (String) -> Unit, onAge: (String) -> Unit, onWeight: (String) -> Unit, onHeight: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(name, onName, label = { Text(stringResource(R.string.name)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(age, onAge, label = { Text(stringResource(R.string.age)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(weight, onWeight, label = { Text(stringResource(R.string.weight_kg)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(height, onHeight, label = { Text(stringResource(R.string.height_cm)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
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
                    Text(stringResource(R.string.hello_name, name), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                    Text(state.quote, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item { DashboardStats(state) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("BMI", "${"%.1f".format(state.user?.bmi ?: 0f)}", Modifier.weight(1f))
                StatCard(stringResource(R.string.water), "${state.water.amountMl} / ${state.water.goalMl} ml", Modifier.weight(1f))
            }
        }
        if (state.isPremiumUser) item { Text("★ ${stringResource(R.string.premium_member)}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold) }
        item {
            ChallengeProgress(challengeCompleted)
        }
        item {
            SectionTitle(stringResource(R.string.recommended_for_you), stringResource(R.string.recommendation_subtitle))
        }
        state.recommendation?.let { recommendation ->
            item {
                Card(onClick = { onPlan(recommendation.planId) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                    Row(Modifier.padding(15.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        state.plans.firstOrNull { it.id == recommendation.planId }?.let { WorkoutPlanIllustration(it, Modifier.size(72.dp)) }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(recommendation.title, fontWeight = FontWeight.Bold)
                            Text(recommendation.reason, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        item {
            Card(onClick = { onNavigate("fitness-score") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.fitness_score), Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("${state.fitnessScore.value} / 100", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
        item {
            Card(onClick = { onNavigate("daily-habits") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(stringResource(R.string.todays_habits, state.dailyHabit.completionPercentage), fontWeight = FontWeight.Bold)
                    LinearProgressIndicator({ state.dailyHabit.completionPercentage / 100f }, Modifier.fillMaxWidth())
                }
            }
        }
        state.recentPlans.firstOrNull()?.let { recent ->
            item {
                SectionTitle(stringResource(R.string.continue_last_workout))
                WorkoutPlanCard(recent, onClick = { onPlan(recent.id) })
            }
        }
        item {
            SectionTitle(stringResource(R.string.todays_workout), stringResource(R.string.today_subtitle))
        }
        state.todayWorkout?.let { plan ->
            item { WorkoutPlanCard(plan, onClick = { onPlan(plan.id) }) }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickButton(stringResource(R.string.quick_start), Modifier.weight(1f)) { onNavigate("quick-workout") }
                QuickButton(stringResource(R.string.my_plan), Modifier.weight(1f)) { onNavigate("workouts") }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickButton(stringResource(R.string.nav_progress), Modifier.weight(1f)) { onNavigate("progress") }
                QuickButton(stringResource(R.string.reports), Modifier.weight(1f)) { onNavigate("fitness-reports") }
            }
        }
        if (!state.isPremiumUser) item { AdBannerPlaceholder() }
    }
}

@Composable
private fun DashboardStats(state: FitnessUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatCard(stringResource(R.string.calories_burned), "${state.totalCalories} kcal", Modifier.weight(1f))
            StatCard(stringResource(R.string.weekly_workouts), "${state.weeklyCount} / 7", Modifier.weight(1f))
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
            SectionTitle(stringResource(R.string.workout_plans), stringResource(R.string.pick_routine))
            OutlinedButton({ onNavigate("favorites") }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.favorite_workouts)) }
        }
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selectedTab == 0, { selectedTab = 0 }, text = { Text(stringResource(R.string.plans)) })
            Tab(selectedTab == 1, { selectedTab = 1 }, text = { Text(stringResource(R.string.challenge_30)) })
        }
        if (selectedTab == 0) {
            LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (state.recentPlans.isNotEmpty()) {
                    item { Text(stringResource(R.string.recently_viewed), fontWeight = FontWeight.Bold) }
                    items(state.recentPlans.take(3), key = { "recent-${it.id}" }) { WorkoutPlanCard(it, { onPlan(it.id) }) }
                }
                item {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.search_workouts)) },
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
                if (filteredPlans.isEmpty()) item { Text(stringResource(R.string.no_workout_match)) }
                items(filteredPlans, key = { it.id }) {
                    val locked = it.premiumOnly && !state.isPremiumUser
                    WorkoutPlanCard(it, onClick = { if (locked) onPremium() else onPlan(it.id) }, locked = locked)
                }
            }
        } else {
            val completedIds = state.completedPlanIds
            LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item { ChallengeProgress(state.challengePlans.count { it.id in completedIds }) }
                if (state.challengePlans.isEmpty()) item { Text(stringResource(R.string.challenge_empty)) }
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
fun WorkoutDetailScreen(
    viewModel: FitnessViewModel,
    onBack: () -> Unit,
    onStartPlayer: () -> Unit,
    onExercise: (Int) -> Unit,
    onPremium: () -> Unit,
    onFinished: () -> Unit = onBack,
) {
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
            if (plan == null) item { Text(stringResource(R.string.workout_loading)) }
            plan?.let {
                item {
                    Text(it.description)
                    Spacer(Modifier.height(6.dp))
                    Text("${it.durationMinutes} min | ${it.estimatedCalories} kcal", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                item {
                    Button(onStartPlayer, Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.PlayArrow, null)
                        Text(" ${stringResource(R.string.start_guided_workout)}")
                    }
                }
                item {
                    OutlinedButton({ viewModel.toggleFavorite(it.id) }, Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Star, null)
                        Text(if (it.id in state.favoritePlanIds) " ${stringResource(R.string.remove_favorites)}" else " ${stringResource(R.string.add_favorites)}")
                    }
                }
            }
            if (plan != null && exercises.isEmpty()) item { Text(stringResource(R.string.exercises_loading)) }
            itemsIndexed(exercises, key = { _, item -> item.id }) { index, exercise ->
                ExerciseCard(
                    index = index + 1,
                    exercise = exercise,
                    isComplete = exercise.id in completed,
                    timerText = if (activeTimerExercise == exercise.id) "$remainingSeconds sec" else null,
                    isPremiumUser = state.isPremiumUser,
                    onPremium = onPremium,
                    onDetails = { onExercise(exercise.id) },
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
                OutlinedButton(onClick = { showStopWarning = true }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.stop_workout)) }
            }
            item {
                Button(
                    onClick = {
                        viewModel.completeSelectedWorkout()
                        onFinished()
                    },
                    enabled = exercises.isNotEmpty() && completed.size == exercises.size,
                    modifier = Modifier.fillMaxWidth().height(54.dp)
                ) { Text(if (completed.size == exercises.size) stringResource(R.string.finish_workout) else stringResource(R.string.complete_each_exercise)) }
            }
        }
    }
    if (showStopWarning) AlertDialog(
        onDismissRequest = { showStopWarning = false },
        title = { Text(stringResource(R.string.stop_question)) },
        text = { Text(stringResource(R.string.stop_message)) },
        confirmButton = { TextButton({ viewModel.skipSelectedWorkout(); showStopWarning = false; onBack() }) { Text(stringResource(R.string.end_workout)) } },
        dismissButton = { TextButton({ showStopWarning = false }) { Text(stringResource(R.string.continue_safely)) } }
    )
}

@Composable
private fun ExerciseCard(
    index: Int,
    exercise: Exercise,
    isComplete: Boolean,
    timerText: String?,
    isPremiumUser: Boolean,
    onPremium: () -> Unit,
    onDetails: () -> Unit,
    onTimer: () -> Unit,
    onComplete: () -> Unit,
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
            ExerciseIllustration(exercise, Modifier.height(144.dp))
            ExerciseVideoPlayer(exercise, isPremiumUser, onPremium)
            Text("${exercise.muscleGroup} | ${exercise.difficulty} | ${exercise.equipment}", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
            Text("${exercise.sets} sets | ${exercise.repsOrDuration} | ${exercise.restSeconds}s rest | ${exercise.caloriesPerMinute} kcal/min", style = MaterialTheme.typography.bodySmall)
            Text(exercise.instruction, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Safety: ${exercise.safetyTips}", style = MaterialTheme.typography.bodySmall)
            Text("Avoid: ${exercise.commonMistakes}", style = MaterialTheme.typography.bodySmall)
            if (exercise.durationSeconds != null) {
                OutlinedButton(onClick = onTimer) { Text(timerText ?: "Start timer") }
            }
            TextButton(onClick = onDetails) { Text(stringResource(R.string.open_exercise_guide)) }
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
        item { SectionTitle(stringResource(R.string.progress_title), stringResource(R.string.progress_subtitle)) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(stringResource(R.string.total_workouts), "${state.history.size}", Modifier.weight(1f))
                StatCard(stringResource(R.string.this_week), "${state.weeklyCount}", Modifier.weight(1f))
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(stringResource(R.string.calories), "${state.totalCalories}", Modifier.weight(1f))
                StatCard(stringResource(R.string.day_streak), "${state.streak}", Modifier.weight(1f))
            }
        }
        item { SectionTitle(stringResource(R.string.recent_workouts)) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickButton(stringResource(R.string.reports), Modifier.weight(1f)) { onNavigate("fitness-reports") }
                QuickButton(stringResource(R.string.pdf_preview), Modifier.weight(1f)) { onNavigate("progress-report") }
            }
        }
        item {
            if (state.isPremiumUser) {
                Text("Advanced analytics: consistency ${(state.weeklyCount / 7f * 100).toInt()}% • average ${state.history.map { it.durationMinutes }.average().takeIf { !it.isNaN() }?.toInt() ?: 0} min", fontWeight = FontWeight.Bold)
            } else OutlinedButton(onPremium, Modifier.fillMaxWidth()) { Icon(Icons.Default.Lock, null); Text(" ${stringResource(R.string.unlock_advanced_analytics)}") }
        }
        if (state.history.isEmpty()) item { Text(stringResource(R.string.no_history)) }
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
    val user = state.user
    if (user == null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
        ) {
            item { SectionTitle(stringResource(R.string.profile_title), stringResource(R.string.profile_missing)) }
        }
        return
    }
    var showEditor by remember { mutableStateOf(false) }
    var showReset by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { SectionTitle(stringResource(R.string.profile_title), stringResource(R.string.profile_subtitle)) }
        item { ProfileSummary(user, state.settings.unitSystem) }
        item { Text("${stringResource(R.string.language)}: ${AppLocaleManager.languageName(state.settings.selectedLanguageCode)}") }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.dark_mode), Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Switch(user.darkMode, viewModel::setDarkMode)
            }
        }
        item {
            Button(onClick = { showEditor = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Edit, null)
                Text(" ${stringResource(R.string.edit_profile)}")
            }
        }
        item {
            OutlinedButton(onClick = onPremium, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Star, null)
                Text(" ${stringResource(R.string.explore_premium)}")
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
                Text(" ${stringResource(R.string.reset_progress)}")
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
    var error by remember { mutableStateOf<String?>(null) }
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
                item { error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val parsedAge = age.toIntOrNull()
                    val parsedWeight = weight.toFloatOrNull()
                    val parsedHeight = height.toFloatOrNull()
                    if (name.isBlank() || parsedAge == null || parsedAge <= 0 || parsedWeight == null || parsedWeight <= 0f || parsedHeight == null || parsedHeight <= 0f) {
                        error = "Enter a name and positive numbers for age, weight, and height."
                    } else {
                        error = null
                        onSave(name.trim(), parsedAge, parsedWeight, parsedHeight, goal)
                    }
                },
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
        TopAppBar(title = { Text(stringResource(R.string.premium_title)) }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
        })
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(54.dp)) }
            item { Text(stringResource(R.string.unlock_next_level), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold) }
            item { Text(stringResource(R.string.trial_cancel), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
            item { Text(if (state.isPremiumUser) "Premium is active on this device." else "Choose a plan and unlock the complete experience.") }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("$2.99\nMonthly", "$19.99\nYearly\nSave 44%", "$29.99\nLifetime\nLimited offer").forEach {
                        Text(it, Modifier.weight(1f).background(FitnessGreen.copy(alpha = .16f)).padding(10.dp), fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Text(stringResource(R.string.free_vs_premium), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(premiumComparison) { feature -> Text("${feature.title}: ${feature.freeValue} | ${feature.premiumValue}") }
            item { Text("Testimonial placeholder: “The short workout options help me stay consistent.”") }
            item { Text("FAQ: Premium unlocks AI-ready previews, reports, advanced analytics, and unlimited tracking. Billing remains a placeholder until Play Billing is connected.") }
            item {
                // TODO: Replace the local toggle with Google Play Billing purchase verification.
                Button(onClick = { viewModel.setPremium(true) }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.start_trial)) }
            }
            item { OutlinedButton(onClick = { viewModel.setPremium(!state.isPremiumUser) }, modifier = Modifier.fillMaxWidth()) { Text(if (state.isPremiumUser) stringResource(R.string.disable_mock_premium) else stringResource(R.string.unlock_premium)) } }
            item { OutlinedButton({}, Modifier.fillMaxWidth(), enabled = false) { Text(stringResource(R.string.restore_purchase)) } }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton({ onNavigate("terms") }) { Text(stringResource(R.string.terms)) }
                    TextButton({ onNavigate("privacy") }) { Text(stringResource(R.string.privacy)) }
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
