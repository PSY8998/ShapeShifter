package app.shapeshifter

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        title = "ShapeShifter",
        onCloseRequest = ::exitApplication,
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = {}
            ) {
                Text("Click Me!")
            }

            Button(
                onClick = {}
            ) {
                Text("Click Me!")
            }
        }
    }
}
