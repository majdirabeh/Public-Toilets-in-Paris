package fr.dev.majdi.domain.model

data class Toilet(
    var datasetid: String,
    var fields: Fields,
    var geometry: Geometry,
    var record_timestamp: String,
    var recordid: String
)