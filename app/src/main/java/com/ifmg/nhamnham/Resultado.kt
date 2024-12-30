package com.ifmg.nhamnham

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.nhamnham.databinding.ActivityResultadoBinding

class Resultado : AppCompatActivity() {

    private lateinit var binding: ActivityResultadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar os componentes da interface
        binding = ActivityResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o listener de ajuste de padding para a tela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtém o resultado do jogo vindo do Intent
        val resultadoDoJogo = intent.getStringExtra("vencedor") ?: "Erro ao obter resultado"

        // Obtenha o placar vindo do Intent, caso já tenha sido passado
        val placarJogador1 = intent.getIntExtra("placarJogador1", 0)
        val placarJogador2 = intent.getIntExtra("placarJogador2", 0)

        // Exibe o resultado
        if (resultadoDoJogo == "Empate") {
            binding.txtResultado.text = "Empate! ☹️"
        } else {
            binding.txtResultado.text = "$resultadoDoJogo ganhou! 🥳"
        }

        // Atualiza o placar
        binding.txtPlacar.text = "Placar: $placarJogador1 x $placarJogador2"

        // Ações dos botões
        binding.btnRetultadoVoltarInicio.setOnClickListener {
            // Quando voltar à tela inicial, o placar é zerado
            val intent = Intent(this, MainActivity::class.java)

            // Limpar placar
            val sharedPreferences = getSharedPreferences("placar", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("placarJogador1", 0)
            editor.putInt("placarJogador2", 0)
            editor.apply()

            startActivity(intent)
        }

        binding.btnReiniciar.setOnClickListener {
            // Reinicia o jogo, mantendo o placar anterior
            val intent = Intent(this, Jogo::class.java)
            intent.putExtra("placarJogador1", placarJogador1)
            intent.putExtra("placarJogador2", placarJogador2)
            startActivity(intent)
        }
    }
}
