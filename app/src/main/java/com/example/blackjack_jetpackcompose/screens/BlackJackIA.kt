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
import com.example.blackjack_jetpackcompose.data.Carta
import com.example.blackjack_jetpackcompose.data.Routes

/**
 * Función Composable que representa la pantalla para el juego de BlackJack Jugador vs Jugador
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 */
@Composable
fun PantallavsBotInicial(
    navController: NavHostController,
    viewModel: BlackJackIAViewModel
) {
    val configJugadores: Boolean by viewModel.configJugadores.observeAsState(initial = true)
    val showEndGameDialog: Boolean by viewModel.gameOverDialog.observeAsState(initial = false)
    val showPuntuaciones: Boolean by viewModel.puntuacionesDialog.observeAsState(initial = false)
    val actualizacionCartas: Boolean by viewModel.actualizarCartas.observeAsState(initial = false)
    val actualizacionPuntuaciones: Boolean by viewModel.actualizarPuntuaciones.observeAsState(initial = false)

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
        ConfigJugador(
            navController,
            viewModel,
            configJugadores
        ) { viewModel.onDismissConfigDialog() }

        EndGameDlog(
            navController,
            viewModel,
            showEndGameDialog
        )
        PuntuacionesDlog(
            viewModel,
            navController,
            showPuntuaciones
        )
        BotLayout(
            viewModel,
            configJugadores,
            actualizacionCartas,
            actualizacionPuntuaciones
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
fun ConfigJugador(
    navController: NavHostController,
    viewModel: BlackJackIAViewModel,
    showConfiguracionDialog: Boolean,
    onDissmiss: () -> Unit
) {
    val nickNameJugador1: String by viewModel.nickName.observeAsState(initial = "")

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
                    ElementNick(
                        viewModel,
                        drawable = R.drawable.jugador1,
                        nickNameJugador1
                    )
                    Botons(
                        navController,
                        viewModel
                    )
                }
            }
        )
    }
}

/**
 * Función que representa un elemento para ingresar el nick del jugador.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param drawable El ID de recurso de la imagen del jugador.
 * @param nickNamePlayer El nick actual del jugador o jugadora.
 */
@Composable
fun ElementNick(
    viewModel: BlackJackIAViewModel,
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
            Nick(
                value = nickNamePlayer,
                onValueChange = { viewModel.onNickNameChange(it) }
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
fun Nick(
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
fun Botons(
    navController: NavHostController,
    viewModel: BlackJackIAViewModel
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
fun EndGameDlog(
    navController: NavHostController,
    viewModel: BlackJackIAViewModel,
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

                TituloDlog(
                    text = viewModel.getGanador(),
                    viewModel
                )
                BotonesEndGameDlog(
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
fun TituloDlog(
    text: String,
    viewModel: BlackJackIAViewModel
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
fun BotonesEndGameDlog(
    viewModel: BlackJackIAViewModel,
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
fun PuntuacionesDlog(
    viewModel: BlackJackIAViewModel,
    navController: NavHostController,
    showPuntuaciones: Boolean
) {
    if (showPuntuaciones) {
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
 * @param actualizacionPuntuaciones Indica si las puntuaciones deben actualizarse.
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun BotLayout(
    viewModel: BlackJackIAViewModel,
    configJugadores: Boolean,
    actualizacionCartasJugador: Boolean,
    actualizacionPuntuaciones: Boolean
) {

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
                Jugador(
                    viewModel
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
                //Visualización de sus cartas del jugador
                items(viewModel.getCartasJugador(1)) { card ->
                    ElementoCartaJg(carta = card)
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
                    ElementoCartaIA(carta = card, viewModel)
                }
            }
        }
    }
}

/**
 * Función composable que representa un objeto de tipo carta en la mano del jugador.
 *
 * @param carta La carta que se va a mostrar
 */
@Composable
fun ElementoCartaJg(
    carta: Carta
) {
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
}

/**
 * Función composable que representa un objeto de tipo carta en la mano del jugador.
 *
 * @param carta La carta que se va a mostrar
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack
 */
@Composable
fun ElementoCartaIA(
    carta: Carta,
    viewModel: BlackJackIAViewModel
) {
    if (viewModel.mostrarCartasIA.value == true) {
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
 * Función que representa la interfaz de usuario del jugador.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 */
@Composable
fun Jugador(
    viewModel: BlackJackIAViewModel
) {
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
                    text = viewModel.infoJugador(),
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

    Spacer(modifier = Modifier.height(20.dp))
    BotonesJg(
        viewModel = viewModel,
        jugadorId = 1
    )
}


/**
 * Función que representa los botones comunes para un jugador.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param jugadorId El identificador del jugador.
 */
@Composable
fun BotonesJg(
    viewModel: BlackJackIAViewModel,
    jugadorId: Int
) {
    val puntuacionBot: Int by viewModel.puntuacionIA.observeAsState(initial = 0)

    Row {
        Button(
            modifier = Modifier
                .padding(end = 5.dp, bottom = 5.dp)
                .height(50.dp)
                .width(120.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = {
                viewModel.dameCarta(jugadorId)
                Thread.sleep(1000)
                if (puntuacionBot < 18)
                    viewModel.dameCarta(2)
                else
                    viewModel.plantaJugador(2, plantarse = true)
            }
        ) {
            Text(text = "Pide carta")
        }
        Button(
            modifier = Modifier
                .padding(end = 5.dp, bottom = 5.dp)
                .height(50.dp)
                .width(120.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = {
                viewModel.plantaJugador(jugadorId, plantarse = true)
                Thread.sleep(1000)
                if (puntuacionBot < 18)
                    viewModel.dameCarta(2)
                viewModel.plantaJugador(2, plantarse = true)
            }
        ) {
            Text(text = "Plantarse")
        }
    }
}