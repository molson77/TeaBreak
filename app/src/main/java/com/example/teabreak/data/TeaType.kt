package com.example.teabreak.data

enum class TeaType {
    GREEN,
    BLACK,
    OOLONG,
    WHITE,
    PU_ERH,
    PURPLE,
    ROOIBOS,
    MATE,
    HERBAL
}

fun TeaType.getName(): String {
    return when(this) {
        TeaType.GREEN -> "Green"
        TeaType.BLACK -> "Black"
        TeaType.OOLONG -> "Oolong"
        TeaType.WHITE -> "White"
        TeaType.PU_ERH -> "Pu-erh"
        TeaType.PURPLE -> "Purple"
        TeaType.ROOIBOS -> "Rooibos"
        TeaType.MATE -> "Mate"
        TeaType.HERBAL -> "Herbal"
    }
}
