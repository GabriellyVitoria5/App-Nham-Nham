package com.ifmg.nhamnham

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.nhamnham.databinding.ActivityJogoBinding

class Jogo : AppCompatActivity() {

    private lateinit var binding: ActivityJogoBinding
    private lateinit var tabuleiro: Tabluleiro
    private var controleVezJogador:Boolean = true // Controlar quando cada jogador
    private var jogoContinua:Boolean = true // Controle para finalizar o jogo

    private var placarJogador1 = 0
    private var placarJogador2 = 0

    private lateinit var resultadoJogo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Placar começa com 0 ao iniciar o aplicativo
        carregarPlacar()

        // Inflar os componentes da interface
        binding = ActivityJogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar o placar caso exista, senão será 0
        placarJogador1 = intent.getIntExtra("placarJogador1", placarJogador1)
        placarJogador2 = intent.getIntExtra("placarJogador2", placarJogador2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Lista com as 9 peças de cada jogador, cada imagem no layout se tornará 3 peças de cada tamanho
        val pecasJogador1 = criarPecas(
            binding.jogador1PecaP,
            binding.jogador1PecaM,
            binding.jogador1PecaG,
            true
        )
        val pecasJogador2 = criarPecas(
            binding.jogador2PecaP,
            binding.jogador2PecaM,
            binding.jogador2PecaG,
            false
        )

        // Instanciando 2 jogadores para jogar o jogo
        val jogador1 = Jogador(pecasJogador1, true)
        val jogador2 = Jogador(pecasJogador2,false)

        // Armazenando os 9 espaços possíveis para colocar as peças
        val blocos = arrayOf(
            Bloco(binding.bloco1),
            Bloco(binding.bloco2),
            Bloco(binding.bloco3),
            Bloco(binding.bloco4),
            Bloco(binding.bloco5),
            Bloco(binding.bloco6),
            Bloco(binding.bloco7),
            Bloco(binding.bloco8),
            Bloco(binding.bloco9),
        )

        // Representa o tabuleiro 3x3 do jogo
        tabuleiro = Tabluleiro(blocos)

        // Configurar listeners que permitem arrastar e soltar uma peça em um dos espaços disponíveis do tabuleiro
        configurarListenersDeArrastar(pecasJogador1, jogador1)
        configurarListenersDeArrastar(pecasJogador2, jogador2)
        configurarListenersDeSoltar(blocos, jogador1, jogador2)

        // Configurar botões de troca de telas
        configurarBotaoVoltar()
        configurarBotaoResultado()
    }

    // Criar uma lista com as 9 peças de um jogador: 3 pequenas, 3 médias e 3 grandes
    private fun criarPecas(pecaPequena: ImageView, pecaMedia: ImageView, pecaGrande: ImageView, donoDaPeca: Boolean) : List<Peca>{
        return listOf(
            Peca(0, 3, pecaPequena, donoDaPeca),
            Peca(1, 3, pecaMedia, donoDaPeca),
            Peca(2, 3, pecaGrande, donoDaPeca)
        )
    }

    // Configurar movimento de arrastar peças dos jogadores
    @SuppressLint("ClickableViewAccessibility")
    private fun configurarListenersDeArrastar(pecas: List<Peca>, jogador: Jogador) {
        pecas.forEach { peca ->
            peca.imagem.setOnTouchListener { view, event ->

                // Bloqueia o arrasto se o jogo acabou
                if (!jogoContinua) {
                    Toast.makeText(this, "O jogo terminou!", Toast.LENGTH_SHORT).show()
                    return@setOnTouchListener false
                }

                if (event.action == MotionEvent.ACTION_DOWN && controleVezJogador == jogador.vezDeJogar) {

                    // Criar uma cópia da peça original para arrastar
                    val copyImageView = ImageView(this).apply {
                        setImageDrawable(peca.imagem.background) // A mesma imagem da peça
                        layoutParams = view.layoutParams // Dimensões do original
                    }

                    // Definir o tamanho da cópia para caber no tabuleiro, por exemplo, redimensionando conforme o bloco
                    val size = peca.imagem.height // Peça é um quadrado, então só precisa de uma de suas dimensões
                    copyImageView.layoutParams = ViewGroup.LayoutParams(size, size)

                    // Criar a sombra personalizada para o arrasto
                    val dragShadowBuilder = object : View.DragShadowBuilder(view) {
                        override fun onProvideShadowMetrics(outShadowSize: android.graphics.Point, outShadowTouchPoint: android.graphics.Point) {
                            val width = size
                            val height = size
                            outShadowSize.set(width, height)
                            outShadowTouchPoint.set(width / 2, height / 2)
                        }

                        override fun onDrawShadow(canvas: android.graphics.Canvas) {
                            copyImageView.draw(canvas) // Desenha a cópia da peça na sombra
                        }
                    }

                    // Iniciar o arrasto com a cópia da peça, não alterando o original
                    view.startDragAndDrop(null, dragShadowBuilder, peca, 0)
                    true
                } else {
                    false
                }
            }
        }
    }

    // Configurar blocos para receber uma peça
    private fun configurarListenersDeSoltar(blocos: Array<Bloco>, jogador1: Jogador, jogador2: Jogador) {
        blocos.forEach { bloco ->
            bloco.view.setOnDragListener { view, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> true
                    DragEvent.ACTION_DROP -> {
                        val peca = event.localState as Peca

                        // Peça pode ser colocada nesse bloco
                        if (peca.quantidade > 0 && bloco.podeReceberPeca(peca)) {
                            (view as ImageView).setImageDrawable(peca.imagem.background)

                            peca.quantidade -= 1

                            // Remove a imagem da peça se a quantidade chegar a 0
                            if (peca.quantidade == 0) {
                                peca.imagem.visibility = View.INVISIBLE
                            }

                            // Atualizar estado do bloco/posição onde a peça foi colocada
                            bloco.tamanhoAtual = peca.tamanho
                            bloco.peca = peca

                            // Alterar qual jogador deve jogar
                            controleVezJogador = !controleVezJogador

                            // Atualizar texto na tela
                            atualizarContadoresDasPecas(jogador1, jogador2)
                            mostrarJogadorAtual()

                            // Verificar se jogo terminou
                            verificarFimDoJogo(jogador1, jogador2, peca)
                        } else {
                            // Feedback visual ou sonoro para peça inválida
                            bloco.view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        }

                        true
                    }
                    DragEvent.ACTION_DRAG_ENDED -> true
                    else -> false
                }
            }
        }
    }

    // Atualiza os contadores de peças restantes de cada jogador
    private fun atualizarContadoresDasPecas(jogador1: Jogador, jogador2: Jogador) {
        val pecasRestantesJ1 = jogador1.pecas.groupBy { it.tamanho }.mapValues { entry ->
            entry.value.sumOf { it.quantidade }
        }
        val pecasRestantesJ2 = jogador2.pecas.groupBy { it.tamanho }.mapValues { entry ->
            entry.value.sumOf { it.quantidade }
        }

        binding.jogador1QuantPecaP.text = "${pecasRestantesJ1[0]}"
        binding.jogador1QuantPecaM.text = "${pecasRestantesJ1[1]}"
        binding.jogador1QuantPecaG.text = "${pecasRestantesJ1[2]}"

        binding.jogador2QuantPecaP.text = "${pecasRestantesJ2[0]}"
        binding.jogador2QuantPecaM.text = "${pecasRestantesJ2[1]}"
        binding.jogador2QuantPecaG.text = "${pecasRestantesJ2[2]}"
    }

    // Mostrar qual jogador deve jogar a seguir
    @SuppressLint("SetTextI18n")
    private fun mostrarJogadorAtual(){
        if (controleVezJogador){
            binding.txtVezJogador.text = "Vez do Jogador 1"
        }
        else{
            binding.txtVezJogador.text = "Vez do Jogador 2"
        }
    }

    // Finalizar o jogo se houver vitória ou empate e atualizar o placar
    private fun verificarFimDoJogo(jogador1: Jogador, jogador2: Jogador, peca: Peca){
        if (tabuleiro.temVencedor()) {
            jogoContinua = false // Jogadores não podem mais fazer movimentos
            binding.btnResultado.visibility = View.VISIBLE // Mostrar botão de resultado
            binding.txtVezJogador.text = ""

            // Exibir mensagem de vitória
            val vencedor = peca.getNomeDonoDaPeca()
            Toast.makeText(this, "$vencedor venceu!", Toast.LENGTH_SHORT).show()

            // Atualizar o placar ao final do jogo
            if (peca.jogador) {
                placarJogador1++
            } else {
                placarJogador2++
            }

            resultadoJogo = vencedor

            // Salvar o placar quando houver um vencedor
            salvarPlacar()

        } else if (tabuleiro.temEmpate(jogador1, jogador2)) {
            jogoContinua = false // Jogadores não podem mais fazer movimentos
            binding.btnResultado.visibility = View.VISIBLE // Mostrar botão de resultado
            resultadoJogo = "Empate"
            binding.txtVezJogador.text = ""

            // Exibir mensagem de empate
            Toast.makeText(this, "O jogo terminou em empate!", Toast.LENGTH_SHORT).show()
        }
    }

    // Salvar o placar no SharedPreferences
    private fun salvarPlacar() {
        val sharedPreferences = getSharedPreferences("placar", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("placarJogador1", placarJogador1)
        editor.putInt("placarJogador2", placarJogador2)
        editor.apply()
    }

    // Carregar o placar do SharedPreferences
    private fun carregarPlacar() {
        val sharedPreferences = getSharedPreferences("placar", MODE_PRIVATE)
        placarJogador1 = sharedPreferences.getInt("placarJogador1", 0)
        placarJogador2 = sharedPreferences.getInt("placarJogador2", 0)
    }

    // Configurar botão para mover para activity Main
    private fun configurarBotaoVoltar(){
        binding.btnJogoVoltarInicio.setOnClickListener {

            // Limpar placar ao voltar para a tela inicial
            val sharedPreferences = getSharedPreferences("placar", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("placarJogador1", 0)
            editor.putInt("placarJogador2", 0)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Configurar botão para mover para activity de Resultado
    private fun configurarBotaoResultado(){
        binding.btnResultado.setOnClickListener {

            // Salva o placar e o resultado do jogo para ser mostrado na tela de resultado
            val intent = Intent(this, Resultado::class.java)
            intent.putExtra("vencedor", resultadoJogo)
            intent.putExtra("placarJogador1", placarJogador1)
            intent.putExtra("placarJogador2", placarJogador2)
            startActivity(intent)
        }
    }
}