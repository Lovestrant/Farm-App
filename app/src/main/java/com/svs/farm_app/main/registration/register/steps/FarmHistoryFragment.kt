package com.svs.farm_app.main.registration.register.steps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.svs.farm_app.R
import com.svs.farm_app.entities.OtherCrops
import com.svs.farm_app.main.registration.register.RegisterFarmerViewModel
import com.svs.farm_app.main.registration.register.FarmHistoryItem
import com.svs.farm_app.main.registration.register.FarmHistoryItemAdapter
import com.tylersuehr.esr.EmptyStateRecyclerView
import com.tylersuehr.esr.TextStateDisplay

/**
 * A simple [Fragment] subclass.
 * Use the [FarmHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FarmHistoryFragment : Fragment(), FarmHistoryItemAdapter.SelectedCropInteractionListener {

    private lateinit var viewModel: RegisterFarmerViewModel

   private val sp_years = resources.getStringArray(R.array.years)

    private val farmHistoryItemAdapter: FarmHistoryItemAdapter = FarmHistoryItemAdapter(this)
    private var listener: FarmHistoryFragmentInteractionListener? = null
    private var cropsList: List<OtherCrops> = ArrayList()

    lateinit var rv: EmptyStateRecyclerView
    private lateinit var etFarmSize: AppCompatEditText

    companion object {
        @JvmStatic
        fun newInstance() = FarmHistoryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[RegisterFarmerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_farm_history, container, false)
        rv = view.findViewById(R.id.rvCrops)
        etFarmSize = view.findViewById(R.id.etFarmSize)

        view.findViewById<FloatingActionButton>(R.id.btnPrevious)
            .setOnClickListener { listener?.onFarmHistoryPreviousSelected() }

        view.findViewById<FloatingActionButton>(R.id.btnNext)
            .setOnClickListener { validateAndSave() }

        view.findViewById<Button>(R.id.btnAddCrop)
            .setOnClickListener { addCrop() }

        return view
    }



    private fun validateAndSave() {
        val farmSize = etFarmSize.text.toString().trim()
        when {
            farmSize.isEmpty() -> {
                showSnack("Enter farm area")
                etFarmSize.requestFocus()
            }
            farmHistoryItemAdapter.farmHistoryItemList.isEmpty() -> {
                showSnack("Add at least one crop")
            }
            (!checkForTotalAreaAndCropsHolding(farmSize)) -> {
                showSnack("Total Crop area should be same as Total Landing Holdings")
            }
            else -> listener?.onFarmHistoryNextSelected(
                farmSize = farmSize,
                farmHistoryItems = farmHistoryItemAdapter.farmHistoryItemList
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cropsList = viewModel.getCrops()
        setUpAdapterViews()
        setRecyclerViewStates(rv)
    }

    private fun checkForTotalAreaAndCropsHolding(farmSize: String): Boolean {
        return (farmSize.toDouble() == calculateCropArea())
    }

    private fun calculateCropArea(): Double {
        return farmHistoryItemAdapter.farmHistoryItemList.sumOf { it.acres }
    }


    private fun setUpAdapterViews() {
        //invoke loading status
        rv.invokeState(EmptyStateRecyclerView.STATE_EMPTY)

        rv.layoutManager = LinearLayoutManager(requireActivity())
        rv.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
        farmHistoryItemAdapter.setSelectedCrops(ArrayList())
        rv.adapter = farmHistoryItemAdapter
    }


    private fun setRecyclerViewStates(rv: EmptyStateRecyclerView) {
        rv.setStateDisplay(
            EmptyStateRecyclerView.STATE_EMPTY,
            TextStateDisplay(requireActivity(), "Empty", "Please add at least one crop")
        )

        rv.setStateDisplay(
            EmptyStateRecyclerView.STATE_ERROR,
            TextStateDisplay(requireActivity(), "SORRY...!", "Something went wrong :(")
        )
    }


    private fun addCrop(farmHistoryItem: FarmHistoryItem? = null) {

        val cropsEligibleForAdding = cropsList.filter {
            farmHistoryItem?.crop == it || !farmHistoryItemAdapter.farmHistoryItemList.map { crop -> crop.crop }
                .contains(it)
        }

        if (cropsEligibleForAdding.isEmpty()) {
            showSnack("All the available crops entered")
            return
        }


        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_add_crop, null)
        builder.setView(view)

        val spCrops = view.findViewById<AppCompatSpinner>(R.id.spCrops)
        val spYears = view.findViewById<AppCompatSpinner>(R.id.spYears)
        val etFarmSize = view.findViewById<AppCompatEditText>(R.id.etFarmSize)
        val etCropWeight = view.findViewById<AppCompatEditText>(R.id.etCropWeight)
        val landOwnedRationGroup = view.findViewById<RadioGroup>(R.id.rgLandOwned)

        ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            cropsEligibleForAdding.map { it.cropName }.toTypedArray()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCrops.adapter = it
        }

        ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            cropsEligibleForAdding.map { R.array.years}.toTypedArray()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spYears.adapter = it
        }

        farmHistoryItem?.let {
            spCrops.setSelection(cropsEligibleForAdding.indexOf(it.crop))
            etFarmSize.setText(it.acres.toString())
            etCropWeight.setText(it.weight.toString())
        }

        builder.setTitle("Add Crop")
        builder.setPositiveButton("ADD") { dialog, _ ->
            val list = farmHistoryItemAdapter.farmHistoryItemList.apply {
                farmHistoryItem?.let { remove(it) }
                add(
                    FarmHistoryItem(
                        crop = cropsEligibleForAdding.find { it.cropName == spCrops.selectedItem.toString() }!!,
                        acres = etFarmSize.text.toString().toDoubleOrNull() ?: 0.0,
                        weight = etCropWeight.text.toString().toDoubleOrNull() ?: 0.0,
                        landOwned = landOwnedRationGroup.checkedRadioButtonId == R.id.rbOwned
                    )
                )
            }
            farmHistoryItemAdapter.setSelectedCrops(list)
            updateRecyclerViewState()
            dialog.cancel()
        }
        builder.setNegativeButton("CANCEL") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onDeleted(farmHistoryItem: FarmHistoryItem) {
        val list = farmHistoryItemAdapter.farmHistoryItemList.apply {
            remove(farmHistoryItem)
        }
        farmHistoryItemAdapter.setSelectedCrops(list)
        updateRecyclerViewState()
    }

    override fun onClicked(farmHistoryItem: FarmHistoryItem) {
        addCrop(farmHistoryItem)
    }

    private fun updateRecyclerViewState() {
        if (farmHistoryItemAdapter.farmHistoryItemList.isEmpty())
            rv.invokeState(EmptyStateRecyclerView.STATE_EMPTY)
        else
            rv.invokeState(EmptyStateRecyclerView.STATE_OK)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FarmHistoryFragmentInteractionListener)
            listener = context
        else
            throw IllegalArgumentException("Activity must be instance of FarmHistoryFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface FarmHistoryFragmentInteractionListener {
        fun onFarmHistoryPreviousSelected()
        fun onFarmHistoryNextSelected(
            farmSize: String,
            farmHistoryItems: MutableList<FarmHistoryItem>
        )
    }

    private fun Fragment.showSnack(message: String) {
        this.activity?.apply {
            Snackbar.make(
                findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG
            ).show()
        }
    }
}