package com.example.blackjack_jetpackcompose.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.blackjack_jetpackcompose.R
import com.example.blackjack_jetpackcompose.data.Routes


/**
 * Función Composable que representa la pantalla para el juego de BlackJack Jugador vs Jugador
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 */
@Composable
fun PantallaMultijugadorInicial(
    navController: NavHostController,
    viewModel: ViewModel
) {
    val configJugadores: Boolean by viewModel.configJugadores.observeAsState(initial = true)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.tapete), // Reemplaza R.drawable.tu_imagen con el ID de tu imagen
            contentDescription = null, // Agrega una descripción si es necesario
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium), // Opcional: clip para redondear las esquinas
            contentScale = ContentScale.Crop
        )
        ConfigJugadores(
            navController,
            viewModel,
            configJugadores
        ) { viewModel.onDismissConfigDialog() }
    }

}

/**
 * Función composable que representa el cuadro de diálogo de configuración para establecer los nicks de los jugadores.
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 * @param viewModel El ViewModel responsable de gestionar la lógica del juego de Blackjack.
 * @param configJugadores Flag indicating whether the configuration dialog is visible.
 * @param onDissmiss Callback function for dismissing the dialog.
 */
@Composable
fun ConfigJugadores(
    navController: NavHostController,
    viewModel: ViewModel,
    configJugadores: Boolean,
    onDissmiss: () -> Unit
) {
    val nickNameJugador1: String by viewModel.nickNameJugador1.observeAsState(initial = "")
    val nickNameJugador2: String by viewModel.nickNameJugador2.observeAsState(initial = "")

    if (configJugadores) {
        Dialog(
            onDismissRequest = { onDissmiss() },
            properties = DialogProperties(dismissOnClickOutside = false),
            content = {
                // Contenido del diálogo con esquinas redondeadas
                Column(
                    modifier = Modifier
                        .background(
                            Color.DarkGray,
                            MaterialTheme.shapes.medium // Ajusta la forma del recorte para redondear las esquinas
                        )
                        .padding(16.dp)
                ) {
                    TituloDialogo(text = "Configuración del juego")
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
 * Función composable que representa la composición del título.
 *
 * @param text El texto que se mostrará como título.
 */
@Composable
fun TituloDialogo(text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
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
    viewModel: ViewModel,
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
                contentDescription = "Jugador 1",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .size(40.dp)
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
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(8.dp),
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
fun Botones(
    navController: NavHostController,
    viewModel: ViewModel
) {
    val btnAceptar: Boolean by viewModel.btnAceptar.observeAsState(initial = false)

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
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
            modifier = Modifier.padding(start = 4.dp),
            onClick = { navController.navigate(Routes.GameScreen.route) }
        ) {
            Text(text = "Cancelar")
        }
    }
}