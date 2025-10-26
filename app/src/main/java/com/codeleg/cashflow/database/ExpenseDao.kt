package com.codeleg.cashflow.database

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

    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<Expense>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId")
    suspend fun getExpenseByCategory(categoryId: Int): List<Expense>

    @Query("SELECT SUM(amount) FROM expenses")
    suspend fun getTotalExpense():Float?

    @Query("SELECT SUM(amount) FROM expenses WHERE categoryId = :categoryId")
    suspend fun getTotalByCategory(categoryId: Int):Float?






}