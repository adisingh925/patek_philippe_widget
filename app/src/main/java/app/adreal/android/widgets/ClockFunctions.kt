package app.adreal.android.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.temporal.IsoFields
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Locale

object ClockFunctions {

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateWidgetBasedOnRemoteViews(
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        remoteViews: RemoteViews
    ) {
        // Get the current time
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        Log.d(
            "MyAppWidgetProvider",
            "updateWidgetBasedOnRemoteViews: ${
                LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            }"
        )

        // Calculate angles for each hand
        val weekAngle = (360.0 / 7.0 * (dayOfWeek - 1)).toFloat()
        val yearWeekAngle = (360.0 / 53.0 * weekOfYear).toFloat()

        remoteViews.setFloat(R.id.week, "setRotation", yearWeekAngle)
        remoteViews.setFloat(R.id.day, "setRotation", weekAngle)
        remoteViews.setTextViewText(R.id.date, dayOfMonth.toString())

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
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
    fun getRemoteViews(
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
        } else if ((rows == 4 && columns >= 4)) {
            RemoteViews(context.packageName, R.layout.large_widget_layout)
        } else if (rows >= 4 && columns == 4) {
            RemoteViews(context.packageName, R.layout.large_widget_layout)
        } else if ((rows == 5 && columns >= 5)) {
            RemoteViews(context.packageName, R.layout.extra_large_widget_layout)
        } else if (rows >= 5 && columns == 5) {
            RemoteViews(context.packageName, R.layout.extra_large_widget_layout)
        } else if ((rows == 6 && columns >= 6)) {
            RemoteViews(context.packageName, R.layout.extra_extra_large_widget_layout)
        } else if (rows >= 6 && columns == 6) {
            RemoteViews(context.packageName, R.layout.extra_extra_large_widget_layout)
        } else {
            RemoteViews(context.packageName, R.layout.widget_layout)
        }
    }
}