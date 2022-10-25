package com.washathomes.Views.Main.Courier.More.Settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.washathomes.R
import com.washathomes.Views.Main.Courier.CourierMainActivity
import com.washathomes.databinding.FragmentSettings2Binding

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettings2Binding
    lateinit var courierMainActivity: CourierMainActivity
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_settings2, container, false)
        binding = FragmentSettings2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CourierMainActivity) {
            courierMainActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        onClick()
    }

    private fun initViews(view: View){
        navController = Navigation.findNavController(view)
    }

    private fun onClick(){
        binding.toolbarBackIcon.setOnClickListener { navController.popBackStack() }
        binding.menuLineIdentificationUpload.setOnClickListener { navController.navigate(
            SettingsFragmentDirections.actionSettingsFragment2ToCourierIdentificationFragment()) }
        binding.menuLineDeliveryService.setOnClickListener { navController.navigate(
            SettingsFragmentDirections.actionSettingsFragment2ToDeliveryServiceFragment()) }
    }
}