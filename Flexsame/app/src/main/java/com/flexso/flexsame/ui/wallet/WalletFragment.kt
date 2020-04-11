package com.flexso.flexsame.ui.wallet

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.WalletFragmentBinding
import com.flexso.flexsame.utils.DefaultItemDecorator

class WalletFragment : Fragment() {

    val viewModel: WalletViewModel by viewModel()
    lateinit var binding : WalletFragmentBinding
    lateinit var spinner : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.wallet_fragment,container,false)

        setupViewModel()
        setupRecyclerView()
        setupSpinner()
        return binding.root
    }

    private fun setupSpinner() {
        spinner = binding.companySpinner
        var adapter : ArrayAdapter<String>

        viewModel.offices.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_item,
                    it.map { it.company.name }.plus("All").toSortedSet().toTypedArray()
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

                spinner.adapter = adapter
            }
        })


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.filterOffices("")
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                var companyName = parent.getItemAtPosition(pos).toString()
                viewModel.filterOffices(companyName)
            }

        }
    }

    private fun setupRecyclerView() {
        val adapter = WalletAdapter(context!!,WalletItemListener {
            officeId ->  Toast.makeText(context, "pushed officeId: ${officeId}", Toast.LENGTH_LONG).show()
        })

        binding.walletList.adapter = adapter

        binding.walletList.addItemDecoration(DefaultItemDecorator(context!!.resources.getDimensionPixelSize(R.dimen.list_horizontal_spacing),
                context!!.resources.getDimensionPixelSize(R.dimen.list_vertical_spacing)))

        viewModel.filteredOffices.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.walletViewModel = viewModel
        val args = WalletFragmentArgs.fromBundle(arguments!!)

        viewModel.setUser(args.currentUser)
    }


}
