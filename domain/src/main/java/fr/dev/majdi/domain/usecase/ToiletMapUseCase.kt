package fr.dev.majdi.domain.usecase

import fr.dev.majdi.domain.repository.ToiletMapRepository
import javax.inject.Inject

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
class ToiletMapUseCase @Inject constructor(
    private val toiletMapRepository: ToiletMapRepository
) {

}