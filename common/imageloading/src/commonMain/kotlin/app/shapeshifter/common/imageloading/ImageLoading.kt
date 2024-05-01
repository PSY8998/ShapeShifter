package app.shapeshifter.common.imageloading

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import coil3.util.DebugLogger

internal fun newImageLoader(
    context: PlatformContext,
    interceptors: Set<Interceptor>,
    debug: Boolean = false,
): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            interceptors.forEach(::add)
        }
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, percent = 0.25)
                .build()
        }
        // TODO: add cache directory
        .apply {
            logger(DebugLogger())
        }
        .build()
}
