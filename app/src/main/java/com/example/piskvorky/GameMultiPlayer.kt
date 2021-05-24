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

    private fun createBoard(x:Int,y:Int): ImageButton {
        val btn: ImageButton =
            findViewById(resources.getIdentifier("btn$x$y", "id", packageName))
        btn.setOnClickListener{
            squareClick(btn)
        }
        return btn
    }

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

    private fun isWinningMove(): Boolean {
        val fields = Array(3){x->
            Array(3){y->
                getSquare(board[x][y])
            }
        }

        for(i in 0..2){
            if((fields[i][0] == fields[i][1])&&
                (fields[i][0] == fields[i][2])&&
                (fields[i][0] != null)
            )return true
        }

        for(i in 0..2){
            if(
                (fields[0][i] == fields[1][i])&&
                (fields[0][i] == fields[2][i])&&
                (fields[0][i] != null)
            )return true
        }

        if(
            (fields[0][0] == fields[1][1])&&
            (fields[0][0] == fields[2][2])&&
            (fields[0][0] != null)
        ) return true

        if(
            (fields[0][2] == fields[1][1])&&
            (fields[0][2] == fields[2][0])&&
            (fields[0][2] != null)
        ) return true

        return false

    }

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

    private fun updateScore() {
        player1ScoreTextView.text = "$player1Points"
        player2ScoreTextView.text = " $player2Points"
        drawScoreTextView.text = "$draws"
    }

    private fun setSquares(enabled:Boolean){
        for (x in 0..2){
            for(y in 0..2){
                board[x][y].isEnabled = enabled
            }
        }
    }
}