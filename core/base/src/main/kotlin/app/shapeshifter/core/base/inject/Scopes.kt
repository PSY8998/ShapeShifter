package app.shapeshifter.core.base.inject

import me.tatarka.inject.annotations.Scope
import kotlinx.coroutines.CoroutineScope

@Scope
annotation class ApplicationScope

@Scope
annotation class ActivityScope

typealias ApplicationCoroutineScope = CoroutineScope
