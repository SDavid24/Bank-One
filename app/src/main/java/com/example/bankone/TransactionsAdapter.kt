package com.example.bankone

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_activity.view.*

class TransactionsAdapter(
    private var context: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class TransactionsViewHolder(view: View): RecyclerView.ViewHolder(view)
    var list  = mutableListOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransactionsViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_activity, parent, false)
        )    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is TransactionsViewHolder){

            holder.itemView.tv_debitOrCredit.text = model.type
            holder.itemView.tv_debited_amount.text = "-" + " $" + model.amount
            holder.itemView.tv_credited_amount.text = "+" + " $" + model.amount
            holder.itemView.tv_phoneNumber.text = model.phoneNumber
            holder.itemView.tv_balance.text = model.balance.toString()
            holder.itemView.tv_date.text = model.created
            holder.itemView.placeholder_image.setImageResource(R.drawable.ic_user_place_holder)

            if (holder.itemView.tv_debitOrCredit.text.toString() == "debited"){

                holder.itemView.tv_debited_amount.visibility = View.VISIBLE
                holder.itemView.tv_credited_amount.visibility = View.GONE
            }else{
                holder.itemView.tv_debited_amount.visibility = View.GONE
                holder.itemView.tv_credited_amount.visibility = View.VISIBLE
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}