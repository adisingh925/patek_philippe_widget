package app.adreal.android.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import java.util.Calendar


class MyAppWidgetProvider : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.S)
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
        appWidgetId: Int,
        remoteViews: RemoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
    ) {
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

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {

        val remoteViews = getRemoteViews(
            context!!, newOptions!!.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH),
            newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        )

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

        appWidgetManager?.updateAppWidget(
            appWidgetId, remoteViews
        )
    }

    /**
     * Returns number of cells needed for given size of the widget.
     *
     * @param size Widget size in dp.
     * @return Size in number of cells.
     */
    private fun getCellsForSize(size: Int): Int {
        var n = 2
        while (70 * n - 30 < size) {
            ++n
        }
        return n - 1
    }

    /**
     * Determine appropriate view based on width provided.
     *
     * @param minWidth
     * @param minHeight
     * @return
     */
    private fun getRemoteViews(
        context: Context, minWidth: Int,
        minHeight: Int
    ): RemoteViews {
        // First find out rows and columns based on width provided.
        val rows = getCellsForSize(minHeight)
        val columns = getCellsForSize(minWidth)

        Log.d("MyAppWidgetProvider", "getRemoteViews: $rows x $columns")

        return if ((rows == 3 && columns >= 3)) {
            RemoteViews(context.packageName, R.layout.widget_layout)
        } else if (rows >= 3 && columns == 3) {
            RemoteViews(context.packageName, R.layout.widget_layout)
        } else if ((rows >= 4 && columns == 4)) {
            RemoteViews(context.packageName, R.layout.large_widget_layout)
        } else if (rows >= 4 && columns > 4) {
            RemoteViews(context.packageName, R.layout.extra_extra_large_widget_layout)
        } else {
            RemoteViews(context.packageName, R.layout.widget_layout)
        }
    }
}