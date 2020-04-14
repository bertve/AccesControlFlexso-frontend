package com.flexso.flexsame.ui.office

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.UnauthorizedpersonlistItemBinding
import com.flexso.flexsame.models.User
import com.google.android.material.card.MaterialCardView

class UnAuthorizedPersonListAdapter (val context : Context, val officeViewModel: OfficeViewModel) : ListAdapter<User, UnAuthorizedPersonListAdapter.ViewHolder>(AuthorizedPersonListAdapter.AuthorizedPersonListDiffCallBack()), Filterable {

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
                UnAuthorizedPersonListItemListener { user,view ->
                    if (this.isCLickable) {
                        (view as MaterialCardView).isChecked = !(view as MaterialCardView).isChecked
                        if (view.isChecked){
                            addToCheckedList(user.userId)
                        }else{
                            removeFromCheckedList(user.userId)
                        }
                    }
                },item)
    }

    private fun removeFromCheckedList(userId: Long){
        officeViewModel.removeFromCheckedList(userId)
    }

    private fun addToCheckedList(userId: Long){
        officeViewModel.addToCheckedList(userId)
    }

    private fun setAnimations(holder: ViewHolder) {
        val binding = holder.binding
        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        binding.name.animation = anim
        binding.personImg.animation = anim
        binding.email.animation = anim
        binding.cardContainer.animation = anim
    }


    class ViewHolder private constructor(val binding : UnauthorizedpersonlistItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: UnAuthorizedPersonListItemListener, item: User) {
            binding.user = item
            binding.clickListener = clickListener
            binding.name.text = item.getFullName()
            binding.email.text = item.email
            binding.cardContainer.isCheckable = true
            binding.cardContainer.isChecked = false
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UnauthorizedpersonlistItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    class UnAuthorizedPersonListItemListener (val clickListener: (user : User,view:View) -> Unit) {
        fun onClick(v : View, u : User) {
            clickListener(u,v)
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    mFilteredList = mListRef?.sortedWith(
                            compareBy({ it.lastName },
                                    {it.firstName}
                            ))
                } else {
                    mListRef?.let {
                        val filteredList = arrayListOf<User>()
                        for (item in mListRef!!) {
                            if (charString.toLowerCase() in item.getFullName().toLowerCase()
                            ) {
                                filteredList.add(item)
                            }

                        }

                        mFilteredList = filteredList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList?.sortedWith(
                        compareBy({ it.lastName },
                                {it.firstName}
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
        }
    }


}