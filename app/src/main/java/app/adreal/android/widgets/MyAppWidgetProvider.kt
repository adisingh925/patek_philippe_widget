package app.adreal.android.widgets

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import java.util.Calendar
import java.util.concurrent.TimeUnit


class MyAppWidgetProvider : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            try {
                Log.d("MyAppWidgetProvider", "onUpdate - try")

                val options = appWidgetManager.getAppWidgetOptions(appWidgetId)

                val remoteViews = ClockFunctions.getRemoteViews(
                    context, options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH),
                    options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
                )

                ClockFunctions.updateWidgetBasedOnRemoteViews(
                    appWidgetManager, appWidgetId, remoteViews
                )
            } catch (e: Exception) {
                Log.d("MyAppWidgetProvider", "onUpdate - catch")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        Log.d("MyAppWidgetProvider", "onAppWidgetOptionsChanged")

        val remoteViews = ClockFunctions.getRemoteViews(
            context!!, newOptions!!.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH),
            newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        )

        ClockFunctions.updateWidgetBasedOnRemoteViews(
            appWidgetManager!!,
            appWidgetId,
            remoteViews
        )
    }

    private fun scheduleWidgetUpdate(context: Context) {
        Log.d("MyAppWidgetProvider", "scheduleWidgetUpdate")

        val dailyWorkRequest = PeriodicWorkRequestBuilder<WorkManager>(
            1,
            TimeUnit.DAYS
        ).build()

        val workManager = androidx.work.WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            "WidgetUpdateWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest
        )
    }

    private fun setAlarm(context: Context) {
        Log.d("MyAppWidgetProvider", "setAlarm")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }
}