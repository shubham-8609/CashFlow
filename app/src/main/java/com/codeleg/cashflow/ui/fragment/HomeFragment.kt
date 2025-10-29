package com.codeleg.cashflow.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.codeleg.cashflow.adapter.ExpenseAdapter
import com.codeleg.cashflow.databinding.FragmentHomeBinding
import com.codeleg.cashflow.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this
        expenseAdapter = ExpenseAdapter(emptyList())
        binding.rvExpenses.adapter = expenseAdapter
        lifecycleScope.launch {
            mainViewModel.allExpense.observe(viewLifecycleOwner) { expenses ->
                expenseAdapter.submitList(expenses ?: emptyList())
            }
        }
        addBtn = binding.fabAddExpense
        addBtn.setOnClickListener { navigationListener?.navigateToAddExpense() }
        return binding.root
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