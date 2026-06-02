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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.example.fitnessworkout.ui.screens.FeedbackScreen
import com.example.fitnessworkout.ui.screens.FitnessTestScreen
import com.example.fitnessworkout.ui.screens.MeasurementTrackerScreen
import com.example.fitnessworkout.ui.screens.MedicalDisclaimerScreen
import com.example.fitnessworkout.ui.screens.PrivacyPolicyScreen
import com.example.fitnessworkout.ui.screens.ProgressPhotosScreen
import com.example.fitnessworkout.ui.screens.ShareWorkoutScreen
import com.example.fitnessworkout.ui.screens.TermsScreen
import com.example.fitnessworkout.viewmodel.FitnessViewModel

private data class BottomDestination(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomDestinations = listOf(
    BottomDestination("home", "Home", Icons.Default.Home),
    BottomDestination("workouts", "Workouts", Icons.Default.FitnessCenter),
    BottomDestination("challenges", "Challenges", Icons.Default.EmojiEvents),
    BottomDestination("progress", "Progress", Icons.Default.BarChart),
    BottomDestination("profile", "Profile", Icons.Default.Person)
)

@Composable
fun FitnessApp(viewModel: FitnessViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomDestinations.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) FitnessBottomNavigation(navController, currentRoute)
        }
    ) { padding ->
        NavHost(navController = navController, startDestination = "onboarding", modifier = Modifier) {
            composable("onboarding") {
                OnboardingScreen(viewModel) {
                    navController.navigate("home") { popUpTo("onboarding") { inclusive = true } }
                }
            }
            composable("home") {
                HomeScreen(viewModel, padding, onNavigate = { navController.navigate(it) }, onPlan = { openPlan(navController, viewModel, it) })
            }
            composable("workouts") {
                WorkoutsScreen(viewModel, padding, onPlan = { openPlan(navController, viewModel, it) }, onPremium = { navController.navigate("premium") })
            }
            composable("challenges") { ChallengesScreen(viewModel, padding, { openPlan(navController, viewModel, it) }, { navController.navigate("premium") }) }
            composable("progress") { ProgressScreen(viewModel, padding, onPremium = { navController.navigate("premium") }) }
            composable("profile") { ProfileScreen(viewModel, padding, onPremium = { navController.navigate("premium") }, onNavigate = { navController.navigate(it) }) }
            composable("detail/{planId}") { entry ->
                val planId = entry.arguments?.getString("planId")?.toIntOrNull()
                LaunchedEffect(planId) { planId?.let(viewModel::selectPlan) }
                WorkoutDetailScreen(viewModel, onBack = { navController.popBackStack() }, onFinished = {
                    navController.navigate("share") { popUpTo("workouts") }
                })
            }
            composable("premium") { PremiumScreen(viewModel, onBack = { navController.popBackStack() }) }
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
            composable("feedback") { FeedbackScreen { navController.popBackStack() } }
            composable("delete-data") { DeleteAllDataScreen(viewModel) { navController.popBackStack() } }
        }
    }
}

@Composable
private fun FitnessBottomNavigation(navController: NavHostController, currentRoute: String?) {
    NavigationBar {
        bottomDestinations.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(destination.icon, destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}

private fun openPlan(navController: NavHostController, viewModel: FitnessViewModel, planId: Int) {
    viewModel.selectPlan(planId)
    navController.navigate("detail/$planId")
}
