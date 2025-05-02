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
    val colorHex: Long,
) {
    WARM_UP(
        1,
        "Warmup Set",
        "A lighter set used to prepare the muscles before heavier work",
        "W",
        0xFF90CAF9,  // Light blue
    ),
    NORMAL(
        2,
        "Normal Set",
        "A standard working set",
        "N",
        0xFF81C784,  // Light green
    ),
    FAILURE(
        3,
        "Failure Set",
        "A set performed until muscle failure",
        "F",
        0xFFFFB74D,  // Light orange
    ),
    DROP_SET(
        4,
        "Drop Set",
        "A technique where weight is reduced after reaching failure to continue the set",
        "D",
        0xFFFF8A65,  // Light red-orange
    ),
    SUPER_SET(
        5,
        "Super Set",
        "Two exercises performed back-to-back with no rest between them",
        "S",
        0xFFBA68C8,  // Light purple
    );

    companion object {
        fun fromId(id: Long?): SetType {
            return entries.find { it.id == id } ?: NORMAL
        }
    }
}
