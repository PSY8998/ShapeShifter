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
fun WeightInput(
    weight: String,
    onWeightChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "What's your Weight?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Weight in kg. Don't worry you can always change it later.", // Subtitle from design
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { newValue ->
                // Basic validation: Allow only digits, maybe decimal point
                if (newValue.matches(Regex("^\\d*\\.?\\d*\$")) && newValue.length <= 5) { // Allow digits and one optional dot
                    onWeightChange(newValue)
                }
            },
            label = { Text("Weight (kg)") },
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = "Weight Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal, // Use Decimal for weight
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(0.8f) // Make text field not full width
        )
    }
}
