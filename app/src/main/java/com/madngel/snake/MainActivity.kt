package com.madngel.snake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.max

private const val COLUMNS = 18
private const val ROWS = 24
private const val BASE_TICK_MS = 160L
private const val MIN_TICK_MS = 70L

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = Color(0xFF101418)) {
                    SnakeScreen()
                }
            }
        }
    }
}

@Composable
fun SnakeScreen() {
    val game = remember { SnakeGame(COLUMNS, ROWS) }

    GameLoop(game)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Snake",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Pontuação: ${game.score}",
            color = Color(0xFF8BC34A),
            fontSize = 18.sp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(COLUMNS.toFloat() / ROWS.toFloat())
                .background(Color(0xFF1B2027))
                .pointerInput(Unit) {
                    var accumulated = Offset.Zero
                    detectDragGestures(
                        onDragStart = { accumulated = Offset.Zero },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            accumulated += dragAmount
                            val threshold = 24f
                            if (abs(accumulated.x) > threshold || abs(accumulated.y) > threshold) {
                                if (abs(accumulated.x) > abs(accumulated.y)) {
                                    game.setDirection(if (accumulated.x > 0) Direction.RIGHT else Direction.LEFT)
                                } else {
                                    game.setDirection(if (accumulated.y > 0) Direction.DOWN else Direction.UP)
                                }
                                accumulated = Offset.Zero
                            }
                        }
                    )
                }
        ) {
            GameBoard(game = game)

            if (game.isGameOver) {
                GameOverOverlay(score = game.score, onRestart = { game.reset() })
            }
        }

        DirectionPad(onDirection = { game.setDirection(it) })
    }
}

@Composable
private fun GameLoop(game: SnakeGame) {
    LaunchedEffect(game.isGameOver) {
        while (!game.isGameOver) {
            val speedBonus = (game.score * 3L).coerceAtMost(BASE_TICK_MS - MIN_TICK_MS)
            val delayMs = max(MIN_TICK_MS, BASE_TICK_MS - speedBonus)
            delay(delayMs)
            game.tick()
        }
    }
}

@Composable
fun GameBoard(game: SnakeGame) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cellWidth = size.width / COLUMNS
        val cellHeight = size.height / ROWS

        game.snake.forEachIndexed { index, cell ->
            drawRect(
                color = if (index == 0) Color(0xFF8BC34A) else Color(0xFF4CAF50),
                topLeft = Offset(cell.x * cellWidth, cell.y * cellHeight),
                size = Size(cellWidth * 0.92f, cellHeight * 0.92f)
            )
        }

        drawRect(
            color = Color(0xFFFF5252),
            topLeft = Offset(game.food.x * cellWidth, game.food.y * cellHeight),
            size = Size(cellWidth * 0.9f, cellHeight * 0.9f)
        )
    }
}

@Composable
fun GameOverOverlay(score: Int, onRestart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Fim de jogo", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(text = "Pontuação final: $score", color = Color.White, fontSize = 18.sp)
            Button(onClick = onRestart) {
                Text("Jogar novamente")
            }
        }
    }
}

@Composable
fun DirectionPad(onDirection: (Direction) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { onDirection(Direction.UP) }) { Text("▲") }
        Row(
            horizontalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Button(onClick = { onDirection(Direction.LEFT) }) { Text("◀") }
            Button(onClick = { onDirection(Direction.RIGHT) }) { Text("▶") }
        }
        Button(onClick = { onDirection(Direction.DOWN) }) { Text("▼") }
    }
}
