package com.example.blackjack_jetpackcompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
    navController: NavHostController
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
        Image(
            painter = painterResource(id = R.drawable.imgbienvenido),
            contentDescription = "Bienvenido al BlackJack",
            modifier = Modifier
                .padding(top = 150.dp)
                .align(Alignment.TopCenter)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón para navegar por la pantalla del juego
            Button(
                onClick = { navController.navigate(Routes.MultiScreen.route) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(5.dp)
                    .size(width = 230.dp, height = 60.dp)
            ) {
                Text(
                    text = "Jugador vs Jugador", fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold)
            }
            // El botón para ir a la pantalla contra la IA
            Button(
                onClick = { navController.navigate(Routes.BotScreen.route) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 30.dp)
                    .size(width = 230.dp, height = 60.dp)
            ) {
                Text(
                    text = "Jugador vs Bot",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold)
            }
            // Botón para salir de programa
            Button(
                onClick = { navController.navigate(Routes.GameScreen.route) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(top = 30.dp)
                    .size(width = 230.dp, height = 60.dp)
            ) {
                Text(
                    text = "Volver atrás",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold)
            }
        }
    }
}