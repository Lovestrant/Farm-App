package com.svs.farm_app.main.registration.update

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.svs.farm_app.entities.*
import com.svs.farm_app.main.registration.RegisterFarmerActivity
import com.svs.farm_app.utils.DatabaseHandler
import com.svs.farm_app.utils.Preferences

class UpdateFarmerViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val db = DatabaseHandler(context)

    fun getVillages(): MutableList<Village> = db.villages

    fun getSubVillages(villageId: String): MutableList<SubVillage> =
        db.getDynamicSubVillages(villageId)

    fun getCrops(): MutableList<OtherCrops> = db.allOtherCrops()


    fun addFarmer(registerData: RegisteredFarmer) {

        db.updateFarmer(
            registerData
        )
    }

}