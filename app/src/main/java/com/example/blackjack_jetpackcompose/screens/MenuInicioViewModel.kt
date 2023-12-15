@file:Suppress("SpellCheckingInspection")
package com.example.blackjack_jetpackcompose.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


/**
 * ViewModel para el Menu de juego.
 * Esta clase gestiona la lógica del juego para gestionar la salida del programa.
 *
 * @param application El contexto de la aplicación utilizado para inicializar ViewModel.
 */
class MenuInicioViewModel(application: Application) : AndroidViewModel(application) {

    private val _cerrarPrograma = MutableLiveData<Boolean>()
    val cerrarPrograma: LiveData<Boolean> = _cerrarPrograma

    fun cerrarPrograma(){
        _cerrarPrograma.value = true
    }
}