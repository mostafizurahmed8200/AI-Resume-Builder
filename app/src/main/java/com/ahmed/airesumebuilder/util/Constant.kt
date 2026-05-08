package com.ahmed.airesumebuilder.util

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

}