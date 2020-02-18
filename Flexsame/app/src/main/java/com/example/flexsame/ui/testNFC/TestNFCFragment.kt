package com.example.flexsame.ui.testNFC

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flexsame.R


class TestNFCFragment : Fragment() {

    companion object {
        fun newInstance() = TestNFCFragment()
    }

    private lateinit var viewModel: TestNFCViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.test_nfc_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TestNFCViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
