package com.jona.golife

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private var gameLoopJob: Job? = null
    private var previousCells: Array<Array<Boolean>>? = null
    private val _gameState = MutableStateFlow(GameOfLife(10, 10)) // Ejemplo de tamaño
    val gameState: StateFlow<GameOfLife> = _gameState.asStateFlow()

    fun startGameLoop() {
        gameLoopJob = viewModelScope.launch {
            while (true) {
                val unchanged = updateGameAndCheckUnchanged()
                if (unchanged) {
                    stopGameLoop()
                    break
                }
                delay(1000) // 1 second delay
            }
        }
    }

    private fun updateGameAndCheckUnchanged(): Boolean {
        val unchanged = _gameState.value.cells contentDeepEquals previousCells
        previousCells =  _gameState.value.cells.map { it.clone() }.toTypedArray()
        nextGeneration()
        return unchanged
    }

    fun stopGameLoop() {
        gameLoopJob?.cancel()
        gameLoopJob = null
    }


    fun nextGeneration() {
        viewModelScope.launch {
            val newGame = _gameState.value.copy() // Assuming GameOfLife class has a 'copy' method
            newGame.nextGeneration() // Your method to calculate the next generation
            _gameState.emit(newGame) // Update the state
        }
    }
}