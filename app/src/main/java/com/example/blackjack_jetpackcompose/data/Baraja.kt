package com.example.blackjack_jetpackcompose.data

import android.content.Context
import com.example.blackjack_jetpackcompose.R

class Baraja {
    companion object {
        private var listaCartas: ArrayList<Carta> = arrayListOf()
        /*init { //Cuando se crea el objeto llama a estos métodos para que se inicialize el funcionamiento
            crearBaraja()
            barajar()
        }*/

        /**
         * Crea la baraja a partir de la lista añadiendo las cartas una a una
         * @param context El contecto de la aplicación
         */
        fun crearBaraja(context: Context) {
            listaCartas.clear()
            var puntosMin: Int
            var puntosMax: Int

            for (palo in 1..4) {
                for (cont in 1..13) {
                    when (cont) {
                        1 -> {
                            puntosMin = 1
                            puntosMax = 11
                        }
                        11, 12, 13 -> {
                            puntosMin = 10
                            puntosMax = 10
                        }
                        else -> {
                            puntosMin = cont
                            puntosMax = cont
                        }
                    }

                    listaCartas.add(
                        Carta(
                            Naipes.values()[cont],
                            Palos.values()[palo],
                            puntosMin,
                            puntosMax,
                            getIdDrawable(
                                context,
                                "${Palos.values()[palo].toString().lowercase()}_${cont}"
                            )
                        )
                    )
                }
            }
        }

        /**
         * Baraja las cartas automáticamente con shuffle()
         */
        fun barajar() {
            listaCartas.shuffle()
        }

        /**
         * Enseña la siguiente carta de la lista y la anterior la borra de la lista
         * Con este método van quedando menos cartas y no repite la carta que se enseñó
         * anteriormente
         *
         * @return regresa la carta correspondiente
         */
        fun dameCarta(): Carta {
            val carta = listaCartas.last()
            listaCartas.removeLast()
            return carta
        }

        /**
         * Carta bocaabajo
         *
         * @return devuelve la carta reverso.
         */
        fun cartaBocaabajo(): Carta {
            return(Carta(Naipes.NINGUNA, Palos.NINGUNA, 0, 0, R.drawable.reverso))
        }

        /**
         * Recupera el recurso ID del elemento robable de una carta según su nombre.
         *
         * @param context El contexto de la aplicación
         * @param nombreCarta El nombre de la carta
         * @return El recurso ID de la carta robable
         */
        private fun getIdDrawable(context: Context, nombreCarta: String) =
            context.resources.getIdentifier(
                nombreCarta,
                "drawable",
                context.packageName)
    }
}