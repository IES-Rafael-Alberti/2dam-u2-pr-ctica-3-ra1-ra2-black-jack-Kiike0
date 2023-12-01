package com.example.blackjack_jetpackcompose

class Carta (val nombre: Naipes, val palo : Palos, var puntosMin: Int, var puntosMax: Int, var idDrawable:Int) {

    /**
     * Funcionamiento para el juego que vamos a utilizar
     */
    init {
        when (nombre.valor) {
            1 -> {
                puntosMin = 1
                puntosMax = 11
            }
            in 2..11 -> {
                puntosMin = nombre.valor
                puntosMax = nombre.valor
            }
            else -> {
                puntosMin = 10
                puntosMax = 10
            }
        }
        idDrawable = nombre.valor + (palo.valor * 13)
    }
}