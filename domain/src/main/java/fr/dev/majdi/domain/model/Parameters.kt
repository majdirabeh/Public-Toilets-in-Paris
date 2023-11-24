package fr.dev.majdi.domain.model

data class Parameters(
    val dataset: String,
    val format: String,
    val rows: Int,
    val start: Int,
    val timezone: String
)