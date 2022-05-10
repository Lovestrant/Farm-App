package com.svs.farm_app.main.registration.update.steps

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.svs.farm_app.R
import com.svs.farm_app.entities.OtherCrops
import com.svs.farm_app.entities.SubVillage
import com.svs.farm_app.entities.Village
import com.svs.farm_app.main.registration.register.FarmAreaItem
import com.svs.farm_app.main.registration.register.FarmAreaItemAdapter
import com.svs.farm_app.main.registration.register.RegisterFarmerViewModel
import com.svs.farm_app.main.registration.register.steps.VillageDetailsFragment
import com.tylersuehr.esr.EmptyStateRecyclerView
import com.tylersuehr.esr.TextStateDisplay


/**
 * A simple [Fragment] subclass.
 * Use the [VillageDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VillageDetailsUpdateFragment : Fragment(),
    FarmAreaItemAdapter.SelectedFarmAreaItemInteractionListener {


    private lateinit var viewModel: RegisterFarmerViewModel
    private val farmAreaItemAdapter: FarmAreaItemAdapter = FarmAreaItemAdapter(this)

    private var listener: VillageDetailsFragmentInteractionListener? = null
    private var villages: List<Village> = ArrayList()
    private var currentSubVillages: List<SubVillage> = ArrayList()
    private var cropsList: List<OtherCrops> = ArrayList()


    private lateinit var spVillage: AppCompatSpinner
    private lateinit var spSubVillage: AppCompatSpinner
    private lateinit var contractRadioGroup: RadioGroup
    private lateinit var cbShowIntent: AppCompatCheckBox
    private lateinit var etContractNo: EditText
    private lateinit var rv: EmptyStateRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[RegisterFarmerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_village_details, container, false).also { view ->
            spVillage = view.findViewById(R.id.spVillage)
            spSubVillage = view.findViewById(R.id.spSubVillage)
            contractRadioGroup = view.findViewById(R.id.rgContract)
            etContractNo = view.findViewById(R.id.etContractNo)
            cbShowIntent = view.findViewById(R.id.cbShowIntent)
            rv = view.findViewById(R.id.rvFarmAreas)

            view.findViewById<FloatingActionButton>(R.id.btnPrevious)
                .setOnClickListener { listener?.onVillageDetailsPreviousSelected() }

            view.findViewById<FloatingActionButton>(R.id.btnNext)
                .setOnClickListener { validateAndSave() }

            view.findViewById<Button>(R.id.btnAddFarmArea)
                .setOnClickListener { addFarmArea() }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        villages = viewModel.getVillages()
        populateVillageSpinners(view)
        setUpAdapterViews()
        setRecyclerViewStates(rv)
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
        farmAreaItemAdapter.setSelectedFarmAreaItems(ArrayList())
        rv.adapter = farmAreaItemAdapter
    }

    private fun updateRecyclerViewState() {
        if (farmAreaItemAdapter.farmAreaItemList.isEmpty())
            rv.invokeState(EmptyStateRecyclerView.STATE_EMPTY)
        else
            rv.invokeState(EmptyStateRecyclerView.STATE_OK)
    }


    private fun setRecyclerViewStates(rv: EmptyStateRecyclerView) {
        rv.setStateDisplay(
            EmptyStateRecyclerView.STATE_EMPTY,
            TextStateDisplay(requireActivity(), "Empty", "Please add at least one farm area")
        )

        rv.setStateDisplay(
            EmptyStateRecyclerView.STATE_ERROR,
            TextStateDisplay(requireActivity(), "SORRY...!", "Something went wrong :(")
        )
    }


    private fun addFarmArea(farmAreaItem: FarmAreaItem? = null) {

        val villagesEligibleForAdding = villages.filter {
            farmAreaItem?.village == it || !farmAreaItemAdapter.farmAreaItemList.map { village -> village.village }
                .contains(it)
        }

        if (villagesEligibleForAdding.isEmpty()) {
            showSnack("All the available villages entered")
            return
        }


        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

        val view =
            LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_add_farm_area, null)
        builder.setView(view)

        val spVillages = view.findViewById<AppCompatSpinner>(R.id.spVillages)
        val etFarmSize = view.findViewById<AppCompatEditText>(R.id.etFarmSize)

        ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            villagesEligibleForAdding.map { it.villageName }.toTypedArray()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spVillages.adapter = it
        }

        //set min and max farm area
        etFarmSize.doAfterTextChanged { setFarmMinMaxArea(it) }

        farmAreaItem?.let {
            spVillages.setSelection(villagesEligibleForAdding.indexOf(it.village))
            etFarmSize.setText(it.estimatedFarmArea.toString())
        }

        builder.setTitle("Add Farm Area")
        builder.setPositiveButton("ADD") { dialog, _ ->
            val list = farmAreaItemAdapter.farmAreaItemList.apply {
                farmAreaItem?.let { remove(it) }
                add(
                    FarmAreaItem(
                        village = villagesEligibleForAdding.find { it.villageName == spVillages.selectedItem.toString() }!!,
                        estimatedFarmArea = etFarmSize.text.toString().toDoubleOrNull() ?: 0.0
                    )
                )
            }
            farmAreaItemAdapter.setSelectedFarmAreaItems(list)
            updateRecyclerViewState()
            dialog.cancel()
        }
        builder.setNegativeButton("CANCEL") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun setFarmMinMaxArea(editable: Editable?) {
        if (editable.isNullOrEmpty()) {
            editable?.clear()
            editable?.append("0")
        }
        if (editable.toString().toFloat() > 20) {
            editable?.clear()
            editable?.append("20")
        }
    }

    private fun validateAndSave() {
        when {
            spVillage.selectedItem == null -> {
                showSnack("Select village to proceed")
                spVillage.requestFocus()
            }
            spSubVillage.selectedItem == null -> {
                showSnack("Select sub-village to proceed")
                spSubVillage.requestFocus()
            }

            farmAreaItemAdapter.farmAreaItemList.isEmpty() -> {
                showSnack("Add at least one farm area")
            }
            ((contractRadioGroup.checkedRadioButtonId == R.id.rbYes) && (etContractNo.text.isNullOrEmpty())) -> {
                showSnack("Add contract no")
            }

            else -> {
                listener?.onVillageDetailsNextSelected(
                    village = villages.find { it.villageName == spVillage.selectedItem.toString() }!!,
                    subVillage = currentSubVillages.find { it.subVillageName == spSubVillage.selectedItem.toString() }!!,
                    showsIntent = cbShowIntent.isChecked,
                    farmAreaItems = farmAreaItemAdapter.farmAreaItemList,
                    contractNo = if (contractRadioGroup.checkedRadioButtonId == R.id.rbYes) {
                        etContractNo.text.toString()
                    } else {
                        null
                    }
                )
            }
        }

    }

    private fun populateVillageSpinners(view: View) {
        val villageNames = villages.map { it.villageName }

        ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            villageNames.toTypedArray()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.findViewById<AppCompatSpinner>(R.id.spVillage).apply {
                //adapter
                this.adapter = it
                //item selected listener
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, v: View?,
                        position: Int, id: Long
                    ) {
                        val village = villages[position]
                        populateSubVillagesSpinner(view, village.villageID)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }

    private fun populateSubVillagesSpinner(view: View, villageID: String) {
        currentSubVillages = viewModel.getSubVillages(villageId = villageID)
        val subVillageNames = currentSubVillages.map { it.subVillageName }
        ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            subVillageNames.toTypedArray()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.findViewById<AppCompatSpinner>(R.id.spSubVillage).adapter = it
        }

    }

    private fun populateCropsSpinners(view: View) {
        cropsList = viewModel.getCrops()
        val cropsNames = cropsList.map { it.cropName }

        ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            cropsNames.toTypedArray()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.findViewById<AppCompatSpinner>(R.id.spOtherCropsOne).adapter = it
        }

        ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            cropsNames.toTypedArray()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.findViewById<AppCompatSpinner>(R.id.spOtherCropsTwo).adapter = it
        }

        ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            cropsNames.toTypedArray()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.findViewById<AppCompatSpinner>(R.id.spOtherCropsThree).adapter = it
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = VillageDetailsUpdateFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is VillageDetailsFragmentInteractionListener)
            listener = context
        else
            throw IllegalArgumentException("Activity must be instance of VillageDetailsFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface VillageDetailsFragmentInteractionListener {
        fun onVillageDetailsPreviousSelected()
        fun onVillageDetailsNextSelected(
            village: Village,
            subVillage: SubVillage,
            showsIntent: Boolean,
            farmAreaItems: MutableList<FarmAreaItem>,
            contractNo: String?
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

    override fun onDeleted(farmAreaItem: FarmAreaItem) {
        val list = farmAreaItemAdapter.farmAreaItemList.apply {
            remove(farmAreaItem)
        }
        farmAreaItemAdapter.setSelectedFarmAreaItems(list)
        updateRecyclerViewState()
    }

    override fun onClicked(farmAreaItem: FarmAreaItem) {
        addFarmArea(farmAreaItem)
    }
}