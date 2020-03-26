package com.flexso.flexsame.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.SettingsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    val viewModel : SettingsViewModel by viewModel()
    lateinit var binding: SettingsFragmentBinding
    lateinit var auth_fingerprint_switch : Switch

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.settings_fragment,container,false)
        setupViewModel()
        setupUI()
        setupObservers()
        return binding.root
    }

    private fun setupUI() {
        val sharedPreferences = this.activity!!.getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE)
        val auth_fingerprint_activated = sharedPreferences.getBoolean("SETTINGS_AUTH_FINGERPRINT",true)
        auth_fingerprint_switch = binding.authenticationFingerprintSwitch
        auth_fingerprint_switch.isChecked = auth_fingerprint_activated
    }

    private fun setupObservers() {
        auth_fingerprint_switch.setOnCheckedChangeListener{
            _,isChecked ->
            setFingerAuthentication(isChecked)
        }
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.settingsViewModel = viewModel
    }

    fun setFingerAuthentication(b : Boolean){
        val sharedPreferencesEditor = this.activity!!.getSharedPreferences("PREFERENCES",android.content.Context.MODE_PRIVATE).edit()
        sharedPreferencesEditor.putBoolean("SETTINGS_AUTH_FINGERPRINT",b)
                .commit()
    }


}
