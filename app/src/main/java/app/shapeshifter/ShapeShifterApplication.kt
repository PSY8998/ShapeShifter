package app.shapeshifter

import android.app.Application
import app.shapeshifter.inject.ApplicationComponent
import app.shapeshifter.inject.create

class ShapeShifterApplication: Application() {

    val component: ApplicationComponent by lazy {
        ApplicationComponent.create(this)
    }
}