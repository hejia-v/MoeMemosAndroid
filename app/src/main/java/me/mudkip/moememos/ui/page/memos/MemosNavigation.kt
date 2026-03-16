package me.mudkip.moememos.ui.page.memos

import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.mudkip.moememos.data.model.Account
import me.mudkip.moememos.ui.page.common.RouteName
import me.mudkip.moememos.viewmodel.LocalUserState

@Composable
fun MemosNavigation(
    drawerState: DrawerState? = null,
    navController: NavHostController
) {
    val userStateViewModel = LocalUserState.current
    val currentAccount by userStateViewModel.currentAccount.collectAsState()
    val hasExplore = currentAccount !is Account.Local

    NavHost(
        navController = navController,
        startDestination = RouteName.MEMOS,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(220),
                initialOffset = { it / 8 }
            ) + fadeIn(animationSpec = tween(160))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200),
                targetOffset = { it / 10 }
            ) + fadeOut(animationSpec = tween(140))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200),
                initialOffset = { it / 10 }
            ) + fadeIn(animationSpec = tween(140))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(180),
                targetOffset = { it / 10 }
            ) + fadeOut(animationSpec = tween(120))
        }
    ) {
        composable(
            RouteName.MEMOS,
        ) {
            MemosHomePage(
                drawerState = drawerState,
                navController = navController,
            )
        }

        composable(
            RouteName.ARCHIVED
        ) {
            ArchivedMemoPage(
                drawerState = drawerState
            )
        }

        composable(
            "${RouteName.TAG}/{tag}"
        ) { entry ->
            TagMemoPage(
                drawerState = drawerState,
                tag = entry.arguments?.getString("tag")?.let(Uri::decode) ?: "",
                navController = navController
            )
        }

        composable(
            RouteName.EXPLORE
        ) {
            if (hasExplore) {
                ExplorePage(
                    drawerState = drawerState
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(RouteName.MEMOS) {
                        popUpTo(RouteName.EXPLORE) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }

        composable(RouteName.SEARCH) {
            SearchPage(navController = navController)
        }
    }
}
