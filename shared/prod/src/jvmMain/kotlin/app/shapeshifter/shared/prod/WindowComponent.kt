package app.shapeshifter.shared.prod

import app.shapeshifter.core.base.inject.ActivityScope
import me.tatarka.inject.annotations.Component

@ActivityScope
@Component
abstract class WindowComponent(
    @Component val applicationComponent: DesktopApplicationComponent,
) : ProdUiComponent {

  companion object
}
