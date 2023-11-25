package fr.dev.majdi.presentation.util

import com.mapbox.geojson.Point
import com.mapbox.turf.TurfMeasurement

/**
 * Created by Majdi RABEH on 25/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
fun areCoordinatesEqual(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean {
    val decimalMultiplier = 100.0 // for 2 decimal places

    val roundedLat1 = (lat1 * decimalMultiplier).toInt()
    val roundedLon1 = (lon1 * decimalMultiplier).toInt()

    val roundedLat2 = (lat2 * decimalMultiplier).toInt()
    val roundedLon2 = (lon2 * decimalMultiplier).toInt()

    return (roundedLat1 == roundedLat2) && (roundedLon1 == roundedLon2)
}

fun calculateDistance(point1: Point, point2: Point): Double {
    return TurfMeasurement.distance(
        point1,
        point2
    )
}