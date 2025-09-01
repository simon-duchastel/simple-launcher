# Contacts Library

Contact selection and data retrieval utilities for the Simple Launcher application.

## Purpose

This library module provides a centralized system for handling Android contacts integration, including contact picker functionality and contact data retrieval from the system contacts database.

## Key Components

- **Contacts Repository** - Core contact management interface
- **Contact Data Models** - Type-safe contact information representation  
- **Contact Selection** - Activity result-based contact picker integration
- **Dependency Injection** - Hilt module configuration

## Architecture

```
modules/libs/contacts/
├── src/main/java/.../libs/contacts/
│   ├── data/
│   │   ├── Contact.kt                    # Contact data model
│   │   ├── ContactsRepository.kt         # Contact management interface
│   │   └── ContactsRepositoryImpl.kt     # Contact implementation
│   └── di/
│       └── ContactsModule.kt             # Hilt DI configuration
└── build.gradle.kts                      # Module dependencies
```

## Contact Models

### Contact
Basic contact information container:
- **id** - Unique contact identifier from system database
- **phoneNumber** - Primary phone number for the contact

## Usage Examples

### Initialize Repository

**NOTE:** This must be done in ALL activities in which `ContactsRepository` is used.

```kotlin
// In Activity onCreate()
@Inject
lateinit var contactsRepository: ContactsRepository

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    contactsRepository.activityOnCreate()
}
```

### Pick Contact
```kotlin
// From coroutine scope
val contact = contactsRepository.pickContact()
contact?.let {
    println("Contact ID: ${it.id}")
    println("Phone: ${it.phoneNumber}")
}
```

## Integration

This library is used by:
- **Settings Feature** - Contact selection for homepage actions
- **SMS Integration** - Contact-based messaging functionality

## Dependencies

- Android Contacts API (ContactsContract)
- AndroidX Activity Result API  
- Kotlin Coroutines for async operations
- Hilt for dependency injection

## Permissions

Requires the following Android permissions:
- **READ_CONTACTS** - Access to system contacts database

## Best Practices

1. **Activity Lifecycle** - Always call `activityOnCreate()` in activity onCreate()
2. **Permission Handling** - Check READ_CONTACTS permission before using
3. **Null Safety** - Handle cases where contact selection is cancelled
4. **Error Recovery** - Gracefully handle contacts without phone numbers
5. **Activity Scoping** - Repository is activity-scoped, don't use across activities

## Testing

Includes tests for:
- Contact selection flow handling
- Contact data resolution logic
- Edge cases and error scenarios
- Permission-related functionality