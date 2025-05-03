package app.shapeshifter.core.base.inject

import me.tatarka.inject.annotations.Qualifier
import me.tatarka.inject.annotations.Scope
import kotlinx.coroutines.CoroutineScope

@Scope
annotation class ApplicationScope

@Scope
annotation class ActivityScope

@Qualifier
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE
)
annotation class DataStorePath

@Qualifier
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE
)
annotation class CachePath

typealias ApplicationCoroutineScope = CoroutineScope
