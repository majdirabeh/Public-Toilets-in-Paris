package fr.dev.majdi.domain.repository

import fr.dev.majdi.domain.model.Toilet

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
interface ToiletMapRepository {
    /**
     * Load toilets from database for the map
     */
    fun loadAllLocalToilet(): MutableList<Toilet>
}