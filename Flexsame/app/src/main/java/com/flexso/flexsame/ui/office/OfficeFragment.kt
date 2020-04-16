package com.flexso.flexsame.ui.office

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.flexso.flexsame.R
import com.flexso.flexsame.databinding.OfficeFragmentBinding
import com.flexso.flexsame.models.Address
import com.flexso.flexsame.models.Office
import com.flexso.flexsame.utils.DefaultItemDecorator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel


class OfficeFragment : Fragment() {

    private val viewModel: OfficeViewModel by viewModel()
    private lateinit var binding: OfficeFragmentBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var collapse_img: ImageView
    private lateinit var authorizedPersonListAdapter: AuthorizedPersonListAdapter
    private lateinit var unAuthorizedPersonListAdapter: UnAuthorizedPersonListAdapter
    private lateinit var addButton: Button

    //helpers
    private var addressHelper: Address = Address("", "", "", "", "")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.office_fragment, container, false)
        setupViewModel()
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
            viewModel.resetCheckedList()
            viewModel.getUnAuthorizedPersons()
        }
        binding.add.setOnClickListener {
            viewModel.addCheckedPersons()
            switchFab()
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

        viewModel.editSucces.observe(viewLifecycleOwner, Observer { onResponseEdit(it) })

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
            val layout: ConstraintLayout = LayoutInflater.from(context).inflate(R.layout.edit_office_address_dialog, null) as ConstraintLayout
            val street = layout.findViewById<EditText>(R.id.street)
            val number = layout.findViewById<EditText>(R.id.number)
            val postalCode = layout.findViewById<EditText>(R.id.postalCode)
            val city = layout.findViewById<EditText>(R.id.city)
            val country = layout.findViewById<EditText>(R.id.country)
            val address = viewModel.office.address
            street.setText(address.street)
            number.setText(address.houseNumber)
            postalCode.setText(address.postalCode)
            city.setText(address.town)
            country.setText(address.country)

            builder.setTitle("Edit office address")
                    .setView(layout)
                    .setPositiveButton(R.string.edit, DialogInterface.OnClickListener { dialog, which ->
                        var a: Address = Address(street.text.toString(),
                                number.text.toString(),
                                postalCode.text.toString(),
                                city.text.toString(),
                                country.text.toString())
                        viewModel.editOfficeAddress(a)
                        addressHelper = a
                    })
                    .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()
        }

        binding.filterUnAuth.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterUnAuth(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUnAuth(newText)
                return true
            }
        })

    }

    private fun setupRecyclerView() {
        val adapter = AuthorizedPersonListAdapter(context!!, viewModel)
        authorizedPersonListAdapter = adapter
        binding.authorizedPersonList.adapter = adapter

        binding.authorizedPersonList.addItemDecoration(DefaultItemDecorator(context!!.resources.getDimensionPixelSize(R.dimen.list_horizontal_spacing),
                context!!.resources.getDimensionPixelSize(R.dimen.list_vertical_spacing)))

        viewModel.authorizedPersons.observe(
                viewLifecycleOwner,
                Observer {
                    if (adapter.mListRef == null) {
                        adapter.mListRef = it
                    }
                    adapter.submitList(it)
                }
        )

        //swipe
        val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(AuthorizedPersonListSwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.authorizedPersonList)

        //unauthPersons

        unAuthorizedPersonListAdapter = UnAuthorizedPersonListAdapter(context!!, viewModel)
        binding.unAuthorizedPersonList.adapter = unAuthorizedPersonListAdapter
        binding.unAuthorizedPersonList.addItemDecoration(DefaultItemDecorator(context!!.resources.getDimensionPixelSize(R.dimen.list_horizontal_spacing),
                context!!.resources.getDimensionPixelSize(R.dimen.list_vertical_spacing)))

        viewModel.unAuthorizedPersons.observe(
                viewLifecycleOwner, Observer {
            if (unAuthorizedPersonListAdapter.mListRef == null) {
                unAuthorizedPersonListAdapter.mListRef = it
            }
            unAuthorizedPersonListAdapter.submitList(it)
        }

        )

        viewModel.checkedPersons.observe(viewLifecycleOwner,
                Observer {
                    Log.i("checkedList", it.toString())
                    this.addButton.isEnabled = it.size != 0
                })

    }

    private fun setupUI() {
        fab = binding.fab
        fab.bringToFront()
        collapse_img = binding.collapse
        addButton = binding.add
    }

    private fun setupViewModel() {
        binding.lifecycleOwner = this
        binding.officeViewModel = viewModel
        val args = OfficeFragmentArgs.fromBundle(arguments!!)
        viewModel.setCurrentOffice(args.currentOffice)
        binding.office = args.currentOffice
    }

    private fun filter(s: String?) {
        authorizedPersonListAdapter.filter.filter(s)
    }

    private fun filterUnAuth(s: String?) {
        unAuthorizedPersonListAdapter.filter.filter(s)
    }

    override fun onResume() {
        super.onResume()
        authorizedPersonListAdapter.filter.filter("")
        unAuthorizedPersonListAdapter.filter.filter("")
    }

    private fun switchFab() {
        fab.isExpanded = !fab.isExpanded
        (binding.authorizedPersonList.adapter as AuthorizedPersonListAdapter).isCLickable = !fab.isExpanded
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
            Toast.makeText(context, "Succesfully added ", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onResponseEdit(succes: Boolean) {
        if (succes) {
            binding.office = Office(viewModel.office.officeId, viewModel.office.company, addressHelper)
            Toast.makeText(context, "Succesfully edited", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to edit", Toast.LENGTH_SHORT).show()
        }
    }

}
