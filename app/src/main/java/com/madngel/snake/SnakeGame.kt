package com.madngel.snake

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    fun isOpposite(other: Direction): Boolean = dx == -other.dx && dy == -other.dy
}

data class Cell(val x: Int, val y: Int)

class SnakeGame(private val columns: Int, private val rows: Int) {

    var snake by mutableStateOf(listOf(Cell(columns / 2, rows / 2)))
        private set

    var food by mutableStateOf(randomFreeCell())
        private set

    var direction by mutableStateOf(Direction.RIGHT)
        private set

    private var pendingDirection: Direction = direction

    var score by mutableStateOf(0)
        private set

    var isGameOver by mutableStateOf(false)
        private set

    fun setDirection(newDirection: Direction) {
        if (!newDirection.isOpposite(direction)) {
            pendingDirection = newDirection
        }
    }

    fun reset() {
        snake = listOf(Cell(columns / 2, rows / 2))
        direction = Direction.RIGHT
        pendingDirection = Direction.RIGHT
        score = 0
        isGameOver = false
        food = randomFreeCell()
    }

    fun tick() {
        if (isGameOver) return

        direction = pendingDirection
        val head = snake.first()
        var newX = head.x + direction.dx
        var newY = head.y + direction.dy

        // Wrap around the edges of the board.
        newX = (newX + columns) % columns
        newY = (newY + rows) % rows

        val newHead = Cell(newX, newY)

        if (snake.contains(newHead)) {
            isGameOver = true
            return
        }

        val grew = newHead == food
        val newSnake = mutableListOf(newHead)
        newSnake.addAll(if (grew) snake else snake.dropLast(1))
        snake = newSnake

        if (grew) {
            score += 1
            food = randomFreeCell()
        }
    }

    private fun randomFreeCell(): Cell {
        while (true) {
            val cell = Cell(Random.nextInt(columns), Random.nextInt(rows))
            if (cell !in snake) return cell
        }
    }
}
