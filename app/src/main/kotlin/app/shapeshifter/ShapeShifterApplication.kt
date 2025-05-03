package app.shapeshifter

import android.app.Application
import android.os.Build
import android.os.StrictMode
import app.shapeshifter.shared.prod.AndroidApplicationComponent
import app.shapeshifter.shared.prod.create

class ShapeShifterApplication : Application() {

    val component: AndroidApplicationComponent by lazy {
        AndroidApplicationComponent.create(this)
    }

    override fun onCreate() {
        super.onCreate()
        setupStrictMode()
    }

    private fun setupStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build(),
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectActivityLeaks()
                .detectLeakedClosableObjects()
                .detectLeakedRegistrationObjects()
                .detectFileUriExposure()
                .detectCleartextNetwork()
                .apply {
                    if (Build.VERSION.SDK_INT >= 26) {
                        detectContentUriWithoutPermission()
                    }
                    if (Build.VERSION.SDK_INT >= 29) {
                        detectCredentialProtectedWhileLocked()
                    }
                    if (Build.VERSION.SDK_INT >= 31) {
                        detectIncorrectContextUse()
                        detectUnsafeIntentLaunch()
                    }
                }
                .penaltyLog()
                .build(),
        )
    }
}
