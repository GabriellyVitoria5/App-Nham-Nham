package com.ifmg.nhamnham

class Jogador(
    val pecas: List<Peca> = mutableListOf(),
    var vezDeJogar: Boolean
){
    // Retornar se o jogador tem peças para jogar
    fun temPecas(): Boolean {
        return pecas.isNotEmpty()
    }
}
