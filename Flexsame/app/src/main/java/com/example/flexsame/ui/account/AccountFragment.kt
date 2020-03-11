package com.example.flexsame.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.flexsame.LoggedInUserViewModel
import com.example.flexsame.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

import com.example.flexsame.R
import com.example.flexsame.databinding.AccountFragmentBinding
import com.example.flexsame.ui.dialogs.LogoutDialog
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password

class AccountFragment : Fragment(), Validator.ValidationListener {

    val viewModel: LoggedInUserViewModel by viewModel()
    lateinit var binding : AccountFragmentBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.account_fragment, container, false)
        setupViewModel()
        validator = Validator(this)
        validator.setValidationListener(this)
        setupUI()
        setupListeners()
        return binding.root
    }

    private fun setupUI() {
        firstName = binding.firstName
        lastName = binding.lastName
        email = binding.email
        password = binding.password
        password_confirm = binding.passwordConfirm
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.accountViewModel = viewModel


    }

    private fun setupListeners() {
         binding.logout.setOnClickListener {
             val dialog = LogoutDialog(activity as MainActivity)
             dialog.show(fragmentManager!!,"Log out")
         }
        binding.update.setOnClickListener{
            validator.validate()
        }

        //no need to push button
        password_confirm.setOnEditorActionListener { _, actionId, _ ->
            when (actionId){
                EditorInfo.IME_ACTION_DONE ->{
                    validator.validate()
                    (activity as MainActivity).hideKeyboard(this.requireView())
                }
            }
            false
        }

        viewModel.updateSucces.observe(viewLifecycleOwner, Observer {
            onResponseUpdate(it)
        })
    }

    private fun onResponseUpdate(succes: Boolean?) {
        if(succes!!){
            Toast.makeText(context,"succesfully updated",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"failed to update",Toast.LENGTH_SHORT).show()
        }
    }


    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        for (error : ValidationError in errors!!.iterator()){
            var view : View = error.view
            val message :  String = error.getCollatedErrorMessage(activity)
            if (view is EditText ){
                (view as EditText).setError(message)
            }else{
                Toast.makeText(activity,message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onValidationSucceeded() {
        viewModel.update(firstName.text.toString(),
                lastName.text.toString(),
                email.text.toString(),
                password.text.toString())
    }


}
