# Nham Nham

## Autores
- Gabrielle de Oliveira Fonseca - 0072379
- Gabrielly Vitória - 0049278

## Descrição do Projeto
O jogo **Nham Nham** é uma aplicação Android desenvolvida para dispositivos móveis, cujo objetivo é proporcionar um momento divertido e estratégico entre dois jogadores. Inspirado em jogos de tabuleiro, a dinâmica baseia-se em um tabuleiro 3x3 onde os jogadores alternam turnos para posicionar peças de tamanhos diferentes (pequeno, médio, grande). O diferencial é que as peças maiores podem sobrepor as menores, adicionando um elemento estratégico ao jogo.

## Funcionalidades
- Interface intuitiva com tela inicial contendo:
  - Título do jogo.
  - Botão para iniciar a partida.
  - Botão para visualizar as regras.
- Dinâmica do jogo com:
  - Tabuleiro 3x3 interativo.
  - Alternância de turnos entre os jogadores.
  - Posicionamento de peças de tamanhos variados.
  - Lógica para sobreposição de peças.
  - Detecção automática de vitória ou empate.
- Tela final para exibir o vencedor ou informar empate, com opções para reiniciar ou retornar à tela inicial.

## Tecnologias Utilizadas
- **Linguagem:** Kotlin
- **Framework:** Android SDK
- **IDE:** Android Studio

## Como Executar o Projeto
1. Clone o repositório:
   ```bash
   git clone https://github.com/GabriellyVitoria5/App-Nham-Nham.git
   ```
2. Abra o projeto no Android Studio.
3. Certifique-se de que o SDK do Android esteja configurado.
4. Conecte um dispositivo ou configure um emulador Android.
5. Navegue até o arquivo `app/src/main/java/com/ifmg/nhamnham/MainActivity.kt`.
6. Execute o projeto clicando no botão "Run" (botão verde na barra superior).

## Regras do Jogo
1. Dois jogadores alternam turnos para posicionar peças no tabuleiro 3x3.
2. Cada jogador possui três peças de tamanhos diferentes: pequeno, médio e grande.
3. Uma peça maior pode sobrepor uma menor que já esteja no tabuleiro.
4. O objetivo é alinhar três peças do mesmo jogador (horizontal, vertical ou diagonal).
5. Se o tabuleiro for completamente preenchido sem um vencedor, o jogo termina em empate.
