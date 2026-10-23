package com.personallife.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.personallife.app.MainActivity
import com.personallife.app.R
import com.personallife.core.widgetdata.provider.UsageStatsDataProvider

/**
 * AppWidgetProvider for the Digital Wellbeing / Screen Time Home Screen Widget.
 */
class ScreenTimeWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val usageProvider = UsageStatsDataProvider(context)
        val snapshot = usageProvider.getTodayScreenTimeSnapshot()

        val hours = snapshot.totalScreenTimeMinutes / 60
        val mins = snapshot.totalScreenTimeMinutes % 60
        val timeString = "${hours}h ${mins}m"

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_screentime)

            views.setTextViewText(R.id.widget_screentime_hours, timeString)
            views.setTextViewText(R.id.widget_screentime_limit, " / 4h Goal")
            views.setProgressBar(R.id.widget_screentime_progress, 240, snapshot.totalScreenTimeMinutes, false)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_screentime_container, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
