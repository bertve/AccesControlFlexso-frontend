package com.flexso.flexsame.ui.company

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import com.flexso.flexsame.databinding.OfficelistItemBinding
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.models.User
import com.flexso.flexsame.ui.admin.AdminFragmentDirections
import com.flexso.flexsame.ui.admin.CompanyListAdapter

class OfficeListAdapter (val context : Context,val companyViewModel: CompanyViewModel) : ListAdapter<Office, OfficeListAdapter.ViewHolder>(OfficeListDiffCallBack()), Filterable {

    var isCLickable : Boolean = true
    var mListRef: List<Office>? = null
    var mFilteredList: List<Office>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        setAnimations(holder)
        holder.bind(
                OfficeListItemListener {
                    office -> if (this.isCLickable){
                    Toast.makeText(context, "pushed office: $office}", Toast.LENGTH_LONG).show()
                }
                },item)
    }

    private fun setAnimations(holder: ViewHolder) {
        val binding = holder.binding
        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        binding.streetHouseNumber.animation = anim
        binding.officeImg.animation = anim
        binding.postalCodeCity.animation = anim
        binding.country.animation = anim
        binding.cardContainer.animation = anim
    }

    fun deleteItem(position: Int){
        val o : Office = this.currentList.get(position)
        val dialog : AlertDialog = AlertDialog.Builder(context)
                .setMessage("delete office: ${o.address.street}")
                .setPositiveButton(R.string.delete, DialogInterface.OnClickListener{ _, _ ->
                    companyViewModel.removeOffice(o.officeId)
                })
                .setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener{ _, _ ->
                            companyViewModel.getOffices()
                        })
                .setOnCancelListener {
                    companyViewModel.getOffices()
                }
                .create()
        dialog.show()

    }


    class ViewHolder private constructor(val binding : OfficelistItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OfficeListItemListener,item: Office) {
            binding.office = item
            binding.clickListener = clickListener
            binding.streetHouseNumber.text = item.streetHouseNumberString()
            binding.postalCodeCity.text = item.postalCodeCity()
            binding.country.text = item.address.country
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OfficelistItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    class OfficeListDiffCallBack : DiffUtil.ItemCallback<Office>(){
        override fun areItemsTheSame(oldItem: Office, newItem: Office): Boolean {
            return oldItem.officeId == newItem.officeId
        }

        override fun areContentsTheSame(oldItem: Office, newItem: Office): Boolean {
            return oldItem == newItem
        }

    }

    class OfficeListItemListener (val clickListener: (office : Office) -> Unit) {
        fun onClick(v : View, o : Office) {
            clickListener(o)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    mFilteredList = mListRef?.sortedWith(
                            compareBy({ it.address.street }
                                    ,{ it.address.houseNumber}
                                    ,{ it.address.postalCode}
                                    ,{ it.address.town}
                                    ,{ it.address.country}
                            ))
                } else {
                    mListRef?.let {
                        val filteredList = arrayListOf<Office>()
                        for (item in mListRef!!) {
                            if (charString.toLowerCase() in item.filterString().toLowerCase()
                            ) {
                                filteredList.add(item)
                            }

                        }

                        mFilteredList = filteredList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList?.sortedWith(
                        compareBy({ it.address.street }
                                ,{ it.address.houseNumber}
                                ,{ it.address.postalCode}
                                ,{ it.address.town}
                                ,{ it.address.country}
                        ))
                return filterResults
            }

            override fun publishResults(
                    charSequence: CharSequence,
                    filterResults: FilterResults
            ) {
                mFilteredList = filterResults.values as List<Office>?
                submitList(mFilteredList)
            }
        }    }
}