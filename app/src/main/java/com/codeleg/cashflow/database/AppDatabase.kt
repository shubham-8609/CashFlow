package com.codeleg.cashflow.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codeleg.cashflow.model.Category
import com.codeleg.cashflow.model.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Expense::class, Category::class], version = 1)
@TypeConverters(Convertors::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cashflow_db"
                ).build()

                INSTANCE = instance

                // Populate defaults if needed (run after INSTANCE assigned)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val dao = instance.categoryDao()
                        if (dao.getCategoriesCount() == 0) {
                            val defaultCategories = listOf(
                                Category(name = "Food"),
                                Category(name = "Transport"),
                                Category(name = "Shopping"),
                                Category(name = "Entertainment"),
                                Category(name = "Bills"),
                                Category(name = "Health"),
                                Category(name = "Other")
                            )
                            dao.insertCategories(defaultCategories)
                        }
                    } catch (e: Exception) {
                        Log.d("db" , "Error causing....")
                    }
                }

                instance
            }
        }
    }
}
