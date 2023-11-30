package app.adreal.android.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import java.util.Calendar

class MyAppLargeWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("MyAppWidgetProvider", "onUpdate")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val remoteViews = RemoteViews(context.packageName, R.layout.large_widget_layout)

        // Get the current time
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Calculate angles for each hand
        val weekAngle = (360.0 / 7.0 * (dayOfWeek - 1)).toFloat()
        val yearWeekAngle = (360.0 / 53.0 * weekOfYear).toFloat()

        remoteViews.setFloat(R.id.week, "setRotation", yearWeekAngle)
        remoteViews.setFloat(R.id.day, "setRotation", weekAngle)
        remoteViews.setTextViewText(R.id.date, dayOfMonth.toString())

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }
}