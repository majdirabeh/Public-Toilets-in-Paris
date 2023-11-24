package fr.dev.majdi.domain.model

data class ToiletsResponse(
    val nhits: Int,
    val parameters: Parameters,
    val records: List<Toilet>
)