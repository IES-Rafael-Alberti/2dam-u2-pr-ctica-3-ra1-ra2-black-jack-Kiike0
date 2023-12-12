package com.example.blackjack_jetpackcompose.data

import androidx.annotation.DrawableRes

/**
 * Data class de Carta que representa los cartas de la baraja que usamos en el blackjack
 *
 * @property nombre un objeto de la clase naipes que representa el nombre del naipe
 * @property palo un objeto de la clase Palos que representa palo que pertenece la carta
 * @property puntosMin un entero que representa los puntos mínimos de la carta
 * @property puntosMax un entero que representa los puntos máximos de la carta
 * @property idDrawable un entero que representa el id de la imagen guardada en la carpeta drawables
 *
 */
data class Carta (val nombre: Naipes, val palo : Palos, var puntosMin: Int, var puntosMax: Int, @DrawableRes var idDrawable:Int)
