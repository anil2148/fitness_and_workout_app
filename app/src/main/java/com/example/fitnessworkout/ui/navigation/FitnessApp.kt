package com.example.fitnessworkout.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fitnessworkout.ui.screens.HomeScreen
import com.example.fitnessworkout.ui.screens.OnboardingScreen
import com.example.fitnessworkout.ui.screens.PremiumScreen
import com.example.fitnessworkout.ui.screens.ProfileScreen
import com.example.fitnessworkout.ui.screens.ProgressScreen
import com.example.fitnessworkout.ui.screens.WorkoutDetailScreen
import com.example.fitnessworkout.ui.screens.WorkoutsScreen
import com.example.fitnessworkout.ui.screens.CalculatorScreen
import com.example.fitnessworkout.ui.screens.ChallengesScreen
import com.example.fitnessworkout.ui.screens.CustomPlanScreen
import com.example.fitnessworkout.ui.screens.DietScreen
import com.example.fitnessworkout.ui.screens.ReminderScreen
import com.example.fitnessworkout.ui.screens.WaterScreen
import com.example.fitnessworkout.ui.screens.AchievementScreen
import com.example.fitnessworkout.ui.screens.DeleteAllDataScreen
import com.example.fitnessworkout.ui.screens.AppFeedbackScreen
import com.example.fitnessworkout.ui.screens.FitnessTestScreen
import com.example.fitnessworkout.ui.screens.MeasurementTrackerScreen
import com.example.fitnessworkout.ui.screens.MedicalDisclaimerScreen
import com.example.fitnessworkout.ui.screens.PrivacyPolicyScreen
import com.example.fitnessworkout.ui.screens.ProgressPhotosScreen
import com.example.fitnessworkout.ui.screens.ShareWorkoutScreen
import com.example.fitnessworkout.ui.screens.TermsScreen
import com.example.fitnessworkout.ui.screens.BugReportScreen
import com.example.fitnessworkout.ui.screens.CalendarScreen
import com.example.fitnessworkout.ui.screens.CommunityScreen
import com.example.fitnessworkout.ui.screens.ConsentScreen
import com.example.fitnessworkout.ui.screens.ExportDataScreen
import com.example.fitnessworkout.ui.screens.FormCheckScreen
import com.example.fitnessworkout.ui.screens.MonetizationScreen
import com.example.fitnessworkout.ui.screens.RecoveryScreen
import com.example.fitnessworkout.ui.screens.SettingsScreen
import com.example.fitnessworkout.ui.screens.SpecializedScreen
import com.example.fitnessworkout.ui.screens.TrainerModeScreen
import com.example.fitnessworkout.ui.screens.WhatsNewScreen
import com.example.fitnessworkout.ui.screens.SplashScreen
import com.example.fitnessworkout.ui.screens.QuickWorkoutScreen
import com.example.fitnessworkout.ui.screens.FitnessScoreScreen
import com.example.fitnessworkout.ui.screens.AIWorkoutCoachScreen
import com.example.fitnessworkout.ui.screens.AIMealSuggestionScreen
import com.example.fitnessworkout.ui.screens.AIProgressAnalysisScreen
import com.example.fitnessworkout.ui.screens.AIMotivationChatScreen
import com.example.fitnessworkout.ui.screens.ProgressReportPreviewScreen
import com.example.fitnessworkout.ui.screens.WarmUpScreen
import com.example.fitnessworkout.ui.screens.CoolDownScreen
import com.example.fitnessworkout.ui.screens.SafetyScreen
import com.example.fitnessworkout.ui.screens.AboutAppScreen
import com.example.fitnessworkout.ui.screens.ContactSupportScreen
import com.example.fitnessworkout.ui.screens.RateAppScreen
import com.example.fitnessworkout.ui.screens.ShareAppScreen
import com.example.fitnessworkout.ui.screens.DataSafetyScreen
import com.example.fitnessworkout.ui.screens.ContentHubScreen
import com.example.fitnessworkout.ui.screens.ContentCategoriesScreen
import com.example.fitnessworkout.ui.screens.DailyHabitScreen
import com.example.fitnessworkout.ui.screens.FitnessReportsScreen
import com.example.fitnessworkout.ui.screens.FavoriteWorkoutsScreen
import com.example.fitnessworkout.ui.screens.GuidedWorkoutPlayerScreen
import com.example.fitnessworkout.ui.screens.ExerciseDetailScreen
import com.example.fitnessworkout.viewmodel.FitnessViewModel

import com.example.fitnessworkout.R

private data class BottomDestination(val route: String, val label: Int, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomDestinations = listOf(
    BottomDestination("home", R.string.nav_home, Icons.Default.Home),
    BottomDestination("workouts", R.string.nav_workouts, Icons.Default.FitnessCenter),
    BottomDestination("challenges", R.string.nav_challenges, Icons.Default.EmojiEvents),
    BottomDestination("progress", R.string.nav_progress, Icons.Default.BarChart),
    BottomDestination("profile", R.string.nav_profile, Icons.Default.Person)
)

@Composable
fun FitnessApp(viewModel: FitnessViewModel) {
    val navController = rememberNavController()
    val state by viewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomDestinations.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) FitnessBottomNavigation(navController, currentRoute)
        }
    ) { padding ->
        NavHost(navController = navController, startDestination = "splash", modifier = Modifier) {
            composable("splash") {
                SplashScreen(state.isLoading) {
                    val destination = if (state.user != null && state.safetyAcknowledgement.medicalDisclaimerAccepted) "home" else "onboarding"
                    navController.navigate(destination) { popUpTo("splash") { inclusive = true } }
                }
            }
            composable("onboarding") {
                OnboardingScreen(viewModel) {
                    navController.navigate("home") { popUpTo("onboarding") { inclusive = true } }
                }
            }
            composable("home") {
                HomeScreen(viewModel, padding, onNavigate = { navController.navigate(it) }, onPlan = { openPlan(navController, viewModel, it) })
            }
            composable("workouts") {
                WorkoutsScreen(viewModel, padding, onPlan = { openPlan(navController, viewModel, it) }, onPremium = { navController.navigate("premium") }, onNavigate = { navController.navigate(it) })
            }
            composable("challenges") { ChallengesScreen(viewModel, padding, { openPlan(navController, viewModel, it) }, { navController.navigate("premium") }) }
            composable("progress") { ProgressScreen(viewModel, padding, onPremium = { navController.navigate("premium") }, onNavigate = { navController.navigate(it) }) }
            composable("profile") { ProfileScreen(viewModel, padding, onPremium = { navController.navigate("premium") }, onNavigate = { navController.navigate(it) }) }
            composable("detail/{planId}") { entry ->
                val planId = entry.arguments?.getString("planId")?.toIntOrNull()
                LaunchedEffect(planId) { planId?.let(viewModel::selectPlan) }
                WorkoutDetailScreen(viewModel, onBack = { navController.popBackStack() }, onStartPlayer = {
                    navController.navigate("player/$planId")
                }, onExercise = { exerciseId ->
                    navController.navigate("exercise/$exerciseId")
                }, onPremium = {
                    navController.navigate("premium")
                }, onFinished = {
                    navController.navigate("cooldown") { popUpTo("workouts") }
                })
            }
            composable("player/{planId}") { entry ->
                val planId = entry.arguments?.getString("planId")?.toIntOrNull()
                LaunchedEffect(planId) { planId?.let(viewModel::selectPlan) }
                GuidedWorkoutPlayerScreen(viewModel, back = { navController.popBackStack() }, onPremium = {
                    navController.navigate("premium")
                }, finished = {
                    navController.navigate("cooldown") { popUpTo("workouts") }
                })
            }
            composable("exercise/{exerciseId}") { entry ->
                ExerciseDetailScreen(
                    vm = viewModel,
                    exerciseId = entry.arguments?.getString("exerciseId")?.toIntOrNull(),
                    back = { navController.popBackStack() },
                    onPremium = { navController.navigate("premium") },
                    onStart = { planId -> navController.navigate("player/$planId") },
                )
            }
            composable("warmup/{planId}") { entry ->
                val planId = entry.arguments?.getString("planId")?.toIntOrNull()
                LaunchedEffect(planId) { planId?.let(viewModel::selectPlan) }
                WarmUpScreen(start = { navController.navigate("detail/$planId") }, back = { navController.popBackStack() })
            }
            composable("cooldown") { CoolDownScreen { navController.navigate("share") { popUpTo("workouts") } } }
            composable("premium") { PremiumScreen(viewModel, onBack = { navController.popBackStack() }, onNavigate = { navController.navigate(it) }) }
            composable("water") { WaterScreen(viewModel) { navController.popBackStack() } }
            composable("calculators") { CalculatorScreen(viewModel) { navController.popBackStack() } }
            composable("diet") { DietScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("custom") { CustomPlanScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("reminders") { ReminderScreen(viewModel) { navController.popBackStack() } }
            composable("measurements") { MeasurementTrackerScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("photos") { ProgressPhotosScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("achievements") { AchievementScreen(viewModel) { navController.popBackStack() } }
            composable("fitness-test") { FitnessTestScreen(viewModel) { navController.popBackStack() } }
            composable("share") { ShareWorkoutScreen(viewModel) { navController.popBackStack() } }
            composable("privacy") { PrivacyPolicyScreen { navController.popBackStack() } }
            composable("terms") { TermsScreen { navController.popBackStack() } }
            composable("medical") { MedicalDisclaimerScreen { navController.popBackStack() } }
            composable("feedback") { AppFeedbackScreen(viewModel) { navController.popBackStack() } }
            composable("delete-data") {
                DeleteAllDataScreen(
                    vm = viewModel,
                    back = { navController.popBackStack() },
                    deleted = {
                        navController.navigate("onboarding") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                )
            }
            composable("settings") { SettingsScreen(viewModel, { navController.popBackStack() }, { navController.navigate(it) }) }
            composable("community") { CommunityScreen(viewModel) { navController.popBackStack() } }
            composable("trainer") { TrainerModeScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("form-check") { FormCheckScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("specialized") { SpecializedScreen { navController.popBackStack() } }
            composable("calendar") { CalendarScreen(viewModel) { navController.popBackStack() } }
            composable("recovery") { RecoveryScreen(viewModel) { navController.popBackStack() } }
            composable("export-data") { ExportDataScreen { navController.popBackStack() } }
            composable("consent") { ConsentScreen { navController.popBackStack() } }
            composable("whats-new") { WhatsNewScreen { navController.popBackStack() } }
            composable("bug-report") { BugReportScreen { navController.popBackStack() } }
            composable("regional-pricing") { MonetizationScreen { navController.popBackStack() } }
            composable("quick-workout") { QuickWorkoutScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("fitness-score") { FitnessScoreScreen(viewModel) { navController.popBackStack() } }
            composable("ai-workout") { AIWorkoutCoachScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("ai-meal") { AIMealSuggestionScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("ai-progress") { AIProgressAnalysisScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("ai-chat") { AIMotivationChatScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("progress-report") { ProgressReportPreviewScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("safety") { SafetyScreen { navController.popBackStack() } }
            composable("about") { AboutAppScreen { navController.popBackStack() } }
            composable("contact-support") { ContactSupportScreen(viewModel) { navController.popBackStack() } }
            composable("rate-app") { RateAppScreen { navController.popBackStack() } }
            composable("share-app") { ShareAppScreen { navController.popBackStack() } }
            composable("data-safety") { DataSafetyScreen { navController.popBackStack() } }
            composable("announcements") { ContentHubScreen(viewModel) { navController.popBackStack() } }
            composable("content-categories") { ContentCategoriesScreen(viewModel, { navController.popBackStack() }, { navController.navigate("premium") }) }
            composable("daily-habits") { DailyHabitScreen(viewModel) { navController.popBackStack() } }
            composable("fitness-reports") { FitnessReportsScreen(viewModel) { navController.popBackStack() } }
            composable("favorites") { FavoriteWorkoutsScreen(viewModel, { navController.popBackStack() }, { openPlan(navController, viewModel, it) }) }
        }
    }
}

@Composable
private fun FitnessBottomNavigation(navController: NavHostController, currentRoute: String?) {
    NavigationBar {
        bottomDestinations.forEach { destination ->
            val label = stringResource(destination.label)
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(destination.icon, label) },
                label = { Text(label) }
            )
        }
    }
}

private fun openPlan(navController: NavHostController, viewModel: FitnessViewModel, planId: Int) {
    viewModel.selectPlan(planId)
    navController.navigate("warmup/$planId")
}
