package com.flexso.flexsame.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.AdminFragmentBinding

class AdminFragment : Fragment() {
    private val viewModel: AdminViewModel by viewModel()
    private lateinit var  binding : AdminFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.admin_fragment,container,false)

        setupViewModel()
        setupRecyclerView()
        setupFilter()
        return binding.root
    }

    private fun setupFilter() {
    }

    private fun setupRecyclerView() {
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.adminViewModel = viewModel
    }


}
