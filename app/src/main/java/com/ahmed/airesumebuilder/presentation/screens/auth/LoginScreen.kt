package com.ahmed.airesumebuilder.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.airesumebuilder.R
import com.ahmed.airesumebuilder.util.Constant

@Composable
fun LoginScreen(
    onNavigationToRegister: () -> Unit,
    onNavigationToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgetPassword by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onNavigationToHome()
    }

    if (showForgetPassword) {
        ForgotPasswordDialog(
            onDismiss = { showForgetPassword = false },
            onSubmit = { emailAddress ->
                viewModel.resetPassword(emailAddress)
                showForgetPassword = false
            })
    }

    //UI Draw
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    )
    {
        Text(
            text = stringResource(R.string.welcome_back),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.sign_in_to_continue),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)

        )
        Constant.MSpacer(40.dp)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ),
            leadingIcon = { Icon(Icons.Default.Email, null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()

        )

        Constant.MSpacer(16.dp)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(stringResource(R.string.password))
            },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(
                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        null
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true

        )

        uiState.errorMessage?.let {
            Constant.MSpacer(8.dp)
            Text(
                it, color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )

        }

        Constant.MSpacer(8.dp)
        TextButton(
            onClick = { showForgetPassword = true },
            modifier = Modifier.align(Alignment.End)
        ) { Text(stringResource(R.string.forget_password)) }

        Constant.MSpacer(8.dp)
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = email.isNotBlank() && password.isNotBlank() && !uiState.isLoading
        )
        {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(stringResource(R.string.sign_in), style = MaterialTheme.typography.labelLarge)
            }


        }
        Constant.MSpacer(16.dp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.don_t_have_an_account),
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onNavigationToRegister) {
                Text(stringResource(R.string.sign_up), fontWeight = FontWeight.Bold)
            }
        }


    }


}


@Composable
@Preview(showSystemUi = true, showBackground = true)
fun LoginScreenPreview() {

}

@Composable
private fun ForgotPasswordDialog(onDismiss: () -> Unit, onSubmit: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss, title = { Text("Reset Password") }, text = {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }, confirmButton = {
            TextButton(onClick = { if (email.isNotBlank()) onSubmit(email) }) {
                Text("Send Reset Link")
            }
        }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}