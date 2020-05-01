package com.flexso.flexsame.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.OfficeFragmentBindingImpl
import com.flexso.flexsame.databinding.WalletFragmentBinding
import com.flexso.flexsame.models.Address
import com.flexso.flexsame.models.Company
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.services.CurrentKey
import com.flexso.flexsame.utils.DefaultItemDecorator
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletFragment : Fragment() {

    val viewModel: WalletViewModel by viewModel()
    lateinit var binding: WalletFragmentBinding
    lateinit var spinner: Spinner
    lateinit var animToLeft : Animation
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.wallet_fragment, container, false)

        setupViewModel()
        setupCurrentUserAndOffice()
        setupUI()
        setupRecyclerView()
        setupSpinner()
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.selectedOffice.observe(viewLifecycleOwner, Observer {
            binding.office = it
            binding.currentSelectedOffice.startAnimation(animToLeft)
            binding.street.startAnimation(animToLeft)
            binding.street.startAnimation(animToLeft)
            binding.postalCodeCity.startAnimation(animToLeft)
            binding.country.startAnimation(animToLeft)
            binding.avatar.startAnimation(animToLeft)
        })

        viewModel.keyToken.observe(viewLifecycleOwner, Observer {
            it?.let {
                CurrentKey.currentKeyToken = it
            }
        })
    }

    private fun setupUI() {
        this.animToLeft = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        binding.currentSelectedOffice.animation = animToLeft
        binding.street.animation = animToLeft
        binding.street.animation = animToLeft
        binding.postalCodeCity.animation = animToLeft
        binding.country.animation = animToLeft
        binding.avatar.animation = animToLeft
    }

    private fun setupCurrentUserAndOffice() {
        val args = WalletFragmentArgs.fromBundle(arguments!!)
        viewModel.setUser(args.currentUser)
        binding.office = viewModel.selectedOffice.value
    }

    private fun setupSpinner() {
        spinner = binding.companySpinner
        var adapter: ArrayAdapter<String>

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

        val adapter = WalletAdapter(context!!, WalletItemListener { office ->
            viewModel.setCurrentOffice(office)
        })

        binding.walletList.adapter = adapter

        binding.walletList.addItemDecoration(DefaultItemDecorator(context!!.resources.getDimensionPixelSize(R.dimen.list_horizontal_spacing),
                context!!.resources.getDimensionPixelSize(R.dimen.list_vertical_spacing)))

        viewModel._filteredOffices.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.walletViewModel = viewModel
    }
}
