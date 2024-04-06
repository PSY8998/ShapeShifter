package app.shapeshifter.inject

import com.slack.circuit.foundation.Circuit
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class ApplicationComponent {
    @Provides
    fun provideCircuit() = Circuit.Builder().build()

    companion object
}