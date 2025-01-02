package com.ifmg.nhamnham

import android.widget.ImageView

class Bloco(val view: ImageView) {
    var tamanhoAtual: Int = -1 // -1 significa que o bloco está vazio
    lateinit var peca: Peca

    // Verifica se a peça pode ser colocada neste bloco
    fun podeReceberPeca(peca: Peca): Boolean {
        return tamanhoAtual == -1 || peca.tamanho > tamanhoAtual // Não é possível colocar peças de tamanho igual em cima da outra
    }
}
