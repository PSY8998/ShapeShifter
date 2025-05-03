package app.shapeshifter.shared.prod

import android.app.Activity
import app.shapeshifter.core.base.inject.ActivityScope
import app.shapeshifter.shared.common.SharedActivityComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ActivityScope
@Component
abstract class AndroidActivityComponent(
    @get:Provides override val activity: Activity,
    @Component val applicationComponent: AndroidApplicationComponent,
) : SharedActivityComponent,
  ProdUiComponent {

  companion object
}
