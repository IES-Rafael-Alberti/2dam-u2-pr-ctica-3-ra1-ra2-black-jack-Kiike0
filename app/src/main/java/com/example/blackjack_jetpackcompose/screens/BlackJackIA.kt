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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
 * Función Composable que representa la pantalla para el juego de BlackJack Jugador vs Máquina
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
    val actualizacionCartas: Boolean by viewModel.actualizarCartas.observeAsState(initial = false)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.tapete),
            contentDescription = "carta mostrada",
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

        EndGameDlg(
            navController,
            viewModel,
            showEndGameDialog
        )

        BotLayout(
            viewModel,
            configJugadores,
            actualizacionCartas
        )


    }

}

/**
 * Función composable que representa el cuadro de diálogo de configuración para establecer el nick del jugador.
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param configJugador Flag indicating whether the configuration dialog is visible.
 * @param onDissmiss Callback function for dismissing the dialog.
 */
@Composable
fun ConfigJugador(
    navController: NavHostController,
    viewModel: BlackJackIAViewModel,
    configJugador: Boolean,
    onDissmiss: () -> Unit
) {
    val nickName: String by viewModel.nickName.observeAsState(initial = "")

    if (configJugador) {
        Dialog(
            onDismissRequest = { onDissmiss() },
            properties = DialogProperties(dismissOnClickOutside = false),
            content = {
                // Contenido del diálogo
                Column(
                    modifier = Modifier
                        .background(
                            Color.DarkGray,
                            MaterialTheme.shapes.medium
                        )
                        .padding(18.dp)
                ) {
                    TituloDlg(
                        text = "Configuración del juego",
                        viewModel
                    )
                    ElementNick(
                        viewModel,
                        idJugador = 1,
                        drawable = R.drawable.jugador1,
                        nickName
                    )
                    Btns(
                        navController,
                        viewModel
                    )
                }
            }
        )
    }


}

/**
 * Función composable que representa la composición de un título.
 *
 * @param text El texto que se mostrará como título.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 */
@Composable
fun TituloDlg(
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
                fontSize = 15.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
        } else {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )

        }

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
fun ElementNick(
    viewModel: BlackJackIAViewModel,
    idJugador: Int,
    @DrawableRes drawable: Int,
    nickNamePlayer: String
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = drawable),
                contentDescription = "Imagen Jugador",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(all = 10.dp)
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "Jugador $idJugador",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Nick(
                value = nickNamePlayer,
                onValueChange = { viewModel.onNickNameChange(it) }
            )
        }
    }
}

/**
 * Función que representa el recuadro para rellenar el nick que elijamos
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
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(10.dp),
            label = { Text(text = "Introduce tu nick") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.Blue,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.Red
            )
        )
    }
}

/**
 * Función composable que representa los botones aceptar o cancelar de la configuración en una fila.
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 */
@Composable
fun Btns(
    navController: NavHostController,
    viewModel: BlackJackIAViewModel
) {
    val btnAceptar: Boolean by viewModel.btnAceptar.observeAsState(initial = false)

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp)
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
 * Función que representa el diálogo que se muestra al final del juego del BlackJack.
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param showEndGameDialog Indica si el diálogo de finalización del juego es visible.
 */
@Composable
fun EndGameDlg(
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
                TituloDlg(
                    text = viewModel.getGanador(),
                    viewModel
                )
                BtnEndGameDialog(
                    viewModel,
                    navController
                )
            }
        }
    }
}

/**
 * Función que representa los botones para el diálogo de fin de juego.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 */
@Composable
fun BtnEndGameDialog(viewModel: BlackJackIAViewModel, navController: NavHostController) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
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
                navController.navigate(Routes.GameScreen.route)
                viewModel.finalizarJuego()
            }
        ) {
            Text(text = "Cerrar")
        }
    }
}

/**
 * Función composable que representa el diseño del Jugador vs IA del BlackJack.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param configJugador Indica si el cuadro de diálogo de configuración está visible.
 * @param actualizacionCartasJugador Indica si las tarjetas de jugador deben actualizarse.
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun BotLayout(
    viewModel: BlackJackIAViewModel,
    configJugador: Boolean,
    actualizacionCartasJugador: Boolean
) {
    val plantarJugador: Boolean by viewModel.plantarJugador.observeAsState(initial = false)
    val plantarJugadorIA: Boolean by viewModel.plantarJugadorIA.observeAsState(initial = false)
    val turnoJugador: Int by viewModel.cambioTurno.observeAsState(initial = 1)

    if (!configJugador) {
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
                Player(
                    viewModel,
                    plantarJugador,
                    turnoJugador
                )
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy((-100).dp),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier
                    .weight(3f)
            ) {
                //Visualización de sus cartas de cada jugador
                items(viewModel.getCartasJugador(1)) { card ->
                    ElementoCartaJg(carta = card)
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy((-100).dp),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier
                    .weight(3f)
            ) {
                items(viewModel.getCartasJugador(2)) { card ->
                    ElementoCartaIA(carta = card, viewModel)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                IAPlayer(
                    viewModel,
                    plantarJugadorIA,
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
 */
@Composable
fun ElementoCartaJg(
    carta: Carta
) {
    Image(
        modifier = Modifier
            .height(240.dp)
            .padding(vertical = 10.dp)
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
                .height(240.dp)
                .padding(vertical = 10.dp)
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
                .height(240.dp)
                .padding(vertical = 10.dp)
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
 * @param plantaJugador Indica si el jugador 1 ha elegido seguir o plantarse.
 * @param turnoJugador Turno del jugador actual.
 */
@Composable
fun Player(
    viewModel: BlackJackIAViewModel,
    plantaJugador: Boolean,
    turnoJugador: Int
) {

    Text(
        modifier = Modifier.padding(bottom = 10.dp),
        text = viewModel.infoJugador(1),
        style = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
    )

    BtnJugador(
        viewModel = viewModel,
        jugadorId = 1,
        plantaJugador = plantaJugador,
        turnoJugador = turnoJugador
    )
}

/**
 * Función que representa la interfaz de usuario de la Máquina.
 *
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param plantaJugadorIA Indica si el bot ha elegido seguir o plantarse.
 * @param turnoJugador Turno del jugador actual.
 */
@Composable
fun IAPlayer(
    viewModel: BlackJackIAViewModel,
    plantaJugadorIA: Boolean,
    turnoJugador: Int
) {
    if (viewModel.mostrarCartasIA.value == true) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = viewModel.infoJugador(2),
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        )
    }

    //Implementar aquí lo que hace la máquina
    //Por ejemplo if lo que sea pide carta else se planta
    //viewModel.dameCarta(idJugador = 3) //El id del bot podría ser 3 por ejemplo
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
fun BtnJugador(
    viewModel: BlackJackIAViewModel,
    jugadorId: Int,
    plantaJugador: Boolean,
    turnoJugador: Int
) {

    Row {
        Button(
            modifier = Modifier
                .padding(end = 5.dp)
                .height(50.dp)
                .width(120.dp),
            enabled = turnoJugador == jugadorId && !plantaJugador,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                viewModel.dameCarta(jugadorId)
            }
        ) {
            Text(text = "Carta")
        }
        Button(
            modifier = Modifier
                .height(50.dp)
                .width(120.dp),
            enabled = turnoJugador == jugadorId && !plantaJugador,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                viewModel.plantaJugador(plantarse = true, cambioTurno = true)
            }
        ) {
            Text(text = "Plantarse")
        }
    }

}