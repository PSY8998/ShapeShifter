package app.shapeshifter.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.OnboardingScreen
import app.shapeshifter.feature.onboarding.data.onboardingPages
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class OnboardingUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is OnboardingScreen -> {
                ui<OnboardingUiState> { state, modifier ->
                    Onboarding(state, modifier)
                }
            }

            else -> null
        }
    }
}

@Composable
fun Onboarding(
    state: OnboardingUiState,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        initialPage = state.currentPageIndex,
        pageCount = { onboardingPages.size },
    )

    LaunchedEffect(state.currentPageIndex) {
        if (pagerState.currentPage != state.currentPageIndex) {
            pagerState.animateScrollToPage(state.currentPageIndex)
        }
    }

    val eventSink = state.eventSink

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false,
        ) { pageIndex ->
            OnboardingPageScreen(
                pageIndex = pageIndex,
                selectedGoal = state.selectedGoal,
                onGoalSelected = { goal -> eventSink(OnboardingUiEvent.SelectGoal(goal)) },
                modifier = Modifier.fillMaxSize(),
            )
        }

        Spacer(
            modifier = Modifier.height(Dimens.Padding.Small),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = if (state.currentPageIndex > 0) Arrangement.SpaceBetween else Arrangement.End,
        ) {
            if (state.currentPageIndex > 0) {
                TextButton(onClick = { eventSink(OnboardingUiEvent.GoPrevious) }) {
                    Text("Previous")
                }
            }

            Button(
                onClick = {
                    if (state.currentPageIndex == onboardingPages.size - 1) {
                        eventSink(OnboardingUiEvent.Finish)
                    } else {
                        eventSink(OnboardingUiEvent.GoNext)
                    }
                },
                enabled = when (state.currentPageIndex) {
                    1 -> state.selectedGoal.isNotEmpty() // Enable only if goal is selected
                    else -> true
                },
            ) {
                Text(if (state.currentPageIndex == onboardingPages.size - 1) "Let's Go!" else "Next")
            }
        }
    }
}

@Composable
fun OnboardingPageScreen(
    pageIndex: Int,
    selectedGoal: String,
    onGoalSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val page = onboardingPages[pageIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (pageIndex) {
            0 -> WelcomePage(page.description)
            1 -> GoalSelectionPage(page.description, selectedGoal, onGoalSelected)
            2 -> TrackAnalyzePage(page.description)
            3 -> PlanQuickStartPage(page.description)
        }
    }
}

@Composable
fun WelcomePage(description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Text(
                "Hero Image",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Welcome",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun GoalSelectionPage(
    description: String,
    selectedGoal: String,
    onGoalSelected: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            "Pick Your Goal",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Text(
            description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            GoalItem(
                icon = Icons.Filled.Add,
                title = "Strength",
                isSelected = selectedGoal == "Strength",
                onClick = { onGoalSelected("Strength") },
                modifier = Modifier.weight(1f),
            )

            GoalItem(
                icon = Icons.Filled.Person,
                title = "Endurance",
                isSelected = selectedGoal == "Endurance",
                onClick = { onGoalSelected("Endurance") },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            GoalItem(
                icon = Icons.Filled.Check,
                title = "Weight Loss",
                isSelected = selectedGoal == "Weight Loss",
                onClick = { onGoalSelected("Weight Loss") },
                modifier = Modifier.weight(1f),
            )

            GoalItem(
                icon = Icons.Filled.Star,
                title = "Flexibility",
                isSelected = selectedGoal == "Flexibility",
                onClick = { onGoalSelected("Flexibility") },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun GoalItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .selectable(
                selected = isSelected,
                onClick = onClick,
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp,
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(48.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun TrackAnalyzePage(description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        "Progress Tracking Graph",
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(100.dp)
                            .padding(vertical = 16.dp),
                    ) {
                        for (height in listOf(0.3f, 0.5f, 0.4f, 0.6f, 0.7f, 0.5f, 0.8f)) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height((height * 100).dp)
                                    .background(MaterialTheme.colorScheme.primary),
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Track & Analyze",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun PlanQuickStartPage(description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            "Plan & Quick Starts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Text(
            description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "Daily Strength",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    "30-minute full-body workout",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "Quick HIIT",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    "15-minute high intensity interval training",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            onClick = { /* Will use the main Next/Finish button */ },
            modifier = Modifier.fillMaxWidth(0.7f),
        ) {
            Text("⚡ Quick Start", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
