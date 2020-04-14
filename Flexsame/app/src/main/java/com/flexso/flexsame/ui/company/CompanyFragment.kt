package com.flexso.flexsame.ui.company

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.flexso.flexsame.MainActivity
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.CompanyFragmentBinding
import com.flexso.flexsame.models.Address
import com.flexso.flexsame.models.Company
import com.flexso.flexsame.utils.DefaultItemDecorator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompanyFragment : Fragment(), Validator.ValidationListener{

    private val viewModel: CompanyViewModel by viewModel()
    private lateinit var  binding : CompanyFragmentBinding
    private lateinit var fab : FloatingActionButton
    private lateinit var collapse_img : ImageView
    private lateinit var officeListAdapter: OfficeListAdapter

    //validation
    private lateinit var validator : Validator
    @NotEmpty(message = "Street is required" )
    private lateinit var street : EditText
    @NotEmpty(message = "House number is required" )
    private lateinit var houseNumber : EditText
    @NotEmpty(message = "Postal code is required" )
    private lateinit var postalCode : EditText
    @NotEmpty(message = "City is required" )
    private lateinit var city : EditText
    @NotEmpty(message = "Country is required")
    private lateinit var country : EditText

    //helpers
    private var companyTitleHelper : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.company_fragment,container,false)
        setupViewModel()
        setupValidator()
        setupUI()
        setupRecyclerView()
        setupObservers()

        return binding.root
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
        country.setOnEditorActionListener { _, actionId, _ ->
            when (actionId){
                EditorInfo.IME_ACTION_DONE ->{
                    validator.validate()
                    (activity as MainActivity).hideKeyboard(this.requireView())
                }
            }
            false
        }

        viewModel.addSucces.observe(
                viewLifecycleOwner,
                Observer {
                    onResponseAdd(it)
                }
        )

        viewModel.removeSucces.observe(
                viewLifecycleOwner,
                Observer {
            onResponseRemove(it)
        })

        binding.filter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }
        })

        binding.edit.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(context)
            val layout : ConstraintLayout= LayoutInflater.from(context).inflate(R.layout.edit_company_name_dialog,null) as ConstraintLayout
            val input = layout.findViewById<EditText>(R.id.companyName)
            input.setText(viewModel.company.name)
            builder.setTitle("Edit company name")
                    .setView(layout)
                    .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                        viewModel.editCompanyName(input.text.toString())
                        companyTitleHelper = input.text.toString()
                    })
                    .setNegativeButton(R.string.cancel,DialogInterface.OnClickListener { dialog, which -> dialog.cancel()})
            builder.show()
        }

        viewModel.editSucces.observe(viewLifecycleOwner, Observer { onResponseEdit(it) })

    }

    private fun setupUI() {
        fab = binding.fab
        fab.bringToFront()
        collapse_img = binding.collapse
        street = binding.street
        houseNumber = binding.houseNumber
        postalCode = binding.postalCode
        city = binding.city
        country = binding.country
    }

    private fun setupValidator() {
        validator = Validator(this)
        validator.setValidationListener(this)
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.companyViewModel = viewModel
        val args = CompanyFragmentArgs.fromBundle(arguments!!)

        viewModel.setCurrentCompany(args.company!!)
        binding.company = args.company
    }

    private fun setupRecyclerView() {
        val adapter = OfficeListAdapter(context!!,viewModel)
        officeListAdapter = adapter
        binding.officeList.adapter = adapter

        binding.officeList.addItemDecoration(DefaultItemDecorator(context!!.resources.getDimensionPixelSize(R.dimen.list_horizontal_spacing),
                context!!.resources.getDimensionPixelSize(R.dimen.list_vertical_spacing)))

        viewModel.offices.observe(
                viewLifecycleOwner,
                Observer{
                    if (adapter.mListRef == null) {
                        adapter.mListRef = it
                    }
                    adapter.submitList(it)
                }
        )

        //swipe
        val itemTouchHelper : ItemTouchHelper = ItemTouchHelper(OfficeListSwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.officeList)
    }

    private fun filter(s : String?){
        officeListAdapter.filter.filter(s)
    }

    private fun onResponseRemove(succes: Boolean) {
        if(succes){
            Toast.makeText(context,"Succesfully removed", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Removal failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onResponseAdd(succes: Boolean) {
        if(succes){
            switchFab()
            resetAddOfficeFields()
            Toast.makeText(context,"Succesfully added ", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Failed to add", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onResponseEdit(succes: Boolean) {
        if(succes){
            binding.company = Company(viewModel.company.companyId,companyTitleHelper)
            Toast.makeText(context,"Succesfully edited", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Failed to edit", Toast.LENGTH_SHORT).show()
        }
    }



    private fun resetAddOfficeFields() {
        street.setText("")
        postalCode.setText("")
        houseNumber.setText("")
        city.setText("")
        country.setText("")
    }

    private fun switchFab() {
        fab.isExpanded = !fab.isExpanded
        (binding.officeList.adapter as OfficeListAdapter).isCLickable = !fab.isExpanded
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
        }    }

    override fun onValidationSucceeded() {
        viewModel.addOffice(Address(
                street.text.toString(),
                houseNumber.text.toString(),
                postalCode.text.toString(),
                city.text.toString(),
                country.text.toString()
        ))
    }

    override fun onResume() {
        super.onResume()
        officeListAdapter.filter.filter("")
    }
}
