# Snake

Jogo da Cobrinha (Snake) para Android, feito em Kotlin com Jetpack Compose.

## Como jogar

- Deslize o dedo sobre o tabuleiro (cima/baixo/esquerda/direita) ou use os
  botões de direção abaixo do tabuleiro para controlar a cobra.
- Coma os quadrados vermelhos (comida) para crescer e ganhar pontos.
- O jogo termina se a cobra colidir com o próprio corpo. Ao atingir a borda
  do tabuleiro, a cobra reaparece do lado oposto.
- A velocidade aumenta conforme a pontuação sobe.
- Toque em "Jogar novamente" na tela de fim de jogo para reiniciar.

## Como compilar e rodar

1. Abra a pasta do projeto no Android Studio (versão recente, com Android
   Gradle Plugin 8.5+ e JDK 17).
2. Deixe o Android Studio sincronizar o Gradle e baixar as dependências.
3. Rode o app em um emulador ou dispositivo físico (`minSdk 24`).

Ou via linha de comando (com o Android SDK instalado e a variável
`ANDROID_HOME`/`ANDROID_SDK_ROOT` configurada):

```
./gradlew assembleDebug
```

O APK gerado fica em `app/build/outputs/apk/debug/app-debug.apk`.

## Estrutura

- `app/src/main/java/com/madngel/snake/SnakeGame.kt` — lógica do jogo
  (estado da cobra, comida, pontuação, colisão).
- `app/src/main/java/com/madngel/snake/MainActivity.kt` — interface em
  Jetpack Compose (tabuleiro, controles, telas de pontuação e fim de jogo).
