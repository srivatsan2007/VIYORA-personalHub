package com.personallife.core.widgetdata.provider

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Process
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

data class ScreenTimeSnapshot(
    val totalScreenTimeMinutes: Int,
    val isPermissionGranted: Boolean,
    val topAppPackage: String? = null
)

@Singleton
class UsageStatsDataProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun hasUsagePermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun getTodayScreenTimeSnapshot(): ScreenTimeSnapshot {
        if (!hasUsagePermission()) {
            return ScreenTimeSnapshot(
                totalScreenTimeMinutes = 145, // Demo fallback minutes
                isPermissionGranted = false
            )
        }

        return try {
            val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            if (usageStatsManager == null) {
                return ScreenTimeSnapshot(145, false)
            }

            val calendar = Calendar.getInstance()
            val endTime = calendar.timeInMillis
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val startTime = calendar.timeInMillis

            val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )

            var totalTimeMs = 0L
            var topApp: String? = null
            var maxAppTime = 0L

            stats?.forEach { usageStat ->
                if (usageStat.totalTimeInForeground > 0) {
                    totalTimeMs += usageStat.totalTimeInForeground
                    if (usageStat.totalTimeInForeground > maxAppTime) {
                        maxAppTime = usageStat.totalTimeInForeground
                        topApp = usageStat.packageName
                    }
                }
            }

            val minutes = (totalTimeMs / (1000 * 60)).toInt()
            ScreenTimeSnapshot(
                totalScreenTimeMinutes = minutes,
                isPermissionGranted = true,
                topAppPackage = topApp
            )
        } catch (_: Exception) {
            ScreenTimeSnapshot(145, false)
        }
    }
}
