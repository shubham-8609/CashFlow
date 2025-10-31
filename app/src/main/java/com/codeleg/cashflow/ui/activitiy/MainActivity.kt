package com.codeleg.cashflow.ui.activitiy

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.codeleg.cashflow.database.PrefManager
import com.codeleg.cashflow.databinding.ActivityMainBinding
import com.codeleg.cashflow.databinding.LayoutSetBudgetBinding
import com.codeleg.cashflow.ui.fragment.AddFragment
import com.codeleg.cashflow.ui.fragment.EditFragment
import com.codeleg.cashflow.ui.fragment.HomeFragment
import com.codeleg.cashflow.ui.fragment.NavigationListener
import com.codeleg.cashflow.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.Toast
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() , NavigationListener {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val pf = PrefManager
    private var isBudgetSet:Boolean = false

    private lateinit var mainContainer: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainContainer = binding.mainContainer

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(binding.mainContainer.id, HomeFragment())
            }
        }
        manageBudget()

    }

    private fun manageBudget(){
        lifecycleScope.launch {
             isBudgetSet = withContext(Dispatchers.IO){ pf.isBudgetSet() }
        if(!isBudgetSet) askToSetBudget() else checkIsReached()
        }
    }

    override fun navigateToAddExpense() {
        supportFragmentManager.commit {
            replace(binding.mainContainer.id, AddFragment())
            addToBackStack(null)
        }
    }

    override fun navigateToHome() {
        supportFragmentManager.popBackStack()
    }

    override fun navigateToEditExpense(expenseId: Int) {
        supportFragmentManager.commit {
            replace(binding.mainContainer.id, EditFragment.newInstance(expenseId))
            addToBackStack(null)
        }
    }

    private fun checkIsReached() {
        lifecycleScope.launch {
            val monthlyBudget = PrefManager.getMonthlyBudget()
            val totalExpense = mainViewModel.totalExpense.value ?: 0f

            if (monthlyBudget > 0 && totalExpense >= monthlyBudget) {
                withContext(Dispatchers.Main) {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("Budget Limit Reached")
                        .setMessage("You've reached your monthly budget limit of ₹${monthlyBudget.toInt()}. Would you like to update your budget?")
                        .setPositiveButton("Change Budget") { dialog, _ ->
                            dialog.dismiss()
                            askToSetBudget()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }
            }
        }
    }

    private fun askToSetBudget(){
        val dialogBinding = LayoutSetBudgetBinding.inflate(LayoutInflater.from(this))

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

// Access views
        val etBudget = dialogBinding.etBudget
        val btnSave = dialogBinding.btnSaveBudget

        btnSave.setOnClickListener {
            val budget = etBudget.text.toString().toFloatOrNull()
            if (budget != null && budget > 0) {
                lifecycleScope.launch(Dispatchers.IO) {
                    PrefManager.saveMonthlyBudget(budget)
                }
                    Toast.makeText(this@MainActivity, "Budget saved successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                etBudget.error = "Please enter a valid number"
            }
        }

        dialog.show()

    }

}