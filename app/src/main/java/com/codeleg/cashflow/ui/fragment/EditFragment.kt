package com.codeleg.cashflow.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.codeleg.cashflow.databinding.FragmentEditBinding
import com.codeleg.cashflow.model.Expense
import com.codeleg.cashflow.model.ExpenseWithCategory
import com.codeleg.cashflow.viewmodel.MainViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditFragment : Fragment() {

    private val vm: MainViewModel by activityViewModels()
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private var navigationListener: NavigationListener? = null
    private var currentExpenseId: Int = -1
    private var currentExpense: ExpenseWithCategory? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationListener = context as? NavigationListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentExpenseId = arguments?.getInt(ARG_EXPENSE_ID, -1) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupCategorySpinner()
        setupButtons()
        loadExpenseData()
    }

    private fun setupToolbar() {
        binding.toolbarEditExpense.setNavigationOnClickListener {
            navigationListener?.navigateToHome()
        }
    }

    private fun setupCategorySpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.allCategory.observe(viewLifecycleOwner) { categories ->
                val categoryNames = categories.map { it.name }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    categoryNames
                ).also {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                binding.spinnerEditCategory.adapter = adapter

                // Once spinner is populated, set the current expense category
                currentExpense?.let { exp ->
                    val selectedIndex = categoryNames.indexOf(exp.category.name)
                    if (selectedIndex >= 0) {
                        binding.spinnerEditCategory.setSelection(selectedIndex)
                    }
                }
            }
        }
    }

    private fun setupButtons() {
        binding.btnEditDate.setOnClickListener { openDatePicker() }
        binding.btnUpdateExpense.setOnClickListener { updateExpense() }
    }

    private fun loadExpenseData() {
        if (currentExpenseId == -1) {
            Toast.makeText(requireContext(), "Invalid expense ID", Toast.LENGTH_SHORT).show()
            navigationListener?.navigateToHome()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            currentExpense = vm.getExpAndCatById(currentExpenseId).await()
            currentExpense?.let { populateFields(it) }
        }

    }

    private fun populateFields(expenseWithCategory: ExpenseWithCategory) {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        binding.etEditTitle.setText(expenseWithCategory.expense.title)
        binding.etEditAmount.setText(expenseWithCategory.expense.amount.toString())
        binding.etEditNotes.setText(expenseWithCategory.expense.note ?: "")
        binding.btnEditDate.text = sdf.format(expenseWithCategory.expense.date)
    }

    private fun openDatePicker() {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select new date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraints)
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val date = Date(selection)
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            binding.btnEditDate.text = sdf.format(date)
        }

        picker.show(childFragmentManager, "EDIT_DATE_PICKER")
    }

    private fun updateExpense() {
        val title = binding.etEditTitle.text.toString()
        val amount = binding.etEditAmount.text.toString().toFloatOrNull()
        val notes = binding.etEditNotes.text.toString()
        val categoryName = binding.spinnerEditCategory.selectedItem?.toString() ?: ""
        val dateString = binding.btnEditDate.text.toString()

        if (title.isEmpty() || amount == null || categoryName.isEmpty() || dateString.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = sdf.parse(dateString)
        val category = vm.allCategory.value?.find { it.name == categoryName }

        if (date != null && category != null && currentExpense != null) {
            val updatedExpense = Expense(
                id = currentExpense!!.expense.id,
                title = title,
                amount = amount,
                date = date,
                categoryId = category.id,
                note = notes.ifEmpty { null }
            )

            vm.updateExpense(updatedExpense)
            Toast.makeText(requireContext(), "Expense updated successfully", Toast.LENGTH_SHORT)
                .show()
            navigationListener?.navigateToHome()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        navigationListener = null
    }

    companion object {
        private const val ARG_EXPENSE_ID = "expense_id"

        fun newInstance(expenseId: Int) = EditFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_EXPENSE_ID, expenseId)
            }
        }
    }
}
