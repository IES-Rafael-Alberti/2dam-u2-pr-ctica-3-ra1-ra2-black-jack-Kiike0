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
 * @property _configJugador LiveData privado que indica si se debe configurar el número de jugadores
 * @property configJugador LiveData público que indica si se debe configurar el número de jugadores
 * @property _conseguidoBlackJack LiveData privado que indica si se ha conseguido un Blackjack durante el juego.
 * @property conseguidoBlackJack LiveData público que indica si se ha conseguido un Blackjack durante el juego.
 * @property _gameOverDialog LiveData privado que indica si se debe mostrar un cuadro de diálogo de fin de juego.
 * @property gameOverDialog LiveData público que indica si se debe mostrar un cuadro de diálogo de fin de juego.
 * @property _btnAceptar LiveData privado que indica si se ha presionado el botón de aceptar.
 * @property btnAceptar LiveData público que indica si se ha presionado el botón de aceptar.
 * @property _jugador LiveData privado: Representa al jugador principal.
 * @property _iaBot LiveData privado: Representa a la IA o bot del juego.
 * @property _puntuacionJg LiveData privado: Puntuación del jugador principal.
 * @property _puntuacionIA LiveData privado: Puntuación de la IA o bot del juego.
 * @property puntuacionIA LiveData público: Puntuación de la IA o bot del juego.
 * @property _puntuacionesDialog LiveData privado que indica si se debe mostrar un cuadro de diálogo de puntuaciones.
 * @property puntuacionesDialog LiveData público que indica si se debe mostrar un cuadro de diálogo de puntuaciones.
 * @property _mostrarCartasIA LiveData privado que indica si se deben mostrar las cartas de la IA.
 * @property mostrarCartasIA LiveData público que indica si se deben mostrar las cartas de la IA.
 * @property _nickName LiveData privado: Nickname o nombre del jugador principal.
 * @property nickName LiveData público: Nickname o nombre del jugador principal.
 * @property _plantarJugador LiveData privado que indica si el jugador principal ha decidido plantarse.
 * @property _plantarJugadorIA LiveData privado que indica si la IA ha decidido plantarse.
 * @property _actualizarCartas LiveData privado que indica si se deben actualizar las cartas en la interfaz de usuario.
 * @property actualizarCartas LiveData público que indica si se deben actualizar las cartas en la interfaz de usuario.
 * @property _actualizarPuntuaciones LiveData privado que indica si se deben actualizar las puntuaciones en la interfaz de usuario.
 * @property actualizarPuntuaciones LiveData público que indica si se deben actualizar las puntuaciones en la interfaz de usuario.
 *
 * @param application El contexto de la aplicación utilizado para inicializar ViewModel.
 *
 */
class BlackJackIAViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _configJugador = MutableLiveData<Boolean>()
    val configJugador: LiveData<Boolean> = _configJugador

    private val _conseguidoBlackJack = MutableLiveData<Boolean>()
    val conseguidoBlackJack: LiveData<Boolean> = _conseguidoBlackJack

    private val _gameOverDialog = MutableLiveData<Boolean>()
    val gameOverDialog: LiveData<Boolean> = _gameOverDialog

    private val _btnAceptar = MutableLiveData<Boolean>()
    val btnAceptar: LiveData<Boolean> = _btnAceptar

    private val _jugador = MutableLiveData<Jugador>()
    private val _iaBot = MutableLiveData<Jugador>()

    private val _puntuacionJg = MutableLiveData<Int>()

    private val _puntuacionIA = MutableLiveData<Int>()
    val puntuacionIA: LiveData<Int> = _puntuacionIA

    private val _puntuacionesDialog = MutableLiveData<Boolean>()
    val puntuacionesDialog: LiveData<Boolean> = _puntuacionesDialog

    private val _mostrarCartasIA = MutableLiveData<Boolean>()
    val mostrarCartasIA: LiveData<Boolean> = _mostrarCartasIA

    private val _nickName = MutableLiveData<String>()
    val nickName: LiveData<String> = _nickName

    private val _plantarJugador = MutableLiveData<Boolean>()
    private val _plantarJugadorIA = MutableLiveData<Boolean>()

    private val _actualizarCartas = MutableLiveData<Boolean>()
    val actualizarCartas: LiveData<Boolean> = _actualizarCartas

    private val _actualizarPuntuaciones = MutableLiveData<Boolean>()
    val actualizarPuntuaciones: LiveData<Boolean> = _actualizarPuntuaciones


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
     * Maneja el clic en los botones que cierran el cuadro del dialogo de final de juego.
     */
    fun setGameOverDialog(valor:Boolean){
        _gameOverDialog.value = valor
    }

    /**
     * Maneja el clic en los botones que cierran los cuadros de diálogo de las puntuaciones.
     */
    fun setPuntuacionesDialog(valor:Boolean){
        _puntuacionesDialog.value = valor
    }


    /**
     * Muestra las puntuaciones de cada jugador
     * @return devuelve las puntuaciones de cada jugador
     */
    fun getPuntuaciones():String{
        return "${_jugador.value?.nick}: ${_puntuacionJg.value} puntos\n" +
                "${_iaBot.value?.nick}: ${_puntuacionIA.value} puntos"
    }

    /**
     * Fuerza la actualización de las cartas de los jugadores en la Vista.
     * (truco para forzar la actualizacion de las puntuaciones)
     */
    private fun forzarActPuntuaciones(){
        if (_actualizarPuntuaciones.value == null) {
            _actualizarPuntuaciones.value = false
        } else {
            _actualizarPuntuaciones.value = !_actualizarPuntuaciones.value!!
        }
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
     * Maneja la decisión deljugador de plantarse o no.
     *
     * @param idJugador El ID del jugador.
     * @param plantarse Maneja la decisión del jugador si se planta (true) o no (false)
     */
    fun plantaJugador(idJugador: Int, plantarse: Boolean) {
        if (idJugador == 1) {
            _plantarJugador.value = plantarse
        } else {
            _plantarJugadorIA.value = plantarse
        }

        _gameOverDialog.value = (_plantarJugador.value == true && _plantarJugadorIA.value == true)

    }


    /**
     * Solicita una nueva carta del mazo para el jugador especificado.
     *
     * @param idJugador El ID del jugador (1 o 2).
     */
    fun dameCarta(idJugador: Int) {
        val jugador = if (idJugador == 1) _jugador.value!! else _iaBot.value!!

        jugador.listadeCartas.add(Baraja.dameCarta())
        calculoPuntos(jugador)

        //Se planta automáticamente si los puntos del jugador son > 21 puntos
        plantaJugador(idJugador, jugador.puntos > 21)

        //Verifica si el juego ha terminado
        _gameOverDialog.value = (_plantarJugador.value == true && _plantarJugadorIA.value == true)

        forzarActualizarCartas()
        forzarActPuntuaciones()

    }

    /**
     * Genera información del jugador, incluido su nick y puntos.
     *
     * @return Una cadena que contiene información de los puntos de cada jugador y su nick.
     */
    fun infoJugador() : String {
        return "${_jugador.value?.nick ?: "Jugador"} ${_jugador.value?.puntos ?: 0} puntos"

    }

    /**
     * Recupera la lista de cartas que tiene un jugador.
     *
     * @param idJugador El ID del jugador (1 o 2).
     * @return Devuelve un ArrayList de objetos Card que representan las cartas del jugador.
     */
    fun getCartasJugador(idJugador: Int) : ArrayList<Carta> {
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
        _puntuacionIA.value = 0
        _mostrarCartasIA.value = false
        _conseguidoBlackJack.value = false
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
     * Además, si ha conseguido 21 justo, ha ganado con un BlackJack!
     *
     * @return Una cadena que indica el ganador o si han empatado
     */
    fun getGanador(): String {
        try {
            return when {
                _jugador.value!!.puntos == 21 && _iaBot.value!!.puntos != 21 -> {
                    _conseguidoBlackJack.value = true
                    _mostrarCartasIA.value = true
                    _puntuacionJg.value = _jugador.value!!.puntos
                    _puntuacionIA.value = _iaBot.value!!.puntos
                    "¡Ha ganado ${_jugador.value!!.nick} con un Blackjack!"
                }
                _iaBot.value!!.puntos == 21 && _jugador.value!!.puntos != 21 -> {
                    _conseguidoBlackJack.value = true
                    _mostrarCartasIA.value = true
                    _puntuacionJg.value = _jugador.value!!.puntos
                    _puntuacionIA.value = _iaBot.value!!.puntos
                    "¡Ha ganado ${_iaBot.value!!.nick} con un Blackjack!"
                }
                _jugador.value!!.puntos > 21 && _iaBot.value!!.puntos <= 21 -> {
                    // Caso: Jugador se ha pasado de 21
                    _mostrarCartasIA.value = true
                    _puntuacionJg.value = _jugador.value!!.puntos
                    _puntuacionIA.value = _iaBot.value!!.puntos
                    "¡Ha ganado ${_iaBot.value!!.nick}!"
                }
                _jugador.value!!.puntos <= 21 && _iaBot.value!!.puntos > 21 -> {
                    // Caso: iaBot se ha pasado de 21
                    _mostrarCartasIA.value = true
                    _puntuacionJg.value = _jugador.value!!.puntos
                    _puntuacionIA.value = _iaBot.value!!.puntos
                    "¡Ha ganado ${_jugador.value!!.nick}!"
                }
                _jugador.value!!.puntos < 21 && _iaBot.value!!.puntos < 21 -> {
                    when {
                        _jugador.value!!.puntos > _iaBot.value!!.puntos -> {
                            _mostrarCartasIA.value = true
                            _puntuacionJg.value = _jugador.value!!.puntos
                            _puntuacionIA.value = _iaBot.value!!.puntos
                            "¡Ha ganado ${_jugador.value!!.nick}!"
                        }
                        _iaBot.value!!.puntos > _jugador.value!!.puntos -> {
                            _mostrarCartasIA.value = true
                            _puntuacionJg.value = _jugador.value!!.puntos
                            _puntuacionIA.value = _iaBot.value!!.puntos
                            "¡Ha ganado ${_iaBot.value!!.nick}!"
                        }
                        else -> {
                            _mostrarCartasIA.value = true
                            _puntuacionJg.value = _jugador.value!!.puntos
                            _puntuacionIA.value = _iaBot.value!!.puntos
                            "¡Empate!"
                        }
                    }
                }
                else -> {
                    _mostrarCartasIA.value = true
                    _puntuacionJg.value = _jugador.value!!.puntos
                    _puntuacionIA.value = _iaBot.value!!.puntos
                    "¡Empate!"
                }
            }
        } catch (e: Exception) {
            return "Error al determinar al ganador"
        }
    }

    /**
     * Finaliza el juego y resetea todas las propiedades para un nuevo juego
     */
    fun finalizarJuego(){
        _configJugador.value = true
        _gameOverDialog.value = false
        _nickName.value = ""
        _btnAceptar.value = false
        _plantarJugadorIA.value = false
    }
}
