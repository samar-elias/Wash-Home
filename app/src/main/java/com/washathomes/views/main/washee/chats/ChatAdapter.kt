package com.washathomes.views.main.washee.chats

import com.washathomes.R
import com.washathomes.AppUtils.AppDefs.AppDefs
import com.washathomes.AppUtils.Modules.Chatmodel.ChatMessage
import com.washathomes.AppUtils.Modules.Chatmodel.Order
import com.washathomes.base.ui.BaseAdapter
import com.washathomes.base.ui.BaseViewHolder
import com.washathomes.base.util.DateUtil

import com.washathomes.databinding.ItemChatBinding


class ChatAdapter(val order: Order?) : BaseAdapter<ItemChatBinding, ChatMessage>(R.layout.item_chat) {

    override fun bindView(binding: ItemChatBinding, item: ChatMessage?) {
        binding.message = item
        binding.isMyMessage = item?.senderId == AppDefs.user.results!!.id!!.toInt()
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemChatBinding, ChatMessage>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.binding.chatMessageCardTheirs.setBackgroundResource(R.drawable.rounded_chat_balloon_left)
        holder.binding.chatMessageCardMine.setBackgroundResource(R.drawable.rounded_chat_balloon_right)

        if (holder.binding.isMyMessage == true) {
            holder.binding.chatMessageTextMine.text = holder.binding.message?.message
            holder.binding.message?.createTime?.let { holder.binding.chatMessageSendTimeMine.text = DateUtil.convertLongToTimeString(it) }
        } else {
            holder.binding.chatMessageTextTheirs.text = holder.binding.message?.message
            holder.binding.message?.createTime?.let { holder.binding.chatMessageSendTimeTheirs.text = DateUtil.convertLongToTimeString(it) }

            when (holder.binding.message?.senderId) {
                order?.seller_user_id -> {
                    holder.binding.chatMessageSenderNameTheirs.text =
                        (order?.seller_first_name + " " + order?.seller_last_name + " (" + holder.itemView.context.getString(R.string.washer) + ")")
                    AppDefs.setImage(order?.seller_profile_picture, holder.binding.chatProfileImageTheirs)
                }

                order?.driver_user_id -> {
                    holder.binding.chatMessageSenderNameTheirs.text =
                        (order?.driver_first_name + " " + order?.driver_last_name + " (" + holder.itemView.context.getString(R.string.driver) + ")")
                    AppDefs.setImage(order?.driver_profile_picture, holder.binding.chatProfileImageTheirs)
                }

                order?.buyer_user_id -> {
                    holder.binding.chatMessageSenderNameTheirs.text =
                        (order?.buyer_first_name + " " + order?.buyer_last_name + " (" + holder.itemView.context.getString(R.string.buyer) + ")")
                    AppDefs.setImage(order?.buyer_profile_picture, holder.binding.chatProfileImageTheirs)
                }
            }
        }

    }
}