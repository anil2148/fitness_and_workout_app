package com.example.fitnessworkout.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R
import com.example.fitnessworkout.data.model.ReminderSettings
import com.example.fitnessworkout.ui.components.ChallengeProgress
import com.example.fitnessworkout.ui.components.SectionTitle
import com.example.fitnessworkout.ui.components.WorkoutPlanCard
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import com.example.fitnessworkout.utils.Units
import com.example.fitnessworkout.ui.components.localizedOption

@Composable fun ChallengesScreen(vm: FitnessViewModel, padding: PaddingValues, openPlan: (Int) -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { SectionTitle(stringResource(R.string.challenges_title), stringResource(R.string.challenges_subtitle)) }
        item { ChallengeProgress(state.challengePlans.count { it.id in state.completedPlanIds }) }
        if (state.challengePlans.isEmpty()) item { Text(stringResource(R.string.challenge_empty)) }
        items(state.challengePlans) { plan -> val locked = plan.premiumOnly && !state.isPremiumUser; WorkoutPlanCard(plan, { if (locked) premium() else openPlan(plan.id) }, locked = locked) }
    }
}

@Composable fun WaterScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); var custom by remember { mutableStateOf("") }; var error by remember { mutableStateOf<String?>(null) }
    val waterAmountError = stringResource(R.string.water_amount_error)
    FeaturePage(stringResource(R.string.water_tracker), back) {
        Text("${Units.water(state.water.amountMl, state.settings.unitSystem)} / ${Units.water(state.water.goalMl, state.settings.unitSystem)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        LinearProgressIndicator({ (state.water.amountMl / state.water.goalMl.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f) }, Modifier.fillMaxWidth())
        Button({ vm.addWater(250) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.add_250_ml)) }
        Field(custom, { custom = it }, stringResource(R.string.custom_amount_ml), true)
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        OutlinedButton({
            val amount = custom.toIntOrNull()
            if (amount == null || amount <= 0) error = waterAmountError
            else { vm.addWater(amount); custom = ""; error = null }
        }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.add_custom_amount)) }
        TextButton(vm::resetWater, Modifier.fillMaxWidth()) { Text(stringResource(R.string.reset_today)) }
    }
}

@Composable fun CalculatorScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); val user = state.user
    var weight by remember { mutableStateOf(user?.weightKg?.toString().orEmpty()) }; var height by remember { mutableStateOf(user?.heightCm?.toString().orEmpty()) }; var age by remember { mutableStateOf(user?.age?.toString().orEmpty()) }; var error by remember { mutableStateOf<String?>(null) }
    val calculatorError = stringResource(R.string.calculator_error)
    FeaturePage(stringResource(R.string.health_calculators), back) {
        Field(weight, { weight = it }, stringResource(R.string.weight_kg), true); Field(height, { height = it }, stringResource(R.string.height_cm), true); Field(age, { age = it }, stringResource(R.string.age), true)
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button({
            val w = weight.toFloatOrNull(); val h = height.toFloatOrNull(); val a = age.toIntOrNull()
            if (w == null || h == null || a == null || w <= 0 || h <= 0 || a <= 0) error = calculatorError
            else { vm.calculateHealth(w, h, a); error = null }
        }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.calculate_health)) }
        state.healthMetric?.let { Text(stringResource(R.string.health_metric_summary, "%.1f".format(it.bmi), bmiLabel(it.bmi), it.bmr.toInt(), it.calorieNeeds.toInt(), it.waterMl, "%.1f".format(it.idealWeightMin), "%.1f".format(it.idealWeightMax))) }
        Text(stringResource(R.string.estimate_disclaimer), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable fun DietScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    if (!state.isPremiumUser) return LockedFeature(stringResource(R.string.diet_meal_guidance), premium, back)
    FeaturePage(stringResource(R.string.diet_guidance), back) {
        Text(stringResource(R.string.balanced_daily_guidance), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.balanced_daily_summary))
        Text(stringResource(R.string.meal_plan_examples))
        Text(stringResource(R.string.nutrition_disclaimer), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable fun CustomPlanScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    if (!state.isPremiumUser) return LockedFeature(stringResource(R.string.custom_workout_plans), premium, back)
    var goal by remember { mutableStateOf("Stay Fit") }; var level by remember { mutableStateOf("Beginner") }; var minutes by remember { mutableStateOf(20) }; var equipment by remember { mutableStateOf("No Equipment") }; var focus by remember { mutableStateOf("Full Body") }
    FeaturePage(stringResource(R.string.custom_plan_builder), back) {
        Choice(stringResource(R.string.goal), goal, listOf("Lose Weight", "Build Muscle", "Stay Fit", "Improve Stamina")) { goal = it }
        Choice(stringResource(R.string.level), level, listOf("Beginner", "Intermediate", "Advanced")) { level = it }
        Choice(stringResource(R.string.time), "$minutes min", listOf("10 min", "20 min", "30 min", "45 min")) { minutes = it.substringBefore(" ").toInt() }
        Choice(stringResource(R.string.equipment), equipment, listOf("No Equipment", "Dumbbells", "Resistance Band", "Gym")) { equipment = it }
        Choice(stringResource(R.string.body_focus), focus, listOf("Full Body", "Chest", "Back", "Legs", "Shoulders", "Arms", "Abs", "Cardio")) { focus = it }
        Button({ vm.generateCustomPlan(goal, level, minutes, equipment, focus) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.generate_local_plan)) }
        state.customPlans.forEach { Text("• ${it.generatedTitle} • ${it.level} • ${it.equipment} • ${it.bodyFocus}") }
    }
}

@Composable fun ReminderScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); var time by remember(state.reminders) { mutableStateOf(state.reminders.workoutTime) }; var water by remember(state.reminders) { mutableStateOf(state.reminders.waterReminder) }; var meal by remember(state.reminders) { mutableStateOf(state.reminders.mealReminder) }; var weight by remember(state.reminders) { mutableStateOf(state.reminders.weightCheckIn) }; var photoDay by remember(state.reminders) { mutableStateOf(state.reminders.progressPhotoDay) }
    FeaturePage(stringResource(R.string.daily_reminders), back) {
        Field(time, { time = it }, stringResource(R.string.workout_reminder_time))
        Toggle(stringResource(R.string.water_reminder), water) { water = it }; Toggle(stringResource(R.string.meal_reminder), meal) { meal = it }; Toggle(stringResource(R.string.weight_check_in), weight) { weight = it }
        Choice(stringResource(R.string.progress_photo_day), photoDay, listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")) { photoDay = it }
        // TODO: Schedule local notifications with WorkManager after notification permission UX is added.
        Button({ vm.saveReminders(ReminderSettings(workoutTime = time, waterReminder = water, mealReminder = meal, weightCheckIn = weight, progressPhotoDay = photoDay)) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.save_reminder_preferences)) }
    }
}

@Composable private fun LockedFeature(title: String, premium: () -> Unit, back: () -> Unit) = FeaturePage(title, back) { Icon(Icons.Default.Lock, null); Text(stringResource(R.string.feature_available_premium)); Button(premium) { Text(stringResource(R.string.view_premium_plans)) } }
@Composable private fun Toggle(label: String, checked: Boolean, change: (Boolean) -> Unit) = Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text(label, Modifier.weight(1f)); Switch(checked, change) }
@Composable private fun Field(value: String, change: (String) -> Unit, label: String, number: Boolean = false) = OutlinedTextField(value, change, Modifier.fillMaxWidth(), label = { Text(label) }, keyboardOptions = KeyboardOptions(keyboardType = if (number) KeyboardType.Number else KeyboardType.Text))
@Composable private fun Choice(label: String, value: String, options: List<String>, change: (String) -> Unit) { var open by remember { mutableStateOf(false) }; Box { OutlinedButton({ open = true }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.label_value, label, localizedOption(value))) }; DropdownMenu(open, { open = false }) { options.forEach { DropdownMenuItem({ Text(localizedOption(it)) }, { change(it); open = false }) } } } }
@OptIn(ExperimentalMaterial3Api::class) @Composable private fun FeaturePage(title: String, back: () -> Unit, content: @Composable ColumnScope.() -> Unit) = Scaffold(topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) } }) }) { padding -> Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content) }
@Composable private fun bmiLabel(bmi: Float) = stringResource(when { bmi < 18.5f -> R.string.bmi_underweight; bmi < 25f -> R.string.bmi_normal; bmi < 30f -> R.string.bmi_overweight; else -> R.string.bmi_obese })
