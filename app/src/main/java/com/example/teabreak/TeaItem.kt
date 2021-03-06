package com.example.teabreak


/**
 * TeaItem:
 *
 * @desc Class representing a certain type of tea.
 *
 * @property name the name of the tea
 * @property type classification of the tea
 * @property amount the amount of tea leaves infused per 8oz. of water, measured in tsp. or tbsp.
 * @property temp the water temperature at which to properly steep the tea
 * @property time how long to steep the tea
 * @property img image of the tea, either from camera or gallery
 */

class TeaItem(
    private val id: Int,
    var name: String,
    var type: String,
    var amount: String,
    var temp: Int,
    var time: Int,
    var img: ByteArray
) {

    fun getId(): Int {
        return id
    }
}