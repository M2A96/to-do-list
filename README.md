# ToDo List App with Jetpack Compose

A modern Android application built with Jetpack Compose that allows users to create, manage, and organize their tasks with a clean and intuitive interface.

## Architecture Overview

This application follows Clean Architecture principles combined with MVI (Model-View-Intent) pattern for the presentation layer. The project is structured into the following layers:

### Clean Architecture Layers

1. **Presentation Layer (UI)**
   - Implements MVI pattern
   - Uses Jetpack Compose for UI
   - ViewModels manage UI state and handle user interactions
   - Compose screens observe state flows and render UI accordingly

2. **Domain Layer**
   - Contains business logic and use cases
   - Defines repository interfaces
   - Houses domain models
   - Independent of any framework-specific code

3. **Data Layer**
   - Implements repository interfaces
   - Handles data operations and transformations
   - Contains API service definitions
   - Manages local storage with Room database

### MVI Implementation

The application follows Model-View-Intent pattern with the following components:

- **Model**: Represented by State classes (e.g., `TaskScreenState`)
- **View**: Compose screens that render UI based on states
- **Intent**: User actions represented by Event classes (e.g., `HomeScreenEvent`)

#### State Management
- States are immutable data classes
- ViewModels expose states through StateFlow
- UI recomposes based on state changes

## Key Features

- Create and manage todo tasks
- Set task priorities
- Mark tasks as completed
- Search and filter tasks
- Sync tasks with remote server
- Offline support with local database
- Dark/Light theme support
- Material Design 3 implementation

## Tech Stack

### Core Libraries
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit
- **Coroutines & Flow** - Asynchronous programming
- **Hilt** - Dependency injection
- **Retrofit** - Network calls
- **Room** - Local database storage
- **Gson** - JSON parsing
- **Navigation Compose** - In-app navigation

### Architecture Components
- **ViewModel** - UI state management
- **StateFlow** - Reactive state holder
- **Lifecycle** - Lifecycle-aware components

### Networking
- **OkHttp** - HTTP client
- **Retrofit** - REST client

## Project Structure

```
io.github.todolist/
├── core/
│   └── di/
│       ├── module/
│       │   ├── AppModule
│       │   ├── DatabaseModule
│       │   ├── NetworkModule
│       │   └── RepositoryModule
│       └── qualifier/
├── data/
│   ├── api/
│   │   ├── MockTaskApiService
│   │   └── TasksApi
│   ├── dao/
│   │   └── ToDoDao
│   ├── dto/
│   ├── local/
│   │   └── database/
│   │       └── ToDoDatabase
│   ├── mapper/
│   └── repository/
│       ├── AllToDoTasksRepositoryImpl
│       └── TaskRepositoryImpl
├── domain/
│   ├── model/
│   │   ├── Priority
│   │   ├── Resource
│   │   ├── TaskStatus
│   │   └── ToDoTask
│   ├── repository/
│   │   ├── AllTasksRepository
│   │   └── TaskRepository
│   └── usecase/
│       ├── AddTaskUseCase
│       ├── EditTaskUseCase
│       ├── GetAllTasksUseCase
│       ├── RemoveTaskUseCase
│       └── SyncTasksUseCase
└── presentation/
    ├── navigation/
    ├── theme/
    └── ui/
        ├── common/
        └── home/
            ├── HomeScreen
            ├── HomeViewModel
            ├── SearchDebouncer
            └── TaskScreenState
```

## UI Components

### Home Screen
- Task list with priority indicators
- Add/Edit task functionality
- Search box with debounced filtering
- Task completion status toggle
- Sync status indicator

## Theme

The app implements a dynamic Material Design 3 theme with:
- Light/Dark mode support
- Custom color schemes
- Consistent typography
- Reusable components

## Network Layer

### API Configuration
- Mock API service for development
- Real API integration for production
- Support for task synchronization

### Error Handling
- Generic error handling through Resource wrapper
- Loading states management
- Error states with user-friendly messages

## Local Storage

- Room Database for persistent storage
- DAO pattern for database access
- Entity mapping between domain and data layers

## Dependency Injection

Hilt is used for dependency injection with the following modules:
- `AppModule`: Application-wide dependencies
- `DatabaseModule`: Database-related dependencies
- `NetworkModule`: Network-related dependencies
- `RepositoryModule`: Repository implementations

## Getting Started

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or newer
- JDK 17 or newer
- Android SDK 34

### Building the Project
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on an emulator or physical device

## Architecture Flow

1. **User Interaction**
   - User interacts with UI
   - Events are created and sent to ViewModel

2. **ViewModel Processing**
   - ViewModel receives events
   - Processes through use cases
   - Updates state

3. **Data Flow**
   - Use cases interact with repositories
   - Repositories fetch from API or local database
   - Data is mapped to domain models

4. **UI Updates**
   - State updates trigger recomposition
   - UI reflects new state
   - Loading and error states handled

## Contributing

Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the LICENSE.md file for details