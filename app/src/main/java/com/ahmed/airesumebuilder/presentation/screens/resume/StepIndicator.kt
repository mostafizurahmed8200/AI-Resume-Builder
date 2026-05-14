package com.ahmed.airesumebuilder.presentation.screens.resume

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahmed.airesumebuilder.util.Constant

@Composable
fun StepIndicator(currentStep: Int, totalStep: Int) {
    Column {
        LinearProgressIndicator(
            progress = currentStep.toFloat() / totalStep,
            modifier = Modifier.fillMaxWidth()
        )
        Constant.KSpacerHeight(4.dp)
        Text(
            text = "Step $currentStep of $totalStep",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

