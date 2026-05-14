package com.ahmed.airesumebuilder.presentation.screens.resume

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.airesumebuilder.domain.model.Education
import com.ahmed.airesumebuilder.util.Constant
import com.ahmed.airesumebuilder.util.Constant.EditTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(
    onNext: () -> Unit, onBack: () -> Unit, viewModel: ResumeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEducation by remember { mutableStateOf<Education?>(null) }


    if (showAddDialog || editingEducation != null) {
        EducationDialog(
            education = editingEducation,
            onDismiss = { showAddDialog = false; editingEducation = null },
            onSave = { edu ->
                if (editingEducation != null) viewModel.updateEducation(edu)
                else viewModel.addEducation(edu)
                showAddDialog = false; editingEducation = null
            })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Education") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showAddDialog = true
                    }) { Icon(Icons.Default.Add, null) }
                }

            )
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            StepIndicator(2, 6)
            Constant.KSpacerHeight(16.dp)
        }

    }


}

@Composable
fun EducationDialog(education: Education?, onDismiss: () -> Unit, onSave: (Education) -> Unit) {
    var degree by remember { mutableStateOf(education?.degree ?: "") }
    var institution by remember { mutableStateOf(education?.institution ?: "") }
    var fieldOfStudy by remember { mutableStateOf(education?.fieldOfStudy ?: "") }
    var gradYear by remember { mutableStateOf(education?.graduationYear ?: "") }
    var gpa by remember { mutableStateOf(education?.gpa ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (education == null) "Add Education" else "Edit Education") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                EditTextField(
                    label = "Degree *", value = degree, onValueChange = { degree = it })
                EditTextField(
                    label = "Institution *",
                    value = institution,
                    onValueChange = { institution = it })
                EditTextField(
                    label = "Field of Study",
                    value = fieldOfStudy,
                    onValueChange = { fieldOfStudy = it })
                EditTextField(
                    label = "Graduation Year", value = gradYear, onValueChange = { gradYear = it })
                EditTextField(
                    label = "GPA (optional)", value = gpa, onValueChange = { gpa = it })
            }

        },
        confirmButton = {

            TextButton(onClick = {
                if (degree.isNotBlank() && institution.isNotBlank()) {
                    onSave(
                        (education ?: Education().copy(
                            degree = degree,
                            institution = institution,
                            fieldOfStudy = fieldOfStudy,
                            graduationYear = gradYear,
                            gpa = gpa
                        ))
                    )

                }
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }

    )

}