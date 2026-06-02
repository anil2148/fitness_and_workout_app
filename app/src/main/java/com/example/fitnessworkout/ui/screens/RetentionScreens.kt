package com.example.fitnessworkout.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fitnessworkout.data.model.BodyMeasurement
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import com.example.fitnessworkout.utils.Units
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R
import java.time.Instant
import java.time.ZoneId

@Composable fun MeasurementTrackerScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState(); var values by remember { mutableStateOf(List(7) { "" }) }; var error by remember { mutableStateOf<String?>(null) }
    TrackerPage("Body measurements", back) {
        item { Text(if (state.isPremiumUser) "Premium history is unlimited." else "Free plan: ${state.measurements.size} / 3 entries used.") }
        item { listOf("Weight kg", "Waist cm", "Chest cm", "Arms cm", "Thighs cm", "Hips cm", "Body fat %").forEachIndexed { i, label -> NumberField(values[i], { text -> values = values.toMutableList().also { it[i] = text } }, label) } }
        item { error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        item { Button({
            val parsed = values.map { if (it.isBlank()) 0f else it.toFloatOrNull() }
            if (parsed[0] == null || parsed[0]!! <= 0f || parsed.any { it == null || it < 0f } || parsed[6]!! > 100f) error = "Enter a positive weight, non-negative measurements, and body fat from 0-100."
            else { vm.saveMeasurement(BodyMeasurement(weightKg = parsed[0]!!, waistCm = parsed[1]!!, chestCm = parsed[2]!!, armsCm = parsed[3]!!, thighsCm = parsed[4]!!, hipsCm = parsed[5]!!, bodyFatPercent = parsed[6]!!)); error = null }
        }, Modifier.fillMaxWidth()) { Text("Save measurement") } }
        if (!state.isPremiumUser && state.measurements.size >= 3) item { OutlinedButton(premium, Modifier.fillMaxWidth()) { Text("Unlock full measurement history") } }
        items(state.measurements) { m -> Text("${date(m.recordedAt)} • ${Units.weight(m.weightKg, state.settings.unitSystem)} • waist ${Units.length(m.waistCm, state.settings.unitSystem)} • body fat ${m.bodyFatPercent}%") }
    }
}

@Composable fun ProgressPhotosScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> uri?.let { vm.savePhoto(it.toString()) } }
    TrackerPage("Progress photos", back) {
        item { Text(if (state.isPremiumUser) "Unlimited local progress photos." else "Free plan: ${state.photos.size} / 2 photos used.") }
        item { Button({ picker.launch("image/*") }, Modifier.fillMaxWidth(), enabled = state.isPremiumUser || state.photos.size < 2) { Text("Choose local photo") } }
        if (!state.isPremiumUser && state.photos.size >= 2) item { OutlinedButton(premium, Modifier.fillMaxWidth()) { Text("Unlock unlimited photos") } }
        if (state.photos.size >= 2) item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { PhotoCard("Before", state.photos.last().imageUri, Modifier.weight(1f)); PhotoCard("After", state.photos.first().imageUri, Modifier.weight(1f)) } }
        items(state.photos) { photo -> Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text("${date(photo.createdAt)} • ${photo.imageUri.take(34)}", Modifier.weight(1f)); TextButton({ vm.deletePhoto(photo.id) }) { Text("Remove") } } }
    }
}

@Composable fun AchievementScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    TrackerPage("Achievements", back) {
        item { Text("Streak reward: ${state.streak} day streak", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        if (state.achievements.isEmpty()) item { Text("Complete a workout or fitness test to unlock your first badge.") }
        items(state.achievements) { Text("★ ${it.title}\n${it.description}") }
    }
}

@Composable fun FitnessTestScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); var values by remember { mutableStateOf(List(4) { "" }) }; var error by remember { mutableStateOf<String?>(null) }
    TrackerPage("Fitness level test", back) {
        item { listOf("Push-ups", "Plank seconds", "Squats", "Resting heart rate").forEachIndexed { i, label -> NumberField(values[i], { text -> values = values.toMutableList().also { it[i] = text } }, label) } }
        item { error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        item { Button({ val input = values.map { it.toIntOrNull() }; if (input.any { it == null } || input.take(3).any { it!! < 0 } || (input[3] ?: 0) <= 0) error = "Enter non-negative exercise results and a positive resting heart rate." else { vm.saveFitnessTest(input[0]!!, input[1]!!, input[2]!!, input[3]!!); error = null } }, Modifier.fillMaxWidth()) { Text("Save fitness test") } }
        items(state.fitnessTests) { Text("${date(it.recordedAt)} • Score ${it.score} • ${it.pushUps} push-ups • ${it.plankSeconds}s plank") }
    }
}

@Composable fun ShareWorkoutScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); val summary = state.shareSummaries.firstOrNull(); val context = LocalContext.current
    TrackerPage("Workout share card", back) {
        item { Text(summary?.let { "${it.workoutTitle}\n${it.durationMinutes} min • ${it.caloriesBurned} kcal • ${it.streak} day streak" } ?: "Complete a workout to generate a share card.", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        item { OutlinedButton({
            summary?.let {
                val text = "I completed ${it.workoutTitle}: ${it.durationMinutes} min, ${it.caloriesBurned} kcal, ${it.streak}-day streak."
                context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text) }, "Share workout"))
            }
        }, Modifier.fillMaxWidth(), enabled = summary != null) { Icon(Icons.Default.Share, null); Text(" Share workout") } }
    }
}

@Composable fun PrivacyPolicyScreen(back: () -> Unit) = LegalScreen("Privacy policy", "Your fitness data stays on this device. Progress photo URIs are stored locally. No analytics or advertising SDK is connected.", back)
@Composable fun TermsScreen(back: () -> Unit) = LegalScreen("Terms", "Use the app responsibly. Premium purchase buttons are local mock controls until billing integration is added.", back)
@Composable fun MedicalDisclaimerScreen(back: () -> Unit) = LegalScreen("Medical disclaimer", stringResource(R.string.medical_disclaimer), back)
@Composable fun DeleteAllDataScreen(vm: FitnessViewModel, back: () -> Unit) { var confirm by remember { mutableStateOf(false) }; LegalScreen("Delete all data", "This removes your local profile, tracking history, photos, tests, reminders, and generated plans.", back) { Button({ confirm = true }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Delete local data") } }; if (confirm) AlertDialog(onDismissRequest = { confirm = false }, title = { Text("Delete all local data?") }, text = { Text("This cannot be undone.") }, confirmButton = { TextButton({ vm.deleteAllData(); confirm = false; back() }) { Text("Delete") } }, dismissButton = { TextButton({ confirm = false }) { Text("Cancel") } }) }

@Composable private fun PhotoCard(label: String, uri: String, modifier: Modifier) = Card(modifier) { Column(Modifier.padding(12.dp)) { Text(label, fontWeight = FontWeight.Bold); AsyncImage(model = uri, contentDescription = label, modifier = Modifier.fillMaxWidth().height(160.dp)); Text(uri.take(32), style = MaterialTheme.typography.bodySmall) } }
@Composable private fun LegalScreen(title: String, text: String, back: () -> Unit, content: @Composable ColumnScope.() -> Unit = {}) = TrackerPage(title, back) { item { Text(text) }; item { Column(content = content) } }
@OptIn(ExperimentalMaterial3Api::class) @Composable private fun TrackerPage(title: String, back: () -> Unit, content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) = Scaffold(topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }) { padding -> LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content) }
@Composable private fun NumberField(value: String, change: (String) -> Unit, label: String) = OutlinedTextField(value, change, Modifier.fillMaxWidth(), label = { Text(label) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
private fun date(timestamp: Long) = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate().toString()
