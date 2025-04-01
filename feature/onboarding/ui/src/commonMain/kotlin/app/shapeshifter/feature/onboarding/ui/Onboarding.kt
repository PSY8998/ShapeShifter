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
 import androidx.compose.runtime.LaunchedEffect
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
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
     // Pager state should be controlled by the presenter's currentPageIndex
     val pagerState = rememberPagerState(
         initialPage = state.currentPageIndex, // Initialize with presenter state
         pageCount = { onboardingPages.size }
     )

     // Sync pager state when presenter state changes
     LaunchedEffect(state.currentPageIndex) {
         if (pagerState.currentPage != state.currentPageIndex) {
             pagerState.animateScrollToPage(state.currentPageIndex)
         }
     }
     // Sync presenter state when user swipes (if userScrollEnabled = true)
     // LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
     //     if (!pagerState.isScrollInProgress) {
     //         // state.eventSink(OnboardingUiEvent.GoToPage(pagerState.currentPage)) // Need GoToPage event
     //     }
     // }

     val eventSink = state.eventSink

     Column(
         modifier = modifier
             .fillMaxSize(),
         horizontalAlignment = Alignment.CenterHorizontally,
     ) {
         HorizontalPager(
             state = pagerState,
             modifier = Modifier.weight(1f),
             userScrollEnabled = false // Controlled by presenter via buttons
         ) { pageIndex -> // Use pageIndex directly
             OnboardingPageScreen(
                 pageIndex = pageIndex, // Pass index
                 age = state.age, // Pass state from presenter
                 onAgeChange = { eventSink(OnboardingUiEvent.UpdateAge(it)) }, // Dispatch event
                 weight = state.weight, // Pass state from presenter
                 onWeightChange = { eventSink(OnboardingUiEvent.UpdateWeight(it)) }, // Dispatch event
                 height = state.height, // Pass state from presenter
                 onHeightChange = { eventSink(OnboardingUiEvent.UpdateHeight(it)) }, // Dispatch event
                 modifier = Modifier.fillMaxSize(),
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
             horizontalArrangement = if (state.currentPageIndex > 0) Arrangement.SpaceBetween else Arrangement.End,
         ) {
             // Show Previous button only after the first page
             if (state.currentPageIndex > 0) {
                 TextButton(onClick = { eventSink(OnboardingUiEvent.GoPrevious) }) {
                     Text("Previous")
                 }
             }

             // Skip button logic (can be added back if needed, dispatching Finish event)
             // if (state.currentPageIndex == 0) { // Example: Show skip only on first page
             //     TextButton(onClick = { eventSink(OnboardingUiEvent.Finish) }) {
             //         Text("Skip")
             //     }
             // }

             Button(
                 onClick = {
                     if (state.currentPageIndex == onboardingPages.size - 1) {
                         eventSink(OnboardingUiEvent.Finish)
                     } else {
                         eventSink(OnboardingUiEvent.GoNext)
                     }
                 },
                 enabled = state.isNextEnabled // Use enabled state from presenter
             ) {
                 Text(if (state.currentPageIndex == onboardingPages.size - 1) "Finish" else "Next")
             }
         }
     }
 }


 @Composable
 fun OnboardingPageScreen(
     pageIndex: Int, // Receive index instead of page object
     modifier: Modifier = Modifier,
     // Receive actual state and callbacks
     age: String,
     onAgeChange: (String) -> Unit,
     weight: String,
     onWeightChange: (String) -> Unit,
     height: String,
     onHeightChange: (String) -> Unit,
 ) {
     val page = onboardingPages[pageIndex] // Get page data using index

     Column(
         modifier = modifier
             .fillMaxSize()
             .padding(16.dp), // Add padding
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Center,
     ) {
         // Render different content based on the page index
         when (pageIndex) {
             1 -> AgeInput( // Index 1 = Age
                 age = age,
                 onAgeChange = onAgeChange,
                 modifier = Modifier.fillMaxWidth()
             )
             2 -> WeightInput( // Index 2 = Weight
                 weight = weight,
                 onWeightChange = onWeightChange,
                 modifier = Modifier.fillMaxWidth()
             )
             3 -> HeightInput( // Index 3 = Height
                 height = height,
                 onHeightChange = onHeightChange,
                 modifier = Modifier.fillMaxWidth()
             )
             else -> { // Index 0 (Welcome) and 4 (Start Journey)
                 // Default screen for Welcome, Start Journey, etc.
                 Text(
                     text = page.title,
                     style = MaterialTheme.typography.headlineMedium, // Adjusted style
                     textAlign = TextAlign.Center
                 )
                 Spacer(modifier = Modifier.height(16.dp))
                 Text(
                     text = page.description,
                     style = MaterialTheme.typography.bodyLarge, // Adjusted style
                     textAlign = TextAlign.Center,
                 )
             }
         }
     }
 }
