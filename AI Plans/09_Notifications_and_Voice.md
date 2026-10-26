# 🔔 Phase 7: Notifications & Voice Assistant

> **Parent Plan:** [00_Master_Implementation_Plan.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/00_Master_Implementation_Plan.md)  
> **Duration:** Weeks 22–24  

---

## 1. Notification System

### 1.1 Priority Levels

| Priority | Examples | While Driving |
|----------|----------|---------------|
| CRITICAL | ADAS alerts, collision warnings | Always show + audio |
| HIGH | Incoming call, nav maneuver, low fuel | Heads-up 5s + chime |
| MEDIUM | New message, email | Status bar icon only |
| LOW | App updates, promotional | Silent, queued |

### 1.2 NotificationListenerService

- Intercepts all system notifications via `NotificationListenerService`
- Applies automotive filtering (priority mapping, text truncation while driving)
- Routes to Heads-Up overlay (CRITICAL/HIGH) or Notification Center (MEDIUM/LOW)
- Enforces `CarUxRestrictions` — limits content length and actions while driving
- Max 2 action buttons per notification (automotive guideline)

### 1.3 Heads-Up Notification

- Slides in from top, centered, 60% screen width
- Auto-dismiss: 5s (HIGH while driving), 8s (HIGH parked), never (CRITICAL)
- Shows: app icon, title, text (truncated), up to 2 action buttons, dismiss button
- Haptic feedback on appearance

### 1.4 Notification Center

- Pull-down overlay (70% screen height) from status bar
- Groups notifications by app package
- "Clear All" button (skips ongoing notifications)
- Empty state with icon + message
- Swipe-to-dismiss individual notifications

---

## 2. Voice Assistant

### 2.1 Architecture

```
Wake Word → STT (SpeechRecognizer) → Intent Parser → Command Executor → TTS Response
```

### 2.2 Supported Commands

| Category | Example Commands |
|----------|-----------------|
| Navigation | "Navigate to [destination]", "Find nearby gas station" |
| Media | "Play [song/artist]", "Pause", "Next song" |
| Phone | "Call [contact]", "Read my messages" |
| Climate | "Set temperature to 22 degrees", "Turn on/off AC" |
| General | "What's the weather?", "What time is it?" |

### 2.3 Implementation

- **STT**: Android `SpeechRecognizer` with partial results for live transcript
- **Intent Parser**: Rule-based parser (upgradable to on-device NLU model)
- **Command Executor**: Routes parsed intents to feature module APIs
- **TTS**: Android `TextToSpeech` for spoken responses
- **UI Overlay**: Full-screen scrim with pulsing mic icon, live transcript, response text

### 2.4 Voice States

```kotlin
sealed interface VoiceState {
    data object Idle : VoiceState
    data object Listening : VoiceState      // Mic active, showing waveform
    data object Processing : VoiceState     // Parsing command
    data class Responding(val text: String) : VoiceState  // Speaking response
    data class Error(val message: String) : VoiceState
}
```

---

## 3. UX Restriction Enforcement

```kotlin
// Observe driving state from CarUxRestrictionsManager
data class UxRestrictionState(
    val isFullyRestricted: Boolean = false,
    val noTextEntry: Boolean = false,
    val noFilteredList: Boolean = false,
    val limitStringLength: Boolean = false,
    val maxStringLength: Int = 120,
    val maxCumulativeContentItems: Int = 12,
)
```

- Provided via `CompositionLocal` to all Composables
- `RestrictedContent` wrapper hides/simplifies content while driving
- Text input disabled while driving
- List items capped at `maxCumulativeContentItems`

---

## 4. Verification Checklist

- [ ] Notification listener receives all system notifications
- [ ] Priority-based routing works correctly
- [ ] Heads-up slides in/out with animation
- [ ] Notification center groups and clears properly
- [ ] Voice recognition starts on button press
- [ ] Live transcript updates during speech
- [ ] All command categories parse and execute
- [ ] TTS speaks responses clearly
- [ ] UX restrictions enforce content limits while driving
- [ ] Voice assistant works hands-free while driving

---

*Next: [10_Testing_and_Quality.md](file:///c:/Users/abhis/AOSP/Projects/IVI%20Project/AI%20Plans/10_Testing_and_Quality.md)*
