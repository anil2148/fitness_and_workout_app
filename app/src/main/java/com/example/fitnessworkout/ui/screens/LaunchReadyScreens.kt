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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R
import com.example.fitnessworkout.data.model.QuickWorkout
import com.example.fitnessworkout.data.model.WorkoutContentCategory
import com.example.fitnessworkout.ui.components.ExerciseIllustration
import com.example.fitnessworkout.ui.components.ExerciseVideoPlayer
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(isLoading: Boolean, finished: () -> Unit) {
    LaunchedEffect(isLoading) {
        if (!isLoading) {
            delay(700)
            finished()
        }
    }
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.FitnessCenter, null, tint = MaterialTheme.colorScheme.primary)
        Text(stringResource(R.string.splash_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text(stringResource(R.string.splash_subtitle))
        if (isLoading) Text(stringResource(R.string.splash_loading))
    }
}

@Composable
fun QuickWorkoutScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    var minutes by remember { mutableIntStateOf(5) }
    var filters by remember { mutableStateOf(setOf("No equipment")) }
    var workout by remember(state.allExercises) { mutableStateOf<QuickWorkout?>(null) }
    LaunchPage(stringResource(R.string.quick_workout), back) {
        Text(stringResource(R.string.quick_workout_body))
        ChipRow(listOf(5, 10, 15).map { "$it min" }, "$minutes min") { minutes = it.substringBefore(" ").toInt() }
        listOf(
            "No equipment" to stringResource(R.string.no_equipment),
            "Low impact" to stringResource(R.string.low_impact),
            "No jumping" to stringResource(R.string.no_jumping),
            "Office friendly" to stringResource(R.string.office_friendly),
            "Beginner safe" to stringResource(R.string.beginner_safe),
        ).forEach { (filter, label) ->
            FilterChip(selected = filter in filters, onClick = { filters = if (filter in filters) filters - filter else filters + filter }, label = { Text(label) })
        }
        Button({ workout = vm.quickWorkout(minutes, filters) }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.generate_quick_workout)) }
        workout?.let { generated ->
            Text(stringResource(R.string.quick_workout_summary, generated.durationMinutes, generated.estimatedCalories), fontWeight = FontWeight.Bold)
            if (generated.exercises.isEmpty()) Text(stringResource(R.string.exercise_library_loading))
            generated.exercises.forEach { exercise ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        ExerciseIllustration(exercise, Modifier.size(width = 96.dp, height = 62.dp))
                        Text(exercise.name, fontWeight = FontWeight.Bold)
                    }
                }
            }
            generated.exercises.firstOrNull()?.let { ExerciseVideoPlayer(it, state.isPremiumUser, premium) }
            Button({ vm.completeQuickWorkout(generated); back() }, Modifier.fillMaxWidth(), enabled = generated.exercises.isNotEmpty()) { Text(stringResource(R.string.complete_quick_workout)) }
        }
    }
}

@Composable
fun FitnessScoreScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    val score = state.fitnessScore
    LaunchPage(stringResource(R.string.fitness_score), back) {
        CircularProgressIndicator(progress = { score.value / 100f })
        Text("${score.value} / 100", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        Text(score.levelLabel, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(score.weeklyComparison)
        Text(score.explanation)
        Text(stringResource(R.string.ways_to_improve), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        score.tips.forEach { Text("• $it") }
    }
}

@Composable fun AIWorkoutCoachScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) =
    AIPreview(vm, stringResource(R.string.ai_workout_coach), stringResource(R.string.ai_workout_response), back, premium)

@Composable fun AIMealSuggestionScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) =
    AIPreview(vm, stringResource(R.string.ai_meal_suggestions), stringResource(R.string.ai_meal_response), back, premium)

@Composable fun AIProgressAnalysisScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) =
    AIPreview(vm, stringResource(R.string.ai_progress_analysis), stringResource(R.string.ai_progress_response), back, premium)

@Composable fun AIMotivationChatScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) =
    AIPreview(vm, stringResource(R.string.ai_motivation_chat), stringResource(R.string.ai_chat_response), back, premium)

@Composable
private fun AIPreview(vm: FitnessViewModel, title: String, response: String, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    LaunchPage(title, back) {
        if (!state.isPremiumUser) {
            Icon(Icons.Default.Lock, null)
            Text(stringResource(R.string.ai_preview_premium))
            Button(premium) { Text(stringResource(R.string.view_premium)) }
        } else {
            Text(response)
            Text(stringResource(R.string.local_rule_preview), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            // TODO: Replace local coaching response with a production AI API after privacy review.
        }
    }
}

@Composable
fun ProgressReportPreviewScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    val report = vm.progressReport()
    LaunchPage(stringResource(R.string.progress_report_preview), back) {
        if (!state.isPremiumUser) {
            Icon(Icons.Default.Lock, null)
            Text(stringResource(R.string.pdf_premium_feature))
            Button(premium) { Text(stringResource(R.string.view_premium)) }
        } else {
            Text(report.userName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(report.dateRange)
            Text(stringResource(R.string.workouts_completed_value, report.workoutsCompleted))
            Text(stringResource(R.string.calories_burned_value, report.caloriesBurned))
            Text(stringResource(R.string.current_streak_value, report.streak))
            Text(stringResource(R.string.measurements_value, report.measurementSummary))
            Text(stringResource(R.string.water_value, report.waterSummary))
            Text(stringResource(R.string.fitness_score_value, report.fitnessScore))
            Text(stringResource(R.string.challenge_progress_text, report.challengeProgress))
            Text(stringResource(R.string.progress_photo_value, report.progressPhotoPlaceholder))
            OutlinedButton({}, Modifier.fillMaxWidth(), enabled = false) { Text(stringResource(R.string.coming_soon_export_pdf)) }
            // TODO: Export this preview with Android PdfDocument.
        }
    }
}

@Composable fun WarmUpScreen(start: () -> Unit, back: () -> Unit) = LaunchPage(stringResource(R.string.warm_up_first), back) {
    Text(stringResource(R.string.warm_up_body), fontWeight = FontWeight.Bold)
    listOf(R.string.warm_up_march, R.string.warm_up_arms, R.string.warm_up_hips, R.string.warm_up_mobility).forEach { Text("• ${stringResource(it)}") }
    Text(stringResource(R.string.stop_safety_warning), color = MaterialTheme.colorScheme.error)
    Button(start, Modifier.fillMaxWidth()) { Text(stringResource(R.string.start_workout)) }
}

@Composable fun CoolDownScreen(done: () -> Unit) = LaunchPage(stringResource(R.string.cool_down), done) {
    Text(stringResource(R.string.cool_down_body), fontWeight = FontWeight.Bold)
    listOf(R.string.cool_down_breathing, R.string.cool_down_hamstring, R.string.cool_down_chest, R.string.cool_down_water).forEach { Text("• ${stringResource(it)}") }
    Button(done, Modifier.fillMaxWidth()) { Text(stringResource(R.string.continue_label)) }
}

@Composable fun SafetyScreen(back: () -> Unit) = LaunchPage(stringResource(R.string.safety_and_trust), back) {
    Text(stringResource(R.string.exercise_safety_tips), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Text(stringResource(R.string.exercise_safety_body))
    Text(stringResource(R.string.pregnancy_injury_notice), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Text(stringResource(R.string.pregnancy_injury_body))
}

@Composable fun AboutAppScreen(back: () -> Unit) = LaunchPage(stringResource(R.string.about_app), back) {
    Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    Text(stringResource(R.string.version_info))
    Text(stringResource(R.string.offline_first_body))
    Text(stringResource(R.string.privacy_url_placeholder))
    Text(stringResource(R.string.support_email_placeholder))
}

@Composable fun ContactSupportScreen(vm: FitnessViewModel, back: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    LaunchPage(stringResource(R.string.contact_support), back) {
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.email)) })
        OutlinedTextField(message, { message = it }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.how_can_we_help)) })
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button({ if (!email.contains("@") || message.isBlank()) error = context.getString(R.string.support_validation_error) else { vm.sendSupportMessage(email, message); sent = true; error = null } }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.save_support_request)) }
        if (sent) Text(stringResource(R.string.support_saved))
    }
}

@Composable fun RateAppScreen(back: () -> Unit) = LaunchPage(stringResource(R.string.rate_app), back) {
    Text(stringResource(R.string.rate_app_body))
    OutlinedButton({}, Modifier.fillMaxWidth(), enabled = false) { Text(stringResource(R.string.coming_soon_rating)) }
}

@Composable fun ShareAppScreen(back: () -> Unit) {
    val context = LocalContext.current
    LaunchPage(stringResource(R.string.share_app), back) {
        Text(stringResource(R.string.share_app_body))
        Button({
            runCatching {
                context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_message))
                }, context.getString(R.string.share_app)))
            }
        }, Modifier.fillMaxWidth()) { Icon(Icons.Default.Share, null); Text(" ${stringResource(R.string.share_app)}") }
    }
}

@Composable fun DataSafetyScreen(back: () -> Unit) = LaunchPage(stringResource(R.string.data_safety), back) {
    Text(stringResource(R.string.data_safety_local))
    Text(stringResource(R.string.data_safety_future))
}

@Composable fun ContentHubScreen(vm: FitnessViewModel, back: () -> Unit) {
    val state by vm.uiState.collectAsState()
    LaunchPage(stringResource(R.string.announcements), back) {
        state.announcements.forEach { item ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text(item.title, fontWeight = FontWeight.Bold)
                    Text(item.message)
                    Text(item.type.uppercase(), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Text(stringResource(R.string.remote_config_todo))
    }
}

@Composable fun ContentCategoriesScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) {
    val state by vm.uiState.collectAsState()
    LaunchPage(stringResource(R.string.workout_diet_categories), back) {
        contentCategories.forEach { category ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(category.title, fontWeight = FontWeight.Bold)
                    Text(category.description)
                    Text(stringResource(if (category.beginnerFriendly) R.string.beginner_friendly else R.string.build_up_gradually), color = MaterialTheme.colorScheme.primary)
                    Text(stringResource(R.string.related_value, category.relatedPlans.joinToString()), style = MaterialTheme.typography.bodySmall)
                    if (category.premiumOnly && !state.isPremiumUser) OutlinedButton(premium) { Text(stringResource(R.string.unlock_with_premium)) }
                }
            }
        }
    }
}

@Composable
private fun ChipRow(options: List<String>, selected: String, choose: (String) -> Unit) =
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        options.forEach { FilterChip(selected = selected == it, onClick = { choose(it) }, label = { Text(it) }) }
    }

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun LaunchPage(title: String, back: () -> Unit, content: @Composable ColumnScope.() -> Unit) =
    Scaffold(topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) } }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Column(verticalArrangement = Arrangement.spacedBy(10.dp), content = content) }
        }
    }

private val contentCategories = listOf(
    WorkoutContentCategory("Weight Loss at Home", "Simple home sessions that combine consistency and movement.", true, false, listOf("Beginner Full Body", "Beginner Cardio")),
    WorkoutContentCategory("No Equipment Workout", "Bodyweight workouts for home, travel, or small spaces.", true, false, listOf("Beginner Full Body")),
    WorkoutContentCategory("Belly Fat Workout", "General core and cardio fitness content. Spot reduction is not guaranteed.", true, false, listOf("Beginner Abs", "Beginner Cardio")),
    WorkoutContentCategory("Beginner Workout", "Start with approachable full-body routines and rest as needed.", true, false, listOf("Beginner Full Body")),
    WorkoutContentCategory("Women Fitness", "Balanced strength, mobility, and wellness routines.", true, true, listOf("Intermediate Full Body")),
    WorkoutContentCategory("Men Muscle Gain", "Progressive strength-oriented workout ideas.", false, true, listOf("Intermediate Chest", "Intermediate Arms")),
    WorkoutContentCategory("Yoga and Stretching", "Gentle mobility and recovery-friendly movement ideas.", true, false, listOf("Beginner Full Body")),
    WorkoutContentCategory("Walking Plan", "Use walking as an accessible base for consistency.", true, false, listOf("Beginner Cardio")),
    WorkoutContentCategory("HIIT Workout", "Higher-intensity intervals for users ready to progress.", false, true, listOf("Advanced Cardio")),
    WorkoutContentCategory("Office Workout", "Short movement breaks and posture resets for desk days.", true, false, listOf("Beginner Full Body")),
    WorkoutContentCategory("Indian Diet Plan", "General Indian meal-pattern ideas with balanced portions.", true, true, listOf("Diet guidance")),
    WorkoutContentCategory("Vegetarian Weight Loss Plan", "General vegetarian meal guidance with protein-rich options.", true, true, listOf("Diet guidance"))
)
