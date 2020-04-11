package com.flexso.flexsame.ui.admin

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flexso.flexsame.MainActivity
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.CompanylistItemBinding
import com.flexso.flexsame.models.User

class CompanyListAdapter(val context : Context,val adminViewModel: AdminViewModel) : ListAdapter<User, CompanyListAdapter.ViewHolder>(CompanyListDiffCallBack()),Filterable {

    var isCLickable : Boolean = true
    var mListRef: List<User>? = null
    var mFilteredList: List<User>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        setAnimations(holder)
        holder.bind(
                CompanyListItemListener {
            user -> if (this.isCLickable){
                    (context as MainActivity).findNavController(R.id.myNavHostFragment).navigate(AdminFragmentDirections.actionAdminFragmentToCompanyFragment(user.company))
                }
        },item)
    }

    private fun setAnimations(holder: ViewHolder) {
        val binding = holder.binding
        val anim = AnimationUtils.loadAnimation(context,R.anim.slide_in_right)
        binding.companyIcon.animation = anim
        binding.companyName.animation = anim
        binding.email.animation = anim
        binding.name.animation = anim
    }

    fun deleteItem(position: Int) {
        val u : User = this.currentList.get(position)
        val dialog : AlertDialog = AlertDialog.Builder(context)
        .setMessage("delete company: ${u.company!!.name}")
                .setPositiveButton(R.string.delete, DialogInterface.OnClickListener{ _, _ ->
                    adminViewModel.removeCompany(u.userId)
                })
                .setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener{ _, _ ->
                            adminViewModel.getCompanyUsers()

                        })
                .setOnCancelListener {
                    adminViewModel.getCompanyUsers()
                }
        .create()
        dialog.show()

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

    class CompanyListItemListener (val clickListener: (user : User) -> Unit) {
        fun onClick(v : View, u : User) {
            clickListener(u)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {
                    mFilteredList = mListRef?.sortedWith(
                            compareBy({ it.company!!.name }
                            ))
                } else {
                    mListRef?.let {
                        val filteredList = arrayListOf<User>()
                        for (item in mListRef!!) {
                                if (charString.toLowerCase() in item.company!!.name.toLowerCase()
                                ) {
                                    filteredList.add(item)
                                }

                        }

                        mFilteredList = filteredList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList?.sortedWith(
                        compareBy({ it.company!!.name }
                        ))
                return filterResults
            }

            override fun publishResults(
                    charSequence: CharSequence,
                    filterResults: FilterResults
            ) {
                mFilteredList = filterResults.values as List<User>?
                submitList(mFilteredList)
            }
        }    }
}