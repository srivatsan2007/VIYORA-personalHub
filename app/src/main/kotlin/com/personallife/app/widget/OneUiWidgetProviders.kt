package com.personallife.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.personallife.app.MainActivity
import com.personallife.app.R
import com.personallife.core.widgetdata.provider.BatteryDataProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * AppWidgetProvider for Samsung One UI Squircle Clock Widget.
 */
class OneUiClockWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, OneUiClockWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val battery = BatteryDataProvider(context).getBatterySnapshot()
            val dateFormat = SimpleDateFormat("EEE, d MMMM", Locale.getDefault())
            val dateStr = dateFormat.format(Date())

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_oneui_clock)

                views.setTextViewText(R.id.widget_oneui_clock_date, dateStr)
                views.setTextViewText(R.id.widget_oneui_clock_battery, "${battery.percentage}%")

                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_oneui_clock_container, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        updateAll(context)
    }
}

/**
 * AppWidgetProvider for Samsung One UI Device Battery Card Widget.
 */
class OneUiBatteryWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, OneUiBatteryWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val battery = BatteryDataProvider(context).getBatterySnapshot()

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_oneui_battery)

                views.setTextViewText(R.id.widget_oneui_battery_percent, "${battery.percentage}%")
                views.setProgressBar(R.id.widget_oneui_battery_progress, 100, battery.percentage, false)

                val stateText = if (battery.isCharging) "Charging • Normal" else "Battery • Normal"
                views.setTextViewText(R.id.widget_oneui_battery_state, stateText)

                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_oneui_battery_container, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        updateAll(context)
    }
}
