package com.ifmg.nhamnham

class Jogador(
    val nome: String, /* jogador 1 = true | jogador 2 = false */
    val pecas: List<Peca> = mutableListOf(),
    var vezDeJogar: Boolean
){
    private fun temPecas(): Boolean {
        return pecas.isNotEmpty()
    }
}
