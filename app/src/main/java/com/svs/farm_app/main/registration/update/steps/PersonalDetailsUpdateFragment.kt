package com.svs.farm_app.main.registration.update.steps

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import com.svs.farm_app.databinding.FragmentPersonalDetailsUpdateBinding
import com.svs.farm_app.entities.RegisteredFarmer
import com.svs.farm_app.main.registration.register.steps.PersonalDetailsFragment

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalDetailsUpdateFragment : Fragment() {

    private var listener: PersonalDetailsFragmentInteractionListener? = null
    private lateinit var firstNameEt: AppCompatEditText
    private lateinit var lastNameEt: AppCompatEditText
    private lateinit var idNumberEt: AppCompatEditText
    private lateinit var phoneEt: AppCompatEditText
    private lateinit var emailEt: AppCompatEditText
    private lateinit var postalAddressEt: AppCompatEditText
    private lateinit var genderSp: AppCompatSpinner
    private lateinit var registeredFarmer: RegisteredFarmer
    private val TAG = "PersonalDetailsUpdateFr"
    lateinit var b:FragmentPersonalDetailsUpdateBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b= FragmentPersonalDetailsUpdateBinding.inflate(inflater,container,false)
        firstNameEt = b.etFName
        lastNameEt = b.etLName
        idNumberEt = b.etIDNum
        phoneEt = b.etPhone
        emailEt = b.etEmail
        postalAddressEt = b.etPostalAddress
        genderSp = b.spGender

        b.btnNext.setOnClickListener { validateAndSave() }
        b.etFName.setText(registeredFarmer.firstName)
        b.etLName.setText(registeredFarmer.lastName)
        b.etIDNum.setText(registeredFarmer.farmerId)
        b.etPhone.setText(registeredFarmer.phone)
        b.etContractNumber.setText(registeredFarmer.contract_number)
        b.etEmail.visibility=View.GONE
        b.etPostalAddress.visibility=View.GONE
        b.spGender.setSelection(if(registeredFarmer.gender.contentEquals("M")){1}else{0})
        return b.root
    }

    private fun validateAndSave() {
        val firstName = firstNameEt.text.toString()
        val lastName = lastNameEt.text.toString()
        val idNumber = idNumberEt.text.toString()
        val phone = phoneEt.text.toString()
        val email = emailEt.text.toString()
        val postalAddress = postalAddressEt.text.toString()
        val gender = genderSp.selectedItem.toString()

        when {
            firstName.isEmpty() -> firstNameEt.apply {
                requestFocus()
                error = "Enter first name"
            }
            lastName.isEmpty() -> lastNameEt.apply {
                requestFocus()
                error = "Enter last name"
            }
            idNumber.isEmpty() -> idNumberEt.apply {
                requestFocus()
                error = "Enter ID number"
            }
            phone.isEmpty() -> phoneEt.apply {
                requestFocus()
                error = "Enter phone number"
            }
            b.etContractNumber.text.toString().isEmpty() -> b.etContractNumber.apply {
                requestFocus()
                error = "Enter Contract number"
            }
            /*email.isEmpty() -> emailEt.apply {
                requestFocus()
                error = "Enter email"
            }
            postalAddress.isEmpty() -> postalAddressEt.apply {
                requestFocus()
                error = "Enter postal address"
            }*/

            else -> {
                listener?.onPersonalDetailsNextSelected(
                    firstName = firstName,
                    lastName = lastName,
                    idNumber = idNumber,
                    phone = phone,
                    email = email,
                    postalAddress = postalAddress,
                    gender = gender,
                    contractNumber = b.etContractNumber.text.toString()
                )
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = PersonalDetailsUpdateFragment()
        fun newInstance(registeredFarmer: RegisteredFarmer): PersonalDetailsUpdateFragment {
            var b=Bundle()
            b.apply {
                putSerializable("farmer_info",registeredFarmer)
            }
           return  PersonalDetailsUpdateFragment().apply {
                arguments=b
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registeredFarmer= arguments?.getSerializable("farmer_info") as RegisteredFarmer
        Log.e(TAG, "onCreate: ${registeredFarmer.toStringx()}", )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PersonalDetailsFragmentInteractionListener)
            listener = context
        else
            throw IllegalArgumentException("Activity must be instance of PersonalDetailsFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface PersonalDetailsFragmentInteractionListener {
        fun onPersonalDetailsNextSelected(
            firstName: String,
            lastName: String,
            idNumber: String,
            phone: String,
            email: String,
            postalAddress: String,
            gender: String,
            contractNumber: String
        )
    }
}