package com.svs.farm_app.main.registration.register

import com.svs.farm_app.entities.Village

data class FarmAreaItem(
    var village: Village? = null,
    var estimatedFarmArea: Double = 0.0
)