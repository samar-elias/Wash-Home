package com.washathomes.Views.Main.Washer.More.OrderHistory.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.washathomes.AppUtils.Modules.OrderHistoryObject
import com.washathomes.R
import com.washathomes.Views.Main.Washer.More.OrderHistory.WasherOrderHistoryFragment

class WasherOrdersAdapter(
    var washerOrderHistoryFragment: WasherOrderHistoryFragment,
    var orders: ArrayList<OrderHistoryObject>
) :
    RecyclerView.Adapter<WasherOrdersAdapter.ViewHolder>() {
    var context: Context? = null
    var hasServices = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.order_history_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val order = orders[position]

        holder.date.text = order.date.toString()
        holder.orderNo.text = "#"+order.id
        holder.price.text = ""+order.total_amount

        holder.orderDetails.setOnClickListener { washerOrderHistoryFragment.navigateToDetails(order) }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderNo: TextView = itemView.findViewById(R.id.order_no)
        var price: TextView = itemView.findViewById(R.id.price)
        var date: TextView = itemView.findViewById(R.id.date)
        var orderDetails: ImageView = itemView.findViewById(R.id.order_details)
    }

}