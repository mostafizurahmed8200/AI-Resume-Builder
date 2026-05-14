package com.ahmed.airesumebuilder.presentation.screens.auth


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.airesumebuilder.R
import com.ahmed.airesumebuilder.util.Constant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onDismissDialog: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()

) {

    val uiState by viewModel.uiState.collectAsState()
    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var passwordMismatch by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isRegistrationSuccess) {
        if (uiState.isRegistrationSuccess) {
            onDismissDialog()
            viewModel.clearState()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.create_account),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.build_your_professional_resume),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Constant.KSpacerHeight(32.dp)
        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text(stringResource(R.string.full_name)) },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Constant.KSpacerHeight(16.dp)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = { Icon(Icons.Default.Email, null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Constant.KSpacerHeight(16.dp)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; passwordMismatch = false },
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Constant.KSpacerHeight(16.dp)
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { 
                confirmPassword = it
                passwordMismatch = false 
            },
            label = { Text(stringResource(R.string.confirm_password)) },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = passwordMismatch,
            supportingText = if (passwordMismatch) {
                { Text(stringResource(R.string.passwords_do_not_match)) }
            } else null,
            trailingIcon = {
                IconButton(onClick = {
                    confirmPasswordVisible = !confirmPasswordVisible
                }) {
                    Icon(
                        if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        null
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        uiState.errorMessage?.let {
            Constant.KSpacerHeight(8.dp)
            Text(it, color = MaterialTheme.colorScheme.error)
        }


        Constant.KSpacerHeight(16.dp)
        Button(
            onClick = {
                if (password != confirmPassword) {
                    passwordMismatch = true; return@Button
                }
                viewModel.register(email, password, displayName)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),

            enabled = displayName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp)
                )
            } else {
                Text(text = stringResource(R.string.create_account))
            }
        }

        Constant.KSpacerHeight(16.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.already_have_an_account))
            TextButton(onClick = onDismissDialog) {
                Text(
                    stringResource(R.string.sign_in), fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
