package fr.dev.majdi.data.repository

import fr.dev.majdi.data.datasource.local.AppDatabase
import fr.dev.majdi.data.datasource.remote.ToiletService
import fr.dev.majdi.data.mapper.toToilet
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.repository.ToiletMapRepository

/**
 * Created by Majdi RABEH on 25/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
class ToiletMapRepositoryImp(
    private val database: AppDatabase,
    private val toiletService: ToiletService
) : ToiletMapRepository {

    //In the Mapbox I will just get Data from Database if exist
    override fun loadAllLocalToilet(): MutableList<Toilet> {
        val listToilet = mutableListOf<Toilet>()
        if (database.toiletDao.loadAll().isNotEmpty()) {
            database.toiletDao.loadAll().map {
                listToilet.add(it.toToilet())
            }
        }
        return listToilet
    }

    //We can call api here to fetch list of toilets or get detail by API
    //TODO

}