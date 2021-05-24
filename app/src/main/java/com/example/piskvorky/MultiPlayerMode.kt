package com.example.piskvorky

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MultiPlayerMode :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer_mode)

        var startGameButton = findViewById<Button>(R.id.startGameSinglePlayerButton)

        startGameButton.setOnClickListener{
            val player1 = findViewById<TextView>(R.id.Player1NameEditText)
            val player2 = findViewById<TextView>(R.id.Player2NameEditText)
            val intent = Intent(this,GameMultiPlayer::class.java)
            intent.putExtra("player1",player1.text.toString())
            intent.putExtra("player2",player2.text.toString())
            startActivity(intent)
        }

        var back = findViewById<ImageButton>(R.id.backMMButton)

        back.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}