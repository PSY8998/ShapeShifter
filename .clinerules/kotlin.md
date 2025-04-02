# Kotlin Language Rules

**Use Kotlin idioms properly**:
- Use extension functions to extend functionality
- Leverage scope functions (let, apply, run, with, also) appropriately
- Use property delegation when applicable (by lazy, by viewModels())

**Immutability First**:
- Default to val for all declarations
- Convert to var only when mutability is explicitly needed
- Use immutable collections (List, Set, Map) instead of mutable ones

**Function Purity**:
- Aim for pure functions (same output for same input, no side effects)
- Separate computation from side effects
- Mark functions with side effects clearly in naming or documentation

**Parameter Handling**:
- Use named parameters for functions with many parameters
- Provide default values for optional parameters
- Avoid boolean flags as parameters; use enum or sealed classes instead

**Extension Functions**:
- Create extension functions for operations that conceptually belong to a class
- Keep extensions focused on a single responsibility
- Group related extensions in files by the type they extend

**Expression-Oriented Programming**:
- Use if, when, and try as expressions instead of statements
- Return values directly from expressions rather than using temporary variables
- Utilize the last expression in a block as its return value

**Exception Handling**:
- Use sealed classes/Result type for expected error conditions
- Reserve exceptions for exceptional circumstances
- Create custom exceptions that clearly express failure conditions

**Null Safety**:
- Design APIs to minimize nullability
- Use non-null assertions (!!) only in tests or when absolutely necessary
- Prefer ?.let chains over nested null checks

**Collection Operations**:
- Favor functional collection operations (map, filter, etc.) over imperative loops
- Use sequence for large collections to improve performance
- Chain operations appropriately for readability and performance

**Higher-Order Functions**:
- Use lambdas for behavior customization
- Keep lambda parameters named meaningfully (avoid default it for complex lambdas)
- Consider extracting complex lambdas to named functions

**Inline Functions**:
- Use inline for higher-order functions that are called frequently
- Apply crossinline and noinline modifiers appropriately
- Document performance implications of inline functions

**Delegation**:
- Use by delegation for composition over inheritance
- Leverage standard delegates (lazy, observable, etc.)
- Create custom property delegates for repeated patterns

**Companion Objects**:
- Use companion objects for factory methods and constants
- Keep companion objects focused on class-related functionality
- Avoid using companion objects as static utility classes

**Sealed Classes and Objects**:
- Use sealed classes for representing restricted class hierarchies
- Prefer sealed classes over enums for complex states with associated data
- Leverage exhaustive when statements with sealed classes

**DSL Construction**:
- Create type-safe builders for complex object construction
- Use receiver types (this) consistently in DSLs
- Document DSL usage with code examples

**Coroutine Guidelines**:
- Structure coroutines with clear parent-child relationships
- Use appropriate dispatchers for different types of work
- Apply timeouts to prevent long-running operations