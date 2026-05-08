# Project Structure

This project follows **Clean Architecture** principles, separating the code into `data`, `domain`, and `presentation` layers.

```text
app/
├── build.gradle.kts
├── src/
│   └── main/
│       ├── AndroidManifest.xml
│       └── java/com/resumebuilder/
│           ├── ResumeBuilderApp.kt         (Application class)
│           ├── MainActivity.kt             (Entry point activity)
│           │
│           ├── data/                       (Data Layer)
│           │   ├── local/                  (Room Database)
│           │   │   ├── dao/
│           │   │   │   ├── ResumeDao.kt
│           │   │   │   └── UserDao.kt
│           │   │   ├── entity/
│           │   │   │   ├── ResumeEntity.kt
│           │   │   │   └── UserEntity.kt
│           │   │   ├── Converters.kt
│           │   │   └── ResumeDatabase.kt
│           │   ├── remote/                 (API & Firebase)
│           │   │   ├── FirebaseService.kt
│           │   │   └── GeminiService.kt
│           │   └── repository/             (Repository Impls)
│           │       ├── AIRepository.kt
│           │       ├── AuthRepository.kt
│           │       └── ResumeRepository.kt
│           │
│           ├── domain/                     (Business Logic)
│           │   ├── model/                  (Plain Data Classes)
│           │   │   ├── Education.kt
│           │   │   ├── Experience.kt
│           │   │   ├── PersonalInfo.kt
│           │   │   ├── Project.kt
│           │   │   ├── Resume.kt
│           │   │   ├── Skill.kt
│           │   │   └── User.kt
│           │   └── usecase/                (Business Actions)
│           │       ├── AuthUseCases.kt
│           │       ├── GenerateResumeUseCase.kt
│           │       └── SaveResumeUseCase.kt
│           │
│           ├── di/                         (Dependency Injection)
│           │   ├── AppModule.kt
│           │   ├── DatabaseModule.kt
│           │   └── NetworkModule.kt
│           │
│           ├── presentation/               (UI Layer)
│           │   └── theme/                  (Compose Styling)
│           │       ├── Color.kt
│           │       ├── Theme.kt
│           │       └── Type.kt
│           │
│           └── util/                       (Utilities)
│               ├── Constants.kt
│               ├── PdfGenerator.kt
│               └── Resource.kt             (State wrappers)
│
├── build.gradle.kts (Project Level)
├── settings.gradle.kts
└── gradle.properties
```

## Layer Responsibilities:

- **Domain Layer**: Contains the core business logic, models, and use cases. It is the innermost layer and has no dependencies on Android or external libraries (ideally).
- **Data Layer**: Responsible for managing data from different sources (Room database, Firebase, Gemini AI). It implements the repositories defined (or used) by the domain layer.
- **Presentation Layer**: Handles the UI components and state management using Jetpack Compose.
- **DI (Dependency Injection)**: Uses Hilt to provide dependencies across the application.
- **Util**: Contains common helper classes used throughout the app.
