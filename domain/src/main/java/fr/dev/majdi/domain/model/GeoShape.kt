package fr.dev.majdi.domain.model

data class GeoShape(
    val coordinates: List<List<Double>>,
    val type: String
)