package com.example.blackjack_jetpackcompose.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
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
fun Screen1() {


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
@Preview(showBackground = true)
@Composable
fun Multijugador() {
    val miBaraja = Baraja

    var jugador1Puntos by rememberSaveable { mutableStateOf(0) }
    var jugador2Puntos by rememberSaveable { mutableStateOf(0) }
    var showCardJugador1 by rememberSaveable { mutableStateOf("reverso") }
    var showCardJugador2 by rememberSaveable { mutableStateOf("reverso") }
    var mostrarCartasJ1 by rememberSaveable { mutableStateOf(true) }
    var mostrarCartasJ2 by rememberSaveable { mutableStateOf(true) }
    var cartasJugador1 by rememberSaveable { mutableStateOf(listOf<Carta>()) }
    var cartasJugador2 by rememberSaveable { mutableStateOf(listOf<Carta>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Jugador(
            nombreJugador = "Jugador 1",
            cartasJugador = cartasJugador1,
            mostrarCartas = mostrarCartasJ1
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Botones para Jugador 1
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {

                    val choosenCard = miBaraja.dameCarta()
                    if (choosenCard != null) {
                        showCardJugador1 = "c${choosenCard.idDrawable}"
                        jugador1Puntos += choosenCard.puntosMin
                        cartasJugador1 = cartasJugador1 + choosenCard
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
                    mostrarCartasJ1 = !mostrarCartasJ1
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = if (mostrarCartasJ1) "Tapar" else "Destapar")
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


        Spacer(modifier = Modifier.height(20.dp))

        // Jugador 2
        Jugador(
            nombreJugador = "Jugador 2",
            cartasJugador = cartasJugador2,
            mostrarCartas = mostrarCartasJ2
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botones para Jugador 2
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {

                    val choosenCard = miBaraja.dameCarta()
                    if (choosenCard != null) {
                        showCardJugador2 = "c${choosenCard.idDrawable}"
                        jugador2Puntos += choosenCard.puntosMin
                        cartasJugador2 = cartasJugador2 + choosenCard
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
                    mostrarCartasJ2 = !mostrarCartasJ2
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = if (mostrarCartasJ2) "Tapar" else "Destapar")
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
        Spacer(modifier = Modifier.height(20.dp))

        // Botones generales
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    cartasJugador1 = emptyList()
                    cartasJugador2 = emptyList()
                    jugador1Puntos = 0
                    jugador2Puntos = 0
                    mostrarCartasJ1 = true
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
                    //salir del juego te vuelve a la pantalla anterior
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            )
            {
                Text("Salir del juego")
            }
        }
    }


}


@Composable
fun Jugador(
    nombreJugador: String,
    cartasJugador: List<Carta>,
    mostrarCartas: Boolean
) {
    val context = LocalContext.current
    val y = 0.dp
    // Jugador Correspondiente
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = nombreJugador,
            style = TextStyle(
                fontSize = 24.sp,
                shadow = Shadow(
                    color = Color.Black,
                    blurRadius = 3f
                )
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box {
            var x = 0.dp

            for (card in cartasJugador) {
                Box(
                    modifier = Modifier
                        .offset(x, y)
                        .fillMaxWidth()
                ) {
                    if (mostrarCartas) {
                        Image(
                            painter = painterResource(
                                id = context.resources.getIdentifier(
                                    "c${card.idDrawable}",
                                    "drawable",
                                    context.packageName
                                )
                            ),
                            contentDescription = "Carta mostrada",
                            modifier = Modifier
                                .size(150.dp)
                                .padding(0.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(
                                id = context.resources.getIdentifier(
                                    "reverso",
                                    "drawable",
                                    context.packageName
                                )
                            ),
                            contentDescription = "reverso carta",
                            modifier = Modifier
                                .size(150.dp)
                                .padding(0.dp)
                        )
                    }
                    x += 30.dp
                }
            }
        }

    }
}

@Composable
fun Maquina() {

}

