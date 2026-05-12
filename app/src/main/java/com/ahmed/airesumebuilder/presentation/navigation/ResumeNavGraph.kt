package com.ahmed.airesumebuilder.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmed.airesumebuilder.presentation.screens.auth.LoginScreen
import com.ahmed.airesumebuilder.presentation.screens.splash.SplashScreen
import com.ahmed.airesumebuilder.util.Constant

@Composable
fun ResumeNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Constant.Routes.SPLASH
    ) {
        composable(Constant.Routes.SPLASH) {
            SplashScreen(
                onNavigationToAuth = {
                    navController.navigate(Constant.Routes.LOGIN) {
                        popUpTo(Constant.Routes.SPLASH) { inclusive = true }
                    }
                },
                onNavigationToHome = {
                    navController.navigate(Constant.Routes.HOME) {
                        popUpTo(Constant.Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Constant.Routes.LOGIN) {
            LoginScreen(
                onNavigationToRegister = {
                    navController.navigate(Constant.Routes.REGISTER)
                },
                onNavigationToHome = { navController.navigate(Constant.Routes.HOME) }
            )
        }

    }
}
