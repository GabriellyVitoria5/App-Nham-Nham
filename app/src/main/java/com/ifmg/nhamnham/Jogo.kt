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
    private var controleVezJogador:Boolean = true
    private var jogoContinua:Boolean = true

    // Placar dos jogadores
    private var placarJogador1 = 0
    private var placarJogador2 = 0

    @SuppressLint("ClickableViewAccessibility") // TODO Configurar o listener de arrastar as peças para um método separado
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Carregar o placar
        carregarPlacar()

        // Inflar os componentes da interface
        binding = ActivityJogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar o placar, caso exista
        placarJogador1 = intent.getIntExtra("placarJogador1", 0)  // Se não tiver, será 0
        placarJogador2 = intent.getIntExtra("placarJogador2", 0)  // Se não tiver, será 0

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

        tabuleiro = Tabluleiro(blocos)

        // Configurando o listener para as peças do jogador 1
        pecasJogador1.forEach { peca ->
            peca.imagem.setOnTouchListener { view, event ->

                if (!jogoContinua) {
                    // Bloqueia o arrasto se o jogo acabou
                    Toast.makeText(this, "O jogo terminou!", Toast.LENGTH_SHORT).show()
                    return@setOnTouchListener false
                }

                // TODO: Resolver o bug visual tira a visualização da peça ao arrastar
//                if (event.action == MotionEvent.ACTION_DOWN && controleVezJogador == jogador1.vezDeJogar) {
//                    val dragShadowBuilder = View.DragShadowBuilder(view)
//                    view.startDragAndDrop(null, dragShadowBuilder, peca, 0) // Passa a peça como dado local
//                    true
//                } else {
//                    false
//                }

                if (event.action == MotionEvent.ACTION_DOWN && controleVezJogador == jogador1.vezDeJogar) {
                    // Criar uma cópia da peça original para arrastar
                    val copyImageView = ImageView(this).apply {
                        setImageDrawable(peca.imagem.drawable) // A mesma imagem da peça
                        layoutParams = view.layoutParams // Usa as mesmas dimensões do original
                    }

                    // Definir o tamanho da cópia para caber no tabuleiro, por exemplo, redimensionando conforme o bloco
                    val size = 100 // Exemplo de tamanho redimensionado para a cópia
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
                            // Desenha a cópia da peça na sombra
                            copyImageView.draw(canvas)
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

        // Configurando o listener para as peças do jogador 2
        pecasJogador2.forEach { peca ->
            peca.imagem.setOnTouchListener { view, event ->

                if (!jogoContinua) {
                    // Bloqueia o arrasto se o jogo acabou
                    Toast.makeText(this, "O jogo terminou!", Toast.LENGTH_SHORT).show()
                    return@setOnTouchListener false
                }

                if (event.action == MotionEvent.ACTION_DOWN && controleVezJogador == jogador2.vezDeJogar) {
                    // Criar uma cópia da peça original para arrastar
                    val copyImageView = ImageView(this).apply {
                        setImageDrawable(peca.imagem.drawable) // A mesma imagem da peça
                        layoutParams = view.layoutParams // Usa as mesmas dimensões do original
                    }

                    // Definir o tamanho da cópia para caber no tabuleiro, por exemplo, redimensionando conforme o bloco
                    val size = 100 // Exemplo de tamanho redimensionado para a cópia
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
                            // Desenha a cópia da peça na sombra
                            copyImageView.draw(canvas)
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

        // TODO arrumar bug visual em que a imagem da peça fica pequena
        blocos.forEach { bloco ->
            bloco.view.setOnDragListener { view, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> true
                    DragEvent.ACTION_DROP -> {
                        val peca = event.localState as Peca

                        if (peca.quantidade > 0 && bloco.podeReceberPeca(peca)) {
                            (view as ImageView).setImageDrawable(peca.imagem.background)

                            // Decrementa a quantidade da peça usada pelo jogador
                            peca.quantidade -= 1

                            // Remove a imagem da peça se a quantidade chegar a 0
                            if (peca.quantidade == 0) {
                                peca.imagem.visibility = View.INVISIBLE
                            }

                            // Atualizar estado do bloco/posição onde a peça foi colocada
                            bloco.tamanhoAtual = peca.tamanho
                            bloco.peca = peca

                            // Alterar qual jogador devejogar
                            controleVezJogador = !controleVezJogador

                            // Atualizar texto na tela
                            atualizarContadoresDasPecas(jogador1, jogador2)
                            mostrarJogadorAtual()

                            // Verificar se jogo terminou
                            verificarFimDoJogo(jogador1, jogador2, peca)
                        }
                        else {
                            // Feedback visual ou sonoro para peça inválida
                            bloco.view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        }

                        true
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        true
                    }
                    else -> false
                }
            }
        }

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

    // Configurar botão para mover para activity Main
    private fun configurarBotaoVoltar(){
        binding.btnJogoVoltarInicio.setOnClickListener {
            // Limpar placar
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
            val intent = Intent(this, Resultado::class.java)
            startActivity(intent)
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
            jogoContinua = false
            binding.btnResultado.visibility = View.VISIBLE // Mostrar botão de resultado

            // Exibir mensagem de vitória
            val vencedor = peca.getNomeDonoDaPeca()
            Toast.makeText(this, "$vencedor venceu!", Toast.LENGTH_SHORT).show()

            // Atualizar o placar ao final do jogo
            if (peca.jogador) {
                placarJogador1++
            } else {
                placarJogador2++
            }

            // Enviar o vencedor e o placar para a tela de resultado
            val intent = Intent(this, Resultado::class.java)
            intent.putExtra("vencedor", vencedor)
            intent.putExtra("placarJogador1", placarJogador1)
            intent.putExtra("placarJogador2", placarJogador2)
            startActivity(intent)

            // Salvar o placar quando houver um vencedor
            salvarPlacar()

        } else if (tabuleiro.temEmpate(jogador1, jogador2)) {
            jogoContinua = false

            // Exibir mensagem de empate
            Toast.makeText(this, "O jogo terminou em empate!", Toast.LENGTH_SHORT).show()

            // Enviar empate para a tela de resultado
            val intent = Intent(this, Resultado::class.java)
            intent.putExtra("vencedor", "Empate")
            startActivity(intent)
        }
    }

    // Função para salvar o placar no SharedPreferences
    private fun salvarPlacar() {
        val sharedPreferences = getSharedPreferences("placar", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("placarJogador1", placarJogador1)
        editor.putInt("placarJogador2", placarJogador2)
        editor.apply()
    }

    // Função para carregar o placar do SharedPreferences
    private fun carregarPlacar() {
        val sharedPreferences = getSharedPreferences("placar", MODE_PRIVATE)
        placarJogador1 = sharedPreferences.getInt("placarJogador1", 0)
        placarJogador2 = sharedPreferences.getInt("placarJogador2", 0)
    }
}