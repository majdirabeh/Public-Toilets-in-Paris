package fr.dev.majdi.presentation.toiletdetailscreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Majdi RABEH on 24/11/2023.
 * Email m.rabeh.majdi@gmail.com
 */
@HiltViewModel
class ToiletDetailViewModel @Inject constructor(
    //we can add use case detail if we want to get detail from service
) : ViewModel() {
    // Add logic of getting detail Toilet from API if we need
}