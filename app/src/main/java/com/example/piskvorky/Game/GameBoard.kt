package com.example.piskvorky.Game

import android.media.Image
import java.util.*

class GameBoard {
    val board = Array(3) { arrayOfNulls<String>(3) }
    var computersMove: Square? = null
    companion object {
        const val PLAYER = "O"
        const val PC = "X"
    }

    val gameOver:Boolean get()=isWinningMove(PC )|| isWinningMove(PLAYER) || availableSquares.isEmpty()

    private val availableSquares:List<Square> get(){
        val squares = mutableListOf<Square>()
        for (x in board.indices) {
            for (y in board.indices) {
                if (board[x][y].isNullOrEmpty()) {
                    squares.add(Square(x, y))
                }
            }
        }
        return squares
    }


    fun isWinningMove(player: String):Boolean{
        for(i in 0..2){
            if((board[i][0] == board[i][1])&&
                (board[i][0] == board[i][2])&&
                (board[i][0] == player)
            )return true
        }

        for(i in 0..2){
            if(
                (board[0][i] == board[1][i])&&
                (board[0][i] == board[2][i])&&
                (board[0][i] == player)
            )return true
        }

        if(
            (board[0][0] == board[1][1])&&
            (board[0][0] == board[2][2])&&
            (board[0][0] == player)
        ) return true

        if(
            (board[0][2] == board[1][1])&&
            (board[0][2] == board[2][0])&&
            (board[0][2] == player)
        ) return true

        return false
    }

    fun easyModePlay(){
        if (availableSquares.isNotEmpty()) {
            val r = Random()
            val randIndex = r.nextInt(availableSquares.size-0)+0
            val cell = availableSquares[randIndex]
            move(cell,PC)
        }
    }


    fun impassibleModePlay(depth: Int, player: String): Int {
        if (isWinningMove(PC)) return +1
        if (isWinningMove(PLAYER)) return -1

        if (availableSquares.isEmpty()) return 0

        var min = Integer.MAX_VALUE
        var max = Integer.MIN_VALUE

        for (i in availableSquares.indices) {
            val square = availableSquares[i]
            if (player == PC) {
                move(square, PC)
                val currentScore = impassibleModePlay(depth + 1, PLAYER)
                max = Math.max(currentScore, max)

                if (currentScore >= 0) {
                    if (depth == 0) computersMove = square
                }

                if (currentScore == 1) {
                    board[square.x][square.y] = ""
                    break
                }

                if (i == availableSquares.size - 1 && max < 0) {
                    if (depth == 0) computersMove = square
                }

            } else if (player == PLAYER) {
                move(square, PLAYER)
                val currentScore = impassibleModePlay(depth + 1, PC)
                min = Math.min(currentScore, min)

                if (min == -1) {
                    board[square.x][square.y] = ""
                    break
                }
            }
            board[square.x][square.y] = ""
        }

        return if (player == PC) max else min
    }

    fun move(square: Square, player: String) {
        board[square.x][square.y] = player
    }

}