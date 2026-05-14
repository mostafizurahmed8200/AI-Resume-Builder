package com.ahmed.airesumebuilder.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmed.airesumebuilder.presentation.screens.auth.LoginScreen
import com.ahmed.airesumebuilder.presentation.screens.auth.RegisterScreen
import com.ahmed.airesumebuilder.presentation.screens.home.HomeScreen
import com.ahmed.airesumebuilder.presentation.screens.resume.EducationScreen
import com.ahmed.airesumebuilder.presentation.screens.resume.PersonalInfoScreen
import com.ahmed.airesumebuilder.presentation.screens.splash.SplashScreen
import com.ahmed.airesumebuilder.util.Constant

@Composable
fun ResumeNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController, startDestination = Constant.Routes.SPLASH
    ) {
        composable(Constant.Routes.SPLASH) {
            SplashScreen(onNavigationToAuth = {
                navController.navigate(Constant.Routes.LOGIN) {
                    popUpTo(Constant.Routes.SPLASH) { inclusive = true }
                }
            }, onNavigationToHome = {
                navController.navigate(Constant.Routes.HOME) {
                    popUpTo(Constant.Routes.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Constant.Routes.LOGIN) {
            LoginScreen(onNavigationToHome = { navController.navigate(Constant.Routes.HOME) })
        }


        composable(Constant.Routes.REGISTER) {
            RegisterScreen(
                onDismissDialog = {},
            )
        }

        composable(route = Constant.Routes.HOME) {
            HomeScreen(
                onOpenResume = { resumeId -> navController.navigate("preview/$resumeId") },
                onCreateResume = { navController.navigate(Constant.Routes.PERSONAL_INFO) },
                onNavigateToProfile = { navController.navigate(Constant.Routes.PROFILE) })
        }

        composable(route = Constant.Routes.PERSONAL_INFO) {
            PersonalInfoScreen(
                onNext = { navController.navigate(Constant.Routes.EDUCATION) },
                onBack = { navController.popBackStack() },

                )
        }

        composable(route = Constant.Routes.EDUCATION) {
            EducationScreen(
                onNext = { navController.navigate(Constant.Routes.EXPERIENCE) },
                onBack = { navController.popBackStack() })
        }

    }
}
