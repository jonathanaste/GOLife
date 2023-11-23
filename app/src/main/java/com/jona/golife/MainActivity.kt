package com.jona.golife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.jona.golife.ui.theme.GOLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GOLifeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val gameViewModel: GameViewModel = GameViewModel()
                    // Llama a GameScreen pasando el ViewModel
                    GameScreen(viewModel = gameViewModel)
                }
            }
        }
    }
}

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val game by viewModel.gameState.collectAsState()

    DisposableEffect(Unit) {
        viewModel.startGameLoop()
        onDispose {
            viewModel.stopGameLoop()
        }
    }
    Column {
        GameOfLifeBoard(game = game, modifier = Modifier.fillMaxSize())
        // Agrega más controles según sea necesario
    }
}

@Composable
fun GameOfLifeBoard(game: GameOfLife, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cellSize = size.width / game.width
        for (y in 0 until game.height) {
            for (x in 0 until game.width) {
                val color = if (game.cells[y][x]) Color.Black else Color.White
                drawRect(
                    color = color,
                    topLeft = Offset(x * cellSize, y * cellSize),
                    size = Size(cellSize, cellSize)
                )
            }
        }
    }
}