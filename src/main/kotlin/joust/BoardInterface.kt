package org.example.joust

import org.example.joust.JoustGameLogic.Cell
import org.example.joust.JoustGameLogic.GameState

interface BoardInterface {
    fun getGameState(cells: List<Cell>): GameState
    fun mapCellPositionToCoordinate(cell: Int): Pair<Int, Int>
    fun filterByAvailableCells(playerMoves: List<Int>, cells: List<Cell>): List<Int>
    fun findPossibleMoves(cell: Pair<Int, Int>): List<Pair<Int, Int>>
    fun findAvailableMovesInBound(possibleMoves: List<Pair<Int, Int>>): List<Pair<Int, Int>>
    fun updateBoardState( playerPosition: Int): List<Cell>
    fun mapToCurrentPlayerTurn(playerTurn: JoustGameLogic.PlayerTurn): Cell
    fun mapCoordinateToIndex(cell: Pair<Int, Int>): Int
}
