package fr.dev.majdi.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Majdi RABEH on 23/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@Entity(tableName = "Toilet")
data class ToiletEntity(

    @PrimaryKey
    val recordId: String,
    val complementAddress: String,
    val horaire: String?,
    val accesPmr: String?,
    val arrondissement: Int,
    val source: String,
    val gestionnaire: String,
    val adresse: String?,
    val type: String,
    val latitudeGeometry: Double,
    val longitudeGeometry: Double
)