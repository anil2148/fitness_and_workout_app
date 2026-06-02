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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable fun MeasurementTrackerScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState(); var values by remember { mutableStateOf(List(7) { "" }) }; var error by remember { mutableStateOf<String?>(null) }
    val fullError = stringResource(R.string.measurement_free_full)
    val validationError = stringResource(R.string.measurement_validation_error)
    TrackerPage(stringResource(R.string.body_measurements), back) {
        item { Text(if (state.isPremiumUser) stringResource(R.string.premium_history_unlimited) else stringResource(R.string.free_measurement_entries, state.measurements.size)) }
        item { listOf(R.string.weight_kg, R.string.waist_cm, R.string.chest_cm, R.string.arms_cm, R.string.thighs_cm, R.string.hips_cm, R.string.body_fat_percent).forEachIndexed { i, label -> NumberField(values[i], { text -> values = values.toMutableList().also { it[i] = text } }, stringResource(label)) } }
        item { error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        item { Button({
            val parsed = values.map { if (it.isBlank()) 0f else it.toFloatOrNull() }
            if (!state.isPremiumUser && state.measurements.size >= 3) error = fullError
            else if (parsed[0] == null || parsed[0]!! <= 0f || parsed.any { it == null || it < 0f } || parsed[6]!! > 100f) error = validationError
            else { vm.saveMeasurement(BodyMeasurement(weightKg = parsed[0]!!, waistCm = parsed[1]!!, chestCm = parsed[2]!!, armsCm = parsed[3]!!, thighsCm = parsed[4]!!, hipsCm = parsed[5]!!, bodyFatPercent = parsed[6]!!)); error = null }
        }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.save_measurement)) } }
        if (!state.isPremiumUser && state.measurements.size >= 3) item { OutlinedButton(premium, Modifier.fillMaxWidth()) { Text(stringResource(R.string.unlock_measurement_history)) } }
        items(state.measurements) { m -> Text(stringResource(R.string.measurement_row, date(m.recordedAt), Units.weight(m.weightKg, state.settings.unitSystem), Units.length(m.waistCm, state.settings.unitSystem), m.bodyFatPercent)) }
    }
}

@Composable fun ProgressPhotosScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> uri?.let { vm.savePhoto(it.toString()) } }
    TrackerPage(stringResource(R.string.progress_photos), back) {
        item { Text(if (state.isPremiumUser) stringResource(R.string.unlimited_progress_photos) else stringResource(R.string.free_photo_entries, state.photos.size)) }
        item { Button({ picker.launch("image/*") }, Modifier.fillMaxWidth(), enabled = state.isPremiumUser || state.photos.size < 2) { Text(stringResource(R.string.choose_local_photo)) } }
        if (!state.isPremiumUser && state.photos.size >= 2) item { OutlinedButton(premium, Modifier.fillMaxWidth()) { Text(stringResource(R.string.unlock_unlimited_photos)) } }
        if (state.photos.size >= 2) item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { PhotoCard(stringResource(R.string.before), state.photos.last().imageUri, Modifier.weight(1f)); PhotoCard(stringResource(R.string.after), state.photos.first().imageUri, Modifier.weight(1f)) } }
        items(state.photos) { photo -> Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text("${date(photo.createdAt)} • ${photo.imageUri.take(34)}", Modifier.weight(1f)); TextButton({ vm.deletePhoto(photo.id) }) { Text(stringResource(R.string.remove)) } } }
    }
}

@Composable fun AchievementScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    TrackerPage(stringResource(R.string.achievements), back) {
        item { Text(stringResource(R.string.streak_reward_value, state.streak), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        if (state.achievements.isEmpty()) item { Text(stringResource(R.string.achievement_empty)) }
        items(state.achievements) { Text("★ ${it.title}\n${it.description}") }
    }
}

@Composable fun FitnessTestScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); var values by remember { mutableStateOf(List(4) { "" }) }; var error by remember { mutableStateOf<String?>(null) }
    val testError = stringResource(R.string.fitness_test_error)
    TrackerPage(stringResource(R.string.fitness_level_test), back) {
        item { listOf(R.string.push_ups, R.string.plank_seconds, R.string.squats, R.string.resting_heart_rate).forEachIndexed { i, label -> NumberField(values[i], { text -> values = values.toMutableList().also { it[i] = text } }, stringResource(label)) } }
        item { error?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        item { Button({ val input = values.map { it.toIntOrNull() }; if (input.any { it == null } || input.take(3).any { it!! < 0 } || (input[3] ?: 0) <= 0) error = testError else { vm.saveFitnessTest(input[0]!!, input[1]!!, input[2]!!, input[3]!!); error = null } }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.save_fitness_test)) } }
        items(state.fitnessTests) { Text(stringResource(R.string.fitness_test_row, date(it.recordedAt), it.score, it.pushUps, it.plankSeconds)) }
    }
}

@Composable fun ShareWorkoutScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState(); val summary = state.shareSummaries.firstOrNull(); val context = LocalContext.current
    TrackerPage(stringResource(R.string.workout_share_card), back) {
        item { Text(summary?.let { stringResource(R.string.share_summary, it.workoutTitle, it.durationMinutes, it.caloriesBurned, it.streak) } ?: stringResource(R.string.share_card_empty), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        item { OutlinedButton({
            summary?.let {
                val text = context.getString(R.string.share_workout_message, it.workoutTitle, it.durationMinutes, it.caloriesBurned, it.streak)
                runCatching {
                    context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text) }, context.getString(R.string.share_workout)))
                }
            }
        }, Modifier.fillMaxWidth(), enabled = summary != null) { Icon(Icons.Default.Share, null); Text(" ${stringResource(R.string.share_workout)}") } }
    }
}

@Composable fun PrivacyPolicyScreen(back: () -> Unit) = LegalScreen(stringResource(R.string.privacy_policy), stringResource(R.string.privacy_body), back)
@Composable fun TermsScreen(back: () -> Unit) = LegalScreen(stringResource(R.string.terms), stringResource(R.string.terms_body), back)
@Composable fun MedicalDisclaimerScreen(back: () -> Unit) = LegalScreen(stringResource(R.string.medical_disclaimer_title), stringResource(R.string.medical_disclaimer), back)
@Composable fun DeleteAllDataScreen(vm: FitnessViewModel, back: () -> Unit, deleted: () -> Unit = back) { var confirm by remember { mutableStateOf(false) }; LegalScreen(stringResource(R.string.delete_all_data), stringResource(R.string.delete_all_data_body), back) { Button({ confirm = true }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text(stringResource(R.string.delete_local_data)) } }; if (confirm) AlertDialog(onDismissRequest = { confirm = false }, title = { Text(stringResource(R.string.delete_all_local_question)) }, text = { Text(stringResource(R.string.cannot_be_undone)) }, confirmButton = { TextButton({ confirm = false; vm.deleteAllData(deleted) }) { Text(stringResource(R.string.delete)) } }, dismissButton = { TextButton({ confirm = false }) { Text(stringResource(R.string.cancel)) } }) }

@Composable private fun PhotoCard(label: String, uri: String, modifier: Modifier) = Card(modifier) { Column(Modifier.padding(12.dp)) { Text(label, fontWeight = FontWeight.Bold); AsyncImage(model = uri, contentDescription = label, modifier = Modifier.fillMaxWidth().height(160.dp)); Text(uri.take(32), style = MaterialTheme.typography.bodySmall) } }
@Composable private fun LegalScreen(title: String, text: String, back: () -> Unit, content: @Composable ColumnScope.() -> Unit = {}) = TrackerPage(title, back) { item { Text(text) }; item { Column(content = content) } }
@OptIn(ExperimentalMaterial3Api::class) @Composable private fun TrackerPage(title: String, back: () -> Unit, content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) = Scaffold(topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) } }) }) { padding -> LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content) }
@Composable private fun NumberField(value: String, change: (String) -> Unit, label: String) = OutlinedTextField(value, change, Modifier.fillMaxWidth(), label = { Text(label) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
private fun date(timestamp: Long) = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault()))
