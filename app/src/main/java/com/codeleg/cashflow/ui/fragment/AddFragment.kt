package com.codeleg.cashflow.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.codeleg.cashflow.databinding.FragmentAddBinding
import com.codeleg.cashflow.viewmodel.MainViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue

class AddFragment : Fragment() {

    private val vm: MainViewModel by activityViewModels()
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var etTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var etNotes: EditText
    private lateinit var btnSave: Button

    private var categories:String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(layoutInflater , container , false)
        setupCategorySpinner()
        binding.btnSelectDate.setOnClickListener {
            openDatePicker()
        }
        etTitle = binding.etTitle
        etAmount = binding.etAmount
        etNotes = binding.etNotes
        btnSave = binding.btnSave
        btnSave.setOnClickListener { saveExpense() }

        vm.allCategory.observe(viewLifecycleOwner) { it ->
            categories = it.map { it.name }.toString()
        }



        return binding.root
    }

    private fun setupCategorySpinner() {
        vm.allCategory.observe(viewLifecycleOwner) { categories ->
            // Convert your list of Category objects to a list of names for the spinner
            val categoryNames = categories.map { it.name }

            // Create ArrayAdapter
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryNames
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            binding.spinnerCategory.adapter = adapter
        }
    }

    private fun saveExpense(){
        val title = etTitle.text.toString()
        val amount = etAmount.text.toString().toFloatOrNull()
        val notes = etNotes.text.toString()
        val selectedCategory = binding.spinnerCategory.selectedItem.toString()
        val selectedDate = binding.btnSelectDate.text.toString()
        // Validate input
        if (title.isEmpty() || amount == null || notes.isEmpty() || selectedCategory.isEmpty() || selectedDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        // Convert date string to Date object
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = sdf.parse(selectedDate)

        // Find the categoryId of selected category
        val category = vm.allCategory.value?.find { it.name == selectedCategory }

        if (date != null && category != null) {
            vm.saveExpense(title, amount, date, category.id, notes.ifEmpty { null })
            Toast.makeText(requireContext(), "Expense saved successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error saving expense", Toast.LENGTH_SHORT).show()
        }



    }

    private fun openDatePicker() {
        // Optional constraints: prevent choosing a future date
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select expense date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraints)
            .build()

        // Use childFragmentManager so the dialog is tied to this fragment
        picker.addOnPositiveButtonClickListener { selection ->
            // selection is the UTC millis as Long
            val timestamp = selection as Long
            val date = Date(timestamp)
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val formatted = sdf.format(date)

            // Set UI
            binding.btnSelectDate.text = formatted

        }

        picker.show(childFragmentManager, "DATE_PICKER")
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}