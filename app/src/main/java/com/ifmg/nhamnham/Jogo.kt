package com.ifmg.nhamnham

import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.nhamnham.databinding.ActivityJogoBinding

class Jogo : AppCompatActivity() {

    private lateinit var binding: ActivityJogoBinding
    private var isImageDropped = false // Para bloquear novos arrastos após o drop

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

        // Armazenando peças do jogador 1
        val pecasJogador1 = listOf(
            binding.jogador1PecaPequena,
            binding.jogador1PecaMedia,
            binding.jogador1PecaGrande
        )

        // Armazenando peças do jogador 2
        val pecasJogador2 = listOf(
            binding.jogador2PecaPequena,
            binding.jogador2PecaMedia,
            binding.jogador2PecaGrande
        )

        // Armazenando os 9 espaços possíveis para colocar as peças
        val blocos = listOf(
            binding.bloco1,
            binding.bloco2,
            binding.bloco3,
            binding.bloco4,
            binding.bloco5,
            binding.bloco6,
            binding.bloco7,
            binding.bloco8,
            binding.bloco9,
        )

        // Aplicar o OnTouchListener para arrastar as peças do jogador 1
        pecasJogador1.forEach { peca ->
            peca.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val shadow = View.DragShadowBuilder(view)
                        view.startDragAndDrop(null, shadow, view, 0)
                        true
                    }
                    else -> false
                }
            }
        }

        // Aplicar o OnTouchListener para arrastar as peças do jogador 2
        pecasJogador2.forEach { peca ->
            peca.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val shadow = View.DragShadowBuilder(view)
                        view.startDragAndDrop(null, shadow, view, 0)
                        true
                    }
                    else -> false
                }
            }
        }

        // TODO bloquear uma peça depois que posicionar em um blobo
        // Configurar cada bloco como área de drop
        blocos.forEach { bloco ->
            bloco.setOnDragListener { _, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                        true // Indica que aceita o arrasto
                    }
                    DragEvent.ACTION_DROP -> {
                            val droppedView = event.localState as View

                            // Obter as dimensões e a posição do bloco (área de soltar)
                            val dropAreaLocation = IntArray(2)
                            bloco.getLocationOnScreen(dropAreaLocation)

                            val dropAreaWidth = bloco.width
                            val dropAreaHeight = bloco.height

                            // Calcular o centro do bloco
                            val centerX = dropAreaLocation[0] + dropAreaWidth / 2
                            val centerY = dropAreaLocation[1] + dropAreaHeight / 2

                            // Obter a posição global do layout pai do droppedView
                            val parentLocation = IntArray(2)
                            (droppedView.parent as View).getLocationOnScreen(parentLocation)

                            // Ajustar as coordenadas relativas ao layout pai do droppedView
                            val newX = centerX - parentLocation[0] - droppedView.width / 2
                            val newY = centerY - parentLocation[1] - droppedView.height / 2

                            // Atualizar a posição da peça arrastada
                            droppedView.x = newX.toFloat()
                            droppedView.y = newY.toFloat()
                            droppedView.visibility = View.VISIBLE

                            // Bloquear novos arrastos
                            isImageDropped = true

                        true
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        val draggedView = event.localState as View
                        draggedView.visibility = View.VISIBLE
                        true
                    }
                    else -> false
                }
            }
        }

        // Configurar botão para mover para activity Main
        binding.btnJogoVoltarInicio.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Configurar botão para mover para activity de Resultado
        binding.btnResultado.setOnClickListener {
            val intent = Intent(this, Resultado::class.java)
            startActivity(intent)
        }
    }
}