package com.ahmed.airesumebuilder.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.util.Constant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCreateResume: () -> Unit,
    onOpenResume: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var resumeToDelete by remember { mutableStateOf<Resume?>(null) }

    resumeToDelete?.let { resume ->

        AlertDialog(
            onDismissRequest = {
                resumeToDelete = null
            },
            title = { Text("Delete Resume") },
            text = { Text("Are you sure you want to delete this resume ? This action can not be undo") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteResume(resume.id); resumeToDelete = null
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { resumeToDelete = null }) { Text("Cancel") } })

    }

    Scaffold(
        // Top Bar
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("My Resume", fontWeight = FontWeight.Bold)
                        viewModel.currentUser?.displayName?.let { name ->
                            Text(
                                text = "Hello $name",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),

                                )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.syncResumes()
                    }) {
                        Icon(Icons.Default.Sync, "Sync")
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, "Profile")
                    }

                },

                )
        },

        //Floating Action Button
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateResume,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("New Resume") }

            )
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                uiState.resumes.isEmpty() -> {

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Description,
                            null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        )

                        Text(
                            "No Resumes Yet",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                        )
                        Text(
                            "Create your first AI-powered resume",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Button(onClick = onCreateResume) {
                            Icon(Icons.Default.Add, null)
                            Constant.MSpacer(8.dp)
                            Text("Create Resume")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(uiState.resumes, key = { it.id }) { resume ->
                            ResumeCard(
                                resume = resume,
                                onClick = { onOpenResume(resume.id) },
                                onDelete = { resumeToDelete = resume })
                        }
                    }
                }
            }

            uiState.errorMessage?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = { TextButton(onClick = { viewModel.clearError() }) { Text("Dismiss") } }) {
                    Text(
                        error
                    )
                }
            }

        }


    }

}


//Resume Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable

private fun ResumeCard(resume: Resume, onClick: () -> Unit, onDelete: () -> Unit) {

    val updateDate =
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(resume.updatedAt))


    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {




    }

}
