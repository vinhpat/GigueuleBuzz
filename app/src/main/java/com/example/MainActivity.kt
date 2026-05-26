package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.CreateSessionRoute
import com.example.ui.screens.CreateSessionScreen
import com.example.ui.screens.HomeRoute
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JoinRoute
import com.example.ui.screens.JoinScreen
import com.example.ui.screens.SessionRoute
import com.example.ui.screens.SessionScreen
import com.example.ui.screens.SplashRoute
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.BuzzerViewModel
import com.example.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    
    private val viewModel: BuzzerViewModel by viewModels { ViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BuzzerApp(viewModel)
            }
        }
    }
}

@Composable
fun BuzzerApp(viewModel: BuzzerViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            if (uiState.session != null) {
                snackbarHostState.showSnackbar(error)
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SplashRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<SplashRoute> {
                SplashScreen(onSplashFinished = {
                    navController.navigate(HomeRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                })
            }
            composable<HomeRoute> {
                HomeScreen(
                    onCreateSession = {
                        viewModel.clearError()
                        navController.navigate(CreateSessionRoute)
                    },
                    onJoinSession = {
                        viewModel.clearError()
                        navController.navigate(JoinRoute)
                    }
                )
            }
            composable<CreateSessionRoute> {
                CreateSessionScreen(
                    onCreateSubmit = { name, desc, max ->
                        viewModel.createSession(name, desc, max)
                        navController.navigate(SessionRoute) {
                            popUpTo(HomeRoute)
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<JoinRoute> {
                JoinScreen(
                    onJoinSubmit = { sessionId, userName ->
                        viewModel.joinSession(sessionId, userName)
                        navController.navigate(SessionRoute) {
                            popUpTo(HomeRoute)
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<SessionRoute> {
                SessionScreen(
                    uiState = uiState,
                    onStartQuiz = { viewModel.startQuiz() },
                    onResetQuiz = { viewModel.resetQuiz() },
                    onBuzz = { viewModel.buzz() },
                    onLeave = {
                        viewModel.clearError()
                        navController.navigate(HomeRoute) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}
