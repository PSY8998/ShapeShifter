package app.shapeshifter.inject

import app.shapeshifter.inject.scopes.ActivityScope
import me.tatarka.inject.annotations.Component

@ActivityScope
@Component
abstract class AndroidActivityComponent {
    companion object
}