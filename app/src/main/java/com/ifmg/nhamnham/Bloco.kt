package com.ifmg.nhamnham

import android.widget.ImageView

class Bloco(val view: ImageView) {
    var tamanhoAtual: Int = -1 // null significa que o bloco está vazio
    lateinit var peca: Peca

    // Verifica se a peça pode ser colocada neste bloco
    fun podeColocar(peca: Peca): Boolean {
        return tamanhoAtual == -1 || peca.tamanho > tamanhoAtual
    }

    // Limpa o bloco (usado, por exemplo, para reiniciar o jogo)
    fun limpar() {
        view.setImageDrawable(null)
        tamanhoAtual = -1
    }
}
