package com.ifmg.nhamnham

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.nhamnham.databinding.ActivityJogoBinding

class Jogo : AppCompatActivity() {

    private lateinit var binding: ActivityJogoBinding
    private var controleVezJogador:Boolean = true

    @SuppressLint("ClickableViewAccessibility") // TODO Configurar o listener de arrastar as peças para um método separado
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar os componentes da interface
        binding = ActivityJogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Lista com as 9 peças de cada jogador, cada imagem no layout se tornará 3 peças de cada tamanho
        val pecasJogador1 = criarPecas(
            binding.jogador1PecaPequena,
            binding.jogador1PecaMedia,
            binding.jogador1PecaGrande,
            true
        )
        val pecasJogador2 = criarPecas(
            binding.jogador2PecaPequena,
            binding.jogador2PecaMedia,
            binding.jogador2PecaGrande,
            false
        )

        // Instanciando 2 jogadores para jogar o jogo
        val jogador1 = Jogador("Jogador 1", pecasJogador1, true)
        val jogador2 = Jogador("Jogador 2", pecasJogador2,false)

        // Armazenando os 9 espaços possíveis para colocar as peças
        /*val blocos = listOf(
            binding.bloco1,
            binding.bloco2,
            binding.bloco3,
            binding.bloco4,
            binding.bloco5,
            binding.bloco6,
            binding.bloco7,
            binding.bloco8,
            binding.bloco9,
        )*/

        val blocos = listOf(
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


        // Configurando o listener para todas as peças
        /*(pecasJogador1 + pecasJogador2).forEach { peca ->
            peca.imagem.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val dragShadowBuilder = View.DragShadowBuilder(view)
                    view.startDragAndDrop(null, dragShadowBuilder, peca, 0) // Passa a peça como dado local
                    true
                } else {
                    false
                }
            }
        }*/

        // Configurando o listener para as peças do joador 1
        pecasJogador1.forEach { peca ->
            peca.imagem.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN && controleVezJogador == jogador1.vezDeJogar) {
                    val dragShadowBuilder = View.DragShadowBuilder(view)
                    view.startDragAndDrop(null, dragShadowBuilder, peca, 0) // Passa a peça como dado local
                    true
                } else {
                    false
                }
            }
        }

        // Configurando o listener para as peças do joador 2
        pecasJogador2.forEach { peca ->
            peca.imagem.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN && controleVezJogador == jogador2.vezDeJogar) {
                    val dragShadowBuilder = View.DragShadowBuilder(view)
                    view.startDragAndDrop(null, dragShadowBuilder, peca, 0) // Passa a peça como dado local
                    true
                } else {
                    false
                }
            }
        }


        // TODO arrumar bug visual em que a imagem da peça fica pequena
        // Configurando o listener de arraste para todos os blocos
        /*blocos.forEach { bloco ->
            bloco.setOnDragListener { view, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> true
                    DragEvent.ACTION_DROP -> {
                        val peca = event.localState as Peca

                        if (peca.quantidade > 0) {
                            // Atualiza o fundo do bloco com a imagem da peça arrastada
                            (view as ImageView).setImageDrawable(peca.imagem.background)

                            // Decrementa a quantidade da peça
                            peca.quantidade -= 1

                            // Remove a imagem da peça se a quantidade chegar a 0
                            if (peca.quantidade == 0) {
                                peca.imagem.visibility = View.INVISIBLE
                            }
                        }
                        true
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        true
                    }
                    else -> false
                }
            }
        }*/

        blocos.forEach { bloco ->
            bloco.view.setOnDragListener { view, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> true
                    DragEvent.ACTION_DROP -> {
                        val peca = event.localState as Peca

                        if (peca.quantidade > 0 && bloco.podeColocar(peca)) {
                            (view as ImageView).setImageDrawable(peca.imagem.background)

                            // Decrementa a quantidade da peça
                            peca.quantidade -= 1

                            // Remove a imagem da peça se a quantidade chegar a 0
                            if (peca.quantidade == 0) {
                                peca.imagem.visibility = View.INVISIBLE
                            }

                            bloco.tamanhoAtual = peca.tamanho
                            bloco.peca = peca
                            controleVezJogador = !controleVezJogador
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


        configurarBotaoVoltar()
        configurarBotaoResultado()
    }

    // Criar uma lista com as 9 peças de um jogador: 3 pequenas, 3 médias e 3 grandes
    fun criarPecas(pecaPequena: ImageView, pecaMedia: ImageView, pecaGrande: ImageView, donoDaPeca: Boolean) : List<Peca>{
        return listOf(
            Peca(0, 3, pecaPequena, donoDaPeca),
            Peca(1, 3, pecaMedia, donoDaPeca),
            Peca(2, 3, pecaGrande, donoDaPeca)
        )
    }

    // Configurar botão para mover para activity Main
    private fun configurarBotaoVoltar(){
        binding.btnJogoVoltarInicio.setOnClickListener {
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
}