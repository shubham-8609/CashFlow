package com.codeleg.cashflow

import android.app.Application
import com.codeleg.cashflow.database.AppDatabase

class CashFlow: Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
}