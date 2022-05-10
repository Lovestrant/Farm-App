package com.svs.farm_app.main.registration.update

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
import com.svs.farm_app.entities.RegisteredFarmer
import com.svs.farm_app.main.dashboard.DashBoardActivity
import com.svs.farm_app.main.registration.RegisterFarmerActivity
import com.svs.farm_app.main.registration.UpdateFarmerActivity
import com.svs.farm_app.main.registration.register.*
import java.io.File

class UpdateFarmerDataActivity : AppCompatActivity(),
    FarmHistoryItemAdapter.SelectedCropInteractionListener,
    FarmAreaItemAdapter.SelectedFarmAreaItemInteractionListener {
    private val TAG = "RegisteredFarmerDataAct"
    private lateinit var viewModel: UpdateFarmerViewModel

    companion object {
        private const val FARMER_ARG = "farmer"
        fun newInstance(
            context: Context,
            registerData: RegisteredFarmer
        ): Intent {
            val TAG = "RegisteredFarmerDataAct"
            val intent = Intent(context, UpdateFarmerDataActivity::class.java)
            intent.putExtra(FARMER_ARG, Gson().toJson(registerData))
            Log.e(TAG, "newInstance: "+ Gson().toJson(registerData))
            return intent
        }
    }

    private var registerData: RegisteredFarmer? = null
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
                RegisteredFarmer::class.java
            )



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
        viewModel = ViewModelProvider(this)[UpdateFarmerViewModel::class.java]
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
            findViewById<AppCompatTextView>(R.id.tvIdNumber).text =  "N/A"
            findViewById<AppCompatTextView>(R.id.tvPhone).text = this.phone ?: "N/A"
            findViewById<AppCompatTextView>(R.id.tvEmail).text =  "N/A"
            findViewById<AppCompatTextView>(R.id.tvPostalAddress).text ="N/A"
            findViewById<AppCompatTextView>(R.id.tvContractNumber).text =this.contract_number?:"N/A"
            findViewById<AppCompatTextView>(R.id.tvGender).text = this.gender ?: "N/A"
            //farm


            findViewById<Button>(R.id.btnSave).setOnClickListener { save() }
        }
    }

    private fun save() {
        Log.e(TAG, "save: "+registerData?.contract_number )
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