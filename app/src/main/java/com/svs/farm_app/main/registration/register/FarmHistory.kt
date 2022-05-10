package com.svs.farm_app.main.registration.register

data class FarmHistory(
    var id: Int? = null,
    var farmerId: Int? = null,
    var seasonId: Int? = null,
    var seasonName: String? = null,
    var totalLandHoldingSize: Double = 0.0
)