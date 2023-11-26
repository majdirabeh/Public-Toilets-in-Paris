package fr.dev.majdi.presentation.toiletslistscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.usecase.ToiletListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    val _isInternetAvailable = MutableLiveData(true)
    val isInternetAvailable: LiveData<Boolean> get() = _isInternetAvailable

    private val _toiletListLiveData = MutableLiveData<List<Toilet>>(null)
    val toiletListLiveData: LiveData<List<Toilet>> get() = _toiletListLiveData

    private val _filteredToiletListLiveData = MutableLiveData<List<Toilet>>()
    val filteredToiletListLiveData: LiveData<List<Toilet>> get() = _filteredToiletListLiveData

    var inProgress by mutableStateOf(true)

    var isToiletItemClicked by mutableStateOf(false)
    var selectedToilet by mutableStateOf<Toilet?>(null)


    fun getToiletListFromService(itemsToLoad: Int = pageSize) {
        if (!inProgress) {
            // Loading is already in progress, ignore the new request.
            return
        }

        toiletListUseCase.setParams(startValue = currentPage * pageSize, rowsValue = itemsToLoad)

        toiletListUseCase.execute(
            onSuccess = { newData ->
                viewModelScope.launch(Dispatchers.IO) {
                    if (newData.isNotEmpty()) {
                        toiletListUseCase.deleteAllToilets()
                    }

                    val currentList = _toiletListLiveData.value ?: emptyList()
                    val updatedList = currentList + newData
                    updatedList.map {
                        toiletListUseCase.saveLocalToilet(it)
                    }
                    withContext(Dispatchers.Main){
                        _toiletListLiveData.postValue(updatedList)
                    }

                    currentPage++
                }
            },
            onError = { error ->
                error.printStackTrace()
            },
            onFinished = {
                inProgress = false
                //If we want we can get local data if the service not available or any problem
                if (toiletListLiveData.value?.isEmpty() == true) {
                    loadToiletsFromLocal()
                }
            }
        )
    }

    // Function to filter toilets by PMR accessibility
    fun filterToiletsByPmr(accessPmr: Boolean, showAllToilets: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                if (showAllToilets) {
                    _filteredToiletListLiveData.postValue(toiletListLiveData.value)
                } else {
                    _filteredToiletListLiveData.value = if (accessPmr) {
                        toiletListLiveData.value?.filter { it.fields.acces_pmr == "Oui" }
                    } else {
                        toiletListLiveData.value?.filter { it.fields.acces_pmr == "Non" }
                    }
                }
            }
        }
    }

    fun loadToiletsFromLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            val listToilet: List<Toilet> = toiletListUseCase.loadListToiletFromLocal()
            withContext(Dispatchers.Main) {
                _toiletListLiveData.value = null
                _toiletListLiveData.postValue(listToilet)
            }
            inProgress = false
        }
    }

}