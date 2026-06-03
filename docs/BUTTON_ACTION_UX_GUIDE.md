# Button Action, Feedback, and Navigation UX Guide

This guide defines how every button, clickable card, form action, dialog action, and premium action should behave in the Fitness and Workout app.

Use this document when fixing or reviewing screens.

## 1. Global Action Rules

Every visible action must provide clear feedback.

### Before an action

- Validate required fields.
- Disable submit buttons when input is invalid where practical.
- Show field-level errors for forms.
- Prevent duplicate taps during save/submit actions.
- Show a loading state for longer actions.

### After successful action

- Show a clear success message with Snackbar, Toast, or persistent success text.
- Update the UI immediately.
- Navigate only after the action succeeds.
- Clear transient form state only after successful save.

### After failed action

- Show a clear error message.
- Keep user input intact.
- Do not navigate.
- Highlight the invalid field where possible.

### Destructive actions

Always show a confirmation dialog before:

- Delete all data
- Reset progress
- Delete profile
- Delete measurement
- Delete progress photo
- Clear water logs
- Clear completed workouts

## 2. Standard Success Messages

Use string resources for these messages:

- `profile_saved_successfully`
- `profile_updated_successfully`
- `settings_saved_successfully`
- `language_updated_successfully`
- `country_updated_successfully`
- `currency_updated_successfully`
- `unit_system_updated_successfully`
- `premium_unlocked_successfully`
- `workout_started`
- `workout_paused`
- `workout_resumed`
- `workout_completed_successfully`
- `progress_saved_successfully`
- `water_added_successfully`
- `measurement_saved_successfully`
- `feedback_sent_successfully`
- `data_deleted_successfully`
- `reminder_saved_successfully`
- `copied_to_clipboard`
- `share_sheet_opened`

## 3. Standard Error Messages

Use string resources for these messages:

- `something_went_wrong`
- `please_try_again`
- `required_field`
- `enter_valid_value`
- `invalid_number`
- `invalid_age`
- `invalid_height`
- `invalid_weight`
- `invalid_amount`
- `invalid_email`
- `failed_to_save_profile`
- `failed_to_save_settings`
- `failed_to_start_workout`
- `failed_to_complete_workout`
- `failed_to_unlock_premium`
- `failed_to_delete_data`
- `feature_not_available_yet`
- `permission_required`
- `action_cancelled`

## 4. Confirmation Dialog Messages

Use string resources for these messages:

- `confirm_delete_title`
- `confirm_delete_message`
- `confirm_reset_progress_title`
- `confirm_reset_progress_message`
- `confirm_clear_data_title`
- `confirm_clear_data_message`
- `confirm_logout_title`
- `confirm_logout_message`

## 5. Current Covered Actions

| Screen | Action | Feedback |
|---|---|---|
| Settings | Language, country, currency, unit changes | Localized snackbar or restart toast; settings remain independent |
| Settings | Save global settings | Localized snackbar and visible saved message |
| Premium | Mock unlock | Localized snackbar; local Premium flag only |
| Workout Detail | Start / finish workout | Localized snackbar and safe navigation |
| Workout Player | Pause / resume / finish | Localized snackbar and existing completion flow |
| Water Tracker | Add water | Localized snackbar; invalid amount remains a visible error |
| Body Measurements | Save measurement | Localized snackbar; free-limit and validation errors stay visible |
| Reminders | Save reminder preferences | Localized snackbar |
| Feedback | Submit feedback | Localized snackbar and success message |
| Share Workout | Open share sheet | Localized snackbar when the system share sheet launches |
| Delete Data / Reset | Destructive action | Confirmation dialog before action |
| Promo / Referral / Affiliate | Placeholder action | Safe Coming Soon page |

## 6. Post-Action Navigation Rules

### Onboarding

After saving profile successfully:

1. Show or trigger `profile_saved_successfully` where the UI remains visible long enough.
2. Navigate to Home.
3. Clear Onboarding from back stack.

### Edit Profile

After saving profile successfully:

1. Show `profile_updated_successfully`.
2. Stay on Profile or navigate back to Profile.
3. Do not go to Home unless explicitly intended.

### Settings

- Language update: show message, apply locale, stay on Settings.
- Country update: show message, update default currency suggestion, stay on Settings.
- Currency update: show message, refresh premium price display, stay on Settings.
- Unit update: show message, refresh profile/calculator unit labels, stay on Settings.

### Workout Detail

After tapping Start Workout:

1. Show `workout_started`.
2. Navigate to Workout Player.

### Workout Player

- Pause: show `workout_paused`, stay on player.
- Resume: show `workout_resumed`, stay on player.
- Next: move to next exercise and show next exercise name where practical.
- Finish: save completed workout, show `workout_completed_successfully`, navigate to Workout Summary or Progress.

### Premium

After mock unlock:

1. Save premium status.
2. Show `premium_unlocked_successfully`.
3. Navigate back to previous locked feature if available, otherwise stay on Premium with unlocked state.

### Water Tracker

After adding valid water amount:

1. Save water log.
2. Show `water_added_successfully`.
3. Stay on Water Tracker.
4. Update progress immediately.

### Calculators

After Calculate:

1. Validate input.
2. Show result on the same screen.
3. Do not navigate.
4. Show field errors for invalid input.

### Delete / Reset Data

After confirmation and success:

1. Show `data_deleted_successfully` where the UI remains visible long enough.
2. If profile was deleted, navigate to Onboarding and clear back stack.
3. If only progress was reset, stay on Profile/Settings or navigate Home.

## 7. UI State Pattern

Screens with actions should have state fields such as:

```kotlin
data class ExampleUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val validationErrors: Map<String, String> = emptyMap(),
    val showConfirmationDialog: Boolean = false
)
```

Use one-time events for Snackbars and navigation:

```kotlin
sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data class Navigate(val route: String) : UiEvent
    data object NavigateBack : UiEvent
}
```

Avoid showing the same Snackbar repeatedly after recomposition.

## 8. UI Quality Rules

Major screens should use:

- Material 3 cards
- Rounded corners
- Proper spacing
- Friendly empty states
- Progress indicators
- Primary and secondary actions
- Scrollable layout for long content
- Clear field-level validation errors
- Accessible button labels
- Content descriptions for icon-only buttons

## 9. Accessibility Rules

- Icon-only buttons must have content descriptions.
- Images must have meaningful content descriptions.
- Tap targets should be at least 48dp.
- Do not rely only on color for errors/success.
- Snackbar messages must be meaningful.

## 10. Adding New Actions

1. Add a string key for success, error, or placeholder copy.
2. Add the key to every locale pack.
3. Use `SnackbarHostState`, visible error text, `AlertDialog`, or `ComingSoonScreen`.
4. Run `python3 scripts/verify_strings.py`.
5. Run `python3 scripts/scan_hardcoded_strings.py`.

## 11. Manual QA Checklist for Actions

Test these actions before release:

- Save onboarding profile
- Edit profile
- Change language
- Change country
- Change currency
- Change unit system
- Start workout
- Pause workout
- Resume workout
- Finish workout
- Unlock premium mock
- Tap locked premium content
- Add water
- Calculate BMI
- Calculate BMR
- Calculate water goal
- Submit feedback
- Reset progress
- Delete all data
- Open Coming Soon feature

Every action should show proper feedback and navigate correctly.
