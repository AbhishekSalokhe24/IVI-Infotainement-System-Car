package com.ivi.phone.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivi.designsystem.components.IVIButton
import com.ivi.designsystem.components.IVICard
import com.ivi.designsystem.components.IVIIconButton
import com.ivi.designsystem.theme.IVIColors
import com.ivi.designsystem.theme.IVIDimensions
import com.ivi.designsystem.theme.IVIShapes
import com.ivi.designsystem.theme.IVISpacing
import com.ivi.designsystem.theme.IVITypography
import com.ivi.phone.domain.model.CallType
import com.ivi.phone.domain.model.Contact
import com.ivi.phone.domain.model.PhoneTab
import com.ivi.phone.ui.dialer.DialerViewModel
import com.ivi.phone.ui.dialer.PhoneAction
import com.ivi.phone.ui.dialer.PhoneUiState

@Composable
fun PhoneScreen(
    viewModel: DialerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IVIColors.Background)
            .padding(IVISpacing.LG),
        verticalArrangement = Arrangement.spacedBy(IVISpacing.MD)
    ) {
        // Tab Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PhoneTab.entries.forEach { tab ->
                val isSelected = uiState.selectedTab == tab
                IVIButton(
                    text = when (tab) {
                        PhoneTab.KEYPAD -> "Keypad"
                        PhoneTab.RECENT -> "Recent Calls"
                        PhoneTab.FAVORITES -> "Favorites"
                        PhoneTab.CONTACTS -> "Contacts"
                    },
                    onClick = { viewModel.onAction(PhoneAction.SelectTab(tab)) },
                    modifier = Modifier.weight(1f),
                    leadingIcon = {
                        Icon(
                            imageVector = when (tab) {
                                PhoneTab.KEYPAD -> Icons.Default.Dialpad
                                PhoneTab.RECENT -> Icons.Default.History
                                PhoneTab.FAVORITES -> Icons.Default.Star
                                PhoneTab.CONTACTS -> Icons.Default.Contacts
                            },
                            contentDescription = null,
                            tint = if (isSelected) IVIColors.Primary else IVIColors.OnSurfaceDim
                        )
                    }
                )
            }
        }

        // Active Call Overlay (if call in progress)
        if (uiState.activeCall != null) {
            val call = uiState.activeCall!!
            IVICard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(IVISpacing.LG),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = IVIColors.Success,
                            modifier = Modifier.size(36.dp)
                        )
                        Column {
                            Text(
                                text = "CALL IN PROGRESS: ${call.callerName}",
                                style = IVITypography.TitleLarge,
                                color = IVIColors.OnSurface
                            )
                            Text(
                                text = "${call.phoneNumber} • Hands-Free Audio",
                                style = IVITypography.BodyMedium,
                                color = IVIColors.OnSurfaceVariant
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)) {
                        IVIIconButton(
                            icon = if (call.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mute",
                            selected = call.isMuted,
                            onClick = { viewModel.onAction(PhoneAction.ToggleMute) }
                        )
                        IVIIconButton(
                            icon = Icons.Default.CallEnd,
                            contentDescription = "End Call",
                            onClick = { viewModel.onAction(PhoneAction.EndCall) }
                        )
                    }
                }
            }
        }

        // Tab Content
        when (uiState.selectedTab) {
            PhoneTab.KEYPAD -> KeypadTab(uiState = uiState, onAction = viewModel::onAction)
            PhoneTab.RECENT -> RecentCallsTab(uiState = uiState, onAction = viewModel::onAction)
            PhoneTab.FAVORITES -> ContactsTab(contacts = uiState.favorites, onAction = viewModel::onAction)
            PhoneTab.CONTACTS -> ContactsTab(contacts = uiState.contacts, onAction = viewModel::onAction)
        }
    }
}

@Composable
private fun KeypadTab(
    uiState: PhoneUiState,
    onAction: (PhoneAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = IVISpacing.MD),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(420.dp)
        ) {
            // Dialed Number Display
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(IVIColors.SurfaceElevated, shape = IVIShapes.Large)
                    .padding(horizontal = IVISpacing.LG),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = uiState.dialedNumber.ifEmpty { "Enter Number" },
                    style = IVITypography.HeadlineLarge,
                    color = if (uiState.dialedNumber.isNotEmpty()) IVIColors.OnSurface else IVIColors.OnSurfaceDim
                )
                if (uiState.dialedNumber.isNotEmpty()) {
                    IVIIconButton(
                        icon = Icons.Default.Backspace,
                        contentDescription = "Backspace",
                        onClick = { onAction(PhoneAction.Backspace) }
                    )
                }
            }

            Spacer(Modifier.height(IVISpacing.MD))

            // Dialpad Grid (3x4)
            val rows = listOf(
                listOf('1' to "", '2' to "ABC", '3' to "DEF"),
                listOf('4' to "GHI", '5' to "JKL", '6' to "MNO"),
                listOf('7' to "PQRS", '8' to "TUV", '9' to "WXYZ"),
                listOf('*' to "", '0' to "+", '#' to "")
            )

            rows.forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = IVISpacing.XXS),
                    horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
                ) {
                    row.forEach { (digit, letters) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .clip(IVIShapes.Large)
                                .background(IVIColors.SurfaceVariant)
                                .clickable { onAction(PhoneAction.DialDigit(digit)) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = digit.toString(),
                                    style = IVITypography.TitleLarge,
                                    color = IVIColors.OnSurface
                                )
                                if (letters.isNotEmpty()) {
                                    Text(
                                        text = letters,
                                        style = IVITypography.LabelSmall,
                                        color = IVIColors.OnSurfaceDim
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(IVISpacing.MD))

            // Call Button
            IVIButton(
                text = "CALL",
                onClick = { onAction(PhoneAction.CallDialed) },
                leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RecentCallsTab(
    uiState: PhoneUiState,
    onAction: (PhoneAction) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(IVISpacing.MD),
        modifier = Modifier.fillMaxSize()
    ) {
        items(uiState.callLog) { entry ->
            IVICard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAction(PhoneAction.CallNumber(entry.phoneNumber)) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(IVISpacing.LG),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
                    ) {
                        Icon(
                            imageVector = when (entry.type) {
                                CallType.INCOMING -> Icons.Default.CallReceived
                                CallType.OUTGOING -> Icons.Default.Call
                                CallType.MISSED -> Icons.Default.CallMissed
                            },
                            contentDescription = null,
                            tint = if (entry.type == CallType.MISSED) IVIColors.Error else IVIColors.Success
                        )
                        Column {
                            Text(
                                text = entry.contactName ?: entry.phoneNumber,
                                style = IVITypography.TitleLarge,
                                color = IVIColors.OnSurface
                            )
                            Text(
                                text = "${entry.phoneNumber} • ${entry.timestampFormatted}",
                                style = IVITypography.BodyMedium,
                                color = IVIColors.OnSurfaceVariant
                            )
                        }
                    }

                    IVIIconButton(
                        icon = Icons.Default.Call,
                        contentDescription = "Call",
                        onClick = { onAction(PhoneAction.CallNumber(entry.phoneNumber)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactsTab(
    contacts: List<Contact>,
    onAction: (PhoneAction) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(IVISpacing.MD),
        modifier = Modifier.fillMaxSize()
    ) {
        items(contacts) { contact ->
            IVICard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onAction(PhoneAction.CallContact(contact)) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(IVISpacing.LG),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(IVISpacing.MD)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = IVIColors.Primary
                        )
                        Column {
                            Text(
                                text = contact.name,
                                style = IVITypography.TitleLarge,
                                color = IVIColors.OnSurface
                            )
                            Text(
                                text = contact.phoneNumber,
                                style = IVITypography.BodyMedium,
                                color = IVIColors.OnSurfaceVariant
                            )
                        }
                    }

                    IVIIconButton(
                        icon = Icons.Default.Call,
                        contentDescription = "Call",
                        onClick = { onAction(PhoneAction.CallContact(contact)) }
                    )
                }
            }
        }
    }
}
