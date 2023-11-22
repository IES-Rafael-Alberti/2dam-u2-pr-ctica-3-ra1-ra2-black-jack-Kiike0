package com.example.blackjack_jetpackcompose.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.blackjack_jetpackcompose.Baraja
import com.example.blackjack_jetpackcompose.Carta
import com.example.blackjack_jetpackcompose.R
import kotlinx.coroutines.delay

/**
 * Screen1
 */
@Preview(showBackground = true)
@Composable
fun Screen1(){


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally //Centra el contenido de la columna
    )
    {
        Button(
            onClick = { },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(250.dp)
                .height(150.dp)
        )
        {
            Text(text = "Jugador vs Jugador", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = { },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(250.dp)
                .height(150.dp)
        )
        {
            Text("Jugador vs IA", fontSize = 20.sp)
        }
    }
}

/**
 * Enseña la carta de cada jugador cuando cada uno independientemente pide carta.
 * Si es el turno del jugador 1, el jugador 1 no verá las cartas de jugador 2 (estaran tapadas)
 * Si es el turno del jugador 2, éste no verá las cartas del otro jugador
 * Las cartas se van añadiendo a la screen
 * Cuando uno se plante se almacenará los puntos de sus cartas en una variable y no podrá
 * ver las cartas del otro jugador, además, éste ya no jugará más (no tendrá más turnos)
 * Se parará el juego hasta que los dos se planten y entonces se calculará segun su puntuación
 * cuál es el jugador ganador.
 */
@Preview (showBackground = true)
@Composable
fun Multijugador() {
    val context = LocalContext.current
    val miBaraja = Baraja
    var jugador1Puntos by rememberSaveable { mutableStateOf(0) }
    var jugador2Puntos by rememberSaveable { mutableStateOf(0) }
    var showCardJugador1 by rememberSaveable { mutableStateOf("reverso") }
    var showCardJugador2 by rememberSaveable { mutableStateOf("reverso") }
    var juegoFinalizado by rememberSaveable { mutableStateOf(false) }
    var mostrarCartas by rememberSaveable { mutableStateOf(true) }
    var cartasJugador1 by rememberSaveable { mutableStateOf(listOf<Carta>()) }
    var cartasJugador2 by rememberSaveable { mutableStateOf(listOf<Carta>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón para cambiar el turno
        Button(
            onClick = {
                // La lógica para cambiar el turno
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Cambiar turno")
        }

        // Jugador 1
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Jugador 1",
                style = TextStyle(
                    fontSize = 24.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 3f
                    )
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (mostrarCartas) {
                LazyRow {
                    items(cartasJugador1.size) { index ->
                        val card = cartasJugador1[index]
                        Image(
                            painter = painterResource(
                                id = context.resources.getIdentifier(
                                    "c${card.idDrawable}",
                                    "drawable",
                                    context.packageName
                                )
                            ),
                            contentDescription = "Carta Jugador 1",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(0.dp)
                        )
                    }
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.reverso),
                    contentDescription = "Reverso",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botones para Jugador 1
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (!juegoFinalizado) {
                            val choosenCard = miBaraja.dameCarta()
                            if (choosenCard != null) {
                                showCardJugador1 = "c${choosenCard.idDrawable}"
                                jugador1Puntos += choosenCard.puntosMin
                                cartasJugador1 = cartasJugador1 + choosenCard
                            }
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Dame carta")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        // Lógica para plantarse
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Plantarse")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Jugador 2
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Jugador 2",
                style = TextStyle(
                    fontSize = 24.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 3f
                    )
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            if (mostrarCartas) {
                LazyRow {
                    items(cartasJugador2.size) { index ->
                        val card = cartasJugador2[index]
                        Image(
                            painter = painterResource(
                                id = context.resources.getIdentifier(
                                    "c${card.idDrawable}",
                                    "drawable",
                                    context.packageName
                                )
                            ),
                            contentDescription = "Carta Jugador 2",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(1.dp)
                        )
                    }
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.reverso),
                    contentDescription = "Reverso",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botones para Jugador 2
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (!juegoFinalizado) {
                            val choosenCard = miBaraja.dameCarta()
                            if (choosenCard != null) {
                                showCardJugador2 = "c${choosenCard.idDrawable}"
                                jugador2Puntos += choosenCard.puntosMin
                                cartasJugador2 = cartasJugador2 + choosenCard
                            }
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Dame carta")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        // Lógica para plantarse
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Plantarse")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botones generales
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    juegoFinalizado = false
                    cartasJugador1 = emptyList()
                    cartasJugador2 = emptyList()
                    jugador1Puntos = 0
                    jugador2Puntos = 0
                    mostrarCartas = true
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            )
            {
                Text("Volver a jugar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    // Lógica para determinar el ganador y mostrar los resultados
                    juegoFinalizado = true
                    // Puedes comparar las puntuaciones y mostrar un mensaje de resultado aquí
                    // También podrías implementar lógica adicional, como determinar si ambos jugadores se han plantado
                    // y calcular el ganador en ese momento
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            )
            {
                Text("Finalizar Juego")
            }
        }
    }
}

@Composable
fun Maquina(){

}




/**
 * Método creado Composable que define lo que pasa en pantalla
 *
 */
@Preview(showBackground = true)
@Composable
fun AccionIniciar() {

    val context = LocalContext.current
    var showCard by rememberSaveable { mutableStateOf("reverso") }
    val miBaraja = Baraja

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally //Centra el contenido de la columna
    )
    {
        Image(
            painter = painterResource(
                id = context.resources.getIdentifier(
                    showCard,
                    "drawable", context.packageName
                )
            ),
            contentDescription = "Carta",
            modifier = Modifier
                .size(400.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { //Enseña la carta de la baraja y si es nula enseña el reverso
                    val choosenCard = miBaraja.dameCarta()
                    showCard = if (choosenCard == null) {
                        "reverso"
                    } else
                        "c${choosenCard.idDrawable}"
                },
                shape = RoundedCornerShape(10.dp), //Hace al botón más cuadrado
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White //Cambia el color de los botones
                )
            ) {
                Text(text = "Dame carta")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { //Reinicia la baraja desde 0
                    miBaraja.crearBaraja()
                    miBaraja.barajar()
                    showCard = "reverso"
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            )
            {
                Text("Reiniciar")
            }
        }
    }
}