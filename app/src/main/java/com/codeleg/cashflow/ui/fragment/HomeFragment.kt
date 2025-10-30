package com.codeleg.cashflow.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.codeleg.cashflow.R
import com.codeleg.cashflow.adapter.ExpenseAdapter
import com.codeleg.cashflow.databinding.FragmentHomeBinding
import com.codeleg.cashflow.databinding.LayoutDetailsBinding
import com.codeleg.cashflow.model.ExpenseWithCategory
import com.codeleg.cashflow.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var addBtn: FloatingActionButton
    private var navigationListener: NavigationListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationListener) navigationListener = context
        else throw RuntimeException("$context must implement NavigationListener")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        expenseAdapter = ExpenseAdapter { item ->
            showExpenseDetails(item)
        }
        binding.viewModel = mainViewModel
        binding.rvExpenses.adapter = expenseAdapter
        mainViewModel.allExpense.observe(viewLifecycleOwner) { expenses ->
            expenseAdapter.submitList(expenses ?: emptyList())
            binding.noExpenseimg.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE
            binding.tvTransactionCount.text = expenses.size.toString()


        }
        addBtn = binding.fabAddExpense
        addBtn.setOnClickListener { navigationListener?.navigateToAddExpense() }
        mainViewModel.totalExpense.observe(viewLifecycleOwner) { totalExpense ->
            binding.tvTotalSpent.text = "₹${totalExpense ?: 0f}"
        }
        return binding.root
    }

    fun showExpenseDetails(item: ExpenseWithCategory) {
        val dialogBinding = LayoutDetailsBinding.inflate(layoutInflater)
        val dialog =
            MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root).create()

        val sdfDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val sdfTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
        dialogBinding.expense = item
        dialogBinding.tvDate.text = sdfDate.format(item.expense.date)
        dialogBinding.tvTime.text = sdfTime.format(item.expense.date)

        dialogBinding.btnEdit.setOnClickListener {
            navigationListener?.navigateToEditExpense(item.expense.id)
            dialog.dismiss()
        }

        dialogBinding.btnDelete.setOnClickListener {
            mainViewModel.deleteExpense(item.expense)
            Snackbar.make(binding.root, "Expense Deleted", Snackbar.LENGTH_LONG).setAction("Undo") {
                    mainViewModel.saveExpense(item.expense)
                    Snackbar.make(binding.root, "Expense Restored", Snackbar.LENGTH_SHORT).show()
                }.show()
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        navigationListener = null
    }

}