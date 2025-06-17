package joust

import org.example.joust.JoustGameLogic
import org.example.joust.JoustGameLogic.Cell
import org.example.joust.JoustGameLogic.PlayerTurn
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.Test
import kotlin.test.assertEquals

class JoustGameLogicTest {
    val boardGame: JoustGameLogic.BoardGame = mock()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    fun `generateBoardPosition, return position 10 `() {
        `when`(boardGame.generateBoardPosition()).thenReturn(10)

        assertEquals(10, boardGame.generateBoardPosition())

    }

    @Test
    fun `generateBoardPosition, return position 30 `() {
        `when`(boardGame.generateBoardPosition()).thenReturn(30)
        assertEquals(30, boardGame.generateBoardPosition())
    }

    @Test
    fun `alternatePlayers, if no available positions left for player1, return gameState`() {
        val gameBoard: MutableList<Cell> =
            MutableList(64) { if (it == 10) Cell.Player1 else if (it == 30) Cell.Player2 else Cell.Unavailable }
        val board = JoustGameLogic.Board(10, 30)

        val actual = board.getGameState(gameBoard)
        assertEquals(JoustGameLogic.GameState.Player2Wins, actual)
    }

    @Test
    fun `getGameState, if list is empty, gameState is Player2Won`() {
        val gameBoard: MutableList<Cell> =
            MutableList(64) { if (it == 10) Cell.Player1 else if (it == 30) Cell.Player2 else Cell.Unavailable }
        val board = JoustGameLogic.Board(10, 30)
        board.findAvailableMovesForPlayer(10, gameBoard)
        val actual = board.getGameState(gameBoard)

        assertEquals(PlayerTurn.Player1, board.playerTurn)
        assertEquals(JoustGameLogic.GameState.Player2Wins, actual)
    }

    @Test
    fun `findPossibleMoves, returns a list of possibleMoves`() {
        val board = JoustGameLogic.Board(10, 30)
        val actual = board.findPossibleMoves(10)

        assertEquals(listOf(4, 0, 20, 16, -7, -5, 27, 25), actual)

    }

    @Test
    fun `findAvailableMovesInBound, returns a list of possibleMoves`() {
        val board = JoustGameLogic.Board(10, 30)

        val possibleMoves = board.findPossibleMoves(10)
        val actual = board.findAvailableMovesInBound(possibleMoves)
        assertEquals(listOf(4, 0, 20, 16, 27, 25), actual)
    }

    @Test
    fun `filterByAvailableCells, return a list of availableCells`() {
        val gameBoard = MutableList(64) {
            if (it == 25) Cell.Unavailable else if (it == 4) Cell.Unavailable else if (it == 16) Cell.Unavailable else Cell.Available
        }
        val board = JoustGameLogic.Board(10, 30)

        val possibleMoves = board.findPossibleMoves(10)
        val availableMovesInBound = board.findAvailableMovesInBound(possibleMoves)
        val actual = board.filterByAvailableCells(availableMovesInBound, gameBoard)

        assertEquals(listOf(0, 20, 27), actual)
    }

    @Test
    fun `findAvailableMovesForPlayer, returns a list of availableMoves`() {
        val gameBoard = MutableList(64) {
            if (it == 25) Cell.Unavailable else if (it == 16) Cell.Unavailable else Cell.Available
        }
        val board = JoustGameLogic.Board(10, 30)
        val actual = board.findAvailableMovesForPlayer(10, gameBoard)

        assertEquals(listOf(4, 0, 20, 27), actual)
    }

    @Test
    fun `getGameState, if player 1 has no more moves`() {
        val gameBoard = MutableList(64) {
            if (it == 25) Cell.Unavailable else if (it == 10) Cell.Player1 else if (it == 30) Cell.Player2 else if (it == 16 || it == 4 || it == 0 || it == 20 || it == 27) Cell.Unavailable else Cell.Available
        }
        val board = JoustGameLogic.Board(10, 30)
        val actual = board.getGameState(gameBoard)

        assertEquals(JoustGameLogic.GameState.Player2Wins, actual)
    }

    @Test
    fun `getGameState, if player 2 has no more moves`() {
        val gameBoard = MutableList(64) {
            if (it == 25) Cell.Unavailable else if (it == 10) Cell.Player1 else if (it == 30) Cell.Player2 else if (it == 16 || it == 4 || it == 0 || it == 20 || it == 27) Cell.Unavailable else Cell.Available
        }
        val board = JoustGameLogic.Board(10, 30)
        val actual = board.getGameState(gameBoard)

        assertEquals(JoustGameLogic.GameState.Player2Wins, actual)
    }
}