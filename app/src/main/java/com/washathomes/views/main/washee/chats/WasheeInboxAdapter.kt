package com.washathomes.views.main.washee.chats

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.washathomes.apputils.modules.Inbox
import com.washathomes.R

class WasheeInboxAdapter(
    var washeeChatsFragment: WasheeChatsFragment,
    var inboxMessages: ArrayList<Inbox>
) :
    RecyclerView.Adapter<WasheeInboxAdapter.ViewHolder>() {
    var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_inbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val inbox = inboxMessages[position]

        holder.orderNo.text = context!!.resources.getString(R.string.order)+" #"+inbox.id
        holder.time.text = inbox.date_created

    }

    override fun getItemCount(): Int {
        return inboxMessages.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderNo: TextView = itemView.findViewById(R.id.inbox_sender)
        var time: TextView = itemView.findViewById(R.id.inbox_time)
    }

}