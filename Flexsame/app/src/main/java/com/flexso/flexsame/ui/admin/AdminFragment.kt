package com.flexso.flexsame.ui.admin

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
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
        val adapter = CompanyListAdapter(context!!, CompanyListItemListener {
            userId -> Toast.makeText(context, "pushed company with userId: ${userId}", Toast.LENGTH_LONG).show()
        })

        binding.companyList.adapter = adapter
        val divider = DividerItemDecoration(context,HORIZONTAL)
        binding.companyList.addItemDecoration(divider)

        viewModel.users.observe(
                viewLifecycleOwner,
                Observer{
                    adapter.submitList(it)
                }
        )
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.adminViewModel = viewModel
    }


}
