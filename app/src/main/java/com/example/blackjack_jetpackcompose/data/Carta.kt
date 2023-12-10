package com.example.blackjack_jetpackcompose.data

import androidx.annotation.DrawableRes

data class Carta (val nombre: Naipes, val palo : Palos, var puntosMin: Int, var puntosMax: Int, @DrawableRes var idDrawable:Int)
