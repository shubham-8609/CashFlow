package com.codeleg.cashflow.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.codeleg.cashflow.CashFlow
import com.codeleg.cashflow.database.PrefManager
import com.codeleg.cashflow.utils.NotificationUtils
import java.util.concurrent.TimeUnit

class BudgetCheckWorker(context: Context , workerParams: WorkerParameters) : CoroutineWorker(context , workerParams) {
    override suspend fun doWork(): Result {
        try {
            val appContext = applicationContext
            val budget = PrefManager.getMonthlyBudget()
            val db = (appContext as CashFlow).database
            val totalExpense = db.expenseDao().getTotalExpenseSum()

            if(budget > 0 && totalExpense >= budget){
                NotificationUtils.showBudgetReachedNotification(appContext , totalExpense, budget)
            }else if(budget > 0){
                val percentage = (totalExpense / budget) * 100
                if(percentage > 80f) NotificationUtils.showBudgetWarningNotification(appContext , totalExpense , budget)
            }
            return Result.success()
        }catch (e: Exception){
            Log.e("BudgetCheckWorker", "Error checking budget", e)
            return Result.failure()
        }

    }

    companion object{
        private const val WORK_NAME = "BudgetCheckWork"

        fun schedule(context: Context){
            val request = PeriodicWorkRequestBuilder<BudgetCheckWorker>(30 , TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORK_NAME ,
                ExistingPeriodicWorkPolicy.UPDATE,
                request)

            val immediateRequest = OneTimeWorkRequestBuilder<BudgetCheckWorker>().build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                "${WORK_NAME}_IMMEDIATE",
                ExistingWorkPolicy.REPLACE,
                immediateRequest
            )

        }
    }
}