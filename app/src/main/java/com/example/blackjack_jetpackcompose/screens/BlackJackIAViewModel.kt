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
 * ViewModel para el juego del Blackjack contra la IA.
 * Esta clase gestiona la lógica del juego y la interacción de la interfaz de usuario.
 *
 * @param application El contexto de la aplicación utilizado para inicializar ViewModel.
 */
class BlackJackIAViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _configJugador = MutableLiveData<Boolean>()
    val configJugadores: LiveData<Boolean> = _configJugador

    private val _conseguidoBlackJack = MutableLiveData<Boolean>()
    val conseguidoBlackJack: LiveData<Boolean> = _conseguidoBlackJack

    private val _gameOverDialog = MutableLiveData<Boolean>()
    val gameOverDialog: LiveData<Boolean> = _gameOverDialog

    private val _btnAceptar = MutableLiveData<Boolean>()
    val btnAceptar: LiveData<Boolean> = _btnAceptar

    private val _jugador = MutableLiveData<Jugador>()
    private val _iaBot = MutableLiveData<Jugador>()

    private val _cambioTurno = MutableLiveData<Int>()
    val cambioTurno: LiveData<Int> = _cambioTurno

    private val _mostrarCartasIA = MutableLiveData<Boolean>()
    val mostrarCartasIA: LiveData<Boolean> = _mostrarCartasIA

    private val _nickName = MutableLiveData<String>()
    val nickName: LiveData<String> = _nickName

    private val _plantarJugador = MutableLiveData<Boolean>()
    val plantarJugador: LiveData<Boolean> = _plantarJugador

    private val _plantarJugadorIA = MutableLiveData<Boolean>()
    val plantarJugadorIA: LiveData<Boolean> = _plantarJugadorIA

    private val _actualizarCartas = MutableLiveData<Boolean>()
    val actualizarCartas: LiveData<Boolean> = _actualizarCartas


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
        _jugador.value = Jugador(_nickName.value!!, ArrayList(), puntos = 0)
        _iaBot.value = Jugador("Máquina", ArrayList(), puntos = 0)
        resetGame()
    }

    /**
     * Maneja el cierre del cuadro de diálogo de configuración.
     */
    fun onDismissConfigDialog() {
        _configJugador.value = true
    }

    /**
     * Maneja el clic en los botones que cierran los cuadros de diálogo.
     */
    fun onClickCloseDialog() {
        _configJugador.value = false
    }

    /**
     * Maneja los cambios en el nick.
     *
     * @param nickName El nick elegido para el jugador o jugadora
     */
    fun onNickNameChange(nickName: String) {
        _nickName.value = nickName
        _btnAceptar.value = !(_nickName.value.isNullOrEmpty())
    }

    /**
     * Fuerza la actualización de las cartas de los jugadores en la Vista.
     * (truco para forzar la recomposición del componente LazyRow en BlackJackMulti.kt)
     */
    private fun forzarActualizarCartas() {
        if (_actualizarCartas.value == null) {
            _actualizarCartas.value = false
        } else {
            _actualizarCartas.value = !_actualizarCartas.value!!
        }
    }

    /**
     * Maneja la decisión de un jugador de plantarse o no.
     *
     * @param plantarse Maneja la decisión del jugador si se planta (true) o no (false)
     * @param cambioTurno Si es true para cambia de jugador.
     */
    fun plantaJugador(plantarse: Boolean, cambioTurno: Boolean = false) {
        _plantarJugador.value = plantarse
        _gameOverDialog.value = (_plantarJugador.value == true && _plantarJugadorIA.value == true)

        if (cambioTurno) {
            cambiaTurno()
        }
    }

    /**
     * Solicita una nueva carta del mazo para el jugador.
     *
     */
    fun dameCarta(idJugador: Int) {
        val jugador = if (idJugador == 1) _jugador.value!! else _iaBot.value!!

        jugador.listadeCartas.add(Baraja.dameCarta())
        calculoPuntos(jugador)

        //Se planta automáticamente si los puntos del jugador son > 21 puntos
        plantaJugador(jugador.puntos > 21)

        //Verifica si el juego ha terminado
        _gameOverDialog.value = (_plantarJugador.value == true && _plantarJugadorIA.value == true)

        forzarActualizarCartas()

        if (
            (_cambioTurno.value == 1 && _plantarJugadorIA.value == false) ||
            (_cambioTurno.value == 2 && _plantarJugador.value == false)
        ) {
            cambiaTurno()
        }
    }

    /**
     * Cambia el turno del jugador.
     */
    private fun cambiaTurno() {
        if (_cambioTurno.value == 1) {
            _cambioTurno.value = 2
        } else {
            _cambioTurno.value = 1
        }
    }

    /**
     * Genera información de cada jugador, incluido su nick y puntos.
     *
     * @param idJugador El ID del jugador.
     * @return Una cadena que contiene información de los puntos de cada jugador y su nick.
     */
    fun infoJugador(idJugador: Int): String {
        return if (idJugador == 1) {
            "${_jugador.value?.nick ?: "Jugador"} - ${_jugador.value?.puntos ?: 0} puntos"
        } else {
            "${_iaBot.value?.nick ?: "Bot"} - ${_iaBot.value?.puntos ?: 0} puntos"
        }
    }

    /**
     * Recupera la lista de cartas que tiene un jugador.
     *
     * @param idJugador El ID del jugador (1).
     * @return Devuelve un ArrayList de objetos Card que representan las cartas del jugador.
     */
    fun getCartasJugador(idJugador: Int): ArrayList<Carta> {
        return if (idJugador == 1) {
            _jugador.value!!.listadeCartas
        } else {
            _iaBot.value!!.listadeCartas
        }
    }


    /**
     * Restablece el mazo de cartas y la información del jugador para comenzar un nuevo juego
     *
     * @param resetJugadores Booleano que indica si es necesario restablecer la información.
     */
    fun resetGame(resetJugadores: Boolean = false) {
        if (resetJugadores) {
            _jugador.value!!.listadeCartas.clear()
            _jugador.value!!.puntos = 0
            _iaBot.value!!.listadeCartas.clear()
            _iaBot.value!!.puntos = 0
        }
        _plantarJugador.value = false
        _plantarJugadorIA.value = false
        _cambioTurno.value = 1
        nuevaBaraja()
        dameCarta(1)
        dameCarta(2)
    }

    /**
     * Calcula los puntos totales del jugador o la ia (también es jugador) en función de sus cartas.
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
            _jugador.value!!.puntos == 21 && _iaBot.value!!.puntos != 21 -> {
                _conseguidoBlackJack.value = true
                "¡Has ganado con un Blackjack!"
            }

            _iaBot.value!!.puntos == 21 && _jugador.value!!.puntos != 21 -> {
                _conseguidoBlackJack.value = true
                "¡Ha ganado la Máquina con un Blackjack!"
            }

            _jugador.value!!.puntos < 21 && (_jugador.value!!.puntos > _iaBot.value!!.puntos || _iaBot.value!!.puntos > 21) -> {
                _conseguidoBlackJack.value = false
                "¡Has ganado!"
            }

            _iaBot.value!!.puntos < 21 -> {
                _conseguidoBlackJack.value = false
                "¡Ha ganado la Máquina!"
            }

            else -> {
                "¡Empate!"
            }
        }
    }

    /**
     * Finaliza el juego y resetea todas las propiedades para un nuevo juego
     */
    fun finalizarJuego() {
        _configJugador.value = true
        _gameOverDialog.value = false
        _nickName.value = ""
        _btnAceptar.value = false
    }

}
