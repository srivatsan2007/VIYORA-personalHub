package com.personallife.core.widgetengine.registry

import com.personallife.core.widgetengine.model.WidgetCapability
import com.personallife.core.widgetengine.model.WidgetCategory
import com.personallife.core.widgetengine.model.WidgetConfiguration
import com.personallife.core.widgetengine.model.WidgetDefinition
import com.personallife.core.widgetengine.model.WidgetPermissionRequirement
import com.personallife.core.widgetengine.model.WidgetSizePreset
import com.personallife.core.widgetengine.model.WidgetStatus
import com.personallife.core.widgetengine.model.WidgetThemePreset

/**
 * Central Widget Registry: Discovers and provides metadata for every widget design in the library.
 */
object WidgetRegistry {

    val definitions: List<WidgetDefinition> = listOf(
        // ==========================================
        // SHEER & OUTLINE KWGT SIGNATURE WIDGETS
        // ==========================================
        WidgetDefinition(
            id = "sheer_duotone_music",
            name = "Sheer: Duotone Music Player",
            description = "Ed Sheeran Shape of You style duotone silhouette player with progress and controls.",
            category = WidgetCategory.MUSIC,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.SHEER_MIDNIGHT,
            capabilities = setOf(WidgetCapability.MEDIA_PLAYBACK_CONTROL, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.MEDIA_SESSION,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.SheerMusicWidgetProvider"
        ),
        WidgetDefinition(
            id = "sheer_sleeping_clock",
            name = "Sheer: Sleeping Moon Clock",
            description = "Signature deep navy card with bold digital time, date, and cute sleeping face.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.WIDE_4X1),
            defaultTheme = WidgetThemePreset.SHEER_MIDNIGHT,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.SleepingClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "sheer_capsule_clock",
            name = "Sheer: Vertical Capsule Clock",
            description = "Futuristic vertical stadium capsule with stacked dual numbers.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.COMPACT_2X1,
            supportedSizes = listOf(WidgetSizePreset.COMPACT_2X1, WidgetSizePreset.SQUARE_2X2),
            defaultTheme = WidgetThemePreset.SHEER_MIDNIGHT,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.VerticalCapsuleClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "sheer_dotted_battery",
            name = "Sheer: Dotted Matrix Battery",
            description = "Dotted matrix battery card with live discharging percent and cornflower corner accent.",
            category = WidgetCategory.BATTERY,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.SHEER_MIDNIGHT,
            capabilities = setOf(WidgetCapability.BATTERY_MONITORING, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.DottedBatteryWidgetProvider"
        ),
        WidgetDefinition(
            id = "sheer_storage_capsule",
            name = "Sheer: Storage Meter Capsule",
            description = "Stadium capsule showing live internal disk usage (e.g. 128.4 / 256 GB).",
            category = WidgetCategory.SYSTEM_INFO,
            defaultSize = WidgetSizePreset.WIDE_4X1,
            supportedSizes = listOf(WidgetSizePreset.WIDE_4X1, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.SHEER_MIDNIGHT,
            capabilities = setOf(WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.StorageCapsuleWidgetProvider"
        ),
        WidgetDefinition(
            id = "sheer_minimal_calendar",
            name = "Sheer: Minimalist Calendar",
            description = "Compact monthly calendar card with today's highlighted date badge.",
            category = WidgetCategory.CALENDAR,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.SHEER_MIDNIGHT,
            capabilities = setOf(WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.MiniCalendarWidgetProvider"
        ),
        WidgetDefinition(
            id = "sheer_outline_tall_clock",
            name = "Sheer: Tall Outline Clock",
            description = "Signature stretched tall outlined numerals with top-right cornflower date badge.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.SHEER_MIDNIGHT,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.TallOutlineClockWidgetProvider"
        ),

        // ==========================================
        // OS ECOSYSTEMS: GOOGLE PIXEL, NOTHING OS & SAMSUNG ONE UI
        // ==========================================
        WidgetDefinition(
            id = "pixel_at_a_glance",
            name = "Pixel: At a Glance",
            description = "Google Pixel Material You pill with real-time day, date, and live battery stats.",
            category = WidgetCategory.ECOSYSTEM,
            defaultSize = WidgetSizePreset.WIDE_4X1,
            supportedSizes = listOf(WidgetSizePreset.WIDE_4X1, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.PIXEL_MATERIAL_YOU,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.BATTERY_MONITORING, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.PixelAtAGlanceWidgetProvider"
        ),
        WidgetDefinition(
            id = "pixel_scalloped_clock",
            name = "Pixel: Scalloped Clock",
            description = "Iconic 12-lobed petal clock in Material You pastel tones with live digital time and date.",
            category = WidgetCategory.ECOSYSTEM,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.PIXEL_MATERIAL_YOU,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.BATTERY_MONITORING, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.PixelClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "nothing_dot_clock",
            name = "Nothing: Dot-Matrix Clock",
            description = "Nothing OS monochrome digital clock with signature red indicator and live system details.",
            category = WidgetCategory.ECOSYSTEM,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.NOTHING_DOT,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.BATTERY_MONITORING, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.NothingClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "nothing_red_battery",
            name = "Nothing: Red-Dot Battery",
            description = "Minimalist dark circular dial with live battery percentage and charging indicator.",
            category = WidgetCategory.ECOSYSTEM,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.COMPACT_2X1),
            defaultTheme = WidgetThemePreset.NOTHING_DOT,
            capabilities = setOf(WidgetCapability.BATTERY_MONITORING, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.NothingBatteryWidgetProvider"
        ),
        WidgetDefinition(
            id = "oneui_squircle_clock",
            name = "One UI: Squircle Clock",
            description = "Samsung One UI 26dp squircle with large typography, live date, and system pill.",
            category = WidgetCategory.ECOSYSTEM,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.WIDE_4X1),
            defaultTheme = WidgetThemePreset.ONE_UI,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.BATTERY_MONITORING, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.OneUiClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "oneui_device_battery",
            name = "One UI: Device Battery Card",
            description = "Samsung One UI multi-device battery card with live level progress and state indicator.",
            category = WidgetCategory.ECOSYSTEM,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.SQUARE_2X2),
            defaultTheme = WidgetThemePreset.ONE_UI,
            capabilities = setOf(WidgetCapability.BATTERY_MONITORING, WidgetCapability.INTERACTIVE_CLICKS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.OneUiBatteryWidgetProvider"
        ),

        // ==========================================
        // CATEGORY A: CLOCK WIDGETS
        // ==========================================
        WidgetDefinition(
            id = "clock_digital_cyber",
            name = "Cyber Digital Clock",
            description = "High-legibility digital time, full day and date with glassmorphic border.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.WIDE_4X1, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.FROSTED_GLASS,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.TRANSLUCENT_GLASS, WidgetCapability.CUSTOM_ACCENT_COLOR),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.DigitalClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "clock_digital_minimal",
            name = "Minimalist Digital Clock",
            description = "Clean modern typography with subtle time pill and zero visual clutter.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.COMPACT_2X1,
            supportedSizes = listOf(WidgetSizePreset.COMPACT_2X1, WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.OLIVE_AND_CREAM,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.DigitalClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "clock_analog_classic",
            name = "Minimalist Analog Dial",
            description = "Classic round analog clock face with vector hour and minute hands.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.FROSTED_GLASS,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.AnalogClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "clock_hybrid_watch",
            name = "Hybrid Analog-Digital Watch",
            description = "Dual timepiece featuring real-time analog dial and digital cyber readout side-by-side.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.MINT_AND_LAVENDER,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.TRANSLUCENT_GLASS, WidgetCapability.CUSTOM_ACCENT_COLOR),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.HybridClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "clock_flip_vintage",
            name = "Retro Flip Clock",
            description = "Nostalgic mechanical split-flap display cards with 3D drop shadow.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.OLIVE_AND_CREAM,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.DigitalClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "clock_amoled_pure",
            name = "AMOLED Midnight Clock",
            description = "Ultra battery-efficient zero-nit true black background with glowing digits.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.COMPACT_2X1,
            supportedSizes = listOf(WidgetSizePreset.COMPACT_2X1, WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.AMOLED_NEON,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.CUSTOM_ACCENT_COLOR),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.DigitalClockWidgetProvider"
        ),
        WidgetDefinition(
            id = "clock_roman_analog",
            name = "Roman Numeral Watch",
            description = "Elegant analog dial featuring Roman numerals and brushed metallic indices.",
            category = WidgetCategory.CLOCK,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.ROSE_QUARTZ,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.AnalogClockWidgetProvider"
        ),

        // ==========================================
        // CATEGORY B: MUSIC WIDGETS
        // ==========================================
        WidgetDefinition(
            id = "music_compact_controller",
            name = "Compact Music Controller",
            description = "Now playing audio controller with Play/Pause, Next/Previous and track title.",
            category = WidgetCategory.MUSIC,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.WIDE_4X1, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.MINT_AND_LAVENDER,
            capabilities = setOf(WidgetCapability.MEDIA_PLAYBACK_CONTROL, WidgetCapability.INTERACTIVE_CLICKS, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.MEDIA_SESSION,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.MusicWidgetProvider"
        ),
        WidgetDefinition(
            id = "music_vinyl_player",
            name = "Vinyl Spin Player",
            description = "Stylized spinning vinyl record artwork with playback controls and track duration.",
            category = WidgetCategory.MUSIC,
            defaultSize = WidgetSizePreset.LARGE_4X3,
            supportedSizes = listOf(WidgetSizePreset.LARGE_4X3, WidgetSizePreset.EXTRA_LARGE_4X4),
            defaultTheme = WidgetThemePreset.OLIVE_AND_CREAM,
            capabilities = setOf(WidgetCapability.MEDIA_PLAYBACK_CONTROL, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.MEDIA_SESSION,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.MusicWidgetProvider"
        ),
        WidgetDefinition(
            id = "music_waveform_glass",
            name = "Waveform Audio Controller",
            description = "Liquid glass translucent player featuring mini animated audio waveform bars.",
            category = WidgetCategory.MUSIC,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.FROSTED_GLASS,
            capabilities = setOf(WidgetCapability.MEDIA_PLAYBACK_CONTROL, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.MEDIA_SESSION,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.MusicWidgetProvider"
        ),

        // ==========================================
        // CATEGORY C: BATTERY WIDGETS
        // ==========================================
        WidgetDefinition(
            id = "battery_circular_gauge",
            name = "Circular Battery Gauge",
            description = "Radial progress ring with live battery percentage, charging bolt and health status.",
            category = WidgetCategory.BATTERY,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.OLIVE_AND_CREAM,
            capabilities = setOf(WidgetCapability.BATTERY_MONITORING, WidgetCapability.TRANSLUCENT_GLASS, WidgetCapability.CUSTOM_ACCENT_COLOR),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.BatteryWidgetProvider"
        ),
        WidgetDefinition(
            id = "battery_horizontal_bar",
            name = "Linear Battery Bar",
            description = "Sleek horizontal battery meter with charging state badge and temperature read.",
            category = WidgetCategory.BATTERY,
            defaultSize = WidgetSizePreset.WIDE_4X1,
            supportedSizes = listOf(WidgetSizePreset.WIDE_4X1, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.FROSTED_GLASS,
            capabilities = setOf(WidgetCapability.BATTERY_MONITORING, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.BatteryWidgetProvider"
        ),

        // ==========================================
        // CATEGORY D: QUOTES & INSPIRATION WIDGETS
        // ==========================================
        WidgetDefinition(
            id = "quote_daily_inspiration",
            name = "Daily Inspiration Card",
            description = "Curated offline collection of mindful quotes and author attribution in Liquid Glass.",
            category = WidgetCategory.QUOTES,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.MINT_AND_LAVENDER,
            capabilities = setOf(WidgetCapability.TRANSLUCENT_GLASS, WidgetCapability.CUSTOM_ACCENT_COLOR),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.QuotesWidgetProvider"
        ),
        WidgetDefinition(
            id = "quote_study_mindset",
            name = "Study Motivation Card",
            description = "Focus and discipline reminders designed to motivate deep work sessions.",
            category = WidgetCategory.QUOTES,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.OLIVE_AND_CREAM,
            capabilities = setOf(WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.IMPLEMENTED
        ),

        // ==========================================
        // CATEGORY E: WEATHER WIDGETS
        // ==========================================
        WidgetDefinition(
            id = "weather_current_condition",
            name = "Current Weather Pill",
            description = "Live temperature, weather condition icon, city, and feels-like temperature.",
            category = WidgetCategory.WEATHER,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.DEEP_OCEAN,
            capabilities = setOf(WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.LOCATION,
            status = WidgetStatus.IMPLEMENTED
        ),
        WidgetDefinition(
            id = "weather_clock_combo",
            name = "Weather & Time Combo",
            description = "Combines current digital time and local weather forecast in one cohesive card.",
            category = WidgetCategory.WEATHER,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.FROSTED_GLASS,
            capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.LOCATION,
            status = WidgetStatus.IMPLEMENTED
        ),

        // ==========================================
        // CATEGORY F: PHOTO & GALLERY WIDGETS
        // ==========================================
        WidgetDefinition(
            id = "gallery_photo_frame",
            name = "Liquid Glass Photo Frame",
            description = "Showcase your favorite photo inside an organic translucent rounded frame.",
            category = WidgetCategory.GALLERY,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.LARGE_4X3, WidgetSizePreset.EXTRA_LARGE_4X4),
            defaultTheme = WidgetThemePreset.CLEAR_TRANSLUCENT,
            capabilities = setOf(WidgetCapability.PHOTO_DISPLAY, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.PHOTO_PICKER,
            status = WidgetStatus.IMPLEMENTED
        ),
        WidgetDefinition(
            id = "gallery_polaroid_memories",
            name = "Vintage Polaroid Frame",
            description = "Classic Polaroid photo layout with handwritten-styled date caption.",
            category = WidgetCategory.GALLERY,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.OLIVE_AND_CREAM,
            capabilities = setOf(WidgetCapability.PHOTO_DISPLAY),
            requiredPermission = WidgetPermissionRequirement.PHOTO_PICKER,
            status = WidgetStatus.IMPLEMENTED
        ),

        // ==========================================
        // CATEGORY G: APP SHORTCUTS & FOLDERS
        // ==========================================
        WidgetDefinition(
            id = "folder_quick_dock",
            name = "4-App Quick Dock",
            description = "A compact launcher dock holding 4 frequently used app shortcuts.",
            category = WidgetCategory.APP_FOLDERS,
            defaultSize = WidgetSizePreset.COMPACT_2X1,
            supportedSizes = listOf(WidgetSizePreset.COMPACT_2X1, WidgetSizePreset.WIDE_4X1),
            defaultTheme = WidgetThemePreset.FROSTED_GLASS,
            capabilities = setOf(WidgetCapability.INTERACTIVE_CLICKS, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.IMPLEMENTED
        ),

        // ==========================================
        // CATEGORY H: SCREEN TIME & FOCUS
        // ==========================================
        WidgetDefinition(
            id = "screentime_progress_ring",
            name = "Digital Wellbeing Ring",
            description = "Visual screen time progress ring comparing today's usage with your daily limit.",
            category = WidgetCategory.SCREEN_TIME,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.DEEP_OCEAN,
            capabilities = setOf(WidgetCapability.USAGE_STATS_TRACKING, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.USAGE_STATS,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.ScreenTimeWidgetProvider"
        ),
        WidgetDefinition(
            id = "screentime_pomodoro_timer",
            name = "Pomodoro Focus Countdown",
            description = "Active study timer with focus streak badge and quick 25-minute launcher.",
            category = WidgetCategory.SCREEN_TIME,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.SUNSET_AMBER,
            capabilities = setOf(WidgetCapability.INTERACTIVE_CLICKS, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.IMPLEMENTED
        ),

        // ==========================================
        // CATEGORY I: CALENDAR & AGENDA
        // ==========================================
        WidgetDefinition(
            id = "calendar_agenda_view",
            name = "Agenda & Upcoming Schedule",
            description = "Displays your next event, appointment countdown, and current date.",
            category = WidgetCategory.CALENDAR,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.OLIVE_AND_CREAM,
            capabilities = setOf(WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.IMPLEMENTED
        ),

        // ==========================================
        // CATEGORY J: TASKS & PRODUCTIVITY
        // ==========================================
        WidgetDefinition(
            id = "tasks_checklist_interactive",
            name = "Interactive Task Checklist",
            description = "To-do agenda with live interactive check marks and progress ratio.",
            category = WidgetCategory.TASKS,
            defaultSize = WidgetSizePreset.LARGE_4X3,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.OLIVE_AND_CREAM,
            capabilities = setOf(WidgetCapability.INTERACTIVE_CLICKS, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED
        ),

        // ==========================================
        // CATEGORY K: EDUCATION WIDGETS
        // ==========================================
        WidgetDefinition(
            id = "education_study_timetable",
            name = "Study Timetable & SRS Cards",
            description = "Study goal progress, flashcards due for spaced repetition, and exam countdown.",
            category = WidgetCategory.EDUCATION,
            defaultSize = WidgetSizePreset.LARGE_4X3,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.MINT_AND_LAVENDER,
            capabilities = setOf(WidgetCapability.TRANSLUCENT_GLASS, WidgetCapability.CUSTOM_ACCENT_COLOR),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED
        ),

        // ==========================================
        // CATEGORY L: EXPENSE & FINANCE
        // ==========================================
        WidgetDefinition(
            id = "finance_budget_tracker",
            name = "Budget & 7-Day Spend Meter",
            description = "Daily spending gauge with remaining budget and mini 7-day spending breakdown.",
            category = WidgetCategory.FINANCE,
            defaultSize = WidgetSizePreset.LARGE_4X3,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.SUNSET_AMBER,
            capabilities = setOf(WidgetCapability.TRANSLUCENT_GLASS, WidgetCapability.CUSTOM_ACCENT_COLOR),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED
        ),

        // ==========================================
        // CATEGORY M: HEALTH & FITNESS
        // ==========================================
        WidgetDefinition(
            id = "health_water_quickadd",
            name = "Water Intake & Steps Tracker",
            description = "Daily hydration progress with one-tap +250ml quick-log button and step count.",
            category = WidgetCategory.HEALTH,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.ROSE_QUARTZ,
            capabilities = setOf(WidgetCapability.INTERACTIVE_CLICKS, WidgetCapability.QUICK_ACTION_LOGGING, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED
        ),

        // ==========================================
        // CATEGORY N: NOTES & PERSONAL VAULT
        // ==========================================
        WidgetDefinition(
            id = "notes_encrypted_scratchpad",
            name = "Hardware Encrypted Scratchpad",
            description = "Quick thought viewer protected by AES-256 Android Keystore hardware keys.",
            category = WidgetCategory.NOTES,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2, WidgetSizePreset.LARGE_4X3),
            defaultTheme = WidgetThemePreset.MINT_AND_LAVENDER,
            capabilities = setOf(WidgetCapability.ENCRYPTED_DATA, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.VERIFIED,
            providerClassName = "com.personallife.app.widget.QuickNoteWidgetProvider"
        ),

        // ==========================================
        // CATEGORY O: SYSTEM & NETWORK WIDGETS
        // ==========================================
        WidgetDefinition(
            id = "system_storage_device_card",
            name = "Storage & Connectivity Card",
            description = "Free device storage meter, Wi-Fi connectivity state, and device specs.",
            category = WidgetCategory.SYSTEM_INFO,
            defaultSize = WidgetSizePreset.MEDIUM_4X2,
            supportedSizes = listOf(WidgetSizePreset.MEDIUM_4X2),
            defaultTheme = WidgetThemePreset.FROSTED_GLASS,
            capabilities = setOf(WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.IMPLEMENTED
        ),

        // ==========================================
        // CATEGORY P: ADDITIONAL UTILITIES
        // ==========================================
        WidgetDefinition(
            id = "util_decision_helper",
            name = "Daily Decision Helper",
            description = "Random choice generator, coin flip, and daily challenge launcher.",
            category = WidgetCategory.UTILITIES,
            defaultSize = WidgetSizePreset.SQUARE_2X2,
            supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2),
            defaultTheme = WidgetThemePreset.OLIVE_AND_CREAM,
            capabilities = setOf(WidgetCapability.INTERACTIVE_CLICKS, WidgetCapability.TRANSLUCENT_GLASS),
            requiredPermission = WidgetPermissionRequirement.NONE,
            status = WidgetStatus.IMPLEMENTED
        )
    )

    fun getDefinitionsByCategory(category: WidgetCategory): List<WidgetDefinition> =
        definitions.filter { it.category == category }

    fun getDefinitionById(id: String): WidgetDefinition? =
        definitions.firstOrNull { it.id == id }

    fun findById(id: String): WidgetDefinition? = getDefinitionById(id)

    fun createDefaultConfiguration(widgetId: String, instanceId: String = "draft_$widgetId"): WidgetConfiguration {
        val def = getDefinitionById(widgetId) ?: definitions.first()
        return WidgetConfiguration(
            instanceId = instanceId,
            widgetId = def.id,
            sizePreset = def.defaultSize,
            themePresetName = def.defaultTheme.name,
            backgroundColorHex = def.defaultTheme.backgroundColorHex,
            accentColorHex = def.defaultTheme.accentColorHex,
            textColorHex = def.defaultTheme.textColorHex,
            secondaryTextColorHex = def.defaultTheme.secondaryTextColorHex,
            glassOpacity = def.defaultTheme.glassOpacity,
            cornerRadiusDp = def.defaultTheme.cornerRadiusDp,
            borderWidthDp = def.defaultTheme.borderWidthDp,
            customTitle = def.name
        )
    }
}
