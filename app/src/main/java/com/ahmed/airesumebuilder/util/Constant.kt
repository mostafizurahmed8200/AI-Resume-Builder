package com.ahmed.airesumebuilder.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp

object Constant {

    const val DATABASE_NAME = "resume_database"
    const val RESUMES_COLLECTION = "resumes"
    const val USERS_COLLECTION = "users"

    object Routes {
        const val SPLASH = "splash"
        const val AUTH = "auth"
        const val LOGIN = "login"
        const val REGISTER = "register"
        const val HOME = "home"
        const val CREATE_RESUME = "create_resume"
        const val PERSONAL_INFO = "personal_info"
        const val EDUCATION = "education"
        const val EXPERIENCE = "experience"
        const val SKILLS = "skills"
        const val PROJECTS = "projects"
        const val TEMPLATES = "templates"
        const val PREVIEW = "preview/{resumeId}"
        const val PROFILE = "profile"
        const val AI_ENHANCE = "ai_enhance"
    }


    @Composable
    fun KSpacerHeight(dp: Dp) {
        Spacer(
            modifier = Modifier.height(dp)
        )
    }

    @Composable
    fun KSpacerWidth(dp: Dp) {
        Spacer(
            modifier = Modifier.width(dp)
        )
    }

    @Composable
    fun EditTextField(
        label: String,
        value: String,
        keyboardType: KeyboardType = KeyboardType.Text,
        minLines: Int = 1,
        maxLines: Int = if (minLines > 1) 10 else 1,
        onValueChange: (String) -> Unit
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = minLines == 1,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next
            ),
            minLines = minLines,
            maxLines = maxLines
        )
    }
}