package com.svs.farm_app.main.registration.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.svs.farm_app.R
import com.svs.farm_app.main.dashboard.DashBoardActivity
import com.svs.farm_app.main.registration.RegisterFarmerActivity
import java.io.File

class RegisteredFarmerDataActivity : AppCompatActivity(),
    FarmHistoryItemAdapter.SelectedCropInteractionListener,
    FarmAreaItemAdapter.SelectedFarmAreaItemInteractionListener {
    private val TAG = "RegisteredFarmerDataAct"
    private lateinit var viewModel: RegisterFarmerViewModel

    companion object {
        private const val FARMER_ARG = "farmer"
        fun newInstance(
            context: Context,
            registerData: RegisterFarmerActivity.RegisterData
        ): Intent {
            val TAG = "RegisteredFarmerDataAct"
            val intent = Intent(context, RegisteredFarmerDataActivity::class.java)
            intent.putExtra(FARMER_ARG, Gson().toJson(registerData))
            Log.e(TAG, "newInstance: "+ Gson().toJson(registerData))
            return intent
        }
    }

    private var registerData: RegisterFarmerActivity.RegisterData? = null
    private val farmHistoryItemAdapter: FarmHistoryItemAdapter = FarmHistoryItemAdapter(this)
    private val farmAreaItemAdapter: FarmAreaItemAdapter = FarmAreaItemAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registered_farmer_data)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(FARMER_ARG)) {
            registerData = Gson().fromJson(
                intent.getStringExtra(FARMER_ARG),
                RegisterFarmerActivity.RegisterData::class.java
            )
            registerData?.farmerPic?.let { loadImage(it) }


        }
        if (registerData == null) {
            showToast("Register data not found")
            finish()
        }
        Log.e(TAG, "onCreate: "+
            intent.getStringExtra(FARMER_ARG))
        Log.e(TAG, "onCreate: "+ Gson().fromJson(
            intent.getStringExtra(FARMER_ARG),
            RegisterFarmerActivity.RegisterData::class.java
        ) )
        viewModel = ViewModelProvider(this)[RegisterFarmerViewModel::class.java]
        populateData()

    }

    private fun loadImage(imagePath: String) {

        val imgFile = File(imagePath)
        if (imgFile.exists()) {
            val myImage = findViewById<ImageView>(R.id.imageView)
            myImage.setImageURI(Uri.fromFile(imgFile));

        }
    }

    private fun populateData() {
        registerData?.apply {
            //personal
            findViewById<AppCompatTextView>(R.id.tvFirstName).text = this.firstName ?: "N/A"
            findViewById<AppCompatTextView>(R.id.tvLastName).text = this.lastName ?: "N/A"
            findViewById<AppCompatTextView>(R.id.tvIdNumber).text = this.idNumber ?: "N/A"
            findViewById<AppCompatTextView>(R.id.tvPhone).text = this.phone ?: "N/A"
            findViewById<AppCompatTextView>(R.id.tvEmail).text = this.email ?: "N/A"
            findViewById<AppCompatTextView>(R.id.tvPostalAddress).text = this.postalAddress ?: "N/A"
            findViewById<AppCompatTextView>(R.id.tvGender).text = this.gender ?: "N/A"
            //farm
            findViewById<AppCompatTextView>(R.id.tvVillage).text =
                this.village?.villageName ?: "N/A"
            findViewById<AppCompatTextView>(R.id.tvSubVillage).text =
                this.subVillage?.subVillageName ?: "N/A"
            findViewById<AppCompatTextView>(R.id.tvStatus).text = this.showsIntent ?: "N/A"

            //farms
            this.farmAreaItems?.let { farmAreaItem ->
                findViewById<RecyclerView>(R.id.rvFarmAreas).apply {
                    this.layoutManager = LinearLayoutManager(this@RegisteredFarmerDataActivity)
                    this.addItemDecoration(
                        DividerItemDecoration(
                            this@RegisteredFarmerDataActivity,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    farmAreaItemAdapter.setSelectedFarmAreaItems(farmAreaItem)
                    this.adapter = farmAreaItemAdapter
                }
            }

            //history
            findViewById<AppCompatTextView>(R.id.tvTotalLandHoldingSize).text =
                (this.farmSize ?: "0.0") + " acre(s)"
            this.farmHistoryItems?.let { crops ->
                findViewById<RecyclerView>(R.id.rvCrops).apply {
                    this.layoutManager = LinearLayoutManager(this@RegisteredFarmerDataActivity)
                    this.addItemDecoration(
                        DividerItemDecoration(
                            this@RegisteredFarmerDataActivity,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    farmHistoryItemAdapter.setSelectedCrops(crops)
                    this.adapter = farmHistoryItemAdapter
                }
            }

            findViewById<Button>(R.id.btnSave).setOnClickListener { save() }
        }
    }

    private fun save() {
        Log.e(TAG, "save: "+registerData?.contractNo )
        viewModel.addFarmer(registerData!!)
        showToast("Saved Farmer")

        startActivity(
            Intent(
                this,
                DashBoardActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    fun Activity.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDeleted(farmHistoryItem: FarmHistoryItem) {
        //DO NOTHING
    }

    override fun onClicked(farmHistoryItem: FarmHistoryItem) {
        //DO NOTHING
    }


    override fun onDeleted(farmAreaItem: FarmAreaItem) {
        //DO NOTHING
    }

    override fun onClicked(farmAreaItem: FarmAreaItem) {
        //DO NOTHING
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
}