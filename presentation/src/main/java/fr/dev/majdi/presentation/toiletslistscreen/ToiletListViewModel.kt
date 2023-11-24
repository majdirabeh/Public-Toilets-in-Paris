package fr.dev.majdi.presentation.toiletslistscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.usecase.ToiletListUseCase
import javax.inject.Inject

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@HiltViewModel
class ToiletListViewModel @Inject constructor(
    private val toiletListUseCase: ToiletListUseCase
) : ViewModel() {

    private val INITIAL_PAGE = 0
    private var currentPage = INITIAL_PAGE
    private var pageSize = 10 // Initial page size

    private val _toiletListLiveData = MutableLiveData<List<Toilet>>(null)
    val toiletListLiveData: LiveData<List<Toilet>> get() = _toiletListLiveData

    var inProgress by mutableStateOf(true)

    fun loadToiletList(itemsToLoad: Int = pageSize) {
        if (!inProgress) {
            // Loading is already in progress, ignore the new request.
            return
        }

        toiletListUseCase.setParams(startValue = currentPage * pageSize, rowsValue = itemsToLoad)

        toiletListUseCase.execute(
            onSuccess = { newData ->
                val currentList = _toiletListLiveData.value ?: emptyList()
                val updatedList = currentList + newData

                _toiletListLiveData.postValue(updatedList)
                currentPage++
            },
            onError = { error ->
                error.printStackTrace()
            },
            onFinished = {
                inProgress = false
            }
        )
    }

}