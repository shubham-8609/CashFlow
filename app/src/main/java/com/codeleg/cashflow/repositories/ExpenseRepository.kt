package com.codeleg.cashflow.repositories

import androidx.lifecycle.LiveData
import com.codeleg.cashflow.database.ExpenseDao
import com.codeleg.cashflow.model.Expense
import kotlin.math.exp

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allExpense = expenseDao.getAllExpenses()
    suspend fun insertDao(expense: Expense) = expenseDao.insertExpense(expense)
    suspend fun updateExpense(expense: Expense) = expenseDao.updateExpense(expense)
    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)
    suspend fun getExpenseByCategory(categoryId: Int): LiveData<List<Expense>> = expenseDao.getExpenseByCategory(categoryId)
    suspend fun getTotalExpense(): LiveData<Float?> = expenseDao.getTotalExpense()
}