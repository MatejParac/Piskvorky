package com.example.piskvorky.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DATABASE_NAME = "MyDB"
val TABLE_NAME = "DbRecords"
val COL_LOSE = "lose"
val COL_WIN = "win"
val COL_DRAW = "draw"
val COL_ID = "id"


/**
 * Class for work with database
 *
 * @constructor
 * Creates a DbHelper
 *
 * @param context
 */
class dbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_WIN + " INTEGER," +
                COL_DRAW + " INTEGER," +
                COL_LOSE + " INTEGER)"
        db?.execSQL(createTable)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    /**
     * Inserts record to database
     *
     * @param record Inserted record
      */
    fun insertData(record:dbRecord){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_WIN,record.win)
        cv.put(COL_LOSE,record.lose)
        cv.put(COL_DRAW,record.draw)

        var result = db.insert(TABLE_NAME,null,cv)
    }

    /**
     * Gets records from database
     *
     * @return List of records
     */
    fun readData():MutableList<dbRecord>{
        var list:MutableList<dbRecord> = ArrayList()

        var db = this.readableDatabase

        var query = "Select * from "+ TABLE_NAME
        var result = db.rawQuery(query,null)

        if(result.moveToFirst()){
            do{
                var record = dbRecord()
                record.win = result.getString(result.getColumnIndex(COL_WIN)).toInt()
                record.lose = result.getString(result.getColumnIndex(COL_LOSE)).toInt()
                record.draw = result.getString(result.getColumnIndex(COL_DRAW)).toInt()
                record.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                list.add(record)
            }while(result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }


    fun deleteData(){
        val db = this.writableDatabase

        db.delete(TABLE_NAME, null,null)
        db.close()
    }

    /**
     * Update the specific record of database
     *
     * @param id Which mode is supposed to be updated
     * @param col Which column is supposed to by updated
     */
    fun updateData(id:Int,col:String){
        val db = this.writableDatabase
        var query = "Select * from $TABLE_NAME where $COL_ID==$id"
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                var cv = ContentValues()
                when{
                    col=="win" ->cv.put(COL_WIN,(result.getInt(result.getColumnIndex(COL_WIN))+1))
                    col=="lose" ->cv.put(COL_LOSE,(result.getInt(result.getColumnIndex(COL_LOSE))+1))
                    col=="draw" ->cv.put(COL_DRAW,(result.getInt(result.getColumnIndex(COL_DRAW))+1))
                }
                db.update(TABLE_NAME,cv, "$COL_ID=?",arrayOf(result.getString(result.getColumnIndex(COL_ID))))
            }while(result.moveToNext())
        }
    }

    /**
     * Sets records to 0 value
     *
     */
    fun resetData(){
        val db = this.writableDatabase
        var query = "Select * from $TABLE_NAME"
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                var cv = ContentValues()

                    cv.put(COL_WIN,0)
                    cv.put(COL_LOSE,0)
                    cv.put(COL_DRAW,0)

                db.update(TABLE_NAME,cv, "$COL_ID=?",arrayOf(result.getString(result.getColumnIndex(COL_ID))))
            }while(result.moveToNext())
        }
    }
}