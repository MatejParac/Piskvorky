package com.example.piskvorky

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.piskvorky.db.dbHelper
import com.example.piskvorky.db.dbRecord

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var multiPlayerbutton = findViewById<Button>(R.id.multiPlayerButton)

        multiPlayerbutton.setOnClickListener{
            val intent = Intent(this,MultiPlayerMode::class.java)
            startActivity(intent)
        }

        var singlePlayerbutton = findViewById<Button>(R.id.singlePlayerButton)

        singlePlayerbutton.setOnClickListener{
            val intent = Intent(this,SinglePlayerMode::class.java)
            startActivity(intent)
        }

        var statsButton = findViewById<Button>(R.id.statsButton)

        statsButton.setOnClickListener{
            val intent = Intent(this,Stats::class.java)
            startActivity(intent)
        }

    }
}