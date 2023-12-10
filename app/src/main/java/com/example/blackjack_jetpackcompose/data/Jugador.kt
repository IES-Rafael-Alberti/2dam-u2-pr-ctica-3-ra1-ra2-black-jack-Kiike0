package com.example.blackjack_jetpackcompose.data

/**
 * Clase que representa al jugador en el juego
 *
 * @property nick El alias que usará el jugador.
 * @property listadeCartas La lista de cartas asignadas al jugador.
 * @property puntos Los puntos totales acumulados por el jugador.
 */
data class Jugador (
    var nick: String,
    val listadeCartas: ArrayList<Carta> = ArrayList(),
    var puntos: Int
)