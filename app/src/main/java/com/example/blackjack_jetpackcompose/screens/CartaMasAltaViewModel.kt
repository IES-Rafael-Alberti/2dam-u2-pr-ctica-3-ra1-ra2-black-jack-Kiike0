package com.example.blackjack_jetpackcompose.screens

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class CartaMasAltaViewModel (application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext


}