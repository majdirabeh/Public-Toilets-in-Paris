package fr.dev.majdi.presentation.toiletsmapscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.dev.majdi.domain.model.Toilet
import fr.dev.majdi.domain.usecase.ToiletMapUseCase
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
class ToiletMapViewModel @Inject constructor(
    private val toiletMapUseCase: ToiletMapUseCase
) : ViewModel() {
    var inProgress by mutableStateOf(true)

    private val _toiletListLiveData = MutableLiveData<List<Toilet>>(null)
    val toiletListLiveData: LiveData<List<Toilet>> get() = _toiletListLiveData

    fun loadLocalToilets() {
        viewModelScope.launch(Dispatchers.IO) {
            inProgress = true
            val listToilet: List<Toilet> = toiletMapUseCase.loadAllLocalToilet()
            delay(1000)
            withContext(Dispatchers.Main) {
                _toiletListLiveData.value = null
                _toiletListLiveData.postValue(listToilet)
            }
            inProgress = false
        }
    }

}