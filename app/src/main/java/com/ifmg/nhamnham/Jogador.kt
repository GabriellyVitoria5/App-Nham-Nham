package com.ifmg.nhamnham

class Jogador(
    val pecas: List<Peca> = mutableListOf(),
    var vezDeJogar: Boolean
){
    private fun temPecas(): Boolean {
        return pecas.isNotEmpty()
    }
}
