package com.codeleg.cashflow.repositories

import androidx.lifecycle.LiveData
import com.codeleg.cashflow.database.ExpenseDao
import com.codeleg.cashflow.model.Expense

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allExpense = expenseDao.getAllExpenseWithCategory()
    val totalExpense = expenseDao.getTotalExpense()
    suspend fun insertExpense(expense: Expense) = expenseDao.insertExpense(expense)
    suspend fun updateExpense(expense: Expense) = expenseDao.updateExpense(expense)
    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)
     fun getExpenseByCategory(categoryId: Int): LiveData<List<Expense>> = expenseDao.getExpenseByCategory(categoryId)
     fun getTotalByCategory(categoryId: Int): LiveData<Float?> = expenseDao.getTotalByCategory(categoryId)
}