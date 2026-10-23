package com.personallife.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.AlarmClock
import android.widget.RemoteViews
import com.personallife.app.MainActivity
import com.personallife.app.R
import com.personallife.core.widgetdata.provider.BatteryDataProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 1. Pixel: At a Glance Home Screen Widget.
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
            val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
            val dateStr = dateFormat.format(Date())

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_at_a_glance)
                views.setTextViewText(R.id.widget_pixel_glance_date, dateStr)
                views.setTextViewText(R.id.widget_pixel_glance_temp, "24°C")
                views.setTextViewText(R.id.widget_pixel_glance_event, "10:00 AM • Design Catchup")

                // Date click opens Calendar
                val calIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_CALENDAR)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val safeCalIntent = if (calIntent.resolveActivity(context.packageManager) != null) {
                    calIntent
                } else {
                    Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                }
                val calPending = PendingIntent.getActivity(
                    context,
                    appWidgetId * 10 + 1,
                    safeCalIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_glance_date_click, calPending)

                // Weather click opens Weather Search
                val weatherIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=weather")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val weatherPending = PendingIntent.getActivity(
                    context,
                    appWidgetId * 10 + 2,
                    weatherIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_glance_weather_click, weatherPending)

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
 * 2. Pixel: Material You 4-Petal Flower Scalloped Analog Clock.
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
            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_flower_clock)

                // Click opens Clock / Alarms
                val clockIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val safeClockIntent = if (clockIntent.resolveActivity(context.packageManager) != null) {
                    clockIntent
                } else {
                    Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    safeClockIntent,
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

/**
 * 3. Pixel: Stacked Digital Clock.
 */
class PixelStackedClockWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, PixelStackedClockWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
            val dateStr = dateFormat.format(Date()).uppercase(Locale.getDefault())

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_stacked_clock)
                views.setTextViewText(R.id.widget_pixel_stacked_date, dateStr)

                val clockIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val safeClockIntent = if (clockIntent.resolveActivity(context.packageManager) != null) {
                    clockIntent
                } else {
                    Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    safeClockIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_stacked_container, pendingIntent)

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
 * 4. Pixel: Google Search Bar Pill with Mic & Lens.
 */
class PixelSearchBarWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, PixelSearchBarWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_search_bar)

                // Search Bar click -> Google Web Search
                val searchIntent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val safeSearch = if (searchIntent.resolveActivity(context.packageManager) != null) {
                    searchIntent
                } else {
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                }
                val searchPending = PendingIntent.getActivity(
                    context,
                    appWidgetId * 10 + 1,
                    safeSearch,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_search_container, searchPending)

                // Mic click -> Voice Search
                val micIntent = Intent("android.speech.action.WEB_SEARCH").apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val safeMic = if (micIntent.resolveActivity(context.packageManager) != null) {
                    micIntent
                } else {
                    safeSearch
                }
                val micPending = PendingIntent.getActivity(
                    context,
                    appWidgetId * 10 + 2,
                    safeMic,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_search_mic, micPending)

                // Lens click -> Google Lens or Image Search
                val lensIntent = Intent(Intent.ACTION_VIEW, Uri.parse("googleapp://lens")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    setPackage("com.google.android.googlequicksearchbox")
                }
                val safeLens = if (lensIntent.resolveActivity(context.packageManager) != null) {
                    lensIntent
                } else {
                    safeSearch
                }
                val lensPending = PendingIntent.getActivity(
                    context,
                    appWidgetId * 10 + 3,
                    safeLens,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_search_lens, lensPending)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }
}

/**
 * 5. Pixel: Material You Battery Card.
 */
class PixelBatteryWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, PixelBatteryWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val battery = BatteryDataProvider(context).getBatterySnapshot()
            val bolt = if (battery.isCharging) "⚡ " else ""
            val percentText = "$bolt${battery.percentage}%"

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_battery)
                views.setTextViewText(R.id.widget_pixel_battery_percent, percentText)
                views.setProgressBar(R.id.widget_pixel_battery_progress, 100, battery.percentage, false)

                // Buds secondary accessory (e.g. 85%)
                views.setTextViewText(R.id.widget_pixel_buds_percent, "85%")
                views.setProgressBar(R.id.widget_pixel_buds_progress, 100, 85, false)

                // Click opens Battery Settings
                val settingsIntent = Intent(Intent.ACTION_POWER_USAGE_SUMMARY).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val safeSettings = if (settingsIntent.resolveActivity(context.packageManager) != null) {
                    settingsIntent
                } else {
                    Intent(android.provider.Settings.ACTION_SETTINGS).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    safeSettings,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_battery_container, pendingIntent)

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
 * 6. Pixel: Diagonal Split Weather Pill.
 */
class PixelWeatherPillWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, PixelWeatherPillWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_weather_diagonal)
                views.setTextViewText(R.id.widget_pixel_weather_temp, "24°")
                views.setTextViewText(R.id.widget_pixel_weather_condition, "Mostly Sunny")
                views.setTextViewText(R.id.widget_pixel_weather_hilow, "H: 28° • L: 19°")

                val weatherIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=weather")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    weatherIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_weather_container, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }
}

/**
 * 7. Pixel: Google Keep Quick Capture Pill.
 */
class PixelKeepCaptureWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, PixelKeepCaptureWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_keep_capture)

                val launchKeep = context.packageManager.getLaunchIntentForPackage("com.google.android.keep")
                    ?: Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    launchKeep,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_keep_container, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }
}

/**
 * 8. Pixel: YouTube Music Turntable Vinyl.
 */
class PixelTurntableMusicWidgetProvider : AppWidgetProvider() {

    companion object {
        fun updateAll(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context) ?: return
            val component = ComponentName(context, PixelTurntableMusicWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(component)
            if (ids != null && ids.isNotEmpty()) {
                updateWidgets(context, appWidgetManager, ids)
            }
        }

        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_pixel_turntable_music)

                val musicIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_MUSIC)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val safeMusic = if (musicIntent.resolveActivity(context.packageManager) != null) {
                    musicIntent
                } else {
                    Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    safeMusic,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_pixel_turntable_container, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }
}
