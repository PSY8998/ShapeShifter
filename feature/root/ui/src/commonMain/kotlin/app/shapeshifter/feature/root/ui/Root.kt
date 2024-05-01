package app.shapeshifter.feature.root.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.resources.shapeshifter
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.common.ui.compose.screens.SavedWorkoutsScreen
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.isAtRoot
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import shapeshifter.feature.root.ui.generated.resources.Res
import shapeshifter.feature.root.ui.generated.resources.grapler_filled
import shapeshifter.feature.root.ui.generated.resources.grapler_outlined

@Composable
fun Root(
    navigator: Navigator,
    backStack: SaveableBackStack,
    modifier: Modifier = Modifier,
) {
    val rootScreen: Screen by remember(backStack) {
        derivedStateOf { backStack.last().screen }
    }

    val navigationItems = remember { buildNavigationItems() }

    NestedScaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = backStack.isAtRoot,
                exit = hideBelow,
                enter = showFromBelow,
            ) {
                RootBottomNavigation(
                    selectedNavigation = rootScreen,
                    navigationItems = navigationItems,
                    onNavigationSelected = { screen ->
                        navigator.resetRootIfDifferent(
                            screen,
                            saveState = true,
                            restoreState = true,
                        )
                    },
                )
            }
        },
    ) {
        NavigableCircuitContent(
            navigator = navigator,
            backStack = backStack,
            modifier = Modifier,
        )
    }
}

@Composable
private fun RootBottomNavigation(
    selectedNavigation: Screen,
    navigationItems: List<RootNavigationItem>,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        windowInsets = WindowInsets.navigationBars,
    ) {
        for (item in navigationItems) {
            NavigationBarItem(
                selected = selectedNavigation == item.screen,
                icon = {
                    RootNavigationIcon(
                        selected = selectedNavigation == item.screen,
                        selectedImage = item.selectedImage,
                        iconImage = item.iconImage,
                        contentDescription = item.contentDescription,
                        modifier = if (item.screen == HomeScreen) {
                            Modifier.padding(3.dp)
                        } else {
                            Modifier
                        },
                    )
                },
                label = {
                    Text(item.label)
                },
                onClick = {
                    onNavigationSelected(item.screen)
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun RootNavigationIcon(
    selected: Boolean,
    iconImage: Any,
    selectedImage: Any,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val selectedPainter = if (selectedImage is DrawableResource) {
        painterResource(selectedImage)
    } else if (selectedImage is ImageVector) {
        rememberVectorPainter(selectedImage)
    } else {
        throw IllegalStateException("unable to determine image type")
    }

    val unSelectedPainter = if (iconImage is DrawableResource) {
        painterResource(iconImage)
    } else if (selectedImage is ImageVector) {
        rememberVectorPainter(selectedImage)
    } else {
        throw IllegalStateException("unable to determine image type")
    }

    Icon(
        painter = if (selected) selectedPainter else unSelectedPainter,
        modifier = modifier,
        contentDescription = contentDescription,
    )
}

@Immutable
private data class RootNavigationItem(
    val screen: Screen,
    val label: String,
    val contentDescription: String,
    val iconImage: Any,
    val selectedImage: Any,
)

@OptIn(ExperimentalResourceApi::class)
private fun buildNavigationItems(): List<RootNavigationItem> {
    return listOf(
        RootNavigationItem(
            screen = HomeScreen,
            label = "Home",
            contentDescription = "Home",
            iconImage = shapeshifter(true),
            selectedImage = shapeshifter(true),
        ),
        RootNavigationItem(
            screen = SavedWorkoutsScreen,
            label = "Workout",
            contentDescription = "Workout",
            iconImage = Res.drawable.grapler_outlined,
            selectedImage = Res.drawable.grapler_filled,
        ),
        RootNavigationItem(
            screen = ExercisesScreen(canSelect = true),
            label = "Exercise",
            contentDescription = "Exercise",
            iconImage = Icons.Outlined.Face,
            selectedImage = Icons.Default.Face,
        ),
    )
}

private fun Navigator.resetRootIfDifferent(
    screen: Screen,
    saveState: Boolean = false,
    restoreState: Boolean = false,
) {
    val backStack = peekBackStack()
    if (backStack.size > 1 || backStack.lastOrNull() != screen) {
        resetRoot(screen, saveState, restoreState)
    }
}

private val FastOutExtraSlowInEasing = CubicBezierEasing(0.208333f, 0.82f, 0.25f, 1f)
private val AccelerateEasing = CubicBezierEasing(0.3f, 0f, 1f, 1f)
private const val DEBUG_MULTIPLIER = 1
private const val SHORT_DURATION = 83 * DEBUG_MULTIPLIER
private const val NORMAL_DURATION = 450 * DEBUG_MULTIPLIER

val hideBelow = slideOutVertically { it }

val showFromBelow = slideInVertically { it }

@Suppress("unused")
val hide = fadeOut(
    animationSpec =
    tween(
        durationMillis = SHORT_DURATION,
        delayMillis = 0,
        easing = AccelerateEasing,
    ),
) +
    slideOutHorizontally(
        targetOffsetX = { fullWidth -> (fullWidth / 10) * -1 },
        animationSpec =
        tween(durationMillis = NORMAL_DURATION, easing = FastOutExtraSlowInEasing),
    ) + shrinkHorizontally(
    animationSpec =
    tween(durationMillis = NORMAL_DURATION, easing = FastOutExtraSlowInEasing),
    targetWidth = { (it * .9f).toInt() },
    shrinkTowards = Alignment.End,
)

@Suppress("unused")
val show = fadeIn(
    animationSpec =
    tween(
        durationMillis = SHORT_DURATION,
        delayMillis = 0,
        easing = LinearEasing,
    ),
) +
    slideInHorizontally(
        initialOffsetX = { fullWidth -> (fullWidth / 10) * -1 },
        animationSpec =
        tween(durationMillis = NORMAL_DURATION, easing = FastOutExtraSlowInEasing),
    ) + EnterTransition.None
