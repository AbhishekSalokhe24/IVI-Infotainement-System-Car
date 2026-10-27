package com.ivi.launcher.ui.notifications

enum class NotificationPriority {
    CRITICAL,  // ADAS, collision alerts (always show, never auto-dismiss)
    HIGH,      // Calls, turn maneuvers, low fuel (heads-up 5s)
    MEDIUM,    // New messages (status bar icon + center)
    LOW        // App updates, informational
}

data class IVINotification(
    val id: String,
    val title: String,
    val message: String,
    val packageName: String,
    val priority: NotificationPriority,
    val timestampFormatted: String = "Just now",
    val primaryActionLabel: String? = null,
    val secondaryActionLabel: String? = null
)
