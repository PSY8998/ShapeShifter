package app.shapeshifter.common.imageloading

import coil3.ImageLoader
import coil3.PlatformContext
import me.tatarka.inject.annotations.Provides

expect interface ImageLoadingPlatformComponent

interface ImageLoadingComponent : ImageLoadingPlatformComponent {

    val imageLoader: ImageLoader

    @Provides
    fun provideImageLoader(
        context: PlatformContext,
    ): ImageLoader = newImageLoader(
        context = context,
        interceptors = emptySet(),
    )
}
