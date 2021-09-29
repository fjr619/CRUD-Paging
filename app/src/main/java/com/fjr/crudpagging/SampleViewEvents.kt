package com.fjr.crudpagging


/**
 * Created by Franky Wijanarko on 28/09/21.
 * frank.jr.619@gmail.com
 */
sealed class SampleViewEvents {
    data class Edit(val sampleEntity: SampleEntity) : SampleViewEvents()
    data class Remove(val sampleEntity: SampleEntity) : SampleViewEvents()
    object InsertItemHeader : SampleViewEvents()
    object InsertItemFooter : SampleViewEvents()
}