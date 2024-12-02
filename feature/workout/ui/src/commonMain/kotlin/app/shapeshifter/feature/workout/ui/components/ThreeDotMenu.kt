package app.shapeshifter.feature.workout.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.shapeshifter.feature.workout.ui.drawable.MoreHorizontal

@Immutable
data class MenuItem(
    val id: Long,
    val name: String,
    val onClick: () -> Unit,
)

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
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            menuItems.forEach { menuItem ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        menuItem.onClick()
                    },
                    text = { Text(menuItem.name) },
                )
            }
        }
    }
}
