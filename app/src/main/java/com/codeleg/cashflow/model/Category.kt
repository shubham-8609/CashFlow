package com.codeleg.cashflow.model

import androidx.room.Entity

@Entity(tableName = "category")
data class Category(
    val id:Int = 0,
    val name: String,
)
