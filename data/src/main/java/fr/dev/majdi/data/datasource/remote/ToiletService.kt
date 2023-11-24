package fr.dev.majdi.data.datasource.remote

import fr.dev.majdi.domain.model.ToiletsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Majdi RABEH on 23/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
interface ToiletService {

    @GET("search/")
    suspend fun searchToilet(
        @Query("dataset") dataset: String,
        @Query("start") start: String,
        @Query("rows") rows: String
    ): ToiletsResponse

}