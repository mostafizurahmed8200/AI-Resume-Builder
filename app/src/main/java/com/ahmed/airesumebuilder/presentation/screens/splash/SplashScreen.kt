package com.ahmed.airesumebuilder.presentation.screens.splash


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.airesumebuilder.R
import com.ahmed.airesumebuilder.presentation.screens.auth.AuthViewModel
import com.ahmed.airesumebuilder.util.Constant
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigationToAuth: () -> Unit,
    onNavigationToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        delay(1500L)
        if (viewModel.isLoggedIn) onNavigationToHome() else onNavigationToAuth()
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)


            )
            Constant.MSpacer(10.dp)
            Text(
                text = stringResource(R.string.resume_builder),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Constant.MSpacer(8.dp)
            Text(
                text = stringResource(R.string.ai_powered_resume_creator),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Constant.MSpacer(40.dp)
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashContent()
}