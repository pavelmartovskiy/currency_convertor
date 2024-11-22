package com.pm.ce.presentation.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pm.ce.domain.CeSubscription
import com.pm.ce.domain.Rates
import kotlinx.coroutines.launch

class HomeViewModel(
    val ratesSubscription: CeSubscription<Unit, Rates>
) : ViewModel() {
    init {
        Log.d("HomeViewModel", "HomeViewModel created!")
        viewModelScope.launch {
            ratesSubscription
                .subscribe(params = Unit)
                .collect { rates ->
                    Log.d("HomeViewModel", "rates : $rates")
                }
        }

    }
}