package com.example.blackjack_jetpackcompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blackjack_jetpackcompose.Baraja

/**
 * Screen1
 *
 *
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
@Composable
fun Multijugador(){

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