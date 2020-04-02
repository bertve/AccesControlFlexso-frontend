package com.flexso.flexsame.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.CompanyFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompanyFragment : Fragment() {

    private val viewModel: CompanyViewModel by viewModel()
    private lateinit var  binding : CompanyFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.company_fragment,container,false)

        setupViewModel()
        setupRecyclerView()
        return binding.root
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.companyViewModel = viewModel
    }

    private fun setupRecyclerView() {

    }


}
