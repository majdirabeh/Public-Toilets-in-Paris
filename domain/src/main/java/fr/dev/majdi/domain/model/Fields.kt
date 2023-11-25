package fr.dev.majdi.domain.model

data class Fields(
    var acces_pmr: String?,
    var adresse: String?,
    var arrondissement: Int,
    var complement_adresse: String,
    var geo_point_2d: List<Double>,
    var geo_shape: GeoShape,
    var gestionnaire: String,
    var horaire: String?,
    var source: String,
    var type: String
)