package org.example

import org.example.joust.JoustGameLogic
import org.example.joust.JoustGameLogic.*

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun playJoust() {
    val initialState = JoustGameLogic().generatePlayersInitialPositions()
    val joustGameLogic = Board(initialState.first(), initialState.last())

    println(gameBoard(joustGameLogic.cells))
    println("${joustGameLogic.playerTurn} moves")
    println("Available moves for player 1 ${joustGameLogic.findAvailableMovesForPlayer(initialState.first(), joustGameLogic.cells)}")
    println("Available moves for player 2 ${joustGameLogic.findAvailableMovesForPlayer(initialState.last(), joustGameLogic.cells)}")

    while (joustGameLogic.getGameState(joustGameLogic.cells) == GameState.None) {
        val inputPosition = readln().trim().toInt() // add try/catch to handle bad input

        joustGameLogic.makeAMove(joustGameLogic.cells, inputPosition)

        println(gameBoard(joustGameLogic.cells))
        println("${joustGameLogic.playerTurn} moves")
        val currentPositionForPlayer1 = joustGameLogic.cells.indexOfFirst { it == Cell.Player1 }
        val currentPositionForPlayer2 = joustGameLogic.cells.indexOfFirst { it == Cell.Player2 }
        println("Player 1 moves${joustGameLogic.findAvailableMovesForPlayer(currentPositionForPlayer1, joustGameLogic.cells)}")
        println("Player 2 moves ${joustGameLogic.findAvailableMovesForPlayer(currentPositionForPlayer2, joustGameLogic.cells)}")

        if (joustGameLogic.getGameState(joustGameLogic.cells) != GameState.None) {
            break
        }
    }
    println("GameOver: ${joustGameLogic.getGameState(joustGameLogic.cells)}")
}

private fun gameBoard(cells: List<Cell>): String {
    var board = " "
    for (i in 0 until cells.size) {
        val cellValue =
            if (cells[i] == Cell.Player1) "K1" else if (cells[i] == Cell.Player2) "K2 " else if (cells[i] == Cell.Unavailable) "UA" else "A"
        board += "  $cellValue  |"

        if ((i + 1) % 8 == 0) {
            board += "\n\n "
        }
    }
    return board
}


fun main() {
    println(playJoust())
}
