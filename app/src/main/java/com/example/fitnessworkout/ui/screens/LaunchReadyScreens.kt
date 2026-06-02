package com.example.fitnessworkout.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitnessworkout.data.model.QuickWorkout
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(finished: () -> Unit) {
    LaunchedEffect(Unit) { delay(700); finished() }
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.FitnessCenter, null, tint = MaterialTheme.colorScheme.primary)
        Text("Fitness and Workout", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text("Build habits. Track progress. Feel stronger.")
    }
}

@Composable
fun QuickWorkoutScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    var minutes by remember { mutableIntStateOf(5) }
    var filters by remember { mutableStateOf(setOf("No equipment")) }
    var workout by remember(state.allExercises) { mutableStateOf<QuickWorkout?>(null) }
    LaunchPage("Quick workout", back) {
        Text("Choose a short session. The local engine selects movements from your exercise library.")
        ChipRow(listOf(5, 10, 15).map { "$it min" }, "$minutes min") { minutes = it.substringBefore(" ").toInt() }
        listOf("No equipment", "Low impact", "No jumping", "Office friendly").forEach { label ->
            FilterChip(selected = label in filters, onClick = { filters = if (label in filters) filters - label else filters + label }, label = { Text(label) })
        }
        Button({ workout = vm.quickWorkout(minutes, filters) }, Modifier.fillMaxWidth()) { Text("Generate quick workout") }
        workout?.let { generated ->
            Text("${generated.durationMinutes} minutes | ${generated.estimatedCalories} kcal", fontWeight = FontWeight.Bold)
            generated.exerciseNames.forEach { Text("• $it") }
            Button({ vm.completeQuickWorkout(generated); back() }, Modifier.fillMaxWidth()) { Text("Complete quick workout") }
        }
    }
}

@Composable
fun FitnessScoreScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    val score = state.fitnessScore
    LaunchPage("Fitness score", back) {
        Text("${score.value} / 100", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        Text(score.explanation)
        Text("Ways to improve", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        score.tips.forEach { Text("• $it") }
    }
}

@Composable fun AIWorkoutCoachScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) =
    AIPreview(vm, "AI workout coach", "Based on your recent activity, try a short full-body session and finish with mobility work.", back, premium)

@Composable fun AIMealSuggestionScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) =
    AIPreview(vm, "AI meal suggestions", "Try a balanced plate with protein, vegetables, whole grains, and water. Adjust for your dietary preference.", back, premium)

@Composable fun AIProgressAnalysisScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) =
    AIPreview(vm, "AI progress analysis", "Your local history suggests that consistency is your strongest next lever. Schedule two short sessions this week.", back, premium)

@Composable fun AIMotivationChatScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) =
    AIPreview(vm, "AI motivation chat", "Coach: Start with five minutes today. A small completed session keeps your routine alive.", back, premium)

@Composable
private fun AIPreview(vm: FitnessViewModel, title: String, response: String, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    LaunchPage(title, back) {
        if (!state.isPremiumUser) {
            Icon(Icons.Default.Lock, null)
            Text("This AI-ready preview is available with Premium.")
            Button(premium) { Text("View Premium") }
        } else {
            Text(response)
            Text("Local rule-based preview", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            // TODO: Replace local coaching response with a production AI API after privacy review.
        }
    }
}

@Composable
fun ProgressReportScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    val report = vm.progressReport()
    LaunchPage("Progress report preview", back) {
        if (!state.isPremiumUser) {
            Icon(Icons.Default.Lock, null)
            Text("PDF progress reports are a Premium feature.")
            Button(premium) { Text("View Premium") }
        } else {
            Text(report.userName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(report.dateRange)
            Text("Workouts completed: ${report.workoutsCompleted}")
            Text("Calories burned: ${report.caloriesBurned}")
            Text("Current streak: ${report.streak} days")
            Text("Measurements: ${report.measurementSummary}")
            Text("Water: ${report.waterSummary}")
            Text("Fitness score: ${report.fitnessScore} / 100")
            OutlinedButton({}, Modifier.fillMaxWidth()) { Text("Generate PDF placeholder") }
            // TODO: Export this preview with Android PdfDocument.
        }
    }
}

@Composable fun WarmUpScreen(start: () -> Unit, back: () -> Unit) = LaunchPage("Warm-up first", back) {
    Text("Prepare your body before starting.", fontWeight = FontWeight.Bold)
    listOf("March in place • 60 sec", "Arm circles • 30 sec", "Hip hinges • 45 sec", "Gentle mobility • 60 sec").forEach { Text("• $it") }
    Text("Stop if you feel pain, dizziness, or unusual discomfort.", color = MaterialTheme.colorScheme.error)
    Button(start, Modifier.fillMaxWidth()) { Text("Start workout") }
}

@Composable fun CoolDownScreen(done: () -> Unit) = LaunchPage("Cool down", done) {
    Text("Nice work. Give your body a gentle reset.", fontWeight = FontWeight.Bold)
    listOf("Slow breathing • 60 sec", "Hamstring stretch • 30 sec", "Chest opener • 30 sec", "Drink water and rest").forEach { Text("• $it") }
    Button(done, Modifier.fillMaxWidth()) { Text("Continue") }
}

@Composable fun SafetyScreen(back: () -> Unit) = LaunchPage("Safety and trust", back) {
    Text("Exercise safety tips", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Text("Warm up first. Use controlled movement. Stop immediately for pain, dizziness, or shortness of breath.")
    Text("Pregnancy and injury notice", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Text("Ask a qualified healthcare professional before starting or changing exercise during pregnancy, after an injury, or with a medical condition.")
}

@Composable fun AboutAppScreen(back: () -> Unit) = LaunchPage("About the app", back) {
    Text("Fitness and Workout App", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    Text("Version 1.0 (1)")
    Text("Offline-first fitness tracking with optional future cloud integrations.")
    Text("Privacy policy URL placeholder: https://example.com/privacy")
}

@Composable fun ContactSupportScreen(vm: FitnessViewModel, back: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }
    LaunchPage("Contact support", back) {
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("Email") })
        OutlinedTextField(message, { message = it }, Modifier.fillMaxWidth(), label = { Text("How can we help?") })
        Button({ vm.sendSupportMessage(email, message); sent = email.isNotBlank() && message.isNotBlank() }, Modifier.fillMaxWidth()) { Text("Save support request locally") }
        if (sent) Text("Thanks. Your support request is saved for the future support integration.")
    }
}

@Composable fun RateAppScreen(back: () -> Unit) = LaunchPage("Rate the app", back) {
    Text("Enjoying your workouts? A future Play Store release will open the in-app review prompt here.")
    OutlinedButton({}, Modifier.fillMaxWidth()) { Text("Rate app placeholder") }
}

@Composable fun ShareAppScreen(back: () -> Unit) {
    val context = LocalContext.current
    LaunchPage("Share the app", back) {
        Text("Invite a friend to build a stronger routine.")
        Button({
            context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Try Fitness and Workout App: https://play.google.com/store/apps/details?id=com.example.fitnessworkout")
            }, "Share app"))
        }, Modifier.fillMaxWidth()) { Icon(Icons.Default.Share, null); Text(" Share app") }
    }
}

@Composable fun DataSafetyScreen(back: () -> Unit) = LaunchPage("Data safety", back) {
    Text("Your workout history, measurements, photos, and preferences remain local on this device.")
    Text("Cloud backup, analytics, authentication, and notifications are placeholders only. They require consent and production configuration before release.")
}

@Composable fun ContentHubScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    LaunchPage("Announcements", back) {
        state.announcements.forEach { item ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text(item.title, fontWeight = FontWeight.Bold)
                    Text(item.message)
                    Text(item.type.uppercase(), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Text("TODO: Replace these local cards with Firebase Remote Config content.")
    }
}

@Composable fun ContentCategoriesScreen(back: () -> Unit) = LaunchPage("Workout and diet categories", back) {
    listOf(
        "Weight Loss at Home", "No Equipment Workout", "Belly Fat Workout", "Beginner Workout",
        "Women Fitness", "Men Muscle Gain", "Yoga and Stretching", "Walking Plan", "HIIT Workout",
        "Office Workout", "Indian Diet Plan", "Vegetarian Weight Loss Plan"
    ).forEach { Text("• $it") }
}

@Composable
private fun ChipRow(options: List<String>, selected: String, choose: (String) -> Unit) =
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        options.forEach { FilterChip(selected = selected == it, onClick = { choose(it) }, label = { Text(it) }) }
    }

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun LaunchPage(title: String, back: () -> Unit, content: @Composable ColumnScope.() -> Unit) =
    Scaffold(topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Column(verticalArrangement = Arrangement.spacedBy(10.dp), content = content) }
        }
    }
