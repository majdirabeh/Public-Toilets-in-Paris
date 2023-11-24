package fr.dev.majdi.domain.model

data class Fields(
    val acces_pmr: String,
    val adresse: String,
    val arrondissement: Int,
    val complement_adresse: String,
    val geo_point_2d: List<Double>,
    val geo_shape: GeoShape,
    val gestionnaire: String,
    val horaire: String,
    val source: String,
    val type: String
)