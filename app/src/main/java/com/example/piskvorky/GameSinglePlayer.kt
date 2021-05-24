package com.example.piskvorky

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.piskvorky.Game.GameBoard
import com.example.piskvorky.Game.Square
import com.example.piskvorky.db.dbHelper

class GameSinglePlayer :AppCompatActivity(){
    private val boardSquares = Array(3) { arrayOfNulls<ImageView>(3) }
    var board = GameBoard()
    private lateinit var textViewResult: TextView
    private var mode :String = ""
    private var playerPoints: Int = 0
    private var pcPoints: Int = 0
    private var draws :Int = 0
    private lateinit var player1ScoreTextView: TextView
    private lateinit var player2ScoreTextView: TextView
    private lateinit var drawScoreTextView: TextView
    private val context = this
    private var db = dbHelper(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        textViewResult = findViewById(R.id.ResultTextView)

        player1ScoreTextView = findViewById(R.id.player1ScoreTextView)
        player2ScoreTextView = findViewById(R.id.player2ScoreTextView)
        drawScoreTextView = findViewById(R.id.drawScoreTextView)

        val bundle: Bundle? = intent.extras
        val mod = bundle!!.getString("mode")
        if (mod != null) {
            mode = mod
        }
        createBoard()
        val btnReset: Button = findViewById(R.id.ResetButton)
        btnReset.setOnClickListener{
            board = GameBoard()
            mapBoardToUi()
            textViewResult.text = ""
        }

        val a = findViewById<ImageButton>(R.id.statsButton)

        a.setOnClickListener{
            val intent = Intent(this,Stats::class.java)
            startActivity(intent)
        }

        val back = findViewById<ImageButton>(R.id.backButton)

        back.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createBoard(){
        for (i in boardSquares.indices) {
            for (j in boardSquares.indices) {
                val btn: ImageButton =
                    findViewById(resources.getIdentifier("btn$i$j", "id", packageName))
                boardSquares[i][j] = btn
                if(mode == "easy")
                {
                    boardSquares[i][j]?.setOnClickListener(Mode(i,j,"easy") )
                }else{
                    boardSquares[i][j]?.setOnClickListener(Mode(i,j,"impossible") )
                }
            }
        }
    }

    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    GameBoard.PLAYER -> {
                        boardSquares[i][j]?.setImageResource(R.drawable.x)
                        boardSquares[i][j]?.isEnabled = false
                    }
                    GameBoard.PC -> {
                        boardSquares[i][j]?.setImageResource(R.drawable.o)
                        boardSquares[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardSquares[i][j]?.setImageResource(0)
                        boardSquares[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }

    fun setDisabled(){

    }

    inner class Mode(private val i: Int,private val j: Int,private val mode:String): View.OnClickListener {
        override fun onClick(p0: View?) {

            if (!board.gameOver) {
                val square = Square(i, j)
                board.move(square, GameBoard.PLAYER)
                if(mode == "impossible")
                {
                    board.impassibleModePlay(0, GameBoard.PC)
                    board.computersMove?.let {
                        board.move(it, GameBoard.PC)
                    }
                    checkWin()
                }else{
                    if(!checkWin())
                    board.easyModePlay()
                }
                mapBoardToUi()
            }


        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateScore() {
        player1ScoreTextView.text = "$playerPoints"
        player2ScoreTextView.text = " $pcPoints"
        drawScoreTextView.text = "$draws"
        setDisabled()
    }

    @SuppressLint("SetTextI18n")
    private fun checkWin() :Boolean{
        when {
            board.isWinningMove(GameBoard.PC) -> {
                pcPoints++
                textViewResult.text = "Vyhrava PC"
                updateScore()
                updateDbMode(mode,"lose")
                return true
            }
            board.isWinningMove(GameBoard.PLAYER) -> {
                playerPoints++
                textViewResult.text = "Vyhral si"
                updateScore()
                updateDbMode(mode,"win")
                return true
            }
            board.gameOver -> {
                draws++
                textViewResult.text = "Remiza"
                updateScore()
                updateDbMode(mode,"draw")
                return true
            }
        }
        return false
    }

    private fun updateDbMode(mode:String,what:String){
        if(mode=="easy")
        {
            db.updateData(3,what)
        }else{
            db.updateData(4,what)
        }

    }
}