package com.example.piskvorky

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

class GameMultiPlayer : AppCompatActivity(){
    private lateinit var board: Array<Array<ImageButton>>
    private lateinit var player1ScoreTextView: TextView
    private lateinit var player2ScoreTextView: TextView
    private lateinit var drawScoreTextView: TextView
    private lateinit var player1TextView: TextView
    private lateinit var player2TextView: TextView

    private lateinit var textViewResult: TextView
    private lateinit var textViewTurn: TextView
    private var turn: Boolean = true
    private var roundCount: Int = 0
    private var player1Points: Int = 0
    private var player2Points: Int = 0
    private var draws :Int = 0
    private var gameCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        player1ScoreTextView = findViewById(R.id.player1ScoreTextView)
        player2ScoreTextView = findViewById(R.id.player2ScoreTextView)
        drawScoreTextView = findViewById(R.id.drawScoreTextView)
        textViewResult = findViewById(R.id.ResultTextView)
        player1TextView = findViewById(R.id.player1TextView)
        player2TextView = findViewById(R.id.player2TextView)
        textViewTurn = findViewById(R.id.turnTextView)

        val bundle: Bundle? = intent.extras
        val player1 = bundle!!.getString("player1")
        val player2 = bundle!!.getString("player2")
        player1TextView.text = player1
        player2TextView.text = player2

        board = Array(3){r->
            Array(3){c->
                createBoard(r, c)
            }
        }

        val btnReset: Button = findViewById(R.id.ResetButton)
        btnReset.setOnClickListener{
            clearBoard()
        }
        textViewTurn.text="Na rade: ${player1TextView.text}"

        var back = findViewById<ImageButton>(R.id.backButton)

        back.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    /**
     * Creates a board of squares and sets onClickListener for every square
     *
     */
    private fun createBoard(x:Int,y:Int): ImageButton {
        val btn: ImageButton =
            findViewById(resources.getIdentifier("btn$x$y", "id", packageName))
        btn.setOnClickListener{
            squareClick(btn)
        }
        return btn
    }

    /**
     * sets clicked square to specific symbol(x,o) and check if it was a winning move
     *
     * @param square Clicked button
     */
    private fun squareClick(square:ImageButton) {
        if(square.drawable != null) return
        if(turn){
            square.setImageResource(R.drawable.x)
            textViewTurn.text="Na rade: ${player2TextView.text}"
        }else{
            square.setImageResource(R.drawable.o)
            textViewTurn.text="Na rade: ${player1TextView.text}"
        }
        roundCount++

        if(isWinningMove()){
            if(turn) win(1) else win(2)
        }else if(roundCount == 9){
            draw()
        }else{
            turn = !turn
        }
    }

    /**
     * Check if player wins
     *
     * @param player Index of player
     */
    private fun win(player: Int) {
        if(player == 1){
            player1Points++
            textViewResult.text = "${player1TextView.text} vyhral"
        }else{
            player2Points++
            textViewResult.text = "${player2TextView.text} vyhral"
        }
        setSquares(false)
        updateScore()
    }

    /**
     * Check if it is draw
     *
     */
    private fun draw(){
        draws++
        Toast.makeText(
            applicationContext,
            "Match Draw!",
            Toast.LENGTH_SHORT).show()
        textViewResult.text = "Remiza"
        setSquares(false)

        updateScore()
    }

    /**
     * Check if it was a winning move
     *
     * @return True if game has winner
     */
    private fun isWinningMove(): Boolean {
        val squares = Array(3){x->
            Array(3){y->
                getSquare(board[x][y])
            }
        }

        for(i in 0..2){
            if((squares[i][0] == squares[i][1])&&
                (squares[i][0] == squares[i][2])&&
                (squares[i][0] != null)
            )return true
        }

        for(i in 0..2){
            if(
                (squares[0][i] == squares[1][i])&&
                (squares[0][i] == squares[2][i])&&
                (squares[0][i] != null)
            )return true
        }

        if(
            (squares[0][0] == squares[1][1])&&
            (squares[0][0] == squares[2][2])&&
            (squares[0][0] != null)
        ) return true

        if(
            (squares[0][2] == squares[1][1])&&
            (squares[0][2] == squares[2][0])&&
            (squares[0][2] != null)
        ) return true

        return false

    }

    /**
     * Gets symbol of the checked square
     *
     * @param imageButton
     * @return char of the checkedSquare
     */
    private fun getSquare(imageButton: ImageButton):Char? {
        val isChecked: Drawable? = imageButton.drawable
        val x: Drawable? = ResourcesCompat.getDrawable(resources,R.drawable.x,null)
        val o: Drawable? = ResourcesCompat.getDrawable(resources,R.drawable.o,null)
        return when(isChecked?.constantState){
            x?.constantState -> 'x'
            o?.constantState -> 'o'
            else->null
        }
    }

    /**
     * Clear the board after restart the game
     *
     */
    private fun clearBoard() {
        for (x in 0..2){
            for(y in 0..2){
                board[x][y].setImageResource(0)
            }
        }
        roundCount = 0
        gameCount++
        turn = gameCount%2 != 1
        textViewResult.text = ""
        setSquares(true)
    }

    /**
     * Update score board
     *
     */
    private fun updateScore() {
        player1ScoreTextView.text = "$player1Points"
        player2ScoreTextView.text = " $player2Points"
        drawScoreTextView.text = "$draws"
    }

    /**
     * sets squares to ether enabled or disabled in order to prevent clicking to square, or in case of enabled to reset board
     *
     * @param enabled True if reset game or false to prevent clicking
     */
    private fun setSquares(enabled:Boolean){
        for (x in 0..2){
            for(y in 0..2){
                board[x][y].isEnabled = enabled
            }
        }
    }
}