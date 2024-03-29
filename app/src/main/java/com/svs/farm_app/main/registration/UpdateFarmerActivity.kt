package com.svs.farm_app.main.registration

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.svs.farm_app.R
import com.svs.farm_app.entities.RegisteredFarmer
import com.svs.farm_app.entities.SubVillage
import com.svs.farm_app.entities.Village
import com.svs.farm_app.main.registration.register.FarmAreaItem
import com.svs.farm_app.main.registration.register.FarmHistoryItem
import com.svs.farm_app.main.registration.register.RegisterFarmerViewModel
import com.svs.farm_app.main.registration.update.steps.FarmHistoryUpdateFragment
import com.svs.farm_app.main.registration.update.steps.PersonalDetailsUpdateFragment
import com.svs.farm_app.main.registration.update.steps.VillageDetailsUpdateFragment
import com.svs.farm_app.main.registration.update.UpdateFarmerDataActivity
import com.svs.farm_app.utils.GPSTracker
import com.svs.farm_app.utils.MyPrefrences
import java.util.*


class UpdateFarmerActivity : AppCompatActivity(),
    PersonalDetailsUpdateFragment.PersonalDetailsFragmentInteractionListener,
    VillageDetailsUpdateFragment.VillageDetailsFragmentInteractionListener,
    FarmHistoryUpdateFragment.FarmHistoryFragmentInteractionListener {

    private lateinit var viewModel: RegisterFarmerViewModel
    private var gpsTracker: GPSTracker? = null
    private val REQUEST_CHECK_SETTINGS = 0x1

    private lateinit var personalDetailsUpdateFragment: PersonalDetailsUpdateFragment
    private lateinit var villageDetailsUpdateFragment: VillageDetailsUpdateFragment
    private lateinit var farmHistoryUpdateFragment: FarmHistoryUpdateFragment

    private val registerData: RegisterData = RegisterData()
    private var registeredFarmer: RegisteredFarmer = RegisteredFarmer()
    private val TAG = "UpdateFarmerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_farmer2)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this)[RegisterFarmerViewModel::class.java]
        gpsTracker = GPSTracker(this)
        registeredFarmer= intent.extras?.getSerializable("farmer_info") as RegisteredFarmer
        Log.e(TAG, "onCreate-registeredFarmer: "+registeredFarmer.toString())
        setUpFragments()
        replaceFragment(personalDetailsUpdateFragment)
    }

    private fun setUpFragments() {
        personalDetailsUpdateFragment = PersonalDetailsUpdateFragment.newInstance(registeredFarmer)
        villageDetailsUpdateFragment = VillageDetailsUpdateFragment.newInstance()
        farmHistoryUpdateFragment = FarmHistoryUpdateFragment.newInstance()
    }


    private fun replaceFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onPersonalDetailsNextSelected(
        firstName: String,
        lastName: String,
        idNumber: String,
        phone: String,
        email: String,
        postalAddress: String,
        gender: String,
        contractNumber: String
    ) {
        registerData.firstName = firstName
        registerData.lastName = lastName
        registerData.idNumber = idNumber
        registerData.phone = phone
        registerData.email = email
        registerData.postalAddress = postalAddress

        registerData.gender = gender
        registeredFarmer.firstName = firstName
        registeredFarmer.lastName = lastName
        registeredFarmer.farmerId = idNumber
        registeredFarmer.phone = phone
        registeredFarmer.gender = gender
        registeredFarmer.contract_number = contractNumber




        toUploadActivity()
//        replaceFragment(villageDetailsUpdateFragment)
    }

    override fun onVillageDetailsPreviousSelected() {
        replaceFragment(personalDetailsUpdateFragment)
    }

    override fun onVillageDetailsNextSelected(
        village: Village,
        subVillage: SubVillage,
        showsIntent: Boolean,
        farmAreaItems: MutableList<FarmAreaItem>,
        contractNo: String?
    ) {
        registerData.village = village
        registerData.subVillage = subVillage
        registerData.showsIntent = if (showsIntent) "shows intent" else "not contracted"
        registerData.farmAreaItems = farmAreaItems
        registerData.contractNo=contractNo
        replaceFragment(farmHistoryUpdateFragment)
    }

    override fun onFarmHistoryPreviousSelected() {
        replaceFragment(villageDetailsUpdateFragment)
    }

    override fun onFarmHistoryNextSelected(
        farmSize: String,
        farmHistoryItems: MutableList<FarmHistoryItem>
    ) {

        registerData.farmSize = farmSize
        registerData.farmHistoryItems = farmHistoryItems

        checkLocation()
    }

    private fun toUploadActivity() {
//        registerData.fingerPath = MyPrefrences.getPrefrence(this, "finger_path")
//        registerData.farmerPic = MyPrefrences.getPrefrence(this, "farmer_pic")
//        registerData.leftThumb = MyPrefrences.getPrefrence(this, "left_thumb")
//        registerData.rightThumb = MyPrefrences.getPrefrence(this, "right_thumb")
//        registerData.latitude = gpsTracker?.latitude ?: 0.0
//        registerData.longitude = gpsTracker?.longitude ?: 0.0
//        gpsTracker?.stopUsingGPS()

        startActivity(UpdateFarmerDataActivity.newInstance(this, registeredFarmer))
    }

    private fun checkLocation() {
        val builder = LocationSettingsRequest.Builder()
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            toUploadActivity()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        this@UpdateFarmerActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        toUploadActivity()
                        Log.e("GPS", "Gps enabled")
                    }
                    Activity.RESULT_CANCELED -> {
                        showSnack("Please Allow Turn On Your Location")
                        // The user was asked to change settings, but chose not to
                        Log.e("GPS", "Gps Canceled")
                    }
                }
            }
        }
    }

    private fun Activity.showSnack(message: String) {
        this.apply {
            Snackbar.make(
                findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    data class RegisterData(
        var fingerPath: String? = null,
        var farmerPic: String? = null,
        var leftThumb: String? = null,
        var rightThumb: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var idNumber: String? = null,
        var phone: String? = null,
        var email: String? = null,
        var postalAddress: String? = null,
        var gender: String? = null,
        var village: Village? = null,
        var contractNo: String? = null,
        var contact: String? = null,
        var subVillage: SubVillage? = null,
        var showsIntent: String? = null,
        var farmSize: String? = null,
        var farmHistoryItems: MutableList<FarmHistoryItem>? = null,
        var farmAreaItems: MutableList<FarmAreaItem>? = null,
        var longitude: Double = 0.0,
        var latitude: Double = 0.0,
        var date: Date = Date(),
        val seasonId: Int = 8,
        val seasonName: String = "2020 - 2021",
        var dateFormatString: String = "yyyy-MM-dd HH:mm:ss"
    )

}