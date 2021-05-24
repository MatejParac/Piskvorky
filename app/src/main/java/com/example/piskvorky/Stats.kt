package com.example.piskvorky

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.piskvorky.db.dbHelper

class Stats :AppCompatActivity(){

    private lateinit var easyWin: TextView
    private lateinit var easyLose: TextView
    private lateinit var easyDraw: TextView

    private lateinit var impossibleWin: TextView
    private lateinit var impossibleLose: TextView
    private lateinit var impossibleDraw: TextView
    private val context = this
    private var db = dbHelper(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        easyWin = findViewById(R.id.easyWinTv)
        easyLose = findViewById(R.id.easyLoseTv)
        easyDraw = findViewById(R.id.easyDrawTv)
        impossibleWin = findViewById(R.id.impossibleWinTv)
        impossibleLose = findViewById(R.id.impossibleLoseTv)
        impossibleDraw = findViewById(R.id.impossibleDrawTv)

        getData()

        val resetStats = findViewById<Button>(R.id.resetStatsButton)

        resetStats.setOnClickListener{
            db.resetData()
            getData()
        }
    }

    /**
     * Write data from database to text views
     *
     */
    private fun getData(){
        var data = db.readData()

        for(i in 0..(data.size-1)){
            if(i==0){
                easyWin.text = data.get(i).win.toString()
                easyLose.text = data.get(i).lose.toString()
                easyDraw.text = data.get(i).draw.toString()
            }else{
                impossibleWin.text = data.get(i).win.toString()
                impossibleLose.text = data.get(i).lose.toString()
                impossibleDraw.text = data.get(i).draw.toString()
            }
        }
    }
}