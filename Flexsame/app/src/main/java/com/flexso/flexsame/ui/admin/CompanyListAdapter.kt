package com.flexso.flexsame.ui.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.CompanylistItemBinding
import com.flexso.flexsame.models.User

class CompanyListAdapter(val context : Context, val clickListener: CompanyListItemListener) : ListAdapter<User, CompanyListAdapter.ViewHolder>(CompanyListDiffCallBack()) {

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
        val anim = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        binding.companyIcon.animation = anim
        binding.companyName.animation = anim
        binding.email.animation = anim
        binding.name.animation = anim
    }

    class ViewHolder private constructor(val binding : CompanylistItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(clickListener: CompanyListItemListener,item: User) {
            binding.user = item
            binding.clickListener = clickListener
            binding.companyName.text = item.company!!.name
            binding.name.text = item.getFullName()
            binding.email.text = item.email
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CompanylistItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    class CompanyListDiffCallBack : DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
}