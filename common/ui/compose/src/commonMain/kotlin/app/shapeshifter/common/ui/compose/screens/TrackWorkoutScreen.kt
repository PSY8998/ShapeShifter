package app.shapeshifter.common.ui.compose.screens

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

/**
 * There are three ways to start this screen
 * - empty workout
 * - from saved workout
 * - resume workout
 *
 * - saveWorkoutId - needs to be passed to link the newly created workout to an saved routine
 * - workoutId - this can be there in case resume workout or we created the workout before hand
 */
@Parcelize
data object TrackWorkoutScreen : Screen
