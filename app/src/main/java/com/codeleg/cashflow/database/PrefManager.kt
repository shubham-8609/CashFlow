package com.codeleg.cashflow.database

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PrefManager {

    private const val PREFS_NAME = "cashflow_prefs"
    private const val KEY_MONTHLY_BUDGET = "monthly_budget"

    private lateinit var prefs: SharedPreferences

    /**
     * Must be called once from Application class (e.g., in onCreate)
     */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Save user's monthly budget asynchronously.
     * Example: PrefManager.saveMonthlyBudget(15000f)
     */
    suspend fun saveMonthlyBudget(amount: Float) = withContext(Dispatchers.IO) {
        prefs.edit().putFloat(KEY_MONTHLY_BUDGET, amount).apply()
    }

    /**
     * Get user's monthly budget asynchronously.
     * Returns 0f if not set.
     */
    suspend fun getMonthlyBudget(): Float = withContext(Dispatchers.IO) {
        prefs.getFloat(KEY_MONTHLY_BUDGET, 0f)
    }

    /**
     * Check if the user has set a budget.
     */
    suspend fun isBudgetSet(): Boolean = withContext(Dispatchers.IO) {
        prefs.getFloat(KEY_MONTHLY_BUDGET, 0f) > 0f
    }

    /**
     * Clear saved budget (reset).
     */
    suspend fun clearBudget() = withContext(Dispatchers.IO) {
        prefs.edit().remove(KEY_MONTHLY_BUDGET).apply()
    }
}
