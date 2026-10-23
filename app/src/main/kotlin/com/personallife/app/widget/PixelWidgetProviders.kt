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
 * AppWidgetProvider for the Pixel "At a Glance" Home Screen Widget.
 */
class PixelAtAGlanceWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, PixelAtAGlanceWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val battery = BatteryDataProvider(context).getBatterySnapshot()
            val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
            val dateStr = dateFormat.format(Date())

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_at_a_glance)

                views.setTextViewText(R.id.widget_pixel_glance_date, dateStr)
                val bolt = if (battery.isCharging) "⚡ " else ""
                views.setTextViewText(R.id.widget_pixel_glance_battery, "$bolt${battery.percentage}%")

                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_glance_container, pendingIntent)

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
 * AppWidgetProvider for the Pixel Scalloped Clock Home Screen Widget.
 */
class PixelClockWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, PixelClockWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val battery = BatteryDataProvider(context).getBatterySnapshot()
            val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
            val dateStr = dateFormat.format(Date()).uppercase(Locale.getDefault())

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_clock)

                views.setTextViewText(R.id.widget_pixel_clock_date, dateStr)
                val charging = if (battery.isCharging) "Charging" else "Battery"
                views.setTextViewText(R.id.widget_pixel_clock_battery, "● ${battery.percentage}% $charging")

                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_clock_container, pendingIntent)

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
