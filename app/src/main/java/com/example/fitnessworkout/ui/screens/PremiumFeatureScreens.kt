package com.example.fitnessworkout.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.fitnessworkout.data.model.ReminderSettings
import com.example.fitnessworkout.ui.components.ChallengeProgress
import com.example.fitnessworkout.ui.components.SectionTitle
import com.example.fitnessworkout.ui.components.WorkoutPlanCard
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import com.example.fitnessworkout.utils.Units

@Composable fun ChallengesScreen(vm: FitnessViewModel, padding: PaddingValues, openPlan: (Int) -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { SectionTitle("30-day challenges", "Beginner fitness is free. Goal challenges unlock with Premium.") }
        item { ChallengeProgress(state.challengePlans.count { it.id in state.completedPlanIds }) }
        items(state.challengePlans) { plan -> val locked = plan.premiumOnly && !state.isPremiumUser; WorkoutPlanCard(plan, { if (locked) premium() else openPlan(plan.id) }, locked = locked) }
    }
}

@Composable fun WaterScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); var custom by remember { mutableStateOf("") }
    FeaturePage("Water tracker", back) {
        Text("${Units.water(state.water.amountMl, state.settings.unitSystem)} / ${Units.water(state.water.goalMl, state.settings.unitSystem)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        LinearProgressIndicator({ (state.water.amountMl / state.water.goalMl.toFloat()).coerceIn(0f, 1f) }, Modifier.fillMaxWidth())
        Button({ vm.addWater(250) }, Modifier.fillMaxWidth()) { Text("Add 250 ml") }
        Field(custom, { custom = it }, "Custom amount (ml)", true)
        OutlinedButton({ custom.toIntOrNull()?.let(vm::addWater); custom = "" }, Modifier.fillMaxWidth()) { Text("Add custom amount") }
        TextButton(vm::resetWater, Modifier.fillMaxWidth()) { Text("Reset today") }
    }
}

@Composable fun CalculatorScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); val user = state.user
    var weight by remember { mutableStateOf(user?.weightKg?.toString().orEmpty()) }; var height by remember { mutableStateOf(user?.heightCm?.toString().orEmpty()) }; var age by remember { mutableStateOf(user?.age?.toString().orEmpty()) }
    FeaturePage("Health calculators", back) {
        Field(weight, { weight = it }, "Weight (kg)", true); Field(height, { height = it }, "Height (cm)", true); Field(age, { age = it }, "Age", true)
        Button({ val w = weight.toFloatOrNull(); val h = height.toFloatOrNull(); val a = age.toIntOrNull(); if (w != null && h != null && a != null) vm.calculateHealth(w, h, a) }, Modifier.fillMaxWidth()) { Text("Calculate BMI, BMR, calories and water") }
        state.healthMetric?.let { Text("BMI ${"%.1f".format(it.bmi)} • ${bmiLabel(it.bmi)}\nBMR ${it.bmr.toInt()} kcal • Daily needs ${it.calorieNeeds.toInt()} kcal\nWater ${it.waterMl} ml • Ideal weight ${"%.1f".format(it.idealWeightMin)}-${"%.1f".format(it.idealWeightMax)} kg") }
        Text("This is only an estimate. Consult a healthcare professional for medical guidance.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable fun DietScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    if (!state.isPremiumUser) return LockedFeature("Diet and meal guidance", premium, back)
    FeaturePage("Diet guidance", back) {
        Text("Balanced daily guidance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Protein 30% • Carbs 45% • Healthy fats 25%\nRecommended water: 2.5-3.0 L")
        listOf("Weight Loss", "Muscle Gain", "Balanced", "Vegetarian", "Indian diet option", "Global high protein", "Mediterranean", "Vegan", "Keto", "Low carb", "Halal-friendly", "Gluten-free", "Dairy-free", "Budget meals").forEach { Text("• $it meal plan") }
        Text("This app provides general fitness and nutrition information only and is not medical advice.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable fun CustomPlanScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    if (!state.isPremiumUser) return LockedFeature("Custom workout plans", premium, back)
    var goal by remember { mutableStateOf("Stay Fit") }; var level by remember { mutableStateOf("Beginner") }; var minutes by remember { mutableStateOf(20) }; var equipment by remember { mutableStateOf("No Equipment") }; var focus by remember { mutableStateOf("Full Body") }
    FeaturePage("Custom plan builder", back) {
        Choice("Goal", goal, listOf("Lose Weight", "Build Muscle", "Stay Fit", "Improve Stamina")) { goal = it }
        Choice("Level", level, listOf("Beginner", "Intermediate", "Advanced")) { level = it }
        Choice("Time", "$minutes min", listOf("10 min", "20 min", "30 min", "45 min")) { minutes = it.substringBefore(" ").toInt() }
        Choice("Equipment", equipment, listOf("No Equipment", "Dumbbells", "Resistance Band", "Gym")) { equipment = it }
        Choice("Body focus", focus, listOf("Full Body", "Chest", "Back", "Legs", "Shoulders", "Arms", "Abs", "Cardio")) { focus = it }
        Button({ vm.generateCustomPlan(goal, level, minutes, equipment, focus) }, Modifier.fillMaxWidth()) { Text("Generate AI-style local plan") }
        state.customPlans.forEach { Text("• ${it.generatedTitle} • ${it.level} • ${it.equipment} • ${it.bodyFocus}") }
    }
}

@Composable fun ReminderScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); var time by remember(state.reminders) { mutableStateOf(state.reminders.workoutTime) }; var water by remember(state.reminders) { mutableStateOf(state.reminders.waterReminder) }; var meal by remember(state.reminders) { mutableStateOf(state.reminders.mealReminder) }; var weight by remember(state.reminders) { mutableStateOf(state.reminders.weightCheckIn) }; var photoDay by remember(state.reminders) { mutableStateOf(state.reminders.progressPhotoDay) }
    FeaturePage("Daily reminders", back) {
        Field(time, { time = it }, "Workout reminder time")
        Toggle("Water reminder", water) { water = it }; Toggle("Meal reminder", meal) { meal = it }; Toggle("Weight check-in", weight) { weight = it }
        Choice("Progress photo day", photoDay, listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")) { photoDay = it }
        // TODO: Schedule local notifications with WorkManager after notification permission UX is added.
        Button({ vm.saveReminders(ReminderSettings(workoutTime = time, waterReminder = water, mealReminder = meal, weightCheckIn = weight, progressPhotoDay = photoDay)) }, Modifier.fillMaxWidth()) { Text("Save reminder preferences") }
    }
}

@Composable private fun LockedFeature(title: String, premium: () -> Unit, back: () -> Unit) = FeaturePage(title, back) { Icon(Icons.Default.Lock, null); Text("This feature is available with Premium."); Button(premium) { Text("View Premium plans") } }
@Composable private fun Toggle(label: String, checked: Boolean, change: (Boolean) -> Unit) = Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text(label, Modifier.weight(1f)); Switch(checked, change) }
@Composable private fun Field(value: String, change: (String) -> Unit, label: String, number: Boolean = false) = OutlinedTextField(value, change, Modifier.fillMaxWidth(), label = { Text(label) }, keyboardOptions = KeyboardOptions(keyboardType = if (number) KeyboardType.Number else KeyboardType.Text))
@Composable private fun Choice(label: String, value: String, options: List<String>, change: (String) -> Unit) { var open by remember { mutableStateOf(false) }; Box { OutlinedButton({ open = true }, Modifier.fillMaxWidth()) { Text("$label: $value") }; DropdownMenu(open, { open = false }) { options.forEach { DropdownMenuItem({ Text(it) }, { change(it); open = false }) } } } }
@OptIn(ExperimentalMaterial3Api::class) @Composable private fun FeaturePage(title: String, back: () -> Unit, content: @Composable ColumnScope.() -> Unit) = Scaffold(topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }) { padding -> Column(Modifier.fillMaxSize().padding(padding).padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content) }
private fun bmiLabel(bmi: Float) = when { bmi < 18.5f -> "Underweight"; bmi < 25f -> "Normal"; bmi < 30f -> "Overweight"; else -> "Obese" }
