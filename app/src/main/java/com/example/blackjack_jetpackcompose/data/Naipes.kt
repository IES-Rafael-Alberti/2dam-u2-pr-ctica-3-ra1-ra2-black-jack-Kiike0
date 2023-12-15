package com.example.blackjack_jetpackcompose.data

import com.example.blackjack_jetpackcompose.data.Palos.CORAZONES
import com.example.blackjack_jetpackcompose.data.Palos.DIAMANTES
import com.example.blackjack_jetpackcompose.data.Palos.NINGUNA
import com.example.blackjack_jetpackcompose.data.Palos.PICAS
import com.example.blackjack_jetpackcompose.data.Palos.TREBOLES


/**
 * Enum class de Naipes que representa los naipes de la baraja de cartas
 *
 * @property NINGUNA Representa un tipo de carta no definido.
 * @property AS Representa el AS.
 * @property DOS Representa el número 2 de la baraja.
 * @property TRES Representa el número 3 de la baraja
 * @property CUATRO Represena el número 4 de la baraja.
 * @property CINCO Represena el número 5 de la baraja.
 * @property SEIS Represena el número 6 de la baraja.
 * @property SIETE Represena el número 7 de la baraja.
 * @property OCHO Represena el número 8 de la baraja.
 * @property NUEVE Represena el número 9 de la baraja.
 * @property DIEZ Represena el número 10 de la baraja.
 * @property VALET Represena la sota.
 * @property DAME Represena la reina.
 * @property ROI Represena el rey.
 */
enum class Naipes {
    NINGUNA, AS, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE, DIEZ, VALET, DAME, ROI
}