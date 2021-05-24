package com.example.piskvorky

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.piskvorky.db.dbHelper

class Stats :AppCompatActivity(){

    lateinit var easyWin: TextView
    lateinit var easyLose: TextView
    lateinit var easyDraw: TextView

    lateinit var impossibleWin: TextView
    lateinit var impossibleLose: TextView
    lateinit var impossibleDraw: TextView
    val context = this
    var db = dbHelper(context)

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

        var resetStats = findViewById<Button>(R.id.resetStatsButton)

        resetStats.setOnClickListener{
            db.resetData()
            getData()
        }
    }

    fun getData(){
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