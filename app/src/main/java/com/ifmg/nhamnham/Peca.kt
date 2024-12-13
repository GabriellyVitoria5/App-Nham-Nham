package com.ifmg.nhamnham

import android.widget.ImageView

class Peca (
    val tamanho: Int, /* Pequeno = 0 | Médio = 1 | Grande = 2 */
    var quantidade: Int,
    val imagem: ImageView,
    val jogador: Boolean /* true = jogador 1 | false = jogador 2*/
) {
    var posicaoAtual: Pair<Int, Int>? = null // Armazena a posição no tabuleiro (linha, coluna)
}