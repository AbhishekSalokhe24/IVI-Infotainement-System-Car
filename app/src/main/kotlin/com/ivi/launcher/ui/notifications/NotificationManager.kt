package com.ivi.launcher.ui.notifications

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IVINotificationManager @Inject constructor() {

    private val _headsUpNotification = MutableStateFlow<IVINotification?>(null)
    val headsUpNotification: StateFlow<IVINotification?> = _headsUpNotification.asStateFlow()

    private val _notificationCenterList = MutableStateFlow<List<IVINotification>>(
        listOf(
            IVINotification(
                id = "n1",
                title = "TPMS Normal",
                message = "All 4 tires are calibrated at optimal 34 PSI.",
                packageName = "com.ivi.vehicle",
                priority = NotificationPriority.LOW,
                timestampFormatted = "10 min ago"
            ),
            IVINotification(
                id = "n2",
                title = "Navigation Route Updated",
                message = "Faster route available via Express Highway saving 4 mins.",
                packageName = "com.ivi.navigation",
                priority = NotificationPriority.MEDIUM,
                timestampFormatted = "25 min ago",
                primaryActionLabel = "Accept Route"
            )
        )
    )
    val notificationCenterList: StateFlow<List<IVINotification>> = _notificationCenterList.asStateFlow()

    fun postNotification(notification: IVINotification) {
        if (notification.priority == NotificationPriority.CRITICAL || notification.priority == NotificationPriority.HIGH) {
            _headsUpNotification.value = notification
        }
        _notificationCenterList.update { listOf(notification) + it }
    }

    fun dismissHeadsUp() {
        _headsUpNotification.value = null
    }

    fun clearAllNotifications() {
        _notificationCenterList.value = emptyList()
    }
}
