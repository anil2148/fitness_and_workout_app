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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R
import com.example.fitnessworkout.data.MockPremiumPlans
import com.example.fitnessworkout.data.model.AppSettings
import com.example.fitnessworkout.utils.Units
import com.example.fitnessworkout.utils.AppLocaleManager
import com.example.fitnessworkout.utils.CurrencyFormatter
import com.example.fitnessworkout.utils.RegionSettings
import com.example.fitnessworkout.data.model.Exercise
import com.example.fitnessworkout.data.model.UserProfile
import com.example.fitnessworkout.data.model.WorkoutPlan
import com.example.fitnessworkout.data.model.SubscriptionPlanUi
import com.example.fitnessworkout.repository.PremiumPricingRepository
import com.example.fitnessworkout.ui.components.AdBannerPlaceholder
import com.example.fitnessworkout.ui.components.ChallengeProgress
import com.example.fitnessworkout.ui.components.ExerciseIllustration
import com.example.fitnessworkout.ui.components.ExerciseVideoPlayer
import com.example.fitnessworkout.ui.components.SectionTitle
import com.example.fitnessworkout.ui.components.StatCard
import com.example.fitnessworkout.ui.components.WorkoutPlanIllustration
import com.example.fitnessworkout.ui.components.WorkoutPlanCard
import com.example.fitnessworkout.ui.components.localizedOption
import com.example.fitnessworkout.ui.theme.FitnessBlack
import com.example.fitnessworkout.ui.theme.FitnessGreen
import com.example.fitnessworkout.viewmodel.FitnessUiState
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var countryCode by remember { mutableStateOf("US") }
    var currencyCode by remember { mutableStateOf("USD") }
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
                    FilterChip(selected = selectedGoal == goal, onClick = { selectedGoal = goal }, label = { Text(localizedOption(goal)) })
                }
            }
        }
        item { OnboardingChoices(stringResource(R.string.fitness_level), listOf("Beginner", "Intermediate", "Advanced"), level) { level = it } }
        item { OnboardingChoices(stringResource(R.string.available_time), listOf(10, 20, 30, 45).map { "$it min" }, "$minutes min") { minutes = it.substringBefore(" ").toInt() } }
        item { OnboardingChoices(stringResource(R.string.equipment), listOf("No Equipment", "Dumbbells", "Resistance Band", "Gym"), equipment) { equipment = it } }
        item { OnboardingChoices(stringResource(R.string.workout_style), listOf("Balanced", "Strength", "Cardio", "Mobility", "HIIT"), style) { style = it } }
        item { OnboardingChoices(stringResource(R.string.country_region), RegionSettings.supportedCountries.map { it.label }, country) {
            val selected = RegionSettings.country(it)
            country = selected.label
            countryCode = selected.code
            currencyCode = selected.currencyCode
            unitSystem = selected.unitSystem
        } }
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
                            AppSettings(
                                country = country,
                                language = AppLocaleManager.languageName(languageCode),
                                selectedLanguageCode = languageCode,
                                selectedCountryCode = countryCode,
                                selectedCurrencyCode = currencyCode,
                                selectedUnitSystem = unitSystem,
                                unitSystem = unitSystem,
                                dietPreference = diet,
                                workoutLocation = location,
                                injurySafeMode = injurySafeMode,
                            )
                        ) { AppLocaleManager.restartUi(context) }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp).testTag("onboarding_save_button")
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
            options.forEach { FilterChip(selected == it, { choose(it) }, label = { Text(localizedOption(it)) }) }
        }
    }
}

@Composable
private fun ProfileFields(
    name: String, age: String, weight: String, height: String,
    onName: (String) -> Unit, onAge: (String) -> Unit, onWeight: (String) -> Unit, onHeight: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(name, onName, label = { Text(stringResource(R.string.name)) }, modifier = Modifier.fillMaxWidth().testTag("onboarding_name_input"))
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
                StatCard(stringResource(R.string.bmi), "${"%.1f".format(state.user?.bmi ?: 0f)}", Modifier.weight(1f))
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
                            FilterChip(selected = selectedLevel == it, onClick = { selectedLevel = it }, label = { Text(localizedOption(it)) })
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val workoutStarted = stringResource(R.string.workout_started)
    val workoutCompleted = stringResource(R.string.workout_completed_successfully)

    LaunchedEffect(activeTimerExercise, remainingSeconds) {
        if (activeTimerExercise != null && remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds--
        } else if (activeTimerExercise != null && remainingSeconds == 0) {
            activeTimerExercise?.let { if (it !in completed) completed.add(it) }
            activeTimerExercise = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
        TopAppBar(title = { Text(plan?.title ?: stringResource(R.string.workout)) }, navigationIcon = {
            IconButton(onClick = { showStopWarning = true }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) }
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
                    Text(stringResource(R.string.workout_summary, it.durationMinutes, it.estimatedCalories), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                item {
                    Button({
                        scope.launch { snackbarHostState.showSnackbar(workoutStarted) }
                        onStartPlayer()
                    }, Modifier.fillMaxWidth().testTag("workout_start_button")) {
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
                        scope.launch { snackbarHostState.showSnackbar(workoutCompleted) }
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
                IconButton(onClick = onComplete) { Icon(Icons.Default.CheckCircle, stringResource(R.string.mark_complete), tint = if (isComplete) MaterialTheme.colorScheme.primary else Color.Gray) }
            }
            ExerciseIllustration(exercise, Modifier.height(144.dp))
            ExerciseVideoPlayer(exercise, isPremiumUser, onPremium)
            Text(stringResource(R.string.exercise_traits, exercise.muscleGroup, exercise.difficulty, exercise.equipment), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
            Text(stringResource(R.string.exercise_stats, exercise.sets, exercise.repsOrDuration, exercise.restSeconds, exercise.caloriesPerMinute), style = MaterialTheme.typography.bodySmall)
            Text(exercise.instruction, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(stringResource(R.string.safety_value, exercise.safetyTips), style = MaterialTheme.typography.bodySmall)
            Text(stringResource(R.string.avoid_value, exercise.commonMistakes), style = MaterialTheme.typography.bodySmall)
            if (exercise.durationSeconds != null) {
                OutlinedButton(onClick = onTimer) { Text(timerText ?: stringResource(R.string.start_timer)) }
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
                Text(stringResource(R.string.advanced_analytics_value, (state.weeklyCount / 7f * 100).toInt(), state.history.map { it.durationMinutes }.average().takeIf { !it.isNaN() }?.toInt() ?: 0), fontWeight = FontWeight.Bold)
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
        item { Text(stringResource(R.string.label_value, stringResource(R.string.language), AppLocaleManager.languageName(state.settings.selectedLanguageCode))) }
        item { Text(stringResource(R.string.label_value, stringResource(R.string.currency), state.settings.selectedCurrencyCode)) }
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
            listOf("water" to R.string.water_tracker, "calculators" to R.string.health_calculators, "diet" to R.string.diet_guidance,
                "custom" to R.string.custom_plan_builder, "reminders" to R.string.daily_reminders, "measurements" to R.string.body_measurements,
                "photos" to R.string.progress_photos, "achievements" to R.string.achievements, "fitness-test" to R.string.fitness_level_test,
                "share" to R.string.workout_share_card, "privacy" to R.string.privacy_policy, "terms" to R.string.terms,
                "medical" to R.string.medical_disclaimer_title, "feedback" to R.string.feedback, "delete-data" to R.string.delete_all_data,
                "settings" to R.string.settings_title, "regional-pricing" to R.string.regional_pricing_preview, "quick-workout" to R.string.quick_workout,
                "fitness-score" to R.string.fitness_score, "ai-workout" to R.string.ai_workout_coach, "ai-meal" to R.string.ai_meal_suggestions,
                "ai-progress" to R.string.ai_progress_analysis, "ai-chat" to R.string.ai_motivation_chat, "progress-report" to R.string.progress_report_preview,
                "safety" to R.string.safety_and_trust, "about" to R.string.about_app, "contact-support" to R.string.contact_support,
                "rate-app" to R.string.rate_app, "share-app" to R.string.share_app, "data-safety" to R.string.data_safety,
                "announcements" to R.string.announcements, "content-categories" to R.string.workout_diet_categories,
                "daily-habits" to R.string.daily_habits, "fitness-reports" to R.string.weekly_monthly_reports,
                "favorites" to R.string.favorite_workouts).forEach { (route, label) ->
                OutlinedButton({ onNavigate(route) }, Modifier.fillMaxWidth()) { Text(stringResource(label)) }
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
        title = { Text(stringResource(R.string.reset_progress_question)) },
        text = { Text(stringResource(R.string.reset_progress_body)) },
        confirmButton = { TextButton(onClick = { viewModel.resetProgress(); showReset = false }) { Text(stringResource(R.string.reset)) } },
        dismissButton = { TextButton(onClick = { showReset = false }) { Text(stringResource(R.string.cancel)) } }
    )
}

@Composable
private fun ProfileSummary(user: UserProfile, unitSystem: String) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = FitnessBlack)) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(user.name, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.profile_age_value, user.age, Units.weight(user.weightKg, unitSystem), Units.length(user.heightCm, unitSystem)), color = Color.LightGray)
            Text(stringResource(R.string.goal_value, user.fitnessGoal), color = FitnessGreen, fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.bmi_value, "%.1f".format(user.bmi), bmiLabel(user.bmi)), color = Color.White)
        }
    }
}

@Composable
private fun bmiLabel(bmi: Float) = stringResource(when {
    bmi < 18.5f -> R.string.bmi_below
    bmi < 25f -> R.string.bmi_healthy
    bmi < 30f -> R.string.bmi_above
    else -> R.string.bmi_high
})

@Composable
private fun EditProfileDialog(user: UserProfile, onDismiss: () -> Unit, onSave: (String, Int, Float, Float, String) -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(user.name) }
    var age by remember { mutableStateOf(user.age.toString()) }
    var weight by remember { mutableStateOf(user.weightKg.toString()) }
    var height by remember { mutableStateOf(user.heightCm.toString()) }
    var goal by remember { mutableStateOf(user.fitnessGoal) }
    var error by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_profile)) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { ProfileFields(name, age, weight, height, { name = it }, { age = it }, { weight = it }, { height = it }) }
                item {
                    goals.forEach { option ->
                        FilterChip(selected = goal == option, onClick = { goal = option }, label = { Text(localizedOption(option)) })
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
                        error = context.getString(R.string.profile_validation_error)
                    } else {
                        error = null
                        onSave(name.trim(), parsedAge, parsedWeight, parsedHeight, goal)
                    }
                },
            ) { Text(stringResource(R.string.save)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(viewModel: FitnessViewModel, onBack: () -> Unit, onNavigate: (String) -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val pricingRepository = remember(state.settings.selectedCurrencyCode) { PremiumPricingRepository(state.settings.selectedCurrencyCode) }
    var subscriptionPlans by remember(pricingRepository) { mutableStateOf(pricingRepository.getSubscriptionPlans()) }
    var promotionalPlans by remember(pricingRepository) { mutableStateOf(pricingRepository.getPromotionalPlans()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val premiumUnlocked = stringResource(R.string.premium_unlocked_successfully)
    val selectedPlan = (subscriptionPlans + promotionalPlans).firstOrNull { it.isSelected }
    val selectPlan: (SubscriptionPlanUi) -> Unit = { plan ->
        pricingRepository.selectPlan(plan.productId, plan.offerId)
        subscriptionPlans = pricingRepository.getSubscriptionPlans()
        promotionalPlans = pricingRepository.getPromotionalPlans()
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
        TopAppBar(title = { Text(stringResource(R.string.premium_title)) }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) }
        })
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(54.dp)) }
            item { Text(stringResource(R.string.unlock_next_level), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold) }
            if (state.isFirstMonthFreeActive) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(stringResource(R.string.first_month_free_active), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(stringResource(R.string.first_month_free_days_remaining, state.premiumTrialDaysRemaining), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text(stringResource(R.string.first_month_free_body), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            promotionalPlans.firstOrNull { it.trialText != null }?.trialText?.let { trial ->
                item { Text(stringResource(R.string.trial_cancel_value, premiumText(trial)), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
            }
            item {
                Text(
                    stringResource(
                        when {
                            state.isFirstMonthFreeActive -> R.string.first_month_free_status
                            state.isPremiumUser -> R.string.premium_active
                            else -> R.string.choose_plan
                        }
                    )
                )
            }
            item { Text(stringResource(R.string.premium_plans), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(subscriptionPlans, key = { "${it.productId}:${it.offerId.orEmpty()}" }) { plan ->
                SubscriptionPlanCard(plan, state.settings.selectedLanguageCode, onClick = { selectPlan(plan) })
            }
            item { Text(stringResource(R.string.promotional_offers), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(promotionalPlans, key = { "${it.productId}:${it.offerId.orEmpty()}" }) { plan ->
                SubscriptionPlanCard(plan, state.settings.selectedLanguageCode, onClick = { selectPlan(plan) })
            }
            item { Text(stringResource(R.string.referral_and_promo), style = MaterialTheme.typography.bodySmall) }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton({ onNavigate("promo-code") }, Modifier.weight(1f)) { Text(stringResource(R.string.promo_code)) }
                    OutlinedButton({ onNavigate("referral-code") }, Modifier.weight(1f)) { Text(stringResource(R.string.referral_code)) }
                }
            }
            item { OutlinedButton({ onNavigate("affiliate-store") }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.affiliate_store)) } }
            item { Text(stringResource(R.string.free_vs_premium), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(premiumComparison) { feature -> Text(stringResource(feature)) }
            item { Text(stringResource(R.string.premium_testimonial)) }
            item { Text(stringResource(R.string.premium_faq)) }
            item {
                // TODO: Replace the local toggle with Google Play Billing purchase verification.
                Button(onClick = {
                    viewModel.setPremium(true)
                    scope.launch { snackbarHostState.showSnackbar(premiumUnlocked) }
                }, modifier = Modifier.fillMaxWidth().testTag("premium_unlock_button")) {
                    Text(stringResource(R.string.enable_mock_plan, selectedPlan?.let { premiumTitle(it) } ?: stringResource(R.string.premium_title)))
                }
            }
            item {
                OutlinedButton(onClick = { viewModel.setPremium(!state.isMockPremiumUser) }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (state.isMockPremiumUser) stringResource(R.string.disable_mock_premium) else stringResource(R.string.unlock_premium))
                }
            }
            item { OutlinedButton({}, Modifier.fillMaxWidth(), enabled = false) { Text(stringResource(R.string.restore_purchase)) } }
            item { Text(stringResource(R.string.mock_purchase_notice), style = MaterialTheme.typography.bodySmall) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton({ onNavigate("terms") }) { Text(stringResource(R.string.terms)) }
                    TextButton({ onNavigate("privacy") }) { Text(stringResource(R.string.privacy)) }
                }
            }
        }
    }
}

@Composable
private fun SubscriptionPlanCard(plan: SubscriptionPlanUi, languageCode: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (plan.isSelected) FitnessGreen.copy(alpha = .2f) else MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(premiumTitle(plan), fontWeight = FontWeight.Bold)
                plan.offerBadge?.let { Text(premiumText(it), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
            }
            Text(premiumDescription(plan), style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                plan.originalPriceAmount?.let { Text(CurrencyFormatter.format(it, plan.currencyCode, languageCode), textDecoration = TextDecoration.LineThrough, style = MaterialTheme.typography.bodySmall) }
                Text(CurrencyFormatter.format(plan.priceAmount, plan.currencyCode, languageCode), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                Text(premiumText(plan.billingPeriodText))
            }
            plan.discountText?.let { Text(premiumText(it), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
            plan.trialText?.let { Text(premiumText(it), color = MaterialTheme.colorScheme.primary) }
            if (plan.isLifetime) Text(stringResource(R.string.lifetime_purchase), style = MaterialTheme.typography.bodySmall)
            if (plan.isSelected) Text(stringResource(R.string.selected), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun premiumTitle(plan: SubscriptionPlanUi): String = stringResource(when (plan.offerId) {
    "monthly-intro-099" -> R.string.monthly_intro_offer
    "yearly-50-off" -> R.string.yearly_limited_offer
    "yearly-free-trial-7-days" -> R.string.yearly_free_trial
    "new-year-offer" -> R.string.yearly_festival_offer
    else -> when (plan.productId) {
        "premium_monthly" -> R.string.monthly_premium
        "premium_yearly" -> R.string.yearly_premium
        else -> R.string.lifetime_premium
    }
})

@Composable
private fun premiumDescription(plan: SubscriptionPlanUi): String = stringResource(when (plan.offerId) {
    "monthly-intro-099" -> R.string.monthly_intro_description
    "yearly-50-off" -> R.string.yearly_limited_description
    "yearly-free-trial-7-days" -> R.string.yearly_trial_description
    "new-year-offer" -> R.string.yearly_festival_description
    else -> when (plan.productId) {
        "premium_monthly" -> R.string.monthly_premium_description
        "premium_yearly" -> R.string.yearly_premium_description
        else -> R.string.lifetime_premium_description
    }
})

@Composable
private fun premiumText(value: String): String = stringResource(when (value) {
    "period_month" -> R.string.period_month
    "period_year" -> R.string.period_year
    "period_one_time" -> R.string.period_one_time
    "period_first_month" -> R.string.period_first_month
    "period_year_after_trial" -> R.string.period_year_after_trial
    "save_44" -> R.string.save_44
    "best_value" -> R.string.best_value
    "lifetime" -> R.string.lifetime
    "first_month_offer" -> R.string.first_month_offer
    "intro_offer" -> R.string.intro_offer
    "yearly_50_off" -> R.string.yearly_50_off
    "limited_time_discount" -> R.string.limited_time_discount
    "seven_days_free" -> R.string.seven_days_free
    "free_trial" -> R.string.free_trial
    "festival_savings" -> R.string.festival_savings
    "new_year_offer" -> R.string.new_year_offer
    else -> R.string.premium_title
})

private val premiumComparison = listOf(
    R.string.compare_progress,
    R.string.compare_plans,
    R.string.compare_analytics,
    R.string.compare_photos,
    R.string.compare_ads,
)
