package com.ahmed.airesumebuilder.presentation.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()

) {
    val uiState by viewModel.uiState.collectAsState()
    val displayName by remember { mutableStateOf("") }
    val email by remember { mutableStateOf("") }
    val password by remember { mutableStateOf("") }
    val confirmPassword by remember { mutableStateOf("") }
    val passwordVisibility by remember { mutableStateOf(false) }
    val passwordMismatch by remember { mutableStateOf(false) }


    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onNavigateToHome()
    }







}