package com.personallife.core.widgetdata.provider

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class BatterySnapshot(
    val percentage: Int,
    val isCharging: Boolean,
    val isPlugged: Boolean,
    val temperatureCelsius: Float,
    val voltageMv: Int,
    val health: String
)

@Singleton
class BatteryDataProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getBatterySnapshot(): BatterySnapshot {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, intentFilter)

        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 80
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: 100
        val percentage = if (scale > 0 && level >= 0) ((level.toFloat() / scale) * 100).toInt() else 80

        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        val plugged = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: 0
        val isPlugged = plugged > 0

        val tempTenths = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 280) ?: 280
        val temperature = tempTenths / 10.0f

        val voltage = batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 4000) ?: 4000

        val healthCode = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD)
        val health = when (healthCode) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            else -> "Normal"
        }

        return BatterySnapshot(
            percentage = percentage.coerceIn(0, 100),
            isCharging = isCharging,
            isPlugged = isPlugged,
            temperatureCelsius = temperature,
            voltageMv = voltage,
            health = health
        )
    }
}
