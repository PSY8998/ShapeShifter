package app.shapeshifter.feature.profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.screens.ProfileScreen
import app.shapeshifter.data.models.workoutlog.WorkoutSession
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return when (screen) {
            is ProfileScreen -> {
                ui<ProfileUiState> { state, modifier ->
                    Profile(state, modifier)
                }
            }

            else -> null
        }
    }
}

@Composable
fun Profile(
    state: ProfileUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            ProfileTopBar(
                modifier = Modifier
                    .fillMaxWidth(),
            )

            PreviousWorkouts(
                state.workouts
            )
        }
    }
}

@Composable
fun ProfileTopBar(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            "Username",
            modifier = Modifier
                .weight(1f),
        )
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopEnd),
        ) {
            ThreeDotDropDownMenu()
        }
    }
}

@Composable
fun ThreeDotDropDownMenu() {
    var expanded by remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded = true },
    ) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Options")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
    ) {
        DropdownMenuItem(
            onClick = { expanded = false },
            text = { Text("Logout") },
        )

        DropdownMenuItem(
            onClick = { expanded = false },
            text = { Text("Edit Profile") },
        )
    }

}

@Composable
fun PreviousWorkouts(
    workoutSessions: List<WorkoutSession>,
    modifier: Modifier = Modifier,
) {
    LazyColumn {
        items(
            items = workoutSessions,
        ) { workoutSession ->
            Text(
                text = workoutSession.workoutLog.id.toString()
            )
        }
    }
}

