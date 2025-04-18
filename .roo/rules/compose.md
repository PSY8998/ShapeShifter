# UI guidelines

1. Use remember and derivedStateOf appropriately
2. Implement proper recomposition optimization
3. Use proper Compose modifiers ordering
4. Follow composable function naming conventions
5. Implement proper preview annotations
6. Use proper state management with MutableState
7. Implement proper error handling and loading states
8. Use proper theming with MaterialTheme
9. Follow accessibility guidelines
10. Implement proper animation patterns
11. Do not add business rules or logic to composable
12. Parameter ordering in composable is as follows
    1. All non optional parameters at top
    2. Modifier as option parameter
    3. Optional parameters at last


# Performance Guidelines

1. Minimize recomposition using proper keys
2. Use proper lazy loading with LazyColumn and LazyRow
3. Implement efficient image loading
4. Use proper state management to prevent unnecessary updates
5. Follow proper lifecycle awareness
6. Implement proper memory management
7. Use proper background processing
8. Collect state with lifecycle aware methods