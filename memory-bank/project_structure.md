# ShapeShifter Project Structure Documentation

This document outlines the structure of the ShapeShifter Kotlin Multiplatform project.

## 1. Overall Structure

The project is organized into several top-level directories, following a modular approach:

-   `app/`: The main Android application module.
-   `common/`: Shared modules used across different features or layers.
-   `core/`: Core base components and utilities.
-   `data/`: Modules related to data sources and repositories.
-   `domain/`: Core business logic and use cases.
-   `feature/`: Self-contained feature modules.
-   `gradle/`: Build system configuration (Gradle).
-   `memory-bank/`: Project documentation and context files.

## 2. Feature Modules (`feature/`)

Features are encapsulated in their own modules under the `feature/` directory (e.g., `feature/exercise`, `feature/home`, `feature/onboarding`). Each feature module typically contains sub-modules for its different layers:

-   `data/`: Data sources and repository implementations specific to the feature.
-   `domain/`: Use cases and business logic specific to the feature.
-   `ui/`: Jetpack Compose UI screens and components for the feature.

This promotes separation of concerns and independent feature development.

## 3. Data Layer (`data/`)

The `data/` directory contains modules responsible for interacting with various data sources:

-   `data/datastore`: Manages local key-value storage (likely using Jetpack DataStore).
-   `data/db`: Handles relational database operations using SQLDelight. Contains `.sq` migration files and generated Kotlin code.
-   `data/models`: Defines shared data transfer objects (DTOs) or data models used across layers.
-   `data/supabase`: Contains logic for interacting with a Supabase backend (authentication, database, etc.).

## 4. Domain Layer (`domain/`)

The top-level `domain/` module likely contains core business logic, use cases (interactors), and domain models that are shared across multiple features or represent the core concepts of the application.

## 5. Common Modules (`common/`)

Modules under `common/` provide shared functionality:

-   `common/imageloading`: Abstracted image loading capabilities (potentially using Coil, Glide, or another library).
-   `common/ui/compose`: Shared Jetpack Compose elements like themes, base components, design system elements, etc.

## 6. Core Module (`core/`)

-   `core/base`: Provides fundamental base classes, interfaces, or utility functions used throughout the application.

## 7. Application Module (`app/`)

The `app/` module is the main Android application. It integrates the various feature modules and common components to build the final APK. It contains:

-   `AndroidManifest.xml`: Android application manifest.
-   `res/`: Android resources (drawables, layouts, values).
-   `src/main/kotlin/`: Main application code, including dependency injection setup, navigation graph, and Application class.

## 8. Build System (`gradle/`)

The project uses Gradle for building and dependency management. Key elements include:

-   `build.gradle.kts` / `settings.gradle.kts`: Root build configuration files.
-   `gradle/libs.versions.toml`: Centralized dependency version catalog.
-   `gradle/build-logic`: Custom Gradle convention plugins for standardizing module builds.

## 9. Documentation (`memory-bank/`)

This directory stores project-related documentation, decision logs, context, etc.

## 10. Configuration Files

Root-level files configure the development environment and build:

-   `.editorconfig`: Defines coding styles for the editor.
-   `.gitignore`: Specifies intentionally untracked files for Git.
-   `.roomodes`: Configuration for the Roo AI assistant.
-   `compose-stability.conf`: Configuration for the Jetpack Compose compiler stability.
-   `gradle.properties`: Gradle build properties.