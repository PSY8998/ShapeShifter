package app.shapeshifter.feature.workout.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.shapeshifter.common.ui.compose.resources.Dimens
import app.shapeshifter.feature.workout.ui.drawable.MoreHorizontal

@Immutable
data class MenuItem(
    val id: Long,
    val name: String,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreeDotMenu(
    menuItems: List<MenuItem>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true },
            modifier = modifier,
        ) {
            Icon(
                imageVector = MoreHorizontal,
                contentDescription = "Options",
            )
        }
        if (expanded) {
            ModalBottomSheet(
                onDismissRequest = { expanded = false },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Padding.Large),
                ) {
                    menuItems.forEach { menuItem ->
                        Button(
                            modifier = modifier
                                .fillMaxWidth(),
                            shape = MaterialTheme.shapes.small,
                            onClick = {
                                expanded = false
                                menuItem.onClick()
                            },
                            colors = if (menuItem.id.toInt() == 2) {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onErrorContainer,
                                    contentColor = MaterialTheme.colorScheme.onError,
                                )
                            } else {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,

                                    )
                            },
                        ) {
                            Text(menuItem.name)
                        }
                    }
                }
            }
        }
    }
}
