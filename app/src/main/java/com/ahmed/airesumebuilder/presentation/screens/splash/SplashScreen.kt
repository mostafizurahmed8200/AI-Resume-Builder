package com.ahmed.airesumebuilder.presentation.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.airesumebuilder.presentation.screens.auth.AuthViewModel
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    onNavigationToAuth: () -> Unit,
    onNavigationToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        delay(1500L)
        if (viewModel.isLoggedIn) onNavigationToHome() else onNavigationToAuth
    }

}