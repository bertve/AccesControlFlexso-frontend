package com.flexso.flexsame.ui.admin

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.flexso.flexsame.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.AdminFragmentBinding
import com.flexso.flexsame.models.dto.auth.SignUpRequestCompany
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password

class AdminFragment : Fragment(), Validator.ValidationListener {
    private val viewModel: AdminViewModel by viewModel()
    private lateinit var  binding : AdminFragmentBinding
    private lateinit var fab : FloatingActionButton
    private lateinit var collapse_img : ImageView
    //validation
    @NotEmpty(message = "Company name is required" )
    private lateinit var companyName : EditText
    @NotEmpty(message = "Firstname is required" )
    private lateinit var firstName : EditText
    @NotEmpty(message = "Lastname is required" )
    private lateinit var lastName : EditText
    @NotEmpty(message = "Email is required" )
    @Email(message = "Must be an email address")
    private lateinit var email : EditText
    @NotEmpty(message = "Password is required")
    @Password(min= 6,scheme = Password.Scheme.ALPHA)
    private lateinit var password : EditText
    @ConfirmPassword
    private lateinit var password_confirm : EditText

    private lateinit var validator : Validator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.admin_fragment,container,false)
        setupViewModel()
        setupValidator()
        setupUI()
        setupRecyclerView()
        setupObservers()
        setupFilter()

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
            when (actionId){
                EditorInfo.IME_ACTION_DONE ->{
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
    }

    private fun onResponseRemove(succes: Boolean) {
        if(succes!!){
            Toast.makeText(context,"succesfully removed",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"removal failed",Toast.LENGTH_SHORT).show()
        }
    }

    private fun onResponseAdd(succes: Boolean) {
        if(succes!!){
            switchFab()
            resetAddCompanyFields()
            Toast.makeText(context,"succesfully added ${this.companyName.text}",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"failed to add ${this.companyName.text}",Toast.LENGTH_SHORT).show()
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

    private fun setupFilter() {
    }

    private fun setupRecyclerView() {
        val adapter = CompanyListAdapter(context!!,viewModel)

        binding.companyList.adapter = adapter
        val divider = DividerItemDecoration(context,HORIZONTAL)
        binding.companyList.addItemDecoration(divider)

        viewModel.users.observe(
                viewLifecycleOwner,
                Observer{
                    adapter.submitList(it)
                }
        )

        //swipe
        val itemTouchHelper : ItemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.companyList)

    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.adminViewModel = viewModel
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        for (error : ValidationError in errors!!.iterator()){
            var view : View = error.view
            val message :  String = error.getCollatedErrorMessage(activity)
            if (view is EditText ){
                view.setError(message)
            }else{
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
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


}
