package com.flexso.flexsame.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.SettingsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    val viewModel : SettingsViewModel by viewModel()
    lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.settings_fragment,container,false)
        setupViewModel()
        return binding.root
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.settingsViewModel = viewModel
    }


}
