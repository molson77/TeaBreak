package com.example.teabreak.data

enum class ScoopUnit {
    TEASPOON,
    TEASPOON_HEAPING,
    TABLESPOON,
    TABLESPOON_HEAPING
}

fun ScoopUnit.getName(): String {
    return when(this) {
        ScoopUnit.TEASPOON -> "Tsp."
        ScoopUnit.TEASPOON_HEAPING -> "Tsp. (H)"
        ScoopUnit.TABLESPOON -> "Tbsp."
        ScoopUnit.TABLESPOON_HEAPING -> "Tbsp. (H)"
    }
}
