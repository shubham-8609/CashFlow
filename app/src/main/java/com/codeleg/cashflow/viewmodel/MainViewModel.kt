package com.codeleg.cashflow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.codeleg.cashflow.CashFlow
import com.codeleg.cashflow.model.Category
import com.codeleg.cashflow.model.Expense
import com.codeleg.cashflow.model.ExpenseWithCategory
import com.codeleg.cashflow.repositories.CategoryRepository
import com.codeleg.cashflow.repositories.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val expenseRepo: ExpenseRepository
    private val categoryRepo: CategoryRepository

    val allExpense: LiveData<List<ExpenseWithCategory>>
    val totalExpense: LiveData<Float>
    val allCategory : LiveData<List<Category>>

    init {
        val expenseDao = (application as CashFlow).database.expenseDao()
        val categoryDao = application.database.categoryDao()
        categoryRepo = CategoryRepository(categoryDao)
        expenseRepo = ExpenseRepository(expenseDao)
        allExpense = expenseRepo.allExpense
         totalExpense = expenseRepo.totalExpense.map { it ?: 0f }
        allCategory = categoryRepo.allCategory



    }

    fun saveExpense(title: String, amount: Float, date: Date, categoryId: Int, note: String? = null) {
        val expense = Expense(
            title = title,
            amount = amount,
            date = date,
            categoryId = categoryId,
            note = note
        )

        viewModelScope.launch(Dispatchers.IO) {
            expenseRepo.insertExpense(expense)
        }
    }

}