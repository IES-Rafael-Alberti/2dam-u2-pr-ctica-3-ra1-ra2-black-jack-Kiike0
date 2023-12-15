package com.example.blackjack_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blackjack_jetpackcompose.data.Routes
import com.example.blackjack_jetpackcompose.screens.PantallaJuego
import com.example.blackjack_jetpackcompose.screens.PantallaMultijugadorInicial
import com.example.blackjack_jetpackcompose.screens.BlackJackMultiViewModel
import com.example.blackjack_jetpackcompose.screens.BlackJackIAViewModel
import com.example.blackjack_jetpackcompose.screens.MenuInicioViewModel
import com.example.blackjack_jetpackcompose.screens.PantallaInicial
import com.example.blackjack_jetpackcompose.screens.PantallavsBotInicial
import com.example.blackjack_jetpackcompose.ui.theme.BlackJack_JetpackComposeTheme

class MainActivity : ComponentActivity() {

    private val bJBotviewModel: BlackJackIAViewModel by viewModels()
    private val bJMultiviewModel: BlackJackMultiViewModel by viewModels()
    private val menuInicioViewModel: MenuInicioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlackJack_JetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Routes.GameScreen.route
                    ) {
                        composable(Routes.GameScreen.route) {
                            PantallaInicial(
                                navController = navController,
                                viewModel = menuInicioViewModel
                            )
                        }
                        composable(Routes.MenuBlackJackScreen.route) {
                            PantallaJuego(
                                navController = navController
                            )
                        }
                        composable(Routes.MultiScreen.route) {
                            PantallaMultijugadorInicial(
                                navController = navController,
                                viewModel = bJMultiviewModel
                            )
                        }
                        composable(Routes.BotScreen.route) {
                            PantallavsBotInicial(
                                navController = navController,
                                viewModel = bJBotviewModel
                            )
                        }
                    }
                }
            }
        }

        menuInicioViewModel.cerrarPrograma.observe(this) { event ->
            if (event) {
                finish()
            }
        }
    }
}
