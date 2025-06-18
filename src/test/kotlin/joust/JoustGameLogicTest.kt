package joust

import org.example.joust.JoustGameLogic
import org.example.joust.JoustGameLogic.Board
import org.example.joust.JoustGameLogic.Cell
import org.example.joust.JoustGameLogic.PlayerTurn.Player1
import org.example.joust.JoustGameLogic.PlayerTurn.Player2
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

class JoustGameLogicTest {
    val board = Board(10, 20)

    @Test
    fun `generatePlayersInitialPositions, assert generated nums are different`() {
        val joustGameLogic = JoustGameLogic()
        val generatedList = joustGameLogic.generatePlayersInitialPositions()
        val diffNums = generatedList.first() != generatedList.last()
        assertTrue(diffNums)
    }

    @Test
    fun `findPossibleMoves, return a list of possible moves`() {
        val possibleMoves = board.findPossibleMoves(10)
        assertEquals(listOf(4, 0, 20, 16, -7, -5, 27, 25), possibleMoves)
    }

    @Test
    fun `findAvailableMovesInBound, returns list of available moves`() {
        val list = listOf(4, 0, 20, 16, -7, -5, 27, 25)
        val movesInBound = board.findAvailableMovesInBound(list)
        assertEquals(listOf(4, 0, 20, 16, 27, 25), movesInBound)
    }

    @Test
    fun `filterByAvailableCells, return a list of available cells`() {
        val cells = MutableList(64) { if (it == 4 || it == 0) Cell.Unavailable else Cell.Available }
        val availableMoves = board.filterByAvailableCells(listOf(4, 0, 20, 16), cells)

        assertEquals(listOf(20, 16), availableMoves)
    }

    @Test
    fun `findAvailableMovesForPlayer, given player position, return list of available moves`() {
        val cells = MutableList(64) { if (it == 4 || it == 0) Cell.Unavailable else Cell.Available }

        val availableMovesForPlayer = board.findAvailableMovesForPlayer(10, cells = cells)
        assertEquals(listOf(20, 16, 27, 25), availableMovesForPlayer)
    }

    @Test
    fun `getGameState, if player1 has no available cells, player2 wins`() {
        val cells =
            MutableList(64) {
                when (it) {
                    4, 0, 20, 16, 27, 25 -> Cell.Unavailable
                    10 -> Cell.Player1
                    30 -> Cell.Player2
                    else -> Cell.Available
                }
            }
        assertEquals(JoustGameLogic.GameState.Player2Wins, board.getGameState(cells = cells))
    }

//    @Test
//    fun `updateBoardState, when player 1 moves to position 25, previous player1 position in Unavailable`() {
//        val cells =
//            MutableList(64) {
//                when (it) {
//                    10 -> Cell.Player1
//                    30 -> Cell.Player2
//                    else -> Cell.Available
//                }
//            }
//
//        board.makeAMove(cells, 25)
//        assertEquals(cells[10], Cell.Unavailable)
//        assertEquals(cells[25], Cell.Player1)
//    }

    @Test
    fun `alternatePlayers, when playerTurn is Player 1, playerTurn is updated to Player 2`() {
        board.alternatePlayers(Player1)
        assertEquals(Player2, board.playerTurn)
    }
}