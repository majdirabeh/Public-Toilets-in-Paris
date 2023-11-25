package fr.dev.majdi.domain.usecase.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
abstract class SingleUseCase<T> : UseCase() {

    internal abstract suspend fun serviceUseCase(): Flow<Result<T>>

    fun execute(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onFinished: () -> Unit = {}
    ) {
        cancelLast()
        lastJob = CoroutineScope(dispatcher).launch {
            serviceUseCase().flowOn(dispatcher).collect { result ->
                when (result) {
                    is Result.Success -> onSuccess(result.data)
                    is Result.Failure -> onError(result.exception)
                }
                onFinished()
            }
        }
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
}
