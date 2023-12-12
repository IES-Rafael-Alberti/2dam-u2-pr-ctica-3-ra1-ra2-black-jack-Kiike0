package com.example.blackjack_jetpackcompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.blackjack_jetpackcompose.R
import com.example.blackjack_jetpackcompose.data.Routes

/**
 * Función composable que representa la pantalla principal para seleccionar el tipo de juego.
 *
 * @param navController El controlador de navegación utilizado para navegar en las diferentes pantallas.
 */
@Composable
fun PantallaJuego(
    navController: NavHostController,
    viewModel: MenuViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.tapete),
            contentDescription = "Tapete del juego",
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.imgbienvenido),
                contentDescription = "Bienvenido al BlackJack",

            )

            // Botón para navegar por la pantalla del juego
            Button(
                onClick = { navController.navigate(Routes.MultiScreen.route) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(240.dp)
                    .height(80.dp)
            ) {
                Text(text = "Jugador vs Jugador", fontSize = 20.sp)
            }

            // Ell botón para ir a la opción contra la IA
            Button(
                onClick = { navController.navigate(Routes.BotScreen.route) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(240.dp)
                    .height(80.dp)
                    .padding(top = 15.dp)
            ) {
                Text(text = "Jugador vs Bot", fontSize = 20.sp)
            }

            // Botón para salir de programa
            Button(
                onClick = { viewModel.cerrarPrograma() },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(240.dp)
                    .height(80.dp)
                    .padding(top = 15.dp)
            ) {
                Text(text = "Salir", fontSize = 20.sp)
            }

        }
    }

}