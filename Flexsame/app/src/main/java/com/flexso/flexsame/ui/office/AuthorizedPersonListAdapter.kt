package com.flexso.flexsame.ui.office

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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.AuthorizedpersonlistItemBinding
import com.flexso.flexsame.models.User


class AuthorizedPersonListAdapter  (val context : Context, val officeViewModel: OfficeViewModel) : ListAdapter<User, AuthorizedPersonListAdapter.ViewHolder>(AuthorizedPersonListDiffCallBack()), Filterable {

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
                AuthorizedPersonListItemListener {
                    user -> if (this.isCLickable){
                   // Toast.makeText(context, "pushed user: $user}", Toast.LENGTH_LONG).show()
                }
                },item)
    }

    private fun setAnimations(holder: ViewHolder) {
        val binding = holder.binding
        val anim = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        binding.name.animation = anim
        binding.personImg.animation = anim
        binding.email.animation = anim
        binding.cardContainer.animation = anim
    }

    fun deleteItem(position: Int){
        val u : User = this.currentList.get(position)
        val dialog : AlertDialog = AlertDialog.Builder(context)
                .setMessage("delete user: ${u.getFullName()}")
                .setPositiveButton(R.string.delete, DialogInterface.OnClickListener{ _, _ ->
                    officeViewModel.deAuthorizeUserFromOffice(u.userId)
                })
                .setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener{ _, _ ->
                            officeViewModel.getAuthorizedPersons()
                        })
                .setOnCancelListener {
                    officeViewModel.getAuthorizedPersons()
                }
                .create()
        dialog.show()

    }


    class ViewHolder private constructor(val binding :AuthorizedpersonlistItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AuthorizedPersonListItemListener,item: User) {
            binding.user = item
            binding.clickListener = clickListener
            binding.name.text = item.getFullName()
            binding.email.text = item.email
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AuthorizedpersonlistItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    class AuthorizedPersonListDiffCallBack : DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

    class AuthorizedPersonListItemListener (val clickListener: (user : User) -> Unit) {
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
        }    }
}