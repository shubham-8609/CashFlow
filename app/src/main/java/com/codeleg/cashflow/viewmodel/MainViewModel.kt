package com.codeleg.cashflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.codeleg.cashflow.CashFlow
import com.codeleg.cashflow.database.PrefManager
import com.codeleg.cashflow.model.Category
import com.codeleg.cashflow.model.Expense
import com.codeleg.cashflow.model.ExpenseWithCategory
import com.codeleg.cashflow.repositories.CategoryRepository
import com.codeleg.cashflow.repositories.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val expenseRepo: ExpenseRepository
    private val categoryRepo: CategoryRepository

    val allExpense: LiveData<List<ExpenseWithCategory>>
    val totalExpense: LiveData<Float>
    val allCategory: LiveData<List<Category>>

    var highestExpense: LiveData<Float>
    var avgExpense: LiveData<Float>
    var spentPercentage: MediatorLiveData<Float>

    init {
        val expenseDao = (application as CashFlow).database.expenseDao()
        val categoryDao = application.database.categoryDao()
        expenseRepo = ExpenseRepository(expenseDao)
        categoryRepo = CategoryRepository(categoryDao)

        allExpense = expenseRepo.allExpense
        allCategory = categoryRepo.allCategory
        totalExpense = expenseRepo.totalExpense.map { it ?: 0f }
        highestExpense = allExpense.map { list ->
            list.maxOfOrNull { it.expense.amount } ?: 0f
        }
        avgExpense = allExpense.map { list ->
            if (list.isNotEmpty()) list.map { it.expense.amount }.average().toFloat() else 0f
        }
        spentPercentage = MediatorLiveData()
        spentPercentage.addSource(totalExpense) { total ->
            viewModelScope.launch(Dispatchers.IO) {
                val budget = PrefManager.getMonthlyBudget()
                val percentage = if (budget > 0) (total / budget) * 100f else 0f
                spentPercentage.postValue(percentage.coerceAtMost(100f))
            }
        }

    }


    fun saveExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepo.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepo.deleteExpense(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepo.updateExpense(expense)
        }
    }

    fun getExpAndCatById(id: Int) = viewModelScope.async(Dispatchers.IO) {
        expenseRepo.getExpAndCatById(id)
    }
}
