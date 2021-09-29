package com.fjr.crudpagging


/**
 * Created by Franky Wijanarko on 28/09/21.
 * frank.jr.619@gmail.com
 */
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

class SampleViewModel : ViewModel() {

    private val _pagingFlow = MutableStateFlow<PagingData<SampleEntity>?>(null)
    val pagingFlow = _pagingFlow.asStateFlow()

//    private val _pagingDataViewStates =
//
//
//    val pagingDataViewStates: LiveData<PagingData<SampleEntity>> = _pagingDataViewStates

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = SampleRepository.PAGE_SIZE)) {
                SamplePagingSource(
                    SampleRepository()
                )
            }.flow
                .cachedIn(viewModelScope)
                .collect {
                    _pagingFlow.value = it
                }
        }
    }

    fun onViewEvent(sampleViewEvents: SampleViewEvents) {
        val paingData = pagingFlow.value ?: return

        when (sampleViewEvents) {
            is SampleViewEvents.Remove -> {
                paingData
                    .filter { sampleViewEvents.sampleEntity.id != it.id }
                    .let { _pagingFlow.value = it }
            }
            is SampleViewEvents.Edit -> {
                paingData
                    .map {
                        if (sampleViewEvents.sampleEntity.id == it.id) {
                            Log.e("TAG","-- ${sampleViewEvents.sampleEntity.id} ${it.id}")
                            return@map it.copy(name = "${it.name} (updated)")
                        }
                        else return@map it
                    }
                    .let { _pagingFlow.value = it }
            }
            is SampleViewEvents.InsertItemHeader -> {
                _pagingFlow.value = paingData.insertHeaderItem(
                    TerminalSeparatorType.FULLY_COMPLETE,
                    SampleEntity(
                        id = Random.nextInt(0, 1000),
                        name = "New item added at the top"
                    )
                )
            }
            is SampleViewEvents.InsertItemFooter -> {
                _pagingFlow.value = paingData.insertFooterItem(
                    TerminalSeparatorType.FULLY_COMPLETE,
                    SampleEntity(
                        id = Random.nextInt(0, 1000),
                        name = "New item added at the bottom"
                    )
                )
            }
        }
    }
}