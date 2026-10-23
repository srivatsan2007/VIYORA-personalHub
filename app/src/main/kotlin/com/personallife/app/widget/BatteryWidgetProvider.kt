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

/**
 * AppWidgetProvider for the Battery Status Home Screen Widget with Real-Time Updates.
 */
class BatteryWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_REFRESH_BATTERY = "com.personallife.app.widget.ACTION_REFRESH_BATTERY"

        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, BatteryWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val batteryProvider = BatteryDataProvider(context)
            val snapshot = batteryProvider.getBatterySnapshot()

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_battery)

                val percentText = "${snapshot.percentage}%"
                val stateText = if (snapshot.isCharging) " • Charging" else " • Battery"

                views.setTextViewText(R.id.widget_battery_percent, percentText)
                views.setTextViewText(R.id.widget_battery_state, stateText)
                views.setProgressBar(R.id.widget_battery_progress, 100, snapshot.percentage, false)

                // Tap launches MainActivity and also triggers refresh
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_battery_container, pendingIntent)

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
