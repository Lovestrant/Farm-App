package com.svs.farm_app.main.registration.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.svs.farm_app.entities.Farmers
import com.svs.farm_app.entities.OtherCrops
import com.svs.farm_app.entities.SubVillage
import com.svs.farm_app.entities.Village
import com.svs.farm_app.main.registration.RegisterFarmerActivity
import com.svs.farm_app.utils.DatabaseHandler
import com.svs.farm_app.utils.Preferences

class RegisterFarmerViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val db = DatabaseHandler(context)

    fun getVillages(): MutableList<Village> = db.villages

    fun getSubVillages(villageId: String): MutableList<SubVillage> =
        db.getDynamicSubVillages(villageId)

    fun getCrops(): MutableList<OtherCrops> = db.allOtherCrops()


    fun addFarmer(registerData: RegisterFarmerActivity.RegisterData) {

        db.addFarmer(
            Farmers(
                registerData.firstName,
                registerData.lastName,
                registerData.gender,
                registerData.idNumber,
                registerData.phone,
                registerData.email,
                registerData.postalAddress,
                registerData.village?.villageID,
                registerData.subVillage?.subVillageID,
                registerData.farmerPic,
                registerData.leftThumb,
                registerData.rightThumb,
                registerData.latitude.toString(),
                registerData.longitude.toString(),
                registerData.showsIntent,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Preferences.COMPANY_ID,
                Preferences.USER_ID,
                registerData.contractNo
            ).apply {
                if (!registerData.farmHistoryItems.isNullOrEmpty()) {
                    this.otherCropsOne = registerData.farmHistoryItems!![0].crop?.cropID
                    if (registerData.farmHistoryItems!!.size > 1) {
                        this.otherCropsTwo = registerData.farmHistoryItems!![1].crop?.cropID
                    }
                    if (registerData.farmHistoryItems!!.size > 2) {
                        this.otherCropsThree = registerData.farmHistoryItems!![2].crop?.cropID
                    }

                    if (!registerData.farmAreaItems.isNullOrEmpty()) {
                        this.farmVidOne = registerData.farmAreaItems!![0].village?.villageID
                        this.estimatedFarmArea = registerData.farmAreaItems!![0].estimatedFarmArea.toString()

                        if (registerData.farmAreaItems!!.size > 1) {
                            this.farmVidTwo= registerData.farmAreaItems!![1].village?.villageID
                            this.estimatedFarmAreaTwo = registerData.farmAreaItems!![1].estimatedFarmArea.toString()
                        }
                        if (registerData.farmAreaItems!!.size > 2) {
                            this.farmVidThree = registerData.farmAreaItems!![2].village?.villageID
                            this.estimatedFarmAreaThree = registerData.farmAreaItems!![2].estimatedFarmArea.toString()
                        }
                        if (registerData.farmAreaItems!!.size > 3) {
                            this.farmVidFour = registerData.farmAreaItems!![3].village?.villageID
                            this.estimatedFarmAreaFour = registerData.farmAreaItems!![3].estimatedFarmArea.toString()
                        }
                    }
                }
            }
        )
        val farmer = db.allFarmers.last()
        db.addFarmHistory(
            FarmHistory(
                farmerId = farmer.id,
                seasonId = registerData.seasonId,
                seasonName = registerData.seasonName,
                totalLandHoldingSize = registerData.farmSize?.toDouble() ?: 0.0,

            )
        )
        val farmHistory = db.allFarmHistories.last()
        registerData.farmHistoryItems?.forEach {
            it.farmHistoryId = farmHistory.id
            it.cropId = it.crop?.cropID?.toInt()
            db.addFarmHistoryItem(it)
        }

    }

}