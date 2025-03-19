package app.shapeshifter.feature.onboarding.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.common.ui.compose.screens.OnboardingScreen
import app.shapeshifter.feature.onboarding.data.OnboardingPage
import app.shapeshifter.feature.onboarding.data.onboardingPages
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f),
        ) { page ->
            OnboardingPageScreen(
                page = onboardingPages[page],
                modifier = Modifier
                    .fillMaxSize(),
            )
        }

        Spacer(
            modifier = Modifier
                .height(Dimens.Padding.Small),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (pagerState.currentPage != onboardingPages.size - 1) {
                TextButton(onClick = { state.eventSink(OnboardingUiEvent.OnFinish) }) {
                    Text("Skip")
                }
            }
            Button(
                onClick = {
                    if (pagerState.currentPage == onboardingPages.size - 1) {
                        state.eventSink(OnboardingUiEvent.OnFinish)
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
            ) {
                Text(if (pagerState.currentPage == onboardingPages.size - 1) "Finish" else "Next")
            }
        }
    }
}

@Composable
fun OnboardingPageScreen(
    page: OnboardingPage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = page.title,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = page.description,
            textAlign = TextAlign.Center,
        )
    }
}
