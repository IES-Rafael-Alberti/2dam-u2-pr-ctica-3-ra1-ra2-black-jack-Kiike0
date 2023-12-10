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
class BJMultiViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = getApplication<Application>().applicationContext

    private val _configJugadores = MutableLiveData<Boolean>()
    val configJugadores: LiveData<Boolean> = _configJugadores

    private val _conseguidoBlackJack = MutableLiveData<Boolean>()
    val conseguidoBlackJack: LiveData<Boolean> = _conseguidoBlackJack

    private val _gameOverDialog = MutableLiveData<Boolean>()
    val gameOverDialog: LiveData<Boolean> = _gameOverDialog

    private val _btnAceptar = MutableLiveData<Boolean>()
    val btnAceptar: LiveData<Boolean> = _btnAceptar

    private val _jugador1 = MutableLiveData<Jugador>()
    private val _jugador2 = MutableLiveData<Jugador>()

    private val _cambioTurno = MutableLiveData<Int>()
    val cambioTurno: LiveData<Int> = _cambioTurno

    private val _mostrarCartasJ1 = MutableLiveData<Boolean>()
    val mostrarCartasJ1: LiveData<Boolean> = _mostrarCartasJ1

    private val _mostrarCartasJ2 = MutableLiveData<Boolean>()
    val mostrarCartasJ2: LiveData<Boolean> = _mostrarCartasJ2

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
     * Inicia un nuevo juego creando nuevos jugadores y restableciendo la información.
     */
    fun newGame() {
        _jugador1.value = Jugador(_nickNameJugador1.value!!, ArrayList(), puntos = 0)
        _jugador2.value = Jugador(_nickNameJugador2.value!!, ArrayList(), puntos = 0)
        resetGame()
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
     * Fuerza la actualización de las cartas de los jugadores en la Vista.
     * (truco para forzar la recomposición del componente LazyRow en BlackJackMulti.kt)
     */
    private fun forzarActualizarCartas() {
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

        forzarActualizarCartas()

        if (
            (_cambioTurno.value == 1 && _plantarJugador2.value == false) ||
            (_cambioTurno.value == 2 && _plantarJugador1.value == false)
        )
        {
            cambiaTurno()
        }
    }

    /**
     * Cambia el turno del jugador.
     */
    private fun cambiaTurno() {
        if (_cambioTurno.value == 1) {
            _mostrarCartasJ2.value = true
            _mostrarCartasJ1.value = false
            _cambioTurno.value = 2
        }
        else{
            _mostrarCartasJ1.value = true
            _mostrarCartasJ2.value = false
            _cambioTurno.value = 1
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
     * Ajusta los puntos de un jugador para tener en cuenta los ases, permitiendo que un as
     * cambie su valor de 1 a 11 si el jugador puede seguir jugando sin pasarse de 21.
     *
     * @param jugador El objeto Jugador para quien se calculan los puntos.
     */
    private fun calculoAses(jugador: Jugador) {
        var tieneAs = false
        for (carta in jugador.listadeCartas) {
            // Verifica si la carta es un as y su valor máximo es 11
            if (carta.puntosMin != carta.puntosMax && carta.puntosMax == 11) {
                tieneAs = true
                break
            }
        }

        // Si el jugador tiene un as con valor máximo de 11
        if (tieneAs) {
            // Calcula los puntos si el as se contara como 11
            val puntosConAsMax = jugador.puntos + 10
            // Verifica si el jugador puede cambiar el valor del as a 10 sin pasarse de 21
            if (puntosConAsMax <= 21) {
                // Ajusta los puntos del jugador sumando 10 al valor actual
                jugador.puntos = puntosConAsMax
            }
            // Si el jugador ya tiene 21 puntos o más, el as se contará como 1
        }
    }

    /**
     * Determina el ganador del juego según los puntos de los jugadores.
     * Además si ha conseguido 21 justo, ha ganado con un BlackJack!
     *
     * @return Una cadena que indica el ganador o si han empatado
     */
    fun getGanador(): String {
        return when {
            _jugador1.value!!.puntos == 21 && _jugador2.value!!.puntos != 21 -> {
                _conseguidoBlackJack.value = true
                "¡Ha ganado ${_jugador1.value!!.nick} con un Blackjack!"
            }
            _jugador2.value!!.puntos == 21 && _jugador1.value!!.puntos != 21 -> {
                _conseguidoBlackJack.value = true
                "¡Ha ganado ${_jugador2.value!!.nick} con un Blackjack!"
            }
            _jugador1.value!!.puntos < 21 && (_jugador1.value!!.puntos > _jugador2.value!!.puntos || _jugador2.value!!.puntos > 21) -> {
                _conseguidoBlackJack.value = false
                "¡Ha ganado ${_jugador1.value!!.nick}!"
            }
            _jugador2.value!!.puntos < 21 -> {
                _conseguidoBlackJack.value = false
                "¡Ha ganado ${_jugador2.value!!.nick}!"
            }
            else -> {
                "¡Empate!"
            }
        }
    }

    /**
     * Finaliza el juego y resetea todas las propiedades para un nuevo juego
     */
    fun finalizarJuego(){
        _configJugadores.value = true
        _gameOverDialog.value = false
        _nickNameJugador1.value = ""
        _nickNameJugador2.value = ""
        _btnAceptar.value = false
    }

}