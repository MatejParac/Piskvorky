package com.example.piskvorky

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 *Choose game mode
 *
 */
class SinglePlayerMode :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singleplayer_mode)

        var button = findViewById<Button>(R.id.buttonEasy)

        button.setOnClickListener{
            val intent = Intent(this,GameSinglePlayer::class.java)
            intent.putExtra("mode","easy")
            startActivity(intent)
        }

        var buttonSinglePlayer = findViewById<Button>(R.id.buttonImposible)

        buttonSinglePlayer.setOnClickListener{
            val intent = Intent(this,GameSinglePlayer::class.java)
            intent.putExtra("mode","imposible")
            startActivity(intent)
        }

        var back = findViewById<ImageButton>(R.id.backSMButton)

        back.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}