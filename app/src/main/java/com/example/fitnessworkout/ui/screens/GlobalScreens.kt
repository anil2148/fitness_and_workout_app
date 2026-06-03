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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.fitnessworkout.data.model.AppSettings
import com.example.fitnessworkout.data.MockPremiumPlans
import com.example.fitnessworkout.utils.Units
import com.example.fitnessworkout.viewmodel.FitnessViewModel
import androidx.compose.ui.res.stringResource
import com.example.fitnessworkout.R
import com.example.fitnessworkout.utils.AppLocaleManager
import com.example.fitnessworkout.utils.CurrencyFormatter
import com.example.fitnessworkout.utils.RegionSettings
import com.example.fitnessworkout.ui.components.localizedOption
import kotlinx.coroutines.launch

@Composable fun SettingsScreen(vm: FitnessViewModel, back: () -> Unit, navigate: (String) -> Unit) {
    val context = LocalContext.current
    val state by vm.uiState.collectAsState(); var settings by remember(state.settings) { mutableStateOf(state.settings) }; var settingsSaved by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val languageUpdated = stringResource(R.string.language_updated_successfully)
    val countryUpdated = stringResource(R.string.country_updated_successfully)
    val currencyUpdated = stringResource(R.string.currency_updated_successfully)
    val unitUpdated = stringResource(R.string.unit_system_updated_successfully)
    val settingsSavedMessage = stringResource(R.string.settings_saved_successfully)
    GlobalPage(stringResource(R.string.settings_title), back, snackbarHostState) {
        Choice(stringResource(R.string.country_region), settings.country, RegionSettings.supportedCountries.map { it.label }, Modifier.testTag("settings_country_selector")) {
            val selected = RegionSettings.country(it)
            settings = settings.copy(
                country = selected.label,
                selectedCountryCode = selected.code,
                selectedCurrencyCode = selected.currencyCode,
                selectedUnitSystem = selected.unitSystem,
                unitSystem = selected.unitSystem,
            )
            scope.launch { snackbarHostState.showSnackbar(countryUpdated) }
        }
        LanguageChoice(settings.selectedLanguageCode) { code ->
            val updated = settings.copy(language = AppLocaleManager.languageName(code), selectedLanguageCode = code)
            settings = updated
            vm.saveSettings(updated) {
                scope.launch { snackbarHostState.showSnackbar(languageUpdated) }
                AppLocaleManager.restartUi(context)
            }
        }
        Choice(stringResource(R.string.currency), settings.selectedCurrencyCode, RegionSettings.supportedCurrencyCodes, Modifier.testTag("settings_currency_selector")) {
            settings = settings.copy(selectedCurrencyCode = RegionSettings.safeCurrencyCode(it))
            scope.launch { snackbarHostState.showSnackbar(currencyUpdated) }
        }
        Choice(stringResource(R.string.unit_system), settings.selectedUnitSystem, listOf("Metric", "Imperial")) {
            settings = settings.copy(selectedUnitSystem = it, unitSystem = it)
            scope.launch { snackbarHostState.showSnackbar(unitUpdated) }
        }
        Choice(stringResource(R.string.diet_preference), settings.dietPreference, listOf("Balanced", "Vegetarian", "Vegan", "Halal-friendly", "Gluten-free", "Dairy-free", "Keto")) { settings = settings.copy(dietPreference = it) }
        Choice(stringResource(R.string.workout_location), settings.workoutLocation, listOf("Home", "Gym", "Office", "Outdoor", "Apartment / no jumping")) { settings = settings.copy(workoutLocation = it) }
        Toggle(stringResource(R.string.prefer_injury_safe), settings.injurySafeMode) { settings = settings.copy(injurySafeMode = it) }
        Toggle(stringResource(R.string.analytics_consent), settings.analyticsConsent) { settings = settings.copy(analyticsConsent = it) }
        Toggle(stringResource(R.string.cloud_sync_consent), settings.cloudSyncConsent) { settings = settings.copy(cloudSyncConsent = it) }
        Toggle(stringResource(R.string.dark_mode), state.user?.darkMode ?: false, vm::setDarkMode)
        Button({
            vm.saveSettings(settings)
            settingsSaved = true
            scope.launch { snackbarHostState.showSnackbar(settingsSavedMessage) }
        }, Modifier.fillMaxWidth()) { Text(stringResource(R.string.save_global_settings)) }
        if (settingsSaved) Text(settingsSavedMessage, color = MaterialTheme.colorScheme.primary)
        Text("${if (state.isPremiumUser) stringResource(R.string.premium_member) else stringResource(R.string.free_member)} • ${stringResource(R.string.app_version)}")
        listOf("community" to R.string.community, "trainer" to R.string.trainer_mode, "form-check" to R.string.ai_form_check, "specialized" to R.string.specialized_routines,
            "calendar" to R.string.smart_calendar, "recovery" to R.string.recovery_score, "export-data" to R.string.export_my_data, "consent" to R.string.data_consent,
            "whats-new" to R.string.whats_new, "bug-report" to R.string.bug_report, "daily-habits" to R.string.daily_habits,
            "fitness-reports" to R.string.reports, "favorites" to R.string.favorite_workouts, "privacy" to R.string.privacy_policy,
            "terms" to R.string.terms, "medical" to R.string.medical_disclaimer_title, "delete-data" to R.string.delete_all_data,
            "reminders" to R.string.daily_reminders, "about" to R.string.about_app, "feedback" to R.string.feedback,
            "contact-support" to R.string.contact_support, "data-safety" to R.string.data_safety, "share-app" to R.string.share_app).forEach { (route, label) ->
            OutlinedButton({ navigate(route) }, Modifier.fillMaxWidth()) { Text(stringResource(label)) }
        }
    }
}

@Composable fun CommunityScreen(vm: FitnessViewModel, back: () -> Unit) { val state by vm.uiState.collectAsState(); GlobalPage(stringResource(R.string.community), back) { Text(stringResource(R.string.community_body)); state.communityPosts.forEach { Text(stringResource(R.string.community_post, it.author, it.message, it.likes)) } } }
@Composable fun TrainerModeScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) = PremiumPlaceholder(vm, stringResource(R.string.trainer_mode), stringResource(R.string.trainer_mode_body), back, premium)
@Composable fun FormCheckScreen(vm: FitnessViewModel, back: () -> Unit, premium: () -> Unit) = PremiumPlaceholder(vm, stringResource(R.string.ai_form_check), stringResource(R.string.ai_form_check_body), back, premium)
@Composable fun SpecializedScreen(back: () -> Unit) = GlobalPage(stringResource(R.string.specialized_routines), back) { Text(stringResource(R.string.specialized_body)); Text(stringResource(R.string.general_wellness_notice), style = MaterialTheme.typography.bodySmall) }
@Composable fun CalendarScreen(vm: FitnessViewModel, back: () -> Unit) { val state by vm.uiState.collectAsState(); GlobalPage(stringResource(R.string.smart_calendar), back) { Text(stringResource(R.string.workouts_completed_value, state.history.size)); Text(stringResource(R.string.calendar_body)); (1..7).forEach { Text(stringResource(R.string.calendar_day, it, stringResource(if (it <= state.weeklyCount) R.string.completed else R.string.plan_or_rest))) } } }
@Composable fun RecoveryScreen(vm: FitnessViewModel, back: () -> Unit) { val state by vm.uiState.collectAsState(); var sleep by remember { mutableStateOf("") }; var soreness by remember { mutableStateOf("") }; var energy by remember { mutableStateOf("") }; var stress by remember { mutableStateOf("") }; var error by remember { mutableStateOf<String?>(null) }; val recoveryError = stringResource(R.string.recovery_error); GlobalPage(stringResource(R.string.recovery_score), back) { Num(sleep, { sleep = it }, stringResource(R.string.sleep_hours)); Num(soreness, { soreness = it }, stringResource(R.string.soreness_range)); Num(energy, { energy = it }, stringResource(R.string.energy_range)); Num(stress, { stress = it }, stringResource(R.string.stress_range)); error?.let { Text(it, color = MaterialTheme.colorScheme.error) }; Button({ val hours = sleep.toFloatOrNull(); val sore = soreness.toIntOrNull(); val power = energy.toIntOrNull(); val pressure = stress.toIntOrNull(); if (hours == null || hours !in 0f..24f || sore !in 1..10 || power !in 1..10 || pressure !in 1..10) error = recoveryError else { vm.saveRecovery(hours, sore!!, power!!, pressure!!); error = null } }) { Text(stringResource(R.string.calculate_recovery)) }; state.recovery.firstOrNull()?.let { Text(stringResource(R.string.recommended_value, it.recommendation), fontWeight = FontWeight.Bold) } } }
@Composable fun ExportDataScreen(back: () -> Unit) = ComingSoonScreen(stringResource(R.string.export_my_data), stringResource(R.string.export_data_body), back)
@Composable fun ConsentScreen(back: () -> Unit) = GlobalPage(stringResource(R.string.data_consent), back) { Text(stringResource(R.string.data_consent_body)) }
@Composable fun WhatsNewScreen(back: () -> Unit) = GlobalPage(stringResource(R.string.whats_new), back) { Text(stringResource(R.string.whats_new_body)) }
@Composable fun BugReportScreen(back: () -> Unit) = ComingSoonScreen(stringResource(R.string.bug_report), stringResource(R.string.bug_report_body), back)
@Composable fun MonetizationScreen(back: () -> Unit) { val pricing = MockPremiumPlans.regionalPricing; GlobalPage(stringResource(R.string.regional_pricing_preview), back) { Text(stringResource(R.string.mock_regional_pricing), fontWeight = FontWeight.Bold); pricing.forEach { Text(stringResource(R.string.regional_price_row, it.currency, CurrencyFormatter.format(it.monthly, it.currency), CurrencyFormatter.format(it.yearly, it.currency), CurrencyFormatter.format(it.lifetime, it.currency))) }; Text(stringResource(R.string.referral_and_promo)); Text(stringResource(R.string.play_billing_todo)) } }

@Composable private fun PremiumPlaceholder(vm: FitnessViewModel, title: String, text: String, back: () -> Unit, premium: () -> Unit) { val state by vm.uiState.collectAsState(); if (!state.isPremiumUser) GlobalPage(title, back) { Icon(Icons.Default.Lock, null); Text(stringResource(R.string.premium_feature)); Button(premium) { Text(stringResource(R.string.view_premium)) } } else ComingSoonScreen(title, text, back) }
@Composable fun ComingSoonScreen(title: String, description: String, back: () -> Unit, premiumLocked: Boolean = false) = GlobalPage(title, back) { if (premiumLocked) Icon(Icons.Default.Lock, null); Text(stringResource(R.string.coming_soon), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Text(description) }
@Composable private fun Toggle(label: String, checked: Boolean, change: (Boolean) -> Unit) = Row(Modifier.fillMaxWidth()) { Text(label, Modifier.weight(1f)); Switch(checked, change) }
@Composable private fun Choice(label: String, value: String, options: List<String>, modifier: Modifier = Modifier, change: (String) -> Unit) { var open by remember { mutableStateOf(false) }; Box { OutlinedButton({ open = true }, modifier.fillMaxWidth()) { Text(stringResource(R.string.label_value, label, localizedOption(value))) }; DropdownMenu(open, { open = false }) { options.forEach { DropdownMenuItem({ Text(localizedOption(it)) }, { change(it); open = false }) } } } }
@Composable private fun LanguageChoice(selectedCode: String, change: (String) -> Unit) = Choice(stringResource(R.string.language), AppLocaleManager.languageName(selectedCode), AppLocaleManager.supportedLanguages.map { it.label }, Modifier.testTag("settings_language_selector")) { change(AppLocaleManager.languageCode(it)) }
@Composable private fun Num(value: String, change: (String) -> Unit, label: String) = OutlinedTextField(value, change, label = { Text(label) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
@OptIn(ExperimentalMaterial3Api::class) @Composable private fun GlobalPage(title: String, back: () -> Unit, snackbarHostState: SnackbarHostState? = null, content: @Composable ColumnScope.() -> Unit) = Scaffold(snackbarHost = { snackbarHostState?.let { SnackbarHost(it) } }, topBar = { TopAppBar({ Text(title) }, navigationIcon = { IconButton(back) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) } }) }) { padding -> LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { item { Column(verticalArrangement = Arrangement.spacedBy(10.dp), content = content) } } }
