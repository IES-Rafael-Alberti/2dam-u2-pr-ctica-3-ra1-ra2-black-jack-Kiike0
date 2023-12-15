package com.example.blackjack_jetpackcompose.data

/**
 * Clase sellada que representa las diferentes pantallas de la aplicación.
 *
 * @property route El identificador de la ruta
 */
sealed class Routes(val route: String) {
    /**
     * Objeto que representa la pantalla del Menu Inicial.
     */
    object GameScreen : Routes("GameScreen")

    /**
     * Objeto que representa la pantalla del juego del BlackJack.
     */
    object MenuBlackJackScreen : Routes("menuBlackJackScreen")

    /**
     * Objeto que representa la pantalla del juego de la Carta más alta.
     */
    object CartaMasAltaScreen : Routes("cartaMasAltaScreen")

    /**
     * Objeto que representa la pantalla del Multijugador.
     */
    object MultiScreen : Routes("multiScreen")

    /**
     * Objeto que representa la pantalla de Jugador vs IA.
     */
    object BotScreen : Routes("botScreen")
}
