package com.codeleg.cashflow.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val amount: Float,
    val date: Date,
    val categoryId: Int,
    val note:String? = null

)
