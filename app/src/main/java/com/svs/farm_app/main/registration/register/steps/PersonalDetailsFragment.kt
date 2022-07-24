package com.svs.farm_app.main.registration.register.steps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.svs.farm_app.R
import com.svs.farm_app.main.registration.update.steps.PersonalDetailsUpdateFragment
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalDetailsUpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalDetailsFragment : Fragment() {

    private var listener: PersonalDetailsFragmentInteractionListener? = null
    private lateinit var firstNameEt: AppCompatEditText
    private lateinit var middleNameEt: AppCompatEditText
    private lateinit var lastNameEt: AppCompatEditText
    private lateinit var idNumberEt: AppCompatEditText
    private lateinit var spYears: AppCompatEditText
    private lateinit var phoneEt: AppCompatEditText
    private lateinit var emailEt: AppCompatEditText
    private lateinit var postalAddressEt: AppCompatEditText
    private lateinit var genderSp: AppCompatSpinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_details, container, false).also { view ->
            firstNameEt = view.findViewById(R.id.etFName)
            spYears = view.findViewById(R.id.spYears)
            middleNameEt = view.findViewById(R.id.etMName)
            lastNameEt = view.findViewById(R.id.etLName)
            idNumberEt = view.findViewById(R.id.etIDNum)
            phoneEt = view.findViewById(R.id.etPhone)
            emailEt = view.findViewById(R.id.etEmail)
            postalAddressEt = view.findViewById(R.id.etPostalAddress)
            genderSp = view.findViewById(R.id.spGender)

            view.findViewById<FloatingActionButton>(R.id.btnNext)
                .setOnClickListener { validateAndSave() }
        }
    }

    private fun validateAndSave() {
        val firstNamee = firstNameEt.text.toString()
        val firstName = firstNamee.substring(0, 1).uppercase(Locale.getDefault()) + firstNamee.substring(1)
        val lastNameee = lastNameEt.text.toString()
        val lastName = firstNamee.substring(0, 1).uppercase(Locale.getDefault()) + lastNameee.substring(1)
        val idNumber = idNumberEt.text.toString()
        val middleNamee = middleNameEt.text.toString()
        val year = spYears.text.toString()
        val middleName = middleNamee.substring(0, 1).uppercase(Locale.getDefault()) + middleNamee.substring(1)
        val phone = phoneEt.text.toString()
        val email = emailEt.text.toString()
        val postalAddress = postalAddressEt.text.toString()
        val gender = genderSp.selectedItem.toString()

        when {
            firstName.isEmpty() -> firstNameEt.apply {
                requestFocus()
                error = "Enter first name"
            }
            middleName.isEmpty() -> middleNameEt.apply {
                requestFocus()
                error = "Enter middle name"
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
                    middleName = middleName,
                    year = year,
                    lastName = lastName,
                    idNumber = idNumber,
                    phone = phone,
                    email = email,
                    postalAddress = postalAddress,
                    gender = gender
                )
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = PersonalDetailsFragment()
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
            middleName: String,
            year: String,
            lastName: String,
            idNumber: String,
            phone: String,
            email: String,
            postalAddress: String,
            gender: String
        )
    }
}