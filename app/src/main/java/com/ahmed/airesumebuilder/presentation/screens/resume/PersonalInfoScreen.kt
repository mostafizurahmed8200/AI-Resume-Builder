package com.ahmed.airesumebuilder.presentation.screens.resume

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.airesumebuilder.R
import com.ahmed.airesumebuilder.domain.model.PersonalInfo
import com.ahmed.airesumebuilder.util.Constant.EditTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    onNext: () -> Unit, onBack: () -> Unit, viewModel: ResumeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val info = uiState.resume.personalInfo

    var fullName by remember { mutableStateOf(info.fullName) }
    var email by remember { mutableStateOf(info.email) }
    var phone by remember { mutableStateOf(info.phone) }
    var location by remember { mutableStateOf(info.location) }
    var linkedIn by remember { mutableStateOf(info.linkedIn) }
    var github by remember { mutableStateOf(info.github) }
    var website by remember { mutableStateOf(info.website) }
    var summary by remember { mutableStateOf(info.summary) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Information") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StepIndicator(1, 6)

            EditTextField(
                stringResource(R.string.full_name_star),
                value = fullName,
                onValueChange = { fullName = it },
                keyboardType = KeyboardType.Text
            )
            EditTextField(
                stringResource(R.string.email_star), value = email, onValueChange = { email = it })
            EditTextField(
                stringResource(R.string.phone),
                value = phone,
                onValueChange = { phone = it },
                keyboardType = KeyboardType.Number
            )
            EditTextField(
                stringResource(R.string.location_city_country),
                value = location,
                onValueChange = { location = it },
            )

            EditTextField(
                stringResource(R.string.linkedin_url),
                value = linkedIn,
                onValueChange = { linkedIn = it },
                keyboardType = KeyboardType.Uri
            )

            EditTextField(
                stringResource(R.string.github_url),
                value = github,
                onValueChange = { github = it },
                keyboardType = KeyboardType.Uri
            )

            EditTextField(
                stringResource(R.string.personal_website),
                value = website,
                onValueChange = { website = it },
                keyboardType = KeyboardType.Uri
            )

            EditTextField(
                stringResource(R.string.personal_summary),
                value = summary,
                onValueChange = { summary = it },
                minLines = 4,
                maxLines = 8
            )
            Button(
                onClick = {
                    val personalInfo = PersonalInfo(
                        fullName = fullName,
                        email = email,
                        location = location,
                        github = github,
                        website = website,
                        summary = summary
                    )
                    viewModel.updatePersonalInfo(personalInfo)
                    onNext()
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = fullName.isNotBlank() && email.isNotBlank()
            ) {
                Text(stringResource(R.string.next_education))
            }
        }
    }
}


