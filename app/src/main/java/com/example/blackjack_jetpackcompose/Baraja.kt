package com.example.blackjack_jetpackcompose

class Baraja {

    companion object {
        private var listaCartas: ArrayList<Carta> = arrayListOf()
        private var tamano = 0
        init { //Cuando se crea el objeto llama a estos métodos para que se inicialize el funcionamiento
            crearBaraja()
            barajar()
        }

        /**
         * Crea la baraja a partir de la lista añadiendo las cartas una a una
         * también cambiamos la variable de tamano de la baraja
         */
        fun crearBaraja() {
            for (palo in Palos.values()) {
                for (naipe in Naipes.values()) {
                    listaCartas.add(Carta(naipe, palo, 0, 0, 0))
                }
            }
            tamano = listaCartas.size
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
         */
        fun dameCarta(): Carta? {
            if (listaCartas.size>0) {
                val lastCard = listaCartas.last()
                listaCartas.remove(lastCard)
                tamano = listaCartas.size
                return lastCard
            }
            return null

        }
    }
}