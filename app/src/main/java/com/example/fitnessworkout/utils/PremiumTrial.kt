package com.example.fitnessworkout.utils

object PremiumTrial {
    const val FIRST_MONTH_DAYS: Int = 30
    private const val DAY_MILLIS: Long = 24L * 60L * 60L * 1000L
    private const val FIRST_MONTH_MILLIS: Long = FIRST_MONTH_DAYS * DAY_MILLIS

    fun isActive(startedAtMillis: Long, nowMillis: Long = System.currentTimeMillis()): Boolean {
        if (startedAtMillis <= 0L || nowMillis < startedAtMillis) return false
        return nowMillis - startedAtMillis < FIRST_MONTH_MILLIS
    }

    fun daysRemaining(startedAtMillis: Long, nowMillis: Long = System.currentTimeMillis()): Int {
        if (!isActive(startedAtMillis, nowMillis)) return 0
        val elapsed = nowMillis - startedAtMillis
        val remaining = (FIRST_MONTH_MILLIS - elapsed).coerceAtLeast(0L)
        return ((remaining + DAY_MILLIS - 1L) / DAY_MILLIS).toInt()
    }
}
