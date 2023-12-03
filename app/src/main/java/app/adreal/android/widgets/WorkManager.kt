package app.adreal.android.widgets

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.Calendar

class WorkManager(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // Code to update your widget
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val widgetProvider = ComponentName(applicationContext, MyAppWidgetProvider::class.java)
        val widgetIds = appWidgetManager.getAppWidgetIds(widgetProvider)

        // Update your widget here with widgetIds
        for (appWidgetId in widgetIds) {
            try {
                val remoteViews = ClockFunctions.getRemoteViews(
                    applicationContext, (AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH).toInt(),
                    (AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT).toInt()
                )

                ClockFunctions.updateWidgetBasedOnRemoteViews(
                    appWidgetManager, appWidgetId, remoteViews
                )
            } catch (e: Exception) {
                ClockFunctions.updateWidgetBasedOnRemoteViews(
                    appWidgetManager, appWidgetId, RemoteViews(
                        applicationContext.packageName,
                        R.layout.widget_layout
                    )
                )
            }
        }

        return Result.success()
    }
}