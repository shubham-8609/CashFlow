package com.codeleg.cashflow.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codeleg.cashflow.model.Expense


@Dao
interface ExpenseDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
     fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId")
     fun getExpenseByCategory(categoryId: Int): LiveData<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses")
     fun getTotalExpense(): LiveData<Float?>

    @Query("SELECT SUM(amount) FROM expenses WHERE categoryId = :categoryId")
     fun getTotalByCategory(categoryId: Int): LiveData<Float?>

}