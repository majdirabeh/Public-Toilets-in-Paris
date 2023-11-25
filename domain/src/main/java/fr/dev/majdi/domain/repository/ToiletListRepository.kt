package fr.dev.majdi.domain.repository

import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.usecase.base.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
interface ToiletListRepository {
    /**
     * Get List of Toilets from API
     */
    fun getToilets(
        dataset: String,
        start: String = "",
        rows: String = ""
    ): Flow<Result<List<Toilet>>>

    /**
     * Save Toilet data on database
     */
    fun addLocalToilet(toilet: Toilet)

    /**
     * Delete Toilet data from database
     */
    fun deleteLocalToilet(toilet: Toilet)

    /**
     * Delete all toilets from database
     */
    fun deleteAllToilet()

    /**
     * Update local Toilet
     */
    fun updateLocalToilet(toilet: Toilet)

    fun loadAllLocalToilet(): MutableList<Toilet>

}