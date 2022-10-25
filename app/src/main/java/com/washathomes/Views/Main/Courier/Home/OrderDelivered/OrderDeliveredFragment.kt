package com.washathomes.Views.Main.Courier.Home.OrderDelivered

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.washathomes.AppUtils.AppDefs.AppDefs
import com.washathomes.R
import com.washathomes.Views.Main.Courier.CourierMainActivity
import com.washathomes.databinding.FragmentOrderDelivered2Binding

class OrderDeliveredFragment : Fragment() {
 
    lateinit var binding: FragmentOrderDelivered2Binding
    lateinit var courierMainActivity: CourierMainActivity
    lateinit var navController: NavController
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_order_delivered2, container, false)
        binding = FragmentOrderDelivered2Binding.inflate(layoutInflater)
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
        setData()
    }

    private fun initViews(view: View){
        navController = Navigation.findNavController(view)
    }

    private fun onClick(){
        binding.toolbarBackIcon.setOnClickListener { navController.popBackStack() }
        binding.reviewOrderBtn.setOnClickListener { navController.navigate(
            OrderDeliveredFragmentDirections.actionOrderDeliveredFragment2ToOrderReviewFragment3()) }
    }

    private fun setData(){
        binding.orderCompletedCodeText.text = resources.getString(R.string.order)+" #"+ AppDefs.activeOrder.id
        binding.orderCompletedDate.text = resources.getString(R.string.order_placed_on)+" "+ AppDefs.activeOrder.time+" "+ AppDefs.activeOrder.date
    }

}