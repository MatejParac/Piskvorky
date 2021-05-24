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
            setBoard()
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

    /**
     * Creates a board of squares and sets onClickListener for every square
     *
     */
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

    /**
     * sets clicked square to specific symbol(x,o) and sets square isEnabled to false to prevent repeat clicking
     *
     */
    private fun setBoard() {
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

    /**
     * After player click, method make move and check if it was a winning move
     *
     * @property x Row index of square
     * @property y Column index of square
     * @property mode Specifie mode of the game
     */
    inner class Mode(private val x: Int,private val y: Int,private val mode:String): View.OnClickListener {
        override fun onClick(p0: View?) {

            if (!board.gameOver) {
                val square = Square(x, y)
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
                setBoard()
            }


        }
    }

    /**
     * Updates score board
     *
     */
    @SuppressLint("SetTextI18n")
    private fun updateScore() {
        player1ScoreTextView.text = "$playerPoints"
        player2ScoreTextView.text = " $pcPoints"
        drawScoreTextView.text = "$draws"
    }

    /**
     * Check if the game is over and chooses the winner
     *
     * @return True if game has winner
     */
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

    /**
     * Updates score in database
     *
     * @param mode Mode of the game
     * @param what Which column od database is supposed to be updated
     */
    private fun updateDbMode(mode:String,what:String){
        if(mode=="easy")
        {
            db.updateData(3,what)
        }else{
            db.updateData(4,what)
        }

    }
}