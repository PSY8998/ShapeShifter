package app.shapeshifter.data.models

/**
 * Enum representation of set types used in the application.
 * Maps to the database set_type table IDs.
 */
enum class SetType(
    val id: Long,
    val label: String,
    val description: String,
    val abbreviation: String,
) {
    WARM_UP(1, "Warmup Set", "A lighter set used to prepare the muscles before heavier work", "W"),
    NORMAL(2, "Normal Set", "A standard working set", "N"),
    FAILURE(3, "Failure Set", "A set performed until muscle failure", "F"),
    DROP_SET(
        4,
        "Drop Set",
        "A technique where weight is reduced after reaching failure to continue the set",
        "D",
    ),
    SUPER_SET(
        5,
        "Super Set",
        "Two exercises performed back-to-back with no rest between them",
        "S",
    );

    companion object {
        fun fromId(id: Long?): SetType {
            return values().find { it.id == id } ?: NORMAL
        }
    }
}
