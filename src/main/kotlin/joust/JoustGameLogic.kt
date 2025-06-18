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
            val listOfMoves = findPossibleMoves(playerPosition)
            val movesWithinBound = findAvailableMovesInBound(listOfMoves)
            val filterByAvailableCells = filterByAvailableCells(movesWithinBound, cells)

            return filterByAvailableCells
        }

        override fun filterByAvailableCells(playerMoves: List<Int>, cells: List<Cell>) =
            playerMoves.filter { it -> cells[it] == Cell.Available }

        override fun findAvailableMovesInBound(possibleMoves: List<Int>) = possibleMoves.filter { it in 0 until 64 }

        override fun findPossibleMoves(index: Int): List<Int> {
            val possibleMoves = listOf(
                (index - MoveByEight + MoveByTwo),
                (index - MoveByEight - MoveByTwo),
                (index + MoveByEight + MoveByTwo),
                (index + MoveByEight - MoveByTwo),
                (index - MoveBySixteen - MoveByOne),
                (index - MoveBySixteen + MoveByOne),
                (index + MoveBySixteen + MoveByOne),
                (index + MoveBySixteen - MoveByOne)
            )

            return possibleMoves
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
