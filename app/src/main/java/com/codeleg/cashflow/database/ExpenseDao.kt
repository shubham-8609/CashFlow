package com.codeleg.cashflow.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.codeleg.cashflow.model.Expense
import com.codeleg.cashflow.model.ExpenseWithCategory


@Dao
interface ExpenseDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Transaction
    @Query("SELECT * FROM expenses ORDER BY date DESC, id DESC")
     fun getAllExpenseWithCategory(): LiveData<List<ExpenseWithCategory>>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId")
     fun getExpenseByCategory(categoryId: Int): LiveData<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses")
     fun getTotalExpense(): LiveData<Float?>

     @Transaction
     @Query("SELECT * FROM expenses WHERE id = :id")
      fun getExpAndCatById(id: Int): ExpenseWithCategory

    @Query("SELECT SUM(amount) FROM expenses WHERE categoryId = :categoryId")
     fun getTotalByCategory(categoryId: Int): LiveData<Float?>

}