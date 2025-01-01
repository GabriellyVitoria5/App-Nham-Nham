package com.ifmg.nhamnham

import android.widget.ImageView

class Peca (
    val tamanho: Int, /* Pequeno = 0 | Médio = 1 | Grande = 2 */
    var quantidade: Int,
    val imagem: ImageView,
    val jogador: Boolean /* true = Jogador 1 | false = Jogador 2*/
) {

    // Pegar o nome do jogador com base na sua peça
    fun getNomeDonoDaPeca(): String{
        return if (jogador) "Jogador 1" else "Jogador 2"
    }
}