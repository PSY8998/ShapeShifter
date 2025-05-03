package app.shapeshifter.shared.common

import app.shapeshifter.core.base.inject.CachePath
import me.tatarka.inject.annotations.Provides

actual interface SharedPlatformApplicationComponent {
    @Provides
    @CachePath
    fun provideCachePath(): String = getCacheDir().absolutePath
}
