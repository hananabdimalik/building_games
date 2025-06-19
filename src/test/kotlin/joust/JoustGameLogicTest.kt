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
        val possibleMoves = board.findPossibleMoves(6 to 2)
        assertEquals(listOf(5 to 4, 7 to 4, 5 to 0, 7 to 0, 4 to 1, 4 to 3, 8 to 1, 8 to 3), possibleMoves)
    }

    @Test
    fun `mapCellToCoordinate, given cell position 30, return the right coordinate in 8X8 grid`() {
        assertEquals(3 to 6, board.mapCellPositionToCoordinate(30))
    }

    @Test
    fun `mapCellToCoordinate, given cell position 63, return the right coordinate in 8X8 grid`() {
        assertEquals(7 to 7, board.mapCellPositionToCoordinate(63))
    }

    @Test
    fun `mapCellToCoordinate, given cell position 50, return the right coordinate in 8X8 grid`() {
        assertEquals(6 to 2, board.mapCellPositionToCoordinate(50))
    }

    @Test
    fun `filterByAvailableCells, return a list of available cells`() {
        val cells = MutableList(64) { if (it == 4 || it == 0) Cell.Unavailable else Cell.Available }
        val availableMoves = board.filterByAvailableCells(listOf(4, 0, 20, 16), cells)

        assertEquals(listOf(20, 16), availableMoves)
    }

    @Test
    fun `findAvailableMovesInBound, return list of available moves within bound`() {
        val availableMovesInBound =
            board.findAvailableMovesInBound(listOf(5 to 4, 7 to 4, 5 to 0, 7 to 0, 4 to 1, 4 to 3, 8 to 1, 8 to 3))
        assertEquals(listOf(5 to 4, 7 to 4, 5 to 0, 7 to 0, 4 to 1, 4 to 3), availableMovesInBound)

    }

    @Test
    fun `mapCoordinateToIndex, given cell coordinate (7,4), return the cell position`() {
        assertEquals(60, board.mapCoordinateToIndex(7 to 4))
    }
    @Test
    fun `findAvailableMovesForPlayer, given player position, return list of available moves`() {
        val cells = MutableList(64) { if (it == 4 || it == 0) Cell.Unavailable else Cell.Available }

        val availableMovesForPlayer = board.findAvailableMovesForPlayer(10, cells = cells)
        assertEquals(listOf(20, 16, 25, 27), availableMovesForPlayer)
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