package app.shapeshifter.feature.onboarding.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HeightInput(
    height: String,
    onHeightChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "What's your Height?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Height in cm. Don't worry you can always change it later.", // Subtitle from design
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { newValue ->
                // Basic validation: Allow only digits
                if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                    onHeightChange(newValue)
                }
            },
            label = { Text("Height (cm)") },
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = "Height Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, // Use Number for height in cm
                imeAction = ImeAction.Done // Assuming this might be the last input before finish
            ),
            modifier = Modifier.fillMaxWidth(0.8f) // Make text field not full width
        )
    }
}
