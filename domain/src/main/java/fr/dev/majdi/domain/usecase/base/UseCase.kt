package fr.dev.majdi.domain.usecase.base

import kotlinx.coroutines.Job

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
abstract class UseCase {
    protected var lastJob: Job? = null

    fun cancelLast() {
        lastJob?.cancel()
    }

    fun cancel() {
        lastJob?.cancel()
    }
}