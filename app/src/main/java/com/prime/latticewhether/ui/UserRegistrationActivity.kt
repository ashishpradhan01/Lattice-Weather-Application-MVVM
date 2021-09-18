package com.prime.latticewhether.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.prime.latticewhether.R
import com.prime.latticewhether.databinding.ActivityUserRegistrationBinding
import com.prime.latticewhether.models.pincode.PincodeResponse
import com.prime.latticewhether.repository.CommonRepository
import com.prime.latticewhether.ui.viewmodel.PincodeViewModel
import com.prime.latticewhether.ui.viewmodel.PincodeViewModelFactory
import com.prime.latticewhether.utils.Resource

class UserRegistrationActivity : AppCompatActivity() {
    private lateinit var userRegistrationBinding: ActivityUserRegistrationBinding
    private lateinit var pincodeViewModel: PincodeViewModel
    private var isValidPincode = false
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRegistrationBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_user_registration
        )
        val pincodeRepository = CommonRepository()
        val pincodeViewModelFactory = PincodeViewModelFactory(pincodeRepository)
        pincodeViewModel = ViewModelProvider(this, pincodeViewModelFactory)
            .get(PincodeViewModel::class.java)

        initGenderDropdownMenu()

        userRegistrationBinding.ivPickDate.setOnClickListener {
            initDateOfBirthPicker()
        }

        userRegistrationBinding.btnCheckPincode.setOnClickListener {
            val pincode = userRegistrationBinding.etPincode.editText
            if (!pincode?.text.isNullOrEmpty()) {
                userRegistrationBinding.etPincode.error = null
                checkPinCode(pincode?.text.toString())
            } else
                userRegistrationBinding.etPincode.error = getString(R.string.cant_be_empty)
        }

        pincodeViewModel.districtAndState.observe(this, Observer { response ->
            setViewDistrictAndPincode(response)
        }
        )

        userRegistrationBinding.btnRegisterUser.setOnClickListener {
            if (checkUserRegistrationForm()) {
                saveUserInformation()
                Toast.makeText(applicationContext,"Registration Successful", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, WhetherActivity::class.java))
            }
        }

        userRegistrationBinding.apply {
            etPincode.editText?.addTextChangedListener {
                btnCheckPincode.isEnabled = it?.length == 6 }
        }
    }

    private fun saveUserInformation() {
        userRegistrationBinding.apply {
            val number = etMobileNumber.editText?.text.toString()
            val name = etFullName.editText?.text.toString()
            val gender = etGenderMenu.editText?.text.toString()
            val dob = etDOB.editText?.text.toString()
            val address1 = etAddress1.editText?.text.toString()
            val address2 = etAddress2.editText?.text.toString()
            val pincode = etPincode.editText?.text.toString()

            sharedPref = getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                putString(getString(R.string.KEY_MOBILE), number)
                putString(getString(R.string.KEY_NAME), name)
                putString(getString(R.string.KEY_GENDER), gender)
                putString(getString(R.string.KEY_DOB), dob)
                putString(getString(R.string.KEY_ADDRESS1), address1)
                if (address1.isNotEmpty()) putString(getString(R.string.KEY_ADDRESS2), address2)
                putString(getString(R.string.KEY_PINCODE), pincode)
            }

    }

}

    private fun checkUserRegistrationForm():Boolean {
        var hasMobile: Boolean
        var hasFullName: Boolean
        var hasDob: Boolean
        var hasGender: Boolean
        var hasAddress1: Boolean
        var hasPincode: Boolean
        userRegistrationBinding.apply {
                hasMobile = if (etMobileNumber.editText?.text?.isNotEmpty()==true){
                    if (etMobileNumber.editText?.text?.length==10){
                        etMobileNumber.error = null
                        true
                    }
                    else {
                        etMobileNumber.error = getString(R.string.invalid_mobile_number)
                        false
                    }

                }else{
                    etMobileNumber.error = getString(R.string.cant_be_empty)
                    false
                }

                hasFullName = if (etFullName.editText?.text?.isNotEmpty()==true){
                    etFullName.error = null
                    true

                }else {
                    etFullName.error = getString(R.string.cant_be_empty)
                    false
                }

                hasGender = if (etGenderMenu.editText?.text?.isNotEmpty() == true){
                    etGenderMenu.error = null
                     true
                }else {
                    etGenderMenu.error = getString(R.string.cant_be_empty)
                    false
                }

                hasDob =  if (etDOB.editText?.text?.isNotEmpty() == true){
                    etDOB.error = null
                    true

                }else {
                    etDOB.error = getString(R.string.cant_be_empty)
                    false
                }

                hasAddress1 =  if (etAddress1.editText?.text?.isNotEmpty() == true){
                    etAddress1.error = null
                    true

                }else {
                    etAddress1.error = getString(R.string.cant_be_empty)
                     false
                }
                hasPincode =  if (etPincode.editText?.text?.isNotEmpty()==true){
                    if (isValidPincode){
                        etPincode.error = null
                        true
                    }else{
                        etPincode.error = getString(R.string.check_pincode)
                        false
                    }
                }else {
                    etPincode.error = getString(R.string.cant_be_empty)
                    false
                }
        }

        return hasMobile&&hasFullName&&hasGender&&hasDob&&hasAddress1&&hasPincode

    }

    private fun setViewDistrictAndPincode(response: Resource<PincodeResponse>?) {
        when (response) {
            is Resource.Success -> {
                response.data?.let {
                    if (!it.postOffice.isNullOrEmpty()) {
                        userRegistrationBinding.etPincode.error = null
                        userRegistrationBinding.tvDistrict.text = it.postOffice[0].district
                        userRegistrationBinding.tvstate.text = it.postOffice[0].state
                        userRegistrationBinding.etPincode.boxStrokeColor =
                            ContextCompat.getColor(this@UserRegistrationActivity, R.color.green)

                        isValidPincode = true
                    } else {
                        isValidPincode = false
                        userRegistrationBinding.tvDistrict.text = ""
                        userRegistrationBinding.tvstate.text = ""
                        userRegistrationBinding.etPincode.error =
                            getString(R.string.invalid_pinocde)
                    }
                }
            }
            is Resource.Error -> {
                userRegistrationBinding.apply {
                   tvDistrict.setTextColor(
                        ContextCompat.getColor(
                            this@UserRegistrationActivity,
                            R.color.error
                        )
                    )
                    tvDistrict.text = getString(R.string.something_went_wrong)
                    tvstate.setTextColor(
                        ContextCompat.getColor(
                            this@UserRegistrationActivity,
                            R.color.error
                        )
                    )
                    tvstate.text = getString(R.string.something_went_wrong)
                }
            }
            is Resource.Loading -> {
                userRegistrationBinding.apply {
                   tvDistrict.text = getString(R.string.fetching)
                   tvstate.text = getString(R.string.fetching)
                }
            }
        }
    }

    private fun checkPinCode(pincode: String) {
       pincodeViewModel.getDistrictAndState(pincode)
    }

    private fun initDateOfBirthPicker() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("SELECT A DATE")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.addOnPositiveButtonClickListener {
            userRegistrationBinding.etDOB.editText?.setText(datePicker.headerText)
        }
        datePicker.show(supportFragmentManager, "calender")
    }

    private fun initGenderDropdownMenu() {
        val genderItems = listOf("Male", "Female")
        val adapter = ArrayAdapter(this, R.layout.gender_list_item, genderItems)
        (userRegistrationBinding.etGenderMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}