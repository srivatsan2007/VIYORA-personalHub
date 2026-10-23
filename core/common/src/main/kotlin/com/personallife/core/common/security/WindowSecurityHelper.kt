package com.personallife.core.common.security

import android.app.Activity
import android.view.WindowManager

/**
 * WindowManager FLAG_SECURE helper to prevent screen capture, screen recording,
 * and hide previews in recent applications list.
 */
object WindowSecurityHelper {

    fun applyFlagSecure(activity: Activity, enable: Boolean) {
        if (enable) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
