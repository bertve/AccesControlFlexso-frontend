package com.example.flexsame.ui.wallet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flexsame.R
import com.example.flexsame.models.Office
import kotlinx.android.synthetic.main.wallet_item.view.*
import org.w3c.dom.Text

class WalletAdapter(val context : Context) : RecyclerView.Adapter<WalletAdapter.ViewHolder>(){
    var data = listOf<Office>()
    set(value){
        field = value
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val companyName : TextView = itemView.findViewById(R.id.company_txt)
        val street : TextView = itemView.findViewById(R.id.street_txt)
        val city : TextView = itemView.findViewById(R.id.city_txt)
        val country : TextView = itemView.findViewById(R.id.country_txt)
        val key_img : ImageView = itemView.findViewById(R.id.key_img)

        fun bind(item: Office) {
            companyName.text = item.company.name
            street.text = item.address.street + " " + item.address.houseNumber
            city.text = item.address.town + " " + item.address.postalCode
            country.text = item.address.country
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wallet_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        setAnimations(holder)
        holder.bind(item)
    }

    private fun setAnimations(holder: ViewHolder) {
        holder.companyName.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        holder.street.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        holder.city.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        holder.country.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        holder.key_img.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
    }

}