package com.ifmg.nhamnham

class Tabluleiro (private val blocos: Array<Bloco>) {

    // Verificar se há um vencedor de acordo com as condições de vitória do jogo da velha
    fun temVencedor(): Boolean {

        // Posições vencedoras em um array
        val combinacoesVencedoras  = arrayOf(
            arrayOf(0, 1, 2), // Linha 1
            arrayOf(3, 4, 5), // Linha 2
            arrayOf(6, 7, 8), // Linha 3
            arrayOf(0, 3, 6), // Coluna 1
            arrayOf(1, 4, 7), // Coluna 2
            arrayOf(2, 5, 8), // Coluna 3
            arrayOf(0, 4, 8), // Diagonal principal
            arrayOf(2, 4, 6)  // Diagonal secundária
        )

        for (combinacao in combinacoesVencedoras) {
            val (a, b, c) = combinacao

            // Verifica se os blocos (posições) não estão vazios e se as peças nos blocos são do mesmo jogador
            if (
                blocos[a].tamanhoAtual != -1 &&
                blocos[b].tamanhoAtual != -1 &&
                blocos[c].tamanhoAtual != -1 &&
                blocos[a].peca.jogador == blocos[b].peca.jogador &&
                blocos[a].peca.jogador == blocos[c].peca.jogador
            ) {
                return true
            }
        }
        return false // Empate
    }

    // Verificar se jogo terminou em empate: jogadores estão sem peças ou as peças restantes não podem são menores do que as peças do tabuleiro
    fun temEmpate(jogador1: Jogador, jogador2: Jogador): Boolean {

        // Verificar se ambos os jogadores não podem fazer jogadas válidas
        val jogador1SemJogadas = jogador1.pecas.none { peca ->
            peca.quantidade > 0 && blocos.any { bloco -> bloco.podeReceberPeca(peca) }
        }
        val jogador2SemJogadas = jogador2.pecas.none { peca ->
            peca.quantidade > 0 && blocos.any { bloco -> bloco.podeReceberPeca(peca) }
        }

        return (!jogador1.temPecas() && !jogador2.temPecas()) || (jogador1SemJogadas && jogador2SemJogadas)
    }
}