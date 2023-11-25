package fr.dev.majdi.data.repository

import fr.dev.majdi.data.datasource.local.AppDatabase
import fr.dev.majdi.data.datasource.remote.ToiletService
import fr.dev.majdi.data.mapper.toEntity
import fr.dev.majdi.data.mapper.toToilet
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.repository.ToiletListRepository
import fr.dev.majdi.domain.usecase.base.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
class ToiletListRepositoryImp(
    private val database: AppDatabase,
    private val toiletService: ToiletService
) : ToiletListRepository {

    /**
     * Get List of Toilets from Service
     */
    override fun getToilets(
        dataset: String,
        start: String,
        rows: String
    ): Flow<Result<List<Toilet>>> = flow {
        try {
            val toilets = toiletService.searchToilet(dataset, start, rows).records
            if (toilets.isEmpty()) {
                emit(Result.Failure(Throwable("Toilet List is Empty")))
            } else {
                emit(Result.Success(toilets))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Save Toilet Entity to Room
     */
    override fun addLocalToilet(toilet: Toilet) {
        database.toiletDao.insert(toilet.toEntity())
    }

    /**
     * Delete Toilet Entity from Room
     */
    override fun deleteLocalToilet(toilet: Toilet) {
        database.toiletDao.delete(toilet.toEntity())
    }

    /**
     * Delete all Toilet Entities from Room
     */
    override fun deleteAllToilet() {
        database.toiletDao.deleteAll()
    }

    /**
     * Update Toilet Entity on Room
     */
    override fun updateLocalToilet(toilet: Toilet) {
        database.toiletDao.update(toilet.toEntity())
    }

    /**
     * Load all Toilet Entity saved in Room
     */
    override fun loadAllLocalToilet(): MutableList<Toilet> {
        val listToilet = mutableListOf<Toilet>()
        if (database.toiletDao.loadAll().isNotEmpty()) {
            database.toiletDao.loadAll().map {
                listToilet.add(it.toToilet())
            }
        }
        return listToilet
    }

}