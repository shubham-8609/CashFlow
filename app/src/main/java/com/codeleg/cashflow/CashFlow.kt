package com.codeleg.cashflow

import android.app.Application
import com.codeleg.cashflow.database.AppDatabase
import com.codeleg.cashflow.database.PrefManager
import com.codeleg.cashflow.workers.BudgetCheckWorker
import com.google.android.material.color.DynamicColors

class CashFlow: Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
    override fun onCreate() {
        super.onCreate()
        PrefManager.init(this)
        DynamicColors.applyToActivitiesIfAvailable(this)
        BudgetCheckWorker.schedule(this)
    }
}