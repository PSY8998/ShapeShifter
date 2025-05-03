package app.shapeshifter.shared.prod

import app.shapeshifter.core.base.inject.ApplicationScope
import app.shapeshifter.shared.common.SharedApplicationComponent
import me.tatarka.inject.annotations.Component

@Component
@ApplicationScope
abstract class DesktopApplicationComponent :
  SharedApplicationComponent,
  ProdApplicationComponent {

  companion object
}
