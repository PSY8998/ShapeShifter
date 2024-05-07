package app.shapeshifter.data.models

enum class Muscles(val displayName: String) {
    ABS("Abs"),
    OBLIQUES("Obliques"),
    INNER_THIGHS("Inner Thighs"),
    BICEPS("Biceps"),
    CALVES("Calves"),
    HEART("Heart"),
    CHEST("Chest"),
    FOREARMS("Forearms"),
    FULL_BODY("Full Body"),
    GLUTES("Glutes"),
    HAMSTRINGS("Hamstrings"),
    LATS("Lats"),
    LOWER_BACK("Lower Back"),
    NECK("Neck"),
    QUADS("Quads"),
    SHOULDERS("Shoulders"),
    TRAPS("Traps"),
    TRICEPS("Triceps"),
    UPPER_BACK("UpperBack"),
    OTHER("Other"),
    ;

    companion object {
        fun safeValueOf(name: String): Muscles =
            entries.find { it.name == name } ?: OTHER
    }
}
