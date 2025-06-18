package org.example.joust

import org.example.joust.JoustGameLogic.Cell
import org.example.joust.JoustGameLogic.GameState

interface BoardInterface {
    fun findPossibleMoves(index: Int): List<Int>
    fun findAvailableMovesInBound(possibleMoves: List<Int>): List<Int>
    fun filterByAvailableCells(playerMoves: List<Int>, cells: List<Cell>): List<Int>
    fun getGameState(cells: List<Cell>): GameState
    fun updateBoardState( playerPosition: Int): List<Cell>
    fun mapToCurrentPlayerTurn(playerTurn: JoustGameLogic.PlayerTurn): Cell
}
