package fr.dev.majdi.data.mapper

import fr.dev.majdi.data.datasource.local.entity.ToiletEntity
import fr.dev.majdi.domain.model.Toilet

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
fun Toilet.toEntity() = ToiletEntity(
    recordId = recordid,
    complementAddress = fields.complement_adresse,
    horaire = fields.horaire,
    accesPmr = fields.acces_pmr,
    arrondissement = fields.arrondissement,
    source = fields.source,
    gestionnaire = fields.gestionnaire,
    adresse = fields.adresse,
    type = fields.type,
    latitudeGeometry = geometry.coordinates.first(),
    longitudeGeometry = geometry.coordinates.last()
)