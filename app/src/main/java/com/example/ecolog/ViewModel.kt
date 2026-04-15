package com.example.ecolog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(
    private val dataStore: DataStoreManager
) : ViewModel() {

    val logs = dataStore.logsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addLog(name: String, category: String, emission: Double) {
        viewModelScope.launch {
            dataStore.addLog(
                CarbonLog(
                    id = UUID.randomUUID().toString(),
                    activityName = name,
                    category = category,
                    carbonEmission = emission
                )
            )
        }
    }

    fun deleteLog(id: String) {
        viewModelScope.launch {
            dataStore.deleteLog(id)
        }
    }

    companion object {
        fun provideFactory(dataStore: DataStoreManager): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(dataStore) as T
            }
        }
    }
}
