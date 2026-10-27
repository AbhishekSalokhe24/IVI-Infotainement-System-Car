package com.ivi.launcher.ui.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivi.designsystem.components.IVIButton
import com.ivi.designsystem.components.IVICard
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import kotlinx.coroutines.delay

@Composable
fun HeadsUpNotification(
    notification: IVINotification?,
    onDismiss: () -> Unit,
    onPrimaryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Auto-dismiss after 5s for HIGH priority (never auto-dismiss CRITICAL)
    LaunchedEffect(notification) {
        if (notification != null && notification.priority == NotificationPriority.HIGH) {
            delay(5_000L)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = notification != null,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier
    ) {
        if (notification != null) {
            val isCritical = notification.priority == NotificationPriority.CRITICAL

            IVICard(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(top = IVISpacing.SM)
            ) {
                Column(modifier = Modifier.padding(IVISpacing.LG)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(IVISpacing.SM)
                        ) {
                            Icon(
                                imageVector = if (isCritical) Icons.Default.Warning else Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = if (isCritical) IVIColors.Error else IVIColors.Warning
                            )
                            Text(
                                text = notification.title,
                                style = IVITypography.TitleLarge,
                                color = if (isCritical) IVIColors.Error else IVIColors.OnSurface
                            )
                        }

                        IVIIconButton(
                            icon = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            onClick = onDismiss
                        )
                    }

                    Spacer(Modifier.height(IVISpacing.XS))
                    Text(
                        text = notification.message,
                        style = IVITypography.BodyLarge,
                        color = IVIColors.OnSurfaceVariant
                    )

                    if (notification.primaryActionLabel != null) {
                        Spacer(Modifier.height(IVISpacing.MD))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IVIButton(
                                text = notification.primaryActionLabel,
                                onClick = {
                                    onPrimaryAction()
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
