package com.example.fitnessworkout.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.fitnessworkout.data.model.AppSettings
import com.example.fitnessworkout.data.model.PricingDisplay
import com.example.fitnessworkout.utils.Units
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R

@Composable fun SettingsScreen(vm: FitnessViewModel, back: () -> Unit, navigate: (String) -> Unit) {
    val state by vm.uiState.collectAsState(); var settings by remember(state.settings) { mutableStateOf(state.settings) }
    GlobalPage(stringResource(R.string.settings_title), back) {
        Choice("Country / region", settings.country, countries) { settings = settings.copy(country = it, unitSystem = Units.defaultSystem(it)) }
        Choice("Language", settings.language, listOf("English", "Hindi", "Spanish", "French", "Arabic")) { settings = settings.copy(language = it) }
        Choice("Unit system", settings.unitSystem, listOf("Metric", "Imperial")) { settings = settings.copy(unitSystem = it) }
        Choice("Diet preference", settings.dietPreference, listOf("Balanced", "Vegetarian", "Vegan", "Halal-friendly", "Gluten-free", "Dairy-free", "Keto")) { settings = settings.copy(dietPreference = it) }
        Choice("Workout location", settings.workoutLocation, listOf("Home", "Gym", "Office", "Outdoor", "Apartment / no jumping")) { settings = settings.copy(workoutLocation = it) }
        Toggle("Analytics consent placeholder", settings.analyticsConsent) { settings = settings.copy(analyticsConsent = it) }
        Toggle("Cloud sync consent placeholder", settings.cloudSyncConsent) { settings = settings.copy(cloudSyncConsent = it) }
        Button({ vm.saveSettings(settings) }, Modifier.fillMaxWidth()) { Text("Save global settings") }
        Text("Premium: ${if (state.isPremiumUser) "Active" else "Free"} • App version 1.0")
        listOf("community" to "Community", "trainer" to "Trainer mode", "form-check" to "AI form check", "specialized" to "Specialized routines",
            "calendar" to "Smart calendar", "recovery" to "Recovery score", "export-data" to "Export my data", "consent" to "Data consent",
            "whats-new" to "What's new", "bug-report" to "Bug report").forEach { (route, label) ->
            OutlinedButton({ navigate(route) }, Modifier.fillMaxWidth()) { Text(label) }
        }
    }
}

@Composable fun CommunityScreen(vm: FitnessViewModel, back: () -> Unit) { val state by vm.uiState.collectAsState(); GlobalPage("Community", back) { Text("Local mock community feed"); state.communityPosts.forEach { Text("★ ${it.author}: ${it.message} • ${it.likes} likes") } } }
@Composable fun TrainerModeScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) = PremiumPlaceholder(vm, "Trainer mode", "Trainer account • local client list • assign plans • view client progress", back, premium)
@Composable fun FormCheckScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) = PremiumPlaceholder(vm, "AI form check", "Camera pose overlay placeholder • Keep your back straight\nTODO: Connect ML Kit or MediaPipe.", back, premium)
@Composable fun SpecializedScreen(back: () -> Unit) = GlobalPage("Specialized routines", back) { Text("Desk Worker Fitness"); listOf("5-minute desk stretch", "Neck pain relief routine", "Lower back mobility", "Eye break reminders", "Posture reset", "Lunch break workout").forEach { Text("• $it") }; Text("Indian and global fitness"); listOf("Indian vegetarian fat loss", "Home workout without gym", "Walking + yoga", "Beginner belly fat", "Low impact", "No jumping", "Apartment friendly").forEach { Text("• $it") }; Text("General wellness routines only. These are not medical treatment.", style = MaterialTheme.typography.bodySmall) }
@Composable fun CalendarScreen(vm: FitnessViewModel, back: () -> Unit) { val state by vm.uiState.collectAsState(); GlobalPage("Smart workout calendar", back) { Text("Completed workouts: ${state.history.size}"); Text("Tracks completed, missed, rest, challenge, measurement, and photo days."); (1..7).forEach { Text("Day $it • ${if (it <= state.weeklyCount) "Completed" else "Plan or rest"}") } } }
@Composable fun RecoveryScreen(vm: FitnessViewModel, back: () -> Unit) { val state by vm.uiState.collectAsState(); var sleep by remember { mutableStateOf("") }; var soreness by remember { mutableStateOf("") }; var energy by remember { mutableStateOf("") }; var stress by remember { mutableStateOf("") }; GlobalPage("Recovery score", back) { Num(sleep, { sleep = it }, "Sleep hours"); Num(soreness, { soreness = it }, "Soreness 1-10"); Num(energy, { energy = it }, "Energy 1-10"); Num(stress, { stress = it }, "Stress 1-10"); Button({ vm.saveRecovery(sleep.toFloatOrNull() ?: 0f, soreness.toIntOrNull() ?: 0, energy.toIntOrNull() ?: 0, stress.toIntOrNull() ?: 0) }) { Text("Calculate recovery") }; state.recovery.firstOrNull()?.let { Text("Recommended: ${it.recommendation}", fontWeight = FontWeight.Bold) } } }
@Composable fun ExportDataScreen(back: () -> Unit) = GlobalPage("Export my data", back) { Text("CSV / JSON export placeholder for local health and workout records.") }
@Composable fun ConsentScreen(back: () -> Unit) = GlobalPage("Data consent", back) { Text("Analytics and cloud sync are optional placeholders. Offline tracking remains available without consent.") }
@Composable fun WhatsNewScreen(back: () -> Unit) = GlobalPage("What's new", back) { Text("Global settings, imperial units, localization packs, recovery scoring, specialized routines, community, and trainer placeholders.") }
@Composable fun BugReportScreen(back: () -> Unit) = GlobalPage("Bug report", back) { Text("Bug report placeholder. Connect your support form before production launch.") }
@Composable fun MonetizationScreen(back: () -> Unit) { val pricing = regionalPricing; GlobalPage("Regional pricing preview", back) { Text("7-day free trial • Limited-time lifetime offer", fontWeight = FontWeight.Bold); pricing.forEach { Text("${it.currency}: ${it.monthly} monthly • ${it.yearly} yearly • ${it.lifetime} lifetime") }; Text("Promo code placeholder • Referral code placeholder"); Text("TODO: Replace display models with Google Play Billing regional offers.") } }

@Composable private fun PremiumPlaceholder(vm: FitnessViewModel, title: String, text: String, back: () -> Unit, premium: () -> Unit) { val state by vm.uiState.collectAsState(); if (!state.isPremiumUser) GlobalPage(title, back) { Icon(Icons.Default.Lock, null); Text("Premium feature"); Button(premium) { Text("View Premium") } } else GlobalPage(title, back) { Text(text) } }
@Composable private fun Toggle(label: String, checked: Boolean, change: (Boolean) -> Unit) = Row(Modifier.fillMaxWidth()) { Text(label, Modifier.weight(1f)); Switch(checked, change) }
@Composable private fun Choice(label: String, value: String, options: List<String>, change: (String) -> Unit) { var open by remember { mutableStateOf(false) }; Box { OutlinedButton({ open = true }, Modifier.fillMaxWidth()) { Text("$label: $value") }; DropdownMenu(open, { open = false }) { options.forEach { DropdownMenuItem({ Text(it) }, { change(it); open = false }) } } } }
@Composable private fun Num(value: String, change: (String) -> Unit, label: String) = OutlinedTextField(value, change, label = { Text(label) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
@OptIn(ExperimentalMaterial3Api::class) @Composable private fun GlobalPage(title: String, back: () -> Unit, content: @Composable ColumnScope.() -> Unit) = Scaffold(topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }) { padding -> LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { item { Column(verticalArrangement = Arrangement.spacedBy(10.dp), content = content) } } }
private val countries = listOf("United States", "India", "Spain", "France", "United Arab Emirates", "Brazil", "United Kingdom")
private val regionalPricing = listOf(PricingDisplay("USD", "$2.99", "$19.99", "$29.99"), PricingDisplay("INR", "₹249", "₹1,699", "₹2,499"), PricingDisplay("EUR", "€2.99", "€19.99", "€29.99"), PricingDisplay("GBP", "£2.49", "£17.99", "£26.99"), PricingDisplay("BRL", "R$14.90", "R$99.90", "R$149.90"))
