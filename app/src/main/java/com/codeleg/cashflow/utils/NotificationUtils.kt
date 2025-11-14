package com.codeleg.cashflow.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.codeleg.cashflow.R

private const val CHANNEL_ID = "budget_channel"

object NotificationUtils {

    fun showBudgetReachedNotification(context: Context, total: Float?, budget: Float){
        val totalText = total?.let { "₹$it" } ?: "₹0"
        showNotification(context , "Budget Reached ! " , "You've reached your monthly limit of  ₹$budget. Total spent: $totalText.")
    }

    fun showBudgetWarningNotification(context: Context, total: Float?, budget: Float){
        val totalText = total?.let { "₹$it" } ?: "₹0"
        showNotification(context , "You're close to the limit!" , "You've spent $totalText out of ₹$budget.")
    }

    private fun showNotification(context: Context , title:String , msg: String){
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID , "Budget Alerts" , NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        val icon = (ResourcesCompat.getDrawable(context.resources , R.drawable.app_icon , null) as BitmapDrawable).bitmap

        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setContentTitle(title)
            setContentText(msg)
            setAutoCancel(true)
            setLargeIcon(icon)
            setSmallIcon(R.drawable.app_icon)
        }

        val notification = builder.build()
        manager.notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt() , notification)
    }

}