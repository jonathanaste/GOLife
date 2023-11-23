package com.jona.golife

class GameOfLife(val width: Int, val height: Int) {
    var cells: Array<Array<Boolean>> = Array(height) { Array(width) { it%2==0 && it%3==0} }


    fun copy(): GameOfLife {
        val newGame = GameOfLife(width, height)
        for (y in cells.indices) {
            for (x in cells[y].indices) {
                newGame.cells[y][x] = this.cells[y][x]
            }
        }
        return newGame
    }


    fun toggleCell(x: Int, y: Int) {
        cells[y][x] = !cells[y][x]
    }

    fun nextGeneration() {
        val newCells = Array(height) { Array(width) { false } }
        for (y in cells.indices) {
            for (x in cells[y].indices) {
                val livingNeighbors = countLivingNeighbors(x, y)
                val isAlive = cells[y][x]
                newCells[y][x] = isAlive && livingNeighbors in 2..3 || !isAlive && livingNeighbors == 3
            }
        }
        cells = newCells
    }

    private fun countLivingNeighbors(x: Int, y: Int): Int {
        var count = 0
        for (i in -1..1) {
            for (j in -1..1) {
                if (i == 0 && j == 0) continue
                val newY = y + i
                val newX = x + j
                if (newY >= 0 && newY < height && newX >= 0 && newX < width) {
                    if (cells[newY][newX]) count++
                }
            }
        }
        return count
    }
}