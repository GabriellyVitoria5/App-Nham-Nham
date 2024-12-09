package com.ifmg.nhamnham

class Jogador(
    val primeiroAJogar: Boolean, /* jogador 1 = true | jogador 2 = false */
    val pecas: List<Peca> = mutableListOf()
){

    private fun temPecas(): Boolean {
        return pecas.isNotEmpty()
    }
}