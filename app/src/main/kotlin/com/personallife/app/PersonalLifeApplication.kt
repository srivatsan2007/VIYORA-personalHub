package com.personallife.app

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.personallife.app.widget.BatteryWidgetProvider
import com.personallife.app.widget.DottedBatteryWidgetProvider
import com.personallife.app.widget.MiniCalendarWidgetProvider
import com.personallife.app.widget.NothingBatteryWidgetProvider
import com.personallife.app.widget.NothingClockWidgetProvider
import com.personallife.app.widget.OneUiBatteryWidgetProvider
import com.personallife.app.widget.OneUiClockWidgetProvider
import com.personallife.app.widget.PixelAtAGlanceWidgetProvider
import com.personallife.app.widget.PixelClockWidgetProvider
import com.personallife.app.widget.SleepingClockWidgetProvider
import com.personallife.app.widget.StorageCapsuleWidgetProvider
import com.personallife.app.widget.TallOutlineClockWidgetProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PersonalLifeApplication : Application() {

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context != null) {
                // Broadcast real-time system power & state updates to all active home screen widgets
                BatteryWidgetProvider.updateAll(context)
                PixelAtAGlanceWidgetProvider.updateAll(context)
                PixelClockWidgetProvider.updateAll(context)
                com.personallife.app.widget.PixelStackedClockWidgetProvider.updateAll(context)
                com.personallife.app.widget.PixelBatteryWidgetProvider.updateAll(context)
                com.personallife.app.widget.PixelSearchBarWidgetProvider.updateAll(context)
                com.personallife.app.widget.PixelWeatherPillWidgetProvider.updateAll(context)
                NothingClockWidgetProvider.updateAll(context)
                NothingBatteryWidgetProvider.updateAll(context)
                OneUiClockWidgetProvider.updateAll(context)
                OneUiBatteryWidgetProvider.updateAll(context)
                DottedBatteryWidgetProvider.updateAll(context)
                com.personallife.app.widget.SheerDottedPercentWidgetProvider.updateAll(context)
                SleepingClockWidgetProvider.updateAll(context)
                StorageCapsuleWidgetProvider.updateAll(context)
                MiniCalendarWidgetProvider.updateAll(context)
                TallOutlineClockWidgetProvider.updateAll(context)
                com.personallife.app.widget.SheerDateCapsuleWidgetProvider.updateAll(context)
                com.personallife.app.widget.SheerDaySelectorWidgetProvider.updateAll(context)
                com.personallife.app.widget.SheerFullCalendarWidgetProvider.updateAll(context)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Register dynamic system listener for real-time battery and power state changes
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(batteryReceiver, filter)
    }
}
