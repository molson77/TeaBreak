package com.example.teabreak

class TeaItem {

    private val id: Int
    var name: String
    var type: String
    var origin: String
    var amount: String
    var temp: Int
    var time: Int

    constructor(id: Int, name: String, type: String, origin: String, amount: String, temp: Int, time: Int) {
        this.id = id
        this.name = name
        this.type = type
        this.origin = origin
        this.amount = amount
        this.temp = temp
        this.time = time

    }

    fun getId(): Int {
        return id
    }
}