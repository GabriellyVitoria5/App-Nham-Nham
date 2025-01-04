package com.ifmg.nhamnham

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.nhamnham.databinding.ActivityResultadoBinding

class Resultado : AppCompatActivity() {

    private lateinit var binding: ActivityResultadoBinding
    private lateinit var resultadoDoJogo: String
    private var placarJogador1: Int = 0
    private var placarJogador2: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar os componentes da interface
        binding = ActivityResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        obterResultado()
        mostrarResultado()

        configurarBotaoVoltarTelaInicial()
        configurarBotaoReiniciar(placarJogador1, placarJogador2)
    }

    // Obtém o resultado do jogo e placar (caso já tenha sido passado) vindo do Intent
    private fun obterResultado(){
        resultadoDoJogo = intent.getStringExtra("vencedor") ?: "Erro ao obter resultado"
        placarJogador1 = intent.getIntExtra("placarJogador1", 0)
        placarJogador2 = intent.getIntExtra("placarJogador2", 0)
    }

    // Mostrar resutado da partida na tela e atualizar placar
    @SuppressLint("SetTextI18n")
    private fun mostrarResultado(){
        if (resultadoDoJogo == "Empate") {
            binding.txtResultado.text = "Empate! ☹️"
        } else {
            binding.txtResultado.text = "$resultadoDoJogo ganhou! 🥳"
        }
        binding.txtPlacar.text = "Placar: $placarJogador1 x $placarJogador2"
    }

    // Quando voltar à tela inicial, o placar é zerado
    private fun configurarBotaoVoltarTelaInicial(){
        binding.btnRetultadoVoltarInicio.setOnClickListener {
            val sharedPreferences = getSharedPreferences("placar", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("placarJogador1", 0)
            editor.putInt("placarJogador2", 0)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Reiniciar o jogo mantendo o placar anterior
    private fun configurarBotaoReiniciar(placarJogador1:Int, placarJogador2:Int){
        binding.btnReiniciar.setOnClickListener {
            val intent = Intent(this, Jogo::class.java)
            intent.putExtra("placarJogador1", placarJogador1)
            intent.putExtra("placarJogador2", placarJogador2)
            startActivity(intent)
        }
    }
}
