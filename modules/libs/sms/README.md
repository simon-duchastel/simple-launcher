# SMS Library

SMS functionality and messaging utilities for the Simple Launcher application.

## Purpose

This library module provides SMS sending, receiving, and management capabilities, integrating with Android's telephony framework to enable messaging features within the launcher.

## Key Components

- **SMS Repository** - Core SMS operations interface and implementation
- **SMS Broadcast Receiver** - Incoming SMS message handling
- **Message Models** - SMS data structures and utilities
- **Dependency Injection** - Hilt module configuration

## Architecture

```
modules/libs/sms/
├── src/main/java/.../libs/sms/
│   ├── data/
│   │   ├── SmsRepository.kt           # SMS operations interface
│   │   ├── SmsRepositoryImpl.kt       # SMS implementation
│   │   ├── SmsBroadcastReceiver.kt    # Incoming SMS handler
│   │   └── SmsBroadcastReceiverFactoryImpl.kt # Receiver factory
│   └── di/
│       └── SmsModule.kt               # Hilt DI configuration
└── src/test/                          # Unit tests
```

## Key Features

### SMS Sending
- **Text Messages** - Send plain text SMS messages
- **Multi-part Messages** - Handle long messages automatically
- **Delivery Confirmation** - Track message delivery status
- **Error Handling** - Handle network and carrier issues

### SMS Receiving
- **Broadcast Receiver** - Listen for incoming SMS messages
- **Message Parsing** - Extract sender, content, and metadata
- **Filtering** - Process relevant messages only
- **Real-time Updates** - Notify other components of new messages

### Message Management
- **Message History** - Access SMS conversation history
- **Contact Integration** - Link messages to contacts
- **Message Search** - Find specific messages or conversations

## Data Models

### SMS Message
```kotlin
data class SmsMessage(
    val id: String,
    val sender: String,
    val recipient: String,
    val content: String,
    val timestamp: Long,
    val status: MessageStatus
)
```

### Message Status
```kotlin
enum class MessageStatus {
    PENDING,
    SENT,
    DELIVERED,
    FAILED
}
```

## Usage Examples

### Send SMS
```kotlin
@Inject
lateinit var smsRepository: SmsRepository

// Send text message
smsRepository.sendSms(
    recipient = "+1234567890",
    message = "Hello from Simple Launcher!"
) { result ->
    when (result) {
        is SmsResult.Success -> showSuccess()
        is SmsResult.Failed -> showError(result.error)
    }
}
```

### Listen for Incoming SMS
```kotlin
// Register SMS receiver
val receiver = SmsBroadcastReceiver { smsMessage ->
    // Process incoming message
    handleIncomingSms(smsMessage)
}

// Lifecycle management handled automatically
```

## Integration

This library integrates with:
- **Permissions Library** - SMS permission management
- **Settings Feature** - SMS configuration and preferences
- **Core Extensions** - Utility functions and extensions

## Dependencies

- Android Telephony APIs (SmsManager, TelephonyManager)
- Broadcast Receivers for SMS listening
- Permissions library for SMS permission handling
- Core Extensions library
- Hilt for dependency injection

## Permission Requirements

Required permissions:
- `android.permission.SEND_SMS` - Send SMS messages
- `android.permission.RECEIVE_SMS` - Receive SMS messages (optional)
- `android.permission.READ_SMS` - Read SMS history (optional)

## Testing

Comprehensive testing includes:
- **SmsRepositoryImplTest** - Core SMS functionality
- **Unit Tests** - Message parsing and validation
- **Integration Tests** - SMS sending and receiving flows

## Security Considerations

1. **Permission Validation** - Ensure SMS permissions before operations
2. **Rate Limiting** - Prevent SMS spam and abuse
3. **Content Filtering** - Sanitize message content
4. **Privacy Protection** - Handle SMS data securely
5. **User Consent** - Require explicit user approval for SMS operations

## Error Handling

```kotlin
sealed class SmsResult {
    object Success : SmsResult()
    data class Failed(val error: SmsError) : SmsResult()
}

enum class SmsError {
    NO_PERMISSION,
    NO_SERVICE,
    INVALID_RECIPIENT,
    MESSAGE_TOO_LONG,
    NETWORK_ERROR
}
```