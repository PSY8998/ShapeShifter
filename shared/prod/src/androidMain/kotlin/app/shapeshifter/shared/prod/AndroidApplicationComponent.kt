package app.shapeshifter.shared.prod

import android.app.Application
import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.shared.common.SharedApplicationComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class AndroidApplicationComponent(
    @get:Provides val application: Application,
) : SharedApplicationComponent,
  ProdApplicationComponent {

  companion object
}
