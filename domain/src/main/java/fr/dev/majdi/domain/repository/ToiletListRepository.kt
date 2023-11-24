package fr.dev.majdi.domain.repository

import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.usecase.base.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
interface ToiletListRepository {
    fun getToilets(
        dataset: String,
        start: String = "",
        rows: String = ""
    ): Flow<Result<List<Toilet>>>
}