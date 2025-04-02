# Product Context

This file provides a high-level overview of the project and the expected product that will be created. Initially it is based upon projectBrief.md (if provided) and all other available project-related information in the working directory. This file is intended to be updated as the project evolves, and should be used to inform all other modes of the project's goals and context.
2025-04-02 19:39:45 - Log of updates made will be appended as footnotes to the end of this file.

*

## Project Goal

*   Likely a fitness and workout tracking application for Android, named "ShapeShifter".

## Key Features

*   User Onboarding (`feature:onboarding`)
*   Home screen/dashboard (`feature:home`)
*   Exercise tracking/logging (`feature:exercise`)
*   Workout planning/execution (`feature:workout`)
*   User Profile management (`feature:profile`)
*   Root navigation structure (`feature:root`)

## Overall Architecture

*   Multi-module Android application following a feature-driven, clean architecture pattern.
*   **Layers/Modules:** `app` (integration), `feature/*` (UI/domain logic per feature), `common/*` (shared UI/utilities), `domain` (core business logic/models), `core/*` (base utilities), `data/*` (repositories, data sources).
*   **Key Technologies:** Kotlin, Jetpack Compose (Multiplatform), Android SDK, SQLDelight (local DB), Supabase (backend DB), DataStore (local preferences), KotlinInject (DI), Circuit (UI architecture), Gradle.
*