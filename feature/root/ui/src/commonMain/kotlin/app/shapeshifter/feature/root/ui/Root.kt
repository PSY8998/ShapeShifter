package app.shapeshifter.feature.root.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.NestedScaffold
import app.shapeshifter.common.ui.compose.resources.shapeshifter
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.common.ui.compose.screens.WorkoutsScreen
import com.slack.circuit.backstack.SaveableBackStack
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
            RootBottomNavigation(
                selectedNavigation = rootScreen,
                navigationItems = navigationItems,
                onNavigationSelected = { screen ->
                    navigator.resetRootIfDifferent(screen, saveState = true, restoreState = true)
                },
            )
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
                        modifier = if (item.screen == HomeScreen) Modifier.padding(3.dp) else Modifier,
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
    } else
        throw IllegalStateException("unable to determine image type")

    val unSelectedPainter = if (iconImage is DrawableResource) {
        painterResource(iconImage)
    } else if (selectedImage is ImageVector) {
        rememberVectorPainter(selectedImage)
    } else
        throw IllegalStateException("unable to determine image type")

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
            screen = WorkoutsScreen,
            label = "Workout",
            contentDescription = "Workout",
            iconImage = Res.drawable.grapler_outlined,
            selectedImage = Res.drawable.grapler_filled,
        ),
        RootNavigationItem(
            screen = ExercisesScreen,
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

