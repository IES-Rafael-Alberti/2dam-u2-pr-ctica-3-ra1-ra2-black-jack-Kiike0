package com.example.blackjack_jetpackcompose.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.blackjack_jetpackcompose.R
import com.example.blackjack_jetpackcompose.data.Routes
import com.example.blackjack_jetpackcompose.data.Carta


/**
 * Función Composable que representa la pantalla para el juego de BlackJack Jugador vs Jugador
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 */
@Composable
fun PantallaMultijugadorInicial(
    navController: NavHostController,
    viewModel: BlackJackMultiViewModel
) {
    val configJugadores: Boolean by viewModel.configJugadores.observeAsState(initial = true)
    val showEndGameDialog: Boolean by viewModel.gameOverDialog.observeAsState(initial = false)
    val showPuntuaciones: Boolean by viewModel.puntuacionesDialog.observeAsState(initial = false)
    val actualizacionCartasJugador: Boolean by viewModel.actualizarCartasJg.observeAsState(initial = false)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.tapete),
            contentDescription = "tapete de fondo",
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        ConfigJugadores(
            navController,
            viewModel,
            configJugadores
        ) { viewModel.onDismissConfigDialog() }

        EndGameDialog(
            navController,
            viewModel,
            showEndGameDialog
        )
        PuntuacionesDialogo(
            viewModel,
            navController,
            showPuntuaciones
        )
        MultijugadorLayout(
            viewModel,
            configJugadores,
            actualizacionCartasJugador
        )
    }
}

/**
 * Función composable que representa el cuadro de diálogo de configuración para establecer los nicks de los jugadores.
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param showConfiguracionDialog Indica si el cuadro de diálogo de configuración está visible.
 * @param onDissmiss Función de devolución de llamada para cerrar el diálogo.
 */
@Composable
fun ConfigJugadores(
    navController: NavHostController,
    viewModel: BlackJackMultiViewModel,
    showConfiguracionDialog: Boolean,
    onDissmiss: () -> Unit
) {
    val nickNameJugador1: String by viewModel.nickNameJugador1.observeAsState(initial = "")
    val nickNameJugador2: String by viewModel.nickNameJugador2.observeAsState(initial = "")

    if (showConfiguracionDialog) {
        Dialog(
            onDismissRequest = { onDissmiss() },
            properties = DialogProperties(dismissOnClickOutside = false),
            content = {
                // Contenido del diálogo
                Column(
                    modifier = Modifier
                        .background(
                            Color.DarkGray,
                            MaterialTheme.shapes.medium // Ajusta la forma del recorte para redondear las esquinas
                        )
                        .padding(18.dp)
                ) {
                    ElementoNick(
                        viewModel,
                        idJugador = 1,
                        drawable = R.drawable.jugador1,
                        nickNameJugador1
                    )
                    ElementoNick(
                        viewModel,
                        idJugador = 2,
                        drawable = R.drawable.jugador2,
                        nickNameJugador2
                    )
                    Botones(
                        navController,
                        viewModel
                    )
                }
            }
        )
    }
}

/**
 * Función que representa un elemento para ingresar el nick de un jugador.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param idJugador El id que representa el jugador si es 1 o 2
 * @param drawable El ID de recurso de la imagen del jugador.
 * @param nickNamePlayer El nick actual del jugador o jugadora.
 */
@Composable
fun ElementoNick(
    viewModel: BlackJackMultiViewModel,
    idJugador: Int,
    @DrawableRes drawable: Int,
    nickNamePlayer: String
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = drawable),
                contentDescription = "Imagen Jugador",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterVertically)
            )
            RellenarNick(
                value = nickNamePlayer,
                onValueChange = { viewModel.onNickNameChange(idJugador, it) }
            )
        }
    }
}

/**
 * Función que representa el recuadro para rellenar los nicks que elijamos
 *
 * @param value Representa el valor actual del campo de texto
 * @param onValueChange Función lambda que se ejecutará cuando el valor del campo de texto cambie. Toma como parámetro el nuevo valor.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RellenarNick(
    value: String,
    onValueChange: (String) -> Unit
) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(10.dp),
            label = { Text(text = "Introduce tu nick", fontFamily = FontFamily.Monospace) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.Blue,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.Red
            )
        )
}

/**
 * Función composable que representa los botones aceptar o cancelar de la configuración en una fila.
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 */
@Composable
fun Botones(
    navController: NavHostController,
    viewModel: BlackJackMultiViewModel
) {
    val btnAceptar: Boolean by viewModel.btnAceptar.observeAsState(initial = false)

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Button(
            enabled = btnAceptar,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = {
                viewModel.newGame()
                viewModel.onClickCloseDialog()
            }
        ) {
            Text(text = "Aceptar")
        }
        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier.padding(start = 5.dp),
            onClick = { navController.navigate(Routes.GameScreen.route) }
        ) {
            Text(text = "Cancelar")
        }
    }
}

/**
 * Función que representa el diálogo que se muestra al final del juego de Multijugador del BlackJack.
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param showEndGameDialog Indica si el diálogo de finalización del juego es visible.
 *
 */
@Composable
fun EndGameDialog(
    navController: NavHostController,
    viewModel: BlackJackMultiViewModel,
    showEndGameDialog: Boolean
) {
    if (showEndGameDialog) {
        Dialog(onDismissRequest = {
            navController.navigate(Routes.GameScreen.route)
            viewModel.finalizarJuego()
        }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        Color.DarkGray,
                        MaterialTheme.shapes.medium
                    )
                    .padding(all = 10.dp)
                    .fillMaxWidth()
            ) {

                TituloDialogo(
                    text = viewModel.getGanador(),
                    viewModel
                )
                BotonesEndGameDialog(
                    viewModel,
                    navController
                )
            }
        }
    }

}

/**
 * Función composable que representa la composición de un título.
 *
 * @param text El texto que se mostrará como título.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 */
@Composable
fun TituloDialogo(
    text: String,
    viewModel: BlackJackMultiViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (viewModel.conseguidoBlackJack.value == true) {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 5.dp)
            )
        } else {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 5.dp)
            )
        }
    }
}

/**
 * Función que representa los botones comunes para el diálogo de fin de juego.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 */
@Composable
fun BotonesEndGameDialog(
    viewModel: BlackJackMultiViewModel,
    navController: NavHostController
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = {
                viewModel.resetGame(true)
                viewModel.onClickCloseDialog()
            }
        ) {
            Text(text = "Reiniciar")
        }
        Spacer(modifier = Modifier.width(10.dp))
        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = {
                viewModel.setPuntuacionesDialog(true)
                viewModel.setGameOverDialog(false)
            }
        ) {
            Text(text = "Puntuaciones")
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    Button(
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White
        ),
        onClick = {
            navController.navigate(Routes.GameScreen.route)
            viewModel.finalizarJuego()
        }
    ) {
        Text(text = "Salir")
    }
}

/**
 * Función composable que representa la composición del cuadro de diálogo de las puntuaciones.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param showPuntuaciones Indica si el cuadro de diálogo de las puntuaciones está visible.
 */
@Composable
fun PuntuacionesDialogo(
    viewModel: BlackJackMultiViewModel,
    navController: NavHostController,
    showPuntuaciones: Boolean
) {
    if(showPuntuaciones){
        AlertDialog(
            onDismissRequest = { navController.navigate(Routes.GameScreen.route) },
            title = {
                Text(
                    text = "Puntuaciones",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
                Spacer(modifier = Modifier.height(40.dp))
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = viewModel.getPuntuaciones(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            viewModel.setPuntuacionesDialog(false)
                            viewModel.setGameOverDialog(true)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Salir")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {},
            modifier = Modifier
                .size(220.dp, 280.dp)
        )
    }

}

/**
 * Función composable que representa el diseño del Multijugador del BlackJack.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param configJugadores Indica si el cuadro de diálogo de configuración está visible.
 * @param actualizacionCartasJugador Indica si las tarjetas de jugador deben actualizarse.
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun MultijugadorLayout(
    viewModel: BlackJackMultiViewModel,
    configJugadores: Boolean,
    actualizacionCartasJugador: Boolean
) {
    val plantarJugador1: Boolean by viewModel.plantarJugador1.observeAsState(initial = false)
    val plantarJugador2: Boolean by viewModel.plantarJugador2.observeAsState(initial = false)
    val turnoJugador: Int by viewModel.cambioTurno.observeAsState(initial = 1)

    if (!configJugadores) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Jugador1(
                    viewModel,
                    plantarJugador1,
                    turnoJugador
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy((-85).dp),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(start = 35.dp, 8.dp),
                modifier = Modifier
                    .weight(1.2f)
            ) {
                //Visualización de sus cartas de cada jugador
                items(viewModel.getCartasJugador(1)) { card ->
                    ElementoCartaJ1(carta = card, viewModel)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy((-85).dp),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(start = 35.dp, 8.dp),
                modifier = Modifier
                    .weight(1.2f)
            ) {
                items(viewModel.getCartasJugador(2)) { card ->
                    ElementoCartaJ2(carta = card, viewModel)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Jugador2(
                    viewModel,
                    plantarJugador2,
                    turnoJugador
                )
            }
        }
    }
}

/**
 * Función composable que representa un objeto de tipo carta en la mano del jugador.
 *
 * @param carta La carta que se va a mostrar
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack
 */
@Composable
fun ElementoCartaJ1(
    carta: Carta,
    viewModel: BlackJackMultiViewModel
) {
    if (viewModel.mostrarCartasJ1.value == true) {
        Image(
            modifier = Modifier
                .height(220.dp)
                .padding(vertical = 5.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp)),
            painter = painterResource(id = carta.idDrawable),
            contentDescription = "${carta.nombre} de ${carta.palo}"
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.reverso),
            contentDescription = "reverso carta",
            modifier = Modifier
                .height(220.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

/**
 * Función composable que representa un objeto de tipo carta en la mano del jugador.
 *
 * @param carta La carta que se va a mostrar
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack
 */
@Composable
fun ElementoCartaJ2(
    carta: Carta,
    viewModel: BlackJackMultiViewModel
) {
    if (viewModel.mostrarCartasJ2.value == true) {
        Image(
            modifier = Modifier
                .height(220.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp)),
            painter = painterResource(id = carta.idDrawable),
            contentDescription = "${carta.nombre} de ${carta.palo}"
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.reverso),
            contentDescription = "reverso carta",
            modifier = Modifier
                .height(220.dp)
                .padding(vertical = 5.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

/**
 * Función que representa la interfaz de usuario del jugador 1.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param plantaJugador1 Indica si el jugador 1 ha elegido seguir o plantarse.
 * @param turnoJugador Turno del jugador actual.
 */
@Composable
fun Jugador1(
    viewModel: BlackJackMultiViewModel,
    plantaJugador1: Boolean,
    turnoJugador: Int,
) {
    if (viewModel.mostrarCartasJ1.value == true) {
        Card(
            modifier = Modifier
                .height(50.dp)
                .width(250.dp)
                .border(1.5.dp, Color.White, shape = RoundedCornerShape(40.dp))
                .clip(RoundedCornerShape(40.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black, shape = RoundedCornerShape(40.dp))
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.Transparent)
                        .height(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jugador1),
                        contentDescription = "Imagen jugador 1",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        text = viewModel.infoJugador(1),
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    BotonesJugador(
        viewModel = viewModel,
        jugadorId = 1,
        plantaJugador = plantaJugador1,
        turnoJugador = turnoJugador
    )
}

/**
 * Función que representa la interfaz de usuario del jugador 2.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param plantaJugador2 Indica si el jugador 2 ha elegido seguir o plantarse.
 * @param turnoJugador Turno del jugador actual.
 */
@Composable
fun Jugador2(
    viewModel: BlackJackMultiViewModel,
    plantaJugador2: Boolean,
    turnoJugador: Int,
) {
    Spacer(modifier = Modifier.height(10.dp))
    BotonesJugador(
        viewModel = viewModel,
        jugadorId = 2,
        plantaJugador = plantaJugador2,
        turnoJugador = turnoJugador
    )
    Spacer(modifier = Modifier.height(20.dp))
    if (viewModel.mostrarCartasJ2.value == true) {
        Card(
            modifier = Modifier
                .height(50.dp)
                .width(250.dp)
                .border(1.5.dp, Color.White, shape = RoundedCornerShape(40.dp))
                .clip(RoundedCornerShape(40.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black, shape = RoundedCornerShape(40.dp))
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.Transparent)
                        .height(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jugador2),
                        contentDescription = "Imagen jugador 2",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        text = viewModel.infoJugador(2),
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }
        }
    }
}

/**
 * Función que representa los botones comunes para un jugador.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param jugadorId El identificador del jugador.
 * @param plantaJugador Indica si el jugador ha elegido seguir o plantarse.
 * @param turnoJugador Turno del jugador actual.
 */
@Composable
fun BotonesJugador(
    viewModel: BlackJackMultiViewModel,
    jugadorId: Int,
    plantaJugador: Boolean,
    turnoJugador: Int
) {

    Row {
        Button(
            modifier = Modifier
                .padding(end = 5.dp, bottom = 5.dp)
                .height(50.dp)
                .width(120.dp),
            enabled = turnoJugador == jugadorId && !plantaJugador,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = {
                viewModel.dameCarta(jugadorId)
            }
        ) {
            Text(text = "Pide carta")
        }
        Button(
            modifier = Modifier
                .padding(end = 5.dp, bottom = 5.dp)
                .height(50.dp)
                .width(120.dp),
            enabled = turnoJugador == jugadorId && !plantaJugador,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = {
                viewModel.plantaJugador(jugadorId, plantarse = true, cambioTurno = true)
            }
        ) {
            Text(text = "Plantarse")
        }
    }
}


