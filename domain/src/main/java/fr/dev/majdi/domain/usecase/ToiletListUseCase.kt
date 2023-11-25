package fr.dev.majdi.domain.usecase

import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.repository.ToiletListRepository
import fr.dev.majdi.domain.usecase.base.Result
import fr.dev.majdi.domain.usecase.base.SingleUseCase
import fr.dev.majdi.domain.util.Constants.Companion.DATA_SET
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
class ToiletListUseCase @Inject constructor(
    private val toiletListRepository: ToiletListRepository
) : SingleUseCase<List<Toilet>>() {

    private var dataSet: String = DATA_SET
    private var start: String = ""
    private var rows: String = ""

    /**
     * Set params before calling Service
     */
    fun setParams(startValue: Int, rowsValue: Int) {
        start = startValue.toString()
        rows = rowsValue.toString()
    }

    /**
     * Call Service
     */
    override suspend fun serviceUseCase(): Flow<Result<List<Toilet>>> {
        return toiletListRepository.getToilets(dataSet, start, rows)
    }

    /**
     * Save local toilet data
     */
    fun saveLocalToilet(toilet: Toilet) {
        toiletListRepository.addLocalToilet(toilet)
    }

    /**
     * Delete local toilet data
     */
    fun deleteLocalToilet(toilet: Toilet) {
        toiletListRepository.deleteLocalToilet(toilet)
    }

    /**
     * Delete all local toilets data
     */
    fun deleteAllToilets() {
        toiletListRepository.deleteAllToilet()
    }

    /**
     * Update local toilet data
     */
    fun updateToilet(toilet: Toilet) {
        toiletListRepository.updateLocalToilet(toilet)
    }

    /**
     * Load local toilets list from local
     */
    fun loadListToiletFromLocal(): MutableList<Toilet> {
        return toiletListRepository.loadAllLocalToilet()
    }

}