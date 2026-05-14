package com.ahmed.airesumebuilder.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.airesumebuilder.domain.model.PersonalInfo
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.presentation.theme.AIResumeBuilderTheme
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
    HomeContent(
        uiState = uiState,
        currentUserDisplayName = viewModel.currentUser?.displayName,
        onCreateResume = onCreateResume,
        onOpenResume = onOpenResume,
        onNavigateToProfile = onNavigateToProfile,
        onSyncResumes = { viewModel.syncResumes() },
        onDeleteResume = { resume -> viewModel.deleteResume(resume.id) },
        onClearError = { viewModel.clearError() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    currentUserDisplayName: String?,
    onCreateResume: () -> Unit,
    onOpenResume: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onSyncResumes: () -> Unit,
    onDeleteResume: (Resume) -> Unit,
    onClearError: () -> Unit
) {
    var resumeToDelete by remember { mutableStateOf<Resume?>(null) }

    resumeToDelete?.let { resume ->
        AlertDialog(
            onDismissRequest = {
                resumeToDelete = null
            },
            title = { Text("Delete Resume") },
            text = { Text("Are you sure you want to delete this resume? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteResume(resume)
                    resumeToDelete = null
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { resumeToDelete = null }) { Text("Cancel") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("My Resumes", fontWeight = FontWeight.Bold)
                        currentUserDisplayName?.let { name ->
                            Text(
                                text = "Hello $name",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onSyncResumes) {
                        Icon(Icons.Default.Sync, "Sync")
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, "Profile")
                    }
                },
            )
        },
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
                        Surface(
                            modifier = Modifier.size(120.dp),
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Description,
                                    null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "No Resumes Yet",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            "Create your first AI-powered resume in seconds",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onCreateResume,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(56.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(Icons.Default.Add, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Create Resume", style = MaterialTheme.typography.titleMedium)
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
                    action = { TextButton(onClick = onClearError) { Text("Dismiss") } }
                ) {
                    Text(error)
                }
            }
        }
    }
}


//Resume Card

@Preview(showSystemUi = true, name = "Home Screen With Content")
@Composable
fun HomeScreenContentPreview() {
    AIResumeBuilderTheme {
        HomeContent(
            uiState = HomeUiState(
                resumes = listOf(
                    Resume(
                        id = "1",
                        personalInfo = PersonalInfo(
                            fullName = "Ahmed Mohamed",
                            email = "ahmed.m@example.com"
                        ),
                        templateId = "Modern",
                        isSynced = true,
                        updatedAt = System.currentTimeMillis()
                    ),
                    Resume(
                        id = "2",
                        personalInfo = PersonalInfo(
                            fullName = "John Doe",
                            email = "john.doe@tech.com"
                        ),
                        templateId = "Professional",
                        isSynced = false,
                        updatedAt = System.currentTimeMillis() - 86400000
                    )
                )
            ),
            currentUserDisplayName = "Ahmed",
            onCreateResume = {},
            onOpenResume = {},
            onNavigateToProfile = {},
            onSyncResumes = {},
            onDeleteResume = {},
            onClearError = {}
        )
    }
}

@Preview(showSystemUi = true, name = "Home Screen Empty State")
@Composable
fun HomeScreenEmptyPreview() {
    AIResumeBuilderTheme {
        HomeContent(
            uiState = HomeUiState(resumes = emptyList()),
            currentUserDisplayName = "Ahmed",
            onCreateResume = {},
            onOpenResume = {},
            onNavigateToProfile = {},
            onSyncResumes = {},
            onDeleteResume = {},
            onClearError = {}
        )
    }
}

@Preview(showSystemUi = true, name = "Resume Card Long Text")
@Composable
fun ResumeCardLongTextPreview() {
    AIResumeBuilderTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                ResumeCard(
                    resume = Resume(
                        personalInfo = PersonalInfo(
                            fullName = "Ahmed Mohamed Abdelrahman Ali Hassan",
                            email = "ahmed.mohamed.abdelrahman.ali.hassan@verylongemaildomain.com"
                        ),
                        templateId = "Modern Professional Deluxe",
                        isSynced = true,
                        updatedAt = System.currentTimeMillis()
                    ),
                    onClick = {},
                    onDelete = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResumeCard(resume: Resume, onClick: () -> Unit, onDelete: () -> Unit) {

    val updateDate =
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(resume.updatedAt))


    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = resume.personalInfo.fullName.firstOrNull()?.uppercase() ?: "R",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = resume.personalInfo.fullName.ifBlank { "Untitled Resume" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = resume.personalInfo.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = resume.templateId.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    modifier = Modifier
                        .height(32.dp)
                        .widthIn(max = 140.dp),
                    shape = MaterialTheme.shapes.medium
                )

                if (resume.isSynced) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Updated $updateDate",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

}
