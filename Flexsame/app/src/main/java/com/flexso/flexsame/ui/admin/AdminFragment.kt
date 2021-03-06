package com.flexso.flexsame.ui.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.flexso.flexsame.MainActivity
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.AdminFragmentBinding
import com.flexso.flexsame.models.dto.auth.SignUpRequestCompany
import com.flexso.flexsame.utils.DefaultItemDecorator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminFragment : Fragment(), Validator.ValidationListener {
    private val viewModel: AdminViewModel by viewModel()
    private lateinit var binding: AdminFragmentBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var collapse_img: ImageView
    private lateinit var companyListAdapter: CompanyListAdapter

    //validation
    @NotEmpty(message = "Company name is required")
    private lateinit var companyName: EditText
    @NotEmpty(message = "Firstname is required")
    private lateinit var firstName: EditText
    @NotEmpty(message = "Lastname is required")
    private lateinit var lastName: EditText
    @NotEmpty(message = "Email is required")
    @Email(message = "Must be an email address")
    private lateinit var email: EditText
    @NotEmpty(message = "Password is required")
    @Password(min = 6, scheme = Password.Scheme.ALPHA)
    private lateinit var password: EditText
    @ConfirmPassword
    private lateinit var password_confirm: EditText

    private lateinit var validator: Validator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.admin_fragment, container, false)

        setupViewModel()
        setupValidator()
        setupUI()
        setupRecyclerView()
        setupObservers()

        return binding.root
    }

    private fun setupValidator() {
        validator = Validator(this)
        validator.setValidationListener(this)
    }

    private fun setupUI() {
        fab = binding.fab
        fab.bringToFront()
        collapse_img = binding.collapse
        companyName = binding.name
        firstName = binding.firstName
        lastName = binding.lastName
        email = binding.email
        password = binding.password
        password_confirm = binding.passwordConfirm
    }

    private fun setupObservers() {
        fab.setOnClickListener {
            switchFab()
        }
        collapse_img.setOnClickListener {
            switchFab()
        }
        binding.add.setOnClickListener {
            validator.validate()
            (activity as MainActivity).hideKeyboard(this.requireView())
        }
        password_confirm.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    validator.validate()
                    (activity as MainActivity).hideKeyboard(this.requireView())
                }
            }
            false
        }

        viewModel.addSucces.observe(viewLifecycleOwner, Observer {
            onResponseAdd(it)
        })

        viewModel.removeSucces.observe(viewLifecycleOwner, Observer {
            onResponseRemove(it)
        })

        binding.filter.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })
    }

    private fun filter(s: String?) {
        companyListAdapter.filter.filter(s)
    }

    private fun onResponseRemove(succes: Boolean) {
        if (succes) {
            Toast.makeText(context, "Succesfully removed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Removal failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onResponseAdd(succes: Boolean) {
        if (succes) {
            switchFab()
            resetAddCompanyFields()
            Toast.makeText(context, "Succesfully added ${this.companyName.text}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to add ${this.companyName.text}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetAddCompanyFields() {
        companyName.setText("")
        firstName.setText("")
        lastName.setText("")
        email.setText("")
        password.setText("")
        password_confirm.setText("")
    }

    private fun switchFab() {
        fab.isExpanded = !fab.isExpanded
        (binding.companyList.adapter as CompanyListAdapter).isCLickable = !fab.isExpanded
    }

    private fun setupRecyclerView() {
        val adapter = CompanyListAdapter(context!!, viewModel)
        companyListAdapter = adapter
        binding.companyList.adapter = adapter
        binding.companyList.addItemDecoration(DefaultItemDecorator(context!!.resources.getDimensionPixelSize(R.dimen.list_horizontal_spacing),
                context!!.resources.getDimensionPixelSize(R.dimen.list_vertical_spacing)))

        viewModel.users.observe(
                viewLifecycleOwner,
                Observer {
                    adapter.mListRef = it
                    adapter.submitList(it.sortedBy { it.company!!.name })
                }
        )

        //swipe
        val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(CompanyListSwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.companyList)

    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.adminViewModel = viewModel
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        for (error: ValidationError in errors!!.iterator()) {
            var view: View = error.view
            val message: String = error.getCollatedErrorMessage(activity)
            if (view is EditText) {
                view.error = message
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onValidationSucceeded() {
        viewModel.addCompany(SignUpRequestCompany(companyName.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                email.text.toString(),
                password.text.toString()))
    }

    override fun onResume() {
        super.onResume()
        companyListAdapter.filter.filter("")

    }


}
