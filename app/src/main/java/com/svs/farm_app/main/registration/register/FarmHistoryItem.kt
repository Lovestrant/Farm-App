package com.svs.farm_app.main.registration.register

import com.svs.farm_app.entities.OtherCrops

data class FarmHistoryItem(
    var id: Int? = null,
    var crop: OtherCrops? = null,
    var farmHistoryId: Int? = null,
    var cropId: Int? = crop?.cropID?.toInt(),
    var acres: Double = 0.0,
    var weight: Double = 0.0,
    var landOwned: Boolean=false
)