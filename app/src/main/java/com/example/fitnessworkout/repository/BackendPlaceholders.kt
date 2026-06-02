package com.example.fitnessworkout.repository

/**
 * Integration boundary for optional cloud services. The app remains offline-first until each
 * provider is connected, consented, and tested for release.
 */
interface BackendPlaceholders {
    // TODO: Connect Firebase Authentication and Google Sign-In.
    fun signIn()

    // TODO: Add Firestore cloud backup behind explicit user consent.
    fun backUpLocalProgress()

    // TODO: Upload exercise videos through Firebase Storage or a production CDN.
    fun fetchExerciseVideos()

    // TODO: Initialize Firebase Analytics, Crashlytics, and Remote Config after consent.
    fun initializeRemoteServices()

    // TODO: Register push notification tokens and schedule user-approved messages.
    fun registerPushNotifications()
}
