package com.codeleg.cashflow

import android.app.Application
import com.codeleg.cashflow.database.AppDatabase
import com.codeleg.cashflow.database.PrefManager

class CashFlow: Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
    override fun onCreate() {
        super.onCreate()
        PrefManager.init(this)
    }
}