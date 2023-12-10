@file:Suppress("SpellCheckingInspection")
package com.example.blackjack_jetpackcompose.screens

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.blackjack_jetpackcompose.data.Baraja
import com.example.blackjack_jetpackcompose.data.Carta
import com.example.blackjack_jetpackcompose.data.Jugador
import java.util.ArrayList

/**
 * ViewModel para el juego del Blackjack.
 * Esta clase gestiona la lógica del juego y la interacción de la interfaz de usuario.
 *
 * @param application El contexto de la aplicación utilizado para inicializar ViewModel.
 */
class ViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _configJugadores = MutableLiveData<Boolean>()
    val configJugadores: LiveData<Boolean> = _configJugadores

    private val _gameOverDialog = MutableLiveData<Boolean>()
    val gameOverDialog: LiveData<Boolean> = _gameOverDialog

    private val _btnAceptar = MutableLiveData<Boolean>()
    val btnAceptar: LiveData<Boolean> = _btnAceptar

    private val _jugador1 = MutableLiveData<Jugador>()
    private val _jugador2 = MutableLiveData<Jugador>()

    private val _cambioTurno = MutableLiveData<Int>()
    val cambioTurno: LiveData<Int> = _cambioTurno

    private val _nickNameJugador1 = MutableLiveData<String>()
    val nickNameJugador1: LiveData<String> = _nickNameJugador1

    private val _nickNameJugador2 = MutableLiveData<String>()
    val nickNameJugador2: LiveData<String> = _nickNameJugador2

    private val _plantarJugador1 = MutableLiveData<Boolean>()
    val plantarJugador1: LiveData<Boolean> = _plantarJugador1

    private val _plantarJugador2 = MutableLiveData<Boolean>()
    val plantarJugador2: LiveData<Boolean> = _plantarJugador2

    private val _actualizarCartasJg = MutableLiveData<Boolean>()
    val actualizarCartasJg: LiveData<Boolean> = _actualizarCartasJg

    init {
        nuevaBaraja()
    }

    /**
     * Inicializa una nueva baraja de cartas para el juego.
     */
    private fun nuevaBaraja() {
        Baraja.crearBaraja(context)
        Baraja.barajar()
    }

    /**
     * Maneja el cierre del cuadro de diálogo de configuración.
     */
    fun onDismissConfigDialog() {
        _configJugadores.value = true
    }

    /**
     * Maneja el clic en los botones que cierran los cuadros de diálogo.
     */
    fun onClickCloseDialog() {
        _configJugadores.value = false
    }

    /**
     * Maneja los cambios en el nick.
     *
     * @param idJugador El ID del jugador (1 o 2).
     * @param nickName El nick elegido para el jugador o jugadora
     */
    fun onNickNameChange(idJugador: Int, nickName: String) {
        if (idJugador == 1) {
            _nickNameJugador1.value = nickName
        }
        else {
            _nickNameJugador2.value = nickName
        }
        _btnAceptar.value = !(_nickNameJugador1.value.isNullOrEmpty() || _nickNameJugador2.value.isNullOrEmpty())
    }

    /**
     * Cambia el turno del jugador.
     */
    private fun cambiaTurno() {
        if (_cambioTurno.value == 1) {
            _cambioTurno.value = 2
        }
        else{
            _cambioTurno.value = 1
        }
    }

    /**
     * Fuerza la actualización de las cartas de los jugadores en la Vista.
     * (truco para forzar la recomposición del componente LazyRow en BlackJackMulti.kt)
     */
    private fun forceRefreshPlayersCards() {
        if (_actualizarCartasJg.value == null) {
            _actualizarCartasJg.value = false
        } else {
            _actualizarCartasJg.value = !_actualizarCartasJg.value!!
        }
    }

    /**
     * Maneja la decisión de un jugador de plantarse o no.
     *
     * @param idJugador El ID del jugador (1 o 2).
     * @param plantarse Maneja la decisión del jugador si se planta (true) o no (false)
     * @param cambioTurno Si es true para cambia de jugador.
     */
    fun plantaJugador(idJugador: Int, plantarse: Boolean, cambioTurno: Boolean = false) {
        if (idJugador == 1) {
            _plantarJugador1.value = plantarse
        } else {
            _plantarJugador2.value = plantarse
        }

        _gameOverDialog.value = (_plantarJugador1.value == true && _plantarJugador2.value == true)

        if (cambioTurno) {
            cambiaTurno()
        }
    }

    /**
     * Solicita una nueva carta del mazo para el jugador especificado.
     *
     * @param idJugador El ID del jugador (1 o 2).
     */
    fun dameCarta(idJugador: Int) {
        val jugador = if (idJugador == 1) _jugador1.value!! else _jugador2.value!!

        jugador.listadeCartas.add(Baraja.dameCarta())
        calculoPuntos(jugador)

        //Se planta automáticamente si los puntos del jugador son > 21 puntos
        plantaJugador(idJugador, jugador.puntos > 21)

        //Verifica si el juego ha terminado
        _gameOverDialog.value = (_plantarJugador1.value == true && _plantarJugador2.value == true)

        forceRefreshPlayersCards()

        if (
            (_cambioTurno.value == 1 && _plantarJugador2.value == false) ||
            (_cambioTurno.value == 2 && _plantarJugador1.value == false)
        )
        {
            cambiaTurno()
        }
    }

    /**
     * Genera información de cada jugador, incluido su nick y puntos.
     *
     * @param idJugador El ID del jugador (1 o 2).
     * @return Una cadena que contiene información de los puntos de cada jugador y su nick.
     */
    fun infoJugador(idJugador: Int) : String {
        return if (idJugador == 1) {
            "${_jugador1.value?.nick ?: "Jugador 1"} - ${_jugador1.value?.puntos ?: 0} puntos"
        } else {
            "${_jugador2.value?.nick ?: "Jugador 2"} - ${_jugador2.value?.puntos ?: 0} puntos"
        }
    }

    /**
     * Recupera la lista de cartas que tiene un jugador.
     *
     * @param idJugador El ID del jugador (1 o 2).
     * @return Devuelve un ArrayList de objetos Card que representan las cartas del jugador.
     */
    fun getCartasJugador(idJugador: Int) : ArrayList<Carta> {
        return if (idJugador == 1) {
            _jugador1.value!!.listadeCartas
        } else {
            _jugador2.value!!.listadeCartas
        }
    }

    /**
     * Inicia un nuevo juego creando nuevos jugadores y restableciendo la información.
     */
    fun newGame() {
        _jugador1.value = Jugador(_nickNameJugador1.value!!, ArrayList(), puntos = 0)
        _jugador2.value = Jugador(_nickNameJugador2.value!!, ArrayList(), puntos = 0)
        resetGame()
    }

    /**
     * Restablece el mazo de cartas y la información del jugador para comenzar un nuevo juego
     *
     * @param resetJugadores Booleano que indica si es necesario restablecer la información.
     */
    fun resetGame(resetJugadores: Boolean = false) {
        if (resetJugadores) {
            _jugador1.value!!.listadeCartas.clear()
            _jugador1.value!!.puntos = 0
            _jugador2.value!!.listadeCartas.clear()
            _jugador2.value!!.puntos = 0
        }
        _plantarJugador1.value = false
        _plantarJugador2.value = false
        _cambioTurno.value = 1
        nuevaBaraja()
        dameCarta(1)
        dameCarta(2)
    }

    /**
     * Calcula los puntos totales de un jugador en función de sus cartas.
     *
     * @param jugador El objeto Jugador para quien se calculan los puntos.
     */
    private fun calculoPuntos(jugador: Jugador) {
        jugador.puntos = 0
        for (card in jugador.listadeCartas) {
            jugador.puntos += card.puntosMin
        }
        calculoAses(jugador)
    }

    /**
     * Ajusta los puntos de un jugador para tener en cuenta los ases.
     *
     * @param jugador El objeto Jugador para quien se calculan los puntos.
     */
    private fun calculoAses(jugador: Jugador) {
        for (carta in jugador.listadeCartas) {
            if (carta.puntosMin != carta.puntosMax &&
                (jugador.puntos - carta.puntosMin + carta.puntosMax) <= 21) {
                jugador.puntos -= carta.puntosMin
                jugador.puntos += carta.puntosMax
            }
        }
    }

    /**
     * Determina el ganador del juego según los puntos de los jugadores.
     *
     * @return Una cadena que indica el ganador o si han empatado
     */
    fun getGanador() : String {
        return if (_jugador1.value!!.puntos <= 21 && _jugador1.value!!.puntos > _jugador2.value!!.puntos) {
            "¡¡Ha ganado ${_jugador1.value!!.nick}!!"
        } else if (_jugador2.value!!.puntos <= 21) {
            "¡¡Ha ganado ${_jugador2.value!!.nick}!!"
        } else {
            "¡Empate!"
        }
    }


}