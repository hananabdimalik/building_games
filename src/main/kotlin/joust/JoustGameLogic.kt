package org.example.joust

private const val MoveByEight = 8
private const val MoveBySixteen = 16
private const val MoveByTwo = 2
private const val MoveByOne = 1

class JoustGameLogic {

    enum class Cell {
        Available,
        Unavailable,
        Player1,
        Player2,
    }

    enum class PlayerTurn {
        Player1,
        Player2,
    }

    enum class GameState {
        Player1Wins,
        Player2Wins,
        None
    }

    class Board(
        player1Position: Int,
        player2Position: Int,
    ) : BoardInterface {
        var playerTurn = PlayerTurn.Player1
        val cells: MutableList<Cell> =
            MutableList(64) { if (it == player1Position) Cell.Player1 else if (it == player2Position) Cell.Player2 else Cell.Available }

        fun makeAMove(cells: List<Cell>, playerInput: Int) {
            val currentPlayerPosition = cells.indexOfFirst { it == mapToCurrentPlayerTurn(playerTurn) }
            val findAvailablePositionForPlayer = findAvailableMovesForPlayer(currentPlayerPosition, cells)

            if (findAvailablePositionForPlayer.isNotEmpty() && findAvailablePositionForPlayer.contains(playerInput)) {
                updateBoardState(playerInput)
                alternatePlayers(playerTurn)
            } else {
                getGameState(cells)
            }
        }

        override fun mapToCurrentPlayerTurn(playerTurn: PlayerTurn): Cell =
            if (playerTurn == PlayerTurn.Player1) Cell.Player1 else Cell.Player2

        override fun mapCoordinateToIndex(cell: Pair<Int, Int>): Int {
            return (cell.first * 8) + cell.second
        }

        fun alternatePlayers(player: PlayerTurn) {
            playerTurn = if (player == PlayerTurn.Player1) {
                PlayerTurn.Player2
            } else {
                PlayerTurn.Player1
            }
        }

        override fun updateBoardState(playerPosition: Int): List<Cell> {
            cells.forEachIndexed { index, cell ->
                if (playerTurn == PlayerTurn.Player1) {
                    if (cells[index] == Cell.Player1) {
                        cells[index] = Cell.Unavailable
                        cells[playerPosition] = Cell.Player1
                    }
                }

                if (playerTurn == PlayerTurn.Player2) {
                    if (cells[index] == Cell.Player2) {
                        cells[index] = Cell.Unavailable
                        cells[playerPosition] = Cell.Player2
                    }
                }
            }
            return cells
        }

        override fun getGameState(cells: List<Cell>): GameState {
            val player1Position = cells.indexOfFirst { it == Cell.Player1 }
            val player2Position = cells.indexOfFirst { it == Cell.Player2 }

            if (playerTurn == PlayerTurn.Player1 && findAvailableMovesForPlayer(player1Position, cells).isEmpty()) {
                return GameState.Player2Wins
            }

            if (playerTurn == PlayerTurn.Player2 && findAvailableMovesForPlayer(player2Position, cells).isEmpty()) {
                return GameState.Player1Wins
            }

            return GameState.None
        }

        fun findAvailableMovesForPlayer(playerPosition: Int, cells: List<Cell>): List<Int> {
            val mapPositionToCoordinate = mapCellPositionToCoordinate(playerPosition)
            val listOfMoves = findPossibleMoves(mapPositionToCoordinate)
            val movesWithinBound = findAvailableMovesInBound(listOfMoves)
            val mapMovesInBoundToCoordinates = movesWithinBound.map {
                mapCoordinateToIndex(it)
            }
            val filterByAvailableCells = filterByAvailableCells(mapMovesInBoundToCoordinates, cells)

            return filterByAvailableCells
        }

        override fun filterByAvailableCells(playerMoves: List<Int>, cells: List<Cell>) =
            playerMoves.filter { it -> cells[it] == Cell.Available }

        override fun findPossibleMoves(cell: Pair<Int, Int>): List<Pair<Int, Int>> {
            return listOf(
                Pair(cell.first - 1, cell.second + 2),
                Pair(cell.first + 1, cell.second + 2),
                Pair(cell.first - 1, cell.second - 2),
                Pair(cell.first + 1, cell.second - 2),

                Pair(cell.first - 2, cell.second - 1),
                Pair(cell.first - 2, cell.second + 1),
                Pair(cell.first + 2, cell.second - 1),
                Pair(cell.first + 2, cell.second + 1),
            )
        }

        override fun findAvailableMovesInBound(possibleMoves: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
            return possibleMoves.filter { it.first in 0 until 8 && it.second in 0 until  8 }
        }


        override fun mapCellPositionToCoordinate(cell: Int): Pair<Int, Int> {
            val column = cell % 8
            val row = cell / 8
            return row to column
        }
    }

    fun generatePlayersInitialPositions(): List<Int> {
        val generatedList = mutableListOf<Int>()
        val player1 = generateNumber()
        var player2 = generateNumber()

        while (player1 == player2) {
            player2 = generateNumber()
        }
        generatedList.add(player1)
        generatedList.add(player2)
        return generatedList
    }

    private fun generateNumber(): Int {
        return (0 until 64).random()
    }
}
