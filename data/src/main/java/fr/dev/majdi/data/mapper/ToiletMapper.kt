package fr.dev.majdi.data.mapper

import fr.dev.majdi.data.datasource.local.entity.ToiletEntity
import fr.dev.majdi.domain.model.Fields
import fr.dev.majdi.domain.model.GeoShape
import fr.dev.majdi.domain.model.Geometry
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.util.Constants

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
/**
 * Convert data model from data source to data local
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

/**
 * Convert data local to data source or data to show in interface
 */
fun ToiletEntity.toToilet() = Toilet(
    recordid = this.recordId,
    fields = Fields(
        this.accesPmr,
        this.adresse,
        this.arrondissement,
        this.complementAddress,
        listOf<Double>(this.latitudeGeometry, this.longitudeGeometry),
        GeoShape(listOf(listOf(this.latitudeGeometry, this.longitudeGeometry)), ""),
        this.gestionnaire,
        this.horaire,
        this.source,
        this.type
    ),
    datasetid = Constants.DATA_SET,
    geometry = Geometry(listOf(this.latitudeGeometry, this.longitudeGeometry), ""),
    record_timestamp = ""
)