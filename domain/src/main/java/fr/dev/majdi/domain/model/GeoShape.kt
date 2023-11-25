package fr.dev.majdi.domain.model

data class GeoShape(
    var coordinates: List<List<Double>>,
    var type: String
)