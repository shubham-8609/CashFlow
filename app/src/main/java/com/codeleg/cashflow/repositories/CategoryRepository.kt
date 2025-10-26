package com.codeleg.cashflow.repositories

import androidx.lifecycle.LiveData
import com.codeleg.cashflow.database.CategoryDao
import com.codeleg.cashflow.model.Category

class CategoryRepository(private val categoryDao: CategoryDao) {
    val allCategory: LiveData<List<Category>> = categoryDao.getAllCategory()
    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    suspend fun getCategoryById(id:Int) = categoryDao.getCategoryById(id)
}