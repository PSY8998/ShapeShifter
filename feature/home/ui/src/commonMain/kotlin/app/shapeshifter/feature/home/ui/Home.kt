package app.shapeshifter.feature.home.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import app.shapeshifter.common.ui.compose.R
import app.shapeshifter.common.ui.compose.resources.shapeshifter
import app.shapeshifter.common.ui.compose.screens.ExercisesScreen
import app.shapeshifter.common.ui.compose.screens.HomeScreen
import app.shapeshifter.common.ui.compose.screens.WorkoutsScreen
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import shapeshifter.feature.home.ui.generated.resources.Res
import shapeshifter.feature.home.ui.generated.resources.grapler_filled
import shapeshifter.feature.home.ui.generated.resources.grapler_outlined

@Inject
class HomeUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is HomeScreen -> {
                ui<EmptyUiState> { _, modifier ->
                    Workout(modifier)
                }
            }

            else -> null
        }
    }
}

@Composable
fun Workout(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        Text(
            text = "Workout",
            modifier = Modifier
                .padding(paddingValues),
        )
    }
}

@Composable
fun Home(
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
            HomeBottomNavigation(
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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun HomeBottomNavigation(
    selectedNavigation: Screen,
    navigationItems: List<HomeNavigationItem>,
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
                    HomeNavigationIcon(
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
                ),
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun HomeNavigationIcon(
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
private data class HomeNavigationItem(
    val screen: Screen,
    val label: String,
    val contentDescription: String,
    val iconImage: Any,
    val selectedImage: Any,
)

@OptIn(ExperimentalResourceApi::class)
private fun buildNavigationItems(): List<HomeNavigationItem> {
    return listOf(
        HomeNavigationItem(
            screen = HomeScreen,
            label = "Home",
            contentDescription = "Home",
            iconImage = shapeshifter(true),
            selectedImage = shapeshifter(true),
        ),
        HomeNavigationItem(
            screen = WorkoutsScreen,
            label = "Workout",
            contentDescription = "Workout",
            iconImage = Res.drawable.grapler_outlined,
            selectedImage = Res.drawable.grapler_filled,
        ),
        HomeNavigationItem(
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
