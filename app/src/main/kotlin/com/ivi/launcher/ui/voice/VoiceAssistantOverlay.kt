package com.ivi.launcher.ui.voice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.ivi.designsystem.components.IVIButton
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography

@Composable
fun VoiceAssistantOverlay(
    voiceState: VoiceState,
    onSimulateSampleCommand: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    AnimatedVisibility(
        visible = voiceState !is VoiceState.Idle,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(IVIColors.Scrim)
                .clickable { onDismiss() }
                .padding(IVISpacing.XXL),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Pulsing Microphone Graphic
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(if (voiceState is VoiceState.Listening) pulseScale else 1f)
                        .clip(CircleShape)
                        .background(IVIColors.PrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Assistant Active",
                        tint = IVIColors.Primary,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(Modifier.height(IVISpacing.XL))

                // State Text
                when (voiceState) {
                    is VoiceState.Listening -> {
                        Text(
                            text = "Listening... Speak your command",
                            style = IVITypography.HeadlineLarge,
                            color = IVIColors.OnSurface
                        )
                        Spacer(Modifier.height(IVISpacing.MD))
                        Row(horizontalArrangement = Arrangement.spacedBy(IVISpacing.SM)) {
                            IVIButton(
                                text = "\"Set temperature to 21 degrees\"",
                                onClick = { onSimulateSampleCommand("Set temperature to 21 degrees") }
                            )
                            IVIButton(
                                text = "\"Play next song\"",
                                onClick = { onSimulateSampleCommand("Play next song") }
                            )
                            IVIButton(
                                text = "\"Navigate to Grand Avenue\"",
                                onClick = { onSimulateSampleCommand("Navigate to Grand Avenue") }
                            )
                        }
                    }
                    is VoiceState.Processing -> {
                        Text(
                            text = "\"${voiceState.transcript}\"",
                            style = IVITypography.HeadlineMedium,
                            color = IVIColors.Primary
                        )
                        Spacer(Modifier.height(IVISpacing.SM))
                        Text(
                            text = "Processing intent...",
                            style = IVITypography.TitleMedium,
                            color = IVIColors.OnSurfaceVariant
                        )
                    }
                    is VoiceState.Responding -> {
                        Text(
                            text = voiceState.speechText,
                            style = IVITypography.HeadlineLarge,
                            color = IVIColors.Success
                        )
                        Spacer(Modifier.height(IVISpacing.SM))
                        Text(
                            text = "Action executed: ${voiceState.executedAction}",
                            style = IVITypography.TitleMedium,
                            color = IVIColors.OnSurfaceVariant
                        )
                    }
                    is VoiceState.Error -> {
                        Text(
                            text = voiceState.message,
                            style = IVITypography.HeadlineLarge,
                            color = IVIColors.Error
                        )
                    }
                    VoiceState.Idle -> {}
                }
            }

            // Close button in top-right
            IVIIconButton(
                icon = Icons.Default.Close,
                contentDescription = "Close Voice Assistant",
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}
