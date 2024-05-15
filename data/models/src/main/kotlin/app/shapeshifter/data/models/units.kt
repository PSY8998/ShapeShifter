@file:Suppress("ktlint:filename")

package app.shapeshifter.data.models

@JvmInline
value class PositiveInt(
    val value: Int,
) {
    init {
        require(value >= 0) {
            "$value should be greater than 0"
        }
    }

    override fun toString(): String {
        return value.toString()
    }
}
