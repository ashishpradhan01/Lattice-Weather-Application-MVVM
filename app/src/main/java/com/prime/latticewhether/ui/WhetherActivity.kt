package com.prime.latticewhether.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.prime.latticewhether.R
import com.prime.latticewhether.databinding.ActivityWhetherBinding
import com.prime.latticewhether.models.whether.WhetherResponse
import com.prime.latticewhether.repository.CommonRepository
import com.prime.latticewhether.ui.viewmodel.PincodeViewModel
import com.prime.latticewhether.ui.viewmodel.PincodeViewModelFactory
import com.prime.latticewhether.ui.viewmodel.WhetherViewModel
import com.prime.latticewhether.ui.viewmodel.WhetherViewModelFactory
import com.prime.latticewhether.utils.Resource

class WhetherActivity : AppCompatActivity() {
    private lateinit var whetherBinding: ActivityWhetherBinding
    private lateinit var whetherViewModel: WhetherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        whetherBinding =  DataBindingUtil.setContentView(this, R.layout.activity_whether)

        val commonRepository = CommonRepository()
        val whetherViewModelFactory = WhetherViewModelFactory(commonRepository)
        whetherViewModel = ViewModelProvider(this, whetherViewModelFactory)
            .get(WhetherViewModel::class.java)

        whetherBinding.apply {
            etCityName.editText?.addTextChangedListener {
                btnShowWhether.isEnabled = it?.isNotEmpty()==true
            }
        }

        whetherBinding.apply {
            btnShowWhether.setOnClickListener{
                if (etCityName.editText?.text?.isNotEmpty()==true){
                    getWhetherReport(etCityName.editText?.text.toString())
                }
            }
            btnChangeCity.setOnClickListener {
                llShowWhetherView.visibility = View.VISIBLE
                llWhetherReport.visibility = View.GONE
                whetherBackground.background = null
            }
        }

        whetherViewModel.whetherReport.observe(this, Observer { response ->
            setViewWhetherReport(response)
        })
    }

    private fun setViewWhetherReport(response: Resource<WhetherResponse>?) {
        when(response){
            is Resource.Success ->{
                whetherBinding.apply {
                    progressHorizontal.visibility = View.GONE
                    llShowWhetherView.visibility = View.GONE
                    llWhetherReport.visibility = View.VISIBLE
                    response.data?.let {
                        tvCityName.text = "${it.location.name}, ${it.location.country}"
                        tvTempWhether.text = "${it.current.tempC}\u2103   ${it.current.tempF}\u2109"
                        tvWindSpeed.text = "${it.current.windKph}km/h"
                        tvHumidity.text = "${it.current.humidity}%"
                        tvLatitude.text = "${it.location.lat}"
                        tvLongitude.text = "${it.location.lon}"
                        tvAboutWhether.text = it.current.condition.text

                        when(it.current.condition.code ){
                            1000 ->{
                                ivCurrentWhetherImage.setImageResource(R.drawable.ic_sunny)
                                whetherBackground.setBackgroundResource(R.drawable.sunny_background)
                            }
                            1006 or 1003 -> {
                                ivCurrentWhetherImage.setImageResource(R.drawable.ic_cloud_with_sun)
                                whetherBackground.setBackgroundResource(R.drawable.cloudy_background)
                            }

                            1087 -> {
                                ivCurrentWhetherImage.setImageResource(R.drawable.ic_rainy)
                                whetherBackground.setBackgroundResource(R.drawable.rainy_background)
                            }

                            else -> {
                                ivCurrentWhetherImage.setImageResource(R.drawable.ic_cloud_2)
                                whetherBackground.setBackgroundResource(R.drawable.cloudy_background)
                            }
                        }
                    }
                }

            }
            is Resource.Error ->{
                whetherBinding.apply {
                    progressHorizontal.visibility = View.GONE
                    llShowWhetherView.visibility = View.VISIBLE
                    llWhetherReport.visibility = View.GONE
                    etCityName.editText?.error = getString(R.string.wrong_input)
                }
            }
            is Resource.Loading ->{
                whetherBinding.apply {
                    progressHorizontal.visibility = View.VISIBLE
                    llShowWhetherView.visibility = View.VISIBLE
                    llWhetherReport.visibility = View.GONE
                    etCityName.editText?.error = null
                }
            }
        }
    }

    private fun getWhetherReport(cityName: String) {
        whetherViewModel.getWhetherReport(cityName)
    }


}