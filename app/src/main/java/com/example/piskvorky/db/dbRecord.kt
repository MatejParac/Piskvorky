package com.example.piskvorky.db

class dbRecord {
    var id:Int = 0
    var lose:Int = 0
    var win:Int = 0
    var draw:Int = 0

    constructor(w:Int,d:Int,l:Int){
        this.lose = l
        this.win = w
        this.draw = d
    }
    constructor(){

    }
}