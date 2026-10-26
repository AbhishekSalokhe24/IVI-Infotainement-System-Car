# 📞 Phase 5: Phone & Communications

> **Parent Plan:** [00_Master_Implementation_Plan.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/00_Master_Implementation_Plan.md)  
> **Module:** `features/phone`  
> **Duration:** Weeks 17–19  
> **Goal:** Bluetooth hands-free calling, contacts, call log, and SMS

---

## 1. Phone Architecture

### 1.1 Bluetooth Profiles Used

| Profile | Full Name | Purpose |
|---------|-----------|---------|
| **HFP** | Hands-Free Profile | Make/receive calls, DTMF, voice dial |
| **PBAP** | Phone Book Access Profile | Sync contacts and call history |
| **MAP** | Message Access Profile | Read/send SMS messages |
| **A2DP** | Advanced Audio Distribution Profile | Media audio streaming |

### 1.2 System Integration

```
┌────────────────────────────────────────────────────┐
│                 IVI Phone Module                    │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐          │
│  │  Dialer  │ │ Contacts │ │ Call Log │          │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘          │
│       │             │            │                 │
│  ┌────┴─────────────┴────────────┴──────────────┐ │
│  │           Phone Repository                    │ │
│  └────┬──────────────────────────────────────────┘ │
│       │                                            │
│  ┌────┴──────────────────────────────────────────┐ │
│  │     TelecomManager / InCallService            │ │
│  │     BluetoothHeadsetClient (HFP)              │ │
│  │     BluetoothPbapClient (PBAP)                │ │
│  │     BluetoothMapClient (MAP)                  │ │
│  └───────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────┘
```

---

## 2. Phone Screens

### 2.1 Dialer Screen

```
┌──────────────────────────────────────────────────────────────┐
│                         PHONE                                │
│  [Favorites] [Recent] [Contacts] [Keypad]                    │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│               ┌──────────────────────┐                       │
│               │   +1 (555) 123-4567  │                       │
│               └──────────────────────┘                       │
│                                                              │
│               ┌─────┐ ┌─────┐ ┌─────┐                       │
│               │  1  │ │  2  │ │  3  │                       │
│               │     │ │ ABC │ │ DEF │                       │
│               ├─────┤ ├─────┤ ├─────┤                       │
│               │  4  │ │  5  │ │  6  │                       │
│               │ GHI │ │ JKL │ │ MNO │                       │
│               ├─────┤ ├─────┤ ├─────┤                       │
│               │  7  │ │  8  │ │  9  │                       │
│               │PQRS │ │ TUV │ │WXYZ │                       │
│               ├─────┤ ├─────┤ ├─────┤                       │
│               │  *  │ │  0  │ │  #  │                       │
│               │     │ │  +  │ │     │                       │
│               └─────┘ └─────┘ └─────┘                       │
│                                                              │
│               ┌─────────────────────┐                        │
│               │    📞 CALL (green)   │                        │
│               └─────────────────────┘                        │
└──────────────────────────────────────────────────────────────┘
```

### 2.2 Dialer Implementation

```kotlin
// features/phone/src/main/kotlin/com/ivi/phone/ui/dialer/DialerScreen.kt
package com.ivi.phone.ui.dialer

@Composable
fun DialerScreen(
    viewModel: DialerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(IVISpacing.LG),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Tab Row: Favorites | Recent | Contacts | Keypad
        PhoneTabRow(
            selectedTab = uiState.selectedTab,
            onTabSelected = { viewModel.onAction(DialerAction.SelectTab(it)) },
        )

        Spacer(Modifier.height(IVISpacing.LG))

        when (uiState.selectedTab) {
            PhoneTab.FAVORITES -> FavoritesList(
                favorites = uiState.favorites,
                onContactCall = { viewModel.onAction(DialerAction.CallContact(it)) },
            )
            PhoneTab.RECENT -> CallLogList(
                callLog = uiState.callLog,
                onEntryCall = { viewModel.onAction(DialerAction.CallNumber(it.number)) },
            )
            PhoneTab.CONTACTS -> ContactsList(
                contacts = uiState.contacts,
                searchQuery = uiState.contactSearchQuery,
                onSearchChange = { viewModel.onAction(DialerAction.SearchContacts(it)) },
                onContactCall = { viewModel.onAction(DialerAction.CallContact(it)) },
            )
            PhoneTab.KEYPAD -> DialPad(
                dialedNumber = uiState.dialedNumber,
                onDigitPress = { viewModel.onAction(DialerAction.DialDigit(it)) },
                onBackspace = { viewModel.onAction(DialerAction.Backspace) },
                onCall = { viewModel.onAction(DialerAction.CallDialed) },
            )
        }
    }
}

@Composable
private fun DialPad(
    dialedNumber: String,
    onDigitPress: (Char) -> Unit,
    onBackspace: () -> Unit,
    onCall: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Number display
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = dialedNumber.ifEmpty { "Enter number" },
                style = IVITypography.HeadlineLarge,
                color = if (dialedNumber.isNotEmpty()) IVIColors.OnSurface
                        else IVIColors.OnSurfaceDim,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            if (dialedNumber.isNotEmpty()) {
                IVIIconButton(
                    icon = Icons.Default.Backspace,
                    contentDescription = "Delete",
                    onClick = onBackspace,
                )
            }
        }

        Spacer(Modifier.height(IVISpacing.LG))

        // Digit Grid (3x4 + call button)
        val digits = listOf(
            Triple('1', "", ""),
            Triple('2', "ABC", ""),
            Triple('3', "DEF", ""),
            Triple('4', "GHI", ""),
            Triple('5', "JKL", ""),
            Triple('6', "MNO", ""),
            Triple('7', "PQRS", ""),
            Triple('8', "TUV", ""),
            Triple('9', "WXYZ", ""),
            Triple('*', "", ""),
            Triple('0', "+", ""),
            Triple('#', "", ""),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.widthIn(max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(IVISpacing.SM),
            horizontalArrangement = Arrangement.spacedBy(IVISpacing.SM),
        ) {
            items(digits) { (digit, sub, _) ->
                DialButton(
                    digit = digit,
                    subText = sub,
                    onClick = { onDigitPress(digit) },
                )
            }
        }

        Spacer(Modifier.height(IVISpacing.LG))

        // Call Button
        FloatingActionButton(
            onClick = onCall,
            modifier = Modifier.size(IVIDimensions.TouchTargetLarge),
            containerColor = IVIColors.Success,
            contentColor = Color.White,
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Call",
                modifier = Modifier.size(IVIDimensions.IconLarge),
            )
        }
    }
}
```

---

## 3. In-Call Screen

```kotlin
// features/phone/src/main/kotlin/com/ivi/phone/ui/incall/InCallScreen.kt
package com.ivi.phone.ui.incall

/**
 * Full-screen in-call UI. Shown as an overlay during active calls.
 * Must be visible even when other IVI screens are active.
 */
@Composable
fun InCallScreen(
    callState: CallState,
    onEndCall: () -> Unit,
    onMute: () -> Unit,
    onSpeaker: () -> Unit,
    onHold: () -> Unit,
    onDialpad: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = IVIColors.Surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(IVISpacing.XXL),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Contact Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(IVIColors.PrimaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                if (callState.contactAvatar != null) {
                    AsyncImage(
                        model = callState.contactAvatar,
                        contentDescription = "Contact Photo",
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(IVIDimensions.IconXLarge),
                        tint = IVIColors.Primary,
                    )
                }
            }

            Spacer(Modifier.height(IVISpacing.LG))

            // Contact Name
            Text(
                text = callState.contactName ?: callState.phoneNumber,
                style = IVITypography.HeadlineLarge,
                color = IVIColors.OnSurface,
            )

            // Call Duration / Status
            Text(
                text = when (callState.status) {
                    CallStatus.RINGING -> "Ringing..."
                    CallStatus.DIALING -> "Dialing..."
                    CallStatus.ACTIVE -> callState.durationFormatted
                    CallStatus.ON_HOLD -> "On Hold"
                    else -> ""
                },
                style = IVITypography.TitleMedium,
                color = IVIColors.OnSurfaceVariant,
            )

            Spacer(Modifier.height(IVISpacing.XXXL))

            // Call Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(IVISpacing.XL),
            ) {
                // Mute
                CallControlButton(
                    icon = if (callState.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    label = "Mute",
                    isActive = callState.isMuted,
                    onClick = onMute,
                )

                // Keypad
                CallControlButton(
                    icon = Icons.Default.Dialpad,
                    label = "Keypad",
                    onClick = onDialpad,
                )

                // Speaker
                CallControlButton(
                    icon = if (callState.isSpeaker) Icons.Default.VolumeUp
                           else Icons.Default.VolumeDown,
                    label = "Speaker",
                    isActive = callState.isSpeaker,
                    onClick = onSpeaker,
                )

                // Hold
                CallControlButton(
                    icon = Icons.Default.Pause,
                    label = "Hold",
                    isActive = callState.status == CallStatus.ON_HOLD,
                    onClick = onHold,
                )
            }

            Spacer(Modifier.height(IVISpacing.XXL))

            // End Call Button
            FloatingActionButton(
                onClick = onEndCall,
                modifier = Modifier.size(IVIDimensions.TouchTargetLarge),
                containerColor = IVIColors.Error,
                contentColor = Color.White,
            ) {
                Icon(
                    imageVector = Icons.Default.CallEnd,
                    contentDescription = "End Call",
                    modifier = Modifier.size(IVIDimensions.IconLarge),
                )
            }
        }
    }
}
```

---

## 4. Contacts Integration (PBAP)

```kotlin
// features/phone/src/main/kotlin/com/ivi/phone/data/source/ContactsDataSource.kt
package com.ivi.phone.data.source

import android.bluetooth.BluetoothPbapClient
import android.content.ContentResolver
import android.provider.ContactsContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Syncs contacts from connected Bluetooth phone via PBAP.
 * Falls back to local contacts provider.
 */
class ContactsDataSource @Inject constructor(
    private val contentResolver: ContentResolver,
) {
    /**
     * Get contacts synced from Bluetooth phone.
     * PBAP automatically syncs contacts to the system contacts provider
     * under a special Bluetooth account.
     */
    fun getContacts(): Flow<List<Contact>> = flow {
        val contacts = mutableListOf<Contact>()

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.STARRED,
            ),
            null, null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                contacts.add(
                    Contact(
                        id = it.getLong(0).toString(),
                        name = it.getString(1) ?: "Unknown",
                        phoneNumber = it.getString(2) ?: "",
                        photoUri = it.getString(3),
                        isFavorite = it.getInt(4) == 1,
                    )
                )
            }
        }

        emit(contacts)
    }

    /**
     * Get call history (synced via PBAP).
     */
    fun getCallLog(): Flow<List<CallLogEntry>> = flow {
        val entries = mutableListOf<CallLogEntry>()

        val cursor = contentResolver.query(
            android.provider.CallLog.Calls.CONTENT_URI,
            arrayOf(
                android.provider.CallLog.Calls._ID,
                android.provider.CallLog.Calls.NUMBER,
                android.provider.CallLog.Calls.CACHED_NAME,
                android.provider.CallLog.Calls.TYPE,
                android.provider.CallLog.Calls.DATE,
                android.provider.CallLog.Calls.DURATION,
            ),
            null, null,
            "${android.provider.CallLog.Calls.DATE} DESC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                entries.add(
                    CallLogEntry(
                        id = it.getLong(0).toString(),
                        number = it.getString(1) ?: "",
                        contactName = it.getString(2),
                        type = mapCallType(it.getInt(3)),
                        timestamp = it.getLong(4),
                        durationSeconds = it.getInt(5),
                    )
                )
            }
        }

        emit(entries)
    }

    private fun mapCallType(type: Int): CallType = when (type) {
        android.provider.CallLog.Calls.INCOMING_TYPE -> CallType.INCOMING
        android.provider.CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
        android.provider.CallLog.Calls.MISSED_TYPE -> CallType.MISSED
        android.provider.CallLog.Calls.REJECTED_TYPE -> CallType.REJECTED
        else -> CallType.UNKNOWN
    }
}

data class Contact(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val photoUri: String? = null,
    val isFavorite: Boolean = false,
)

data class CallLogEntry(
    val id: String,
    val number: String,
    val contactName: String?,
    val type: CallType,
    val timestamp: Long,
    val durationSeconds: Int,
)

enum class CallType { INCOMING, OUTGOING, MISSED, REJECTED, UNKNOWN }
```

---

## 5. SMS Viewer

```kotlin
// features/phone/src/main/kotlin/com/ivi/phone/ui/sms/SmsScreen.kt
package com.ivi.phone.ui.sms

/**
 * SMS viewer with driving restrictions.
 * While driving: Only shows sender name and "New Message" indicator.
 * While parked: Shows message preview.
 * Voice-to-text reply available in both states.
 */
@Composable
fun SmsScreen(
    viewModel: SmsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val restrictions = LocalUxRestrictions.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(IVISpacing.LG),
    ) {
        Text(
            text = "Messages",
            style = IVITypography.HeadlineMedium,
            color = IVIColors.OnSurface,
        )

        Spacer(Modifier.height(IVISpacing.MD))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(IVISpacing.SM),
        ) {
            items(uiState.conversations) { conversation ->
                SmsConversationItem(
                    conversation = conversation,
                    isRestricted = restrictions.isFullyRestricted,
                    onTap = { /* Open conversation */ },
                    onVoiceReply = {
                        viewModel.onAction(SmsAction.VoiceReply(conversation.id))
                    },
                )
            }
        }
    }
}

@Composable
private fun SmsConversationItem(
    conversation: SmsConversation,
    isRestricted: Boolean,
    onTap: () -> Unit,
    onVoiceReply: () -> Unit,
) {
    IVICard(onClick = if (isRestricted) null else onTap) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(IVISpacing.MD),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar
            ContactAvatar(
                name = conversation.contactName,
                photoUri = conversation.contactPhotoUri,
                size = IVIDimensions.IconXLarge,
            )

            Spacer(Modifier.width(IVISpacing.MD))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.contactName ?: conversation.phoneNumber,
                    style = IVITypography.TitleMedium,
                    color = IVIColors.OnSurface,
                )
                if (!isRestricted) {
                    Text(
                        text = conversation.lastMessage,
                        style = IVITypography.BodyMedium,
                        color = IVIColors.OnSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    Text(
                        text = "New message received",
                        style = IVITypography.BodyMedium,
                        color = IVIColors.OnSurfaceVariant,
                    )
                }
            }

            // Voice reply button (always accessible)
            IVIIconButton(
                icon = Icons.Default.Mic,
                contentDescription = "Voice Reply",
                onClick = onVoiceReply,
            )
        }
    }
}
```

---

## 6. Domain Models

```kotlin
// features/phone/src/main/kotlin/com/ivi/phone/domain/model/PhoneModels.kt
package com.ivi.phone.domain.model

data class CallState(
    val status: CallStatus = CallStatus.IDLE,
    val phoneNumber: String = "",
    val contactName: String? = null,
    val contactAvatar: String? = null,
    val isMuted: Boolean = false,
    val isSpeaker: Boolean = false,
    val startTime: Long = 0L,
) {
    val durationFormatted: String
        get() {
            if (startTime == 0L) return "00:00"
            val elapsed = (System.currentTimeMillis() - startTime) / 1000
            val min = elapsed / 60
            val sec = elapsed % 60
            return "%02d:%02d".format(min, sec)
        }
}

enum class CallStatus {
    IDLE, DIALING, RINGING, ACTIVE, ON_HOLD, DISCONNECTED
}

data class SmsConversation(
    val id: String,
    val phoneNumber: String,
    val contactName: String?,
    val contactPhotoUri: String?,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int,
)
```

---

## 7. Verification Checklist

- [ ] Dialer keypad renders with 76dp+ touch targets
- [ ] Dialing a number initiates a Bluetooth HFP call
- [ ] Incoming call shows full-screen In-Call UI
- [ ] Mute, speaker, hold, and keypad controls work
- [ ] End call terminates the Bluetooth HFP connection
- [ ] Contacts sync from phone via PBAP
- [ ] Contact search filters correctly (name + number)
- [ ] Favorites show starred contacts
- [ ] Call log displays with correct call type icons
- [ ] SMS conversations load from Bluetooth MAP
- [ ] SMS content is hidden while driving (UX restrictions)
- [ ] Voice reply button triggers speech-to-text
- [ ] Missed call badge shows on phone nav rail icon

---

*Next: [08_Settings_and_Profiles.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/08_Settings_and_Profiles.md) — System settings, user profiles, connectivity management*
