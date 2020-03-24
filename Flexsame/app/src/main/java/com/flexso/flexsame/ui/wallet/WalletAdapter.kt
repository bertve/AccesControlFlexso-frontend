package com.flexso.flexsame.ui.wallet

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.flexso.flexsame.R
import com.flexso.flexsame.models.Office
import androidx.recyclerview.widget.ListAdapter
import com.flexso.flexsame.databinding.WalletItemBinding

class WalletAdapter(val context : Context,val clickListener: WalletItemListener) : ListAdapter<Office,WalletAdapter.ViewHolder>(WalletDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        setAnimations(holder)
        holder.bind(clickListener,item)
    }

    private fun setAnimations(holder: ViewHolder) {
        val binding = holder.binding
        binding.companyTxt.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        binding.streetTxt.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        binding.cityTxt.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        binding.countryTxt.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        binding.keyImg.animation = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
    }

    class ViewHolder private constructor(val binding : WalletItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(clickListener: WalletItemListener,item: Office) {
            binding.office = item
            binding.clickListener = clickListener
            binding.companyTxt.text = item.company.name
            binding.streetTxt.text = item.address.street + " " + item.address.houseNumber
            binding.cityTxt.text = item.address.town + " " + item.address.postalCode
            binding.countryTxt.text = item.address.country
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WalletItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    class WalletDiffCallBack : DiffUtil.ItemCallback<Office>(){
        override fun areItemsTheSame(oldItem: Office, newItem: Office): Boolean {
            return oldItem.officeId == newItem.officeId
        }

        override fun areContentsTheSame(oldItem: Office, newItem: Office): Boolean {
            return oldItem == newItem
        }

    }

}