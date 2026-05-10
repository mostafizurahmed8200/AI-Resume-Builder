package com.ahmed.airesumebuilder.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ahmed.airesumebuilder.util.Constant

@Composable
fun ResumeNavGraph(
    navController: NavController = rememberNavController()
) {

    NavHost(
        navController = navController, startDestination = Constant.Routes.SPLASH
    ) {


    }
}