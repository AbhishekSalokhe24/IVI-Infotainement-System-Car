package com.ivi.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ivi.designsystem.theme.IVITheme
import com.ivi.launcher.navigation.IVIDestination
import com.ivi.launcher.navigation.IVINavGraph
import com.ivi.launcher.ui.notifications.IVINotificationManager
import com.ivi.launcher.ui.shell.StatusBarViewModel
import com.ivi.launcher.ui.shell.SystemShell
import com.ivi.launcher.ui.voice.VoiceAssistantManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val statusBarViewModel: StatusBarViewModel by viewModels()

    @Inject
    lateinit var notificationManager: IVINotificationManager

    @Inject
    lateinit var voiceAssistantManager: VoiceAssistantManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scope = rememberCoroutineScope()
            var isNightMode by remember { mutableStateOf(true) }
            var temperature by remember { mutableFloatStateOf(22.0f) }
            var volume by remember { mutableFloatStateOf(0.6f) }
            var brightness by remember { mutableFloatStateOf(0.85f) }
            var notificationCenterOpen by remember { mutableStateOf(false) }

            IVITheme(darkTheme = isNightMode) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: IVIDestination.HOME.route
                val statusState by statusBarViewModel.statusState.collectAsStateWithLifecycle()
                val voiceState by voiceAssistantManager.voiceState.collectAsStateWithLifecycle()
                val headsUpNotification by notificationManager.headsUpNotification.collectAsStateWithLifecycle()
                val notificationsList by notificationManager.notificationCenterList.collectAsStateWithLifecycle()

                SystemShell(
                    currentRoute = currentRoute,
                    onNavigate = { destination ->
                        if (currentRoute != destination.route) {
                            navController.navigate(destination.route) {
                                popUpTo(IVIDestination.HOME.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    statusBarState = statusState,
                    temperature = temperature,
                    volume = volume,
                    brightness = brightness,
                    isNightMode = isNightMode,
                    onTemperatureChange = { temperature = it },
                    onVolumeChange = { volume = it },
                    onBrightnessChange = { brightness = it },
                    onToggleNightMode = { isNightMode = !isNightMode },
                    // Voice & Notifications bindings
                    voiceState = voiceState,
                    onVoiceClick = { voiceAssistantManager.startListening() },
                    onVoiceCommand = { command ->
                        scope.launch { voiceAssistantManager.processSpeech(command) }
                    },
                    onVoiceDismiss = { voiceAssistantManager.cancelVoice() },
                    headsUpNotification = headsUpNotification,
                    onDismissHeadsUp = { notificationManager.dismissHeadsUp() },
                    notificationCenterOpen = notificationCenterOpen,
                    onToggleNotificationCenter = { notificationCenterOpen = !notificationCenterOpen },
                    notificationsList = notificationsList,
                    onClearAllNotifications = { notificationManager.clearAllNotifications() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    IVINavGraph(
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
