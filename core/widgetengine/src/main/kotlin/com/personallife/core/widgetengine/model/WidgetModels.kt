package com.personallife.core.widgetengine.model

/**
 * 16 comprehensive categories of widgets supported by the Widget Studio.
 */
enum class WidgetCategory(val title: String, val subtitle: String, val iconName: String) {
    CLOCK("Clocks", "Digital, Analog, Hybrid & Word Clocks", "schedule"),
    MUSIC("Music", "Media Player & Playback Controllers", "music_note"),
    BATTERY("Battery", "Power Gauges & Charging Status", "battery_charging_full"),
    QUOTES("Inspiration", "Daily Quotes, Affimations & Thoughts", "format_quote"),
    WEATHER("Weather", "Forecasts, Temperature & Radar", "wb_sunny"),
    GALLERY("Photos", "Frames, Slideshows & Memories", "photo_library"),
    APP_FOLDERS("Shortcuts", "App Folders, Quick Launchers & Docks", "folder"),
    SCREEN_TIME("Digital Wellbeing", "Usage Stats, Focus & Pomodoro", "hourglass_top"),
    CALENDAR("Calendar", "Agenda, Schedules & Event Countdowns", "calendar_month"),
    TASKS("Tasks", "To-Do Checklists, Deadlines & Habits", "check_circle"),
    EDUCATION("Education", "Study Timetable, Flashcards & Revision", "school"),
    FINANCE("Finance", "Daily Budgets, Spend Meter & Trends", "account_balance_wallet"),
    HEALTH("Health", "Water Intake, Step Count & Goals", "favorite"),
    NOTES("Vault & Notes", "Encrypted Scratchpad & Documents", "lock"),
    SYSTEM_INFO("System", "Storage, Connectivity & Device Info", "info"),
    UTILITIES("Utilities", "Timers, Scanners & Tools", "build"),
    ECOSYSTEM("OS Ecosystems", "Pixel, Nothing OS & One UI", "devices")
}

/**
 * Real implementation status of each widget design in the library.
 */
enum class WidgetStatus(val label: String, val isInstallable: Boolean) {
    VERIFIED("Verified Functional", true),
    IMPLEMENTED("Home Screen Ready", true),
    PREVIEW_ONLY("Studio Preview", false),
    REQUIRES_PERMISSION("Permission Needed", true),
    REQUIRES_SETUP("Setup Required", true),
    PLANNED("Roadmap Planned", false)
}

/**
 * Supported logical launcher grid dimensions.
 */
enum class WidgetSizePreset(
    val label: String,
    val cellWidth: Int,
    val cellHeight: Int,
    val minWidthDp: Int,
    val minHeightDp: Int
) {
    COMPACT_2X1("2x1 Compact", 2, 1, 110, 50),
    SQUARE_2X2("2x2 Square", 2, 2, 110, 110),
    WIDE_4X1("4x1 Wide", 4, 1, 240, 50),
    MEDIUM_4X2("4x2 Medium", 4, 2, 240, 110),
    LARGE_4X3("4x3 Large", 4, 3, 240, 180),
    EXTRA_LARGE_4X4("4x4 Full", 4, 4, 280, 280)
}

/**
 * Capabilities supported by specific widget designs.
 */
enum class WidgetCapability {
    INTERACTIVE_CLICKS,
    REALTIME_CLOCK,
    MEDIA_PLAYBACK_CONTROL,
    BATTERY_MONITORING,
    USAGE_STATS_TRACKING,
    PHOTO_DISPLAY,
    CUSTOM_ACCENT_COLOR,
    TRANSLUCENT_GLASS,
    ENCRYPTED_DATA,
    QUICK_ACTION_LOGGING
}

/**
 * Android system permissions required by particular widgets.
 */
enum class WidgetPermissionRequirement(val label: String, val permissionName: String?) {
    NONE("No Permission Needed", null),
    USAGE_STATS("Usage Access", "android.permission.PACKAGE_USAGE_STATS"),
    MEDIA_SESSION("Notification / Media Access", "android.permission.MEDIA_CONTENT_CONTROL"),
    PHOTO_PICKER("Photo Picker", null),
    LOCATION("Location Access", "android.permission.ACCESS_FINE_LOCATION"),
    POST_NOTIFICATIONS("Notification Permission", "android.permission.POST_NOTIFICATIONS")
}

/**
 * Liquid Glass and Premium Theme presets inspired by Olive & Cream, Mint & Lavender, and Frosted Glass.
 */
data class WidgetThemePreset(
    val name: String,
    val displayName: String,
    val backgroundColorHex: String,
    val accentColorHex: String,
    val textColorHex: String,
    val secondaryTextColorHex: String,
    val glassOpacity: Float = 0.85f,
    val cornerRadiusDp: Int = 26,
    val borderWidthDp: Float = 1.5f,
    val isDarkTheme: Boolean = true
) {
    companion object {
        val OLIVE_AND_CREAM = WidgetThemePreset(
            name = "OLIVE_CREAM",
            displayName = "Olive & Cream",
            backgroundColorHex = "#232A1C", // Deep organic olive
            accentColorHex = "#A3E635",      // Fresh lime-mint accent
            textColorHex = "#F7F5EE",        // Warm cream
            secondaryTextColorHex = "#A3A398",
            glassOpacity = 0.90f,
            cornerRadiusDp = 28,
            borderWidthDp = 1.5f,
            isDarkTheme = true
        )

        val MINT_AND_LAVENDER = WidgetThemePreset(
            name = "MINT_LAVENDER",
            displayName = "Mint & Lavender",
            backgroundColorHex = "#181829", // Deep violet-slate
            accentColorHex = "#34D399",      // Vibrant mint
            textColorHex = "#EDE9FE",        // Soft lavender white
            secondaryTextColorHex = "#C4B5FD",// Muted lavender
            glassOpacity = 0.88f,
            cornerRadiusDp = 26,
            borderWidthDp = 1.5f,
            isDarkTheme = true
        )

        val FROSTED_GLASS = WidgetThemePreset(
            name = "FROSTED_GLASS",
            displayName = "Frosted Glass",
            backgroundColorHex = "#1E293B", // Translucent slate
            accentColorHex = "#38BDF8",      // Sky cyan
            textColorHex = "#F8FAFC",        // Crisp white
            secondaryTextColorHex = "#94A3B8",
            glassOpacity = 0.82f,
            cornerRadiusDp = 26,
            borderWidthDp = 1.5f,
            isDarkTheme = true
        )

        val CLEAR_TRANSLUCENT = WidgetThemePreset(
            name = "CLEAR_TRANSLUCENT",
            displayName = "Clear Translucent",
            backgroundColorHex = "#0F172A",
            accentColorHex = "#E2E8F0",
            textColorHex = "#FFFFFF",
            secondaryTextColorHex = "#CBD5E1",
            glassOpacity = 0.65f,
            cornerRadiusDp = 24,
            borderWidthDp = 1.0f,
            isDarkTheme = true
        )

        val AMOLED_NEON = WidgetThemePreset(
            name = "AMOLED_NEON",
            displayName = "AMOLED Pure Black",
            backgroundColorHex = "#000000",
            accentColorHex = "#00F5FF",
            textColorHex = "#FFFFFF",
            secondaryTextColorHex = "#71717A",
            glassOpacity = 0.95f,
            cornerRadiusDp = 28,
            borderWidthDp = 1.5f,
            isDarkTheme = true
        )

        val DEEP_OCEAN = WidgetThemePreset(
            name = "DEEP_OCEAN",
            displayName = "Deep Ocean",
            backgroundColorHex = "#0A192F",
            accentColorHex = "#38BDF8",
            textColorHex = "#E0F2FE",
            secondaryTextColorHex = "#7DD3FC",
            glassOpacity = 0.85f,
            cornerRadiusDp = 26,
            borderWidthDp = 1.5f,
            isDarkTheme = true
        )

        val SUNSET_AMBER = WidgetThemePreset(
            name = "SUNSET_AMBER",
            displayName = "Sunset Amber",
            backgroundColorHex = "#1C1917",
            accentColorHex = "#F59E0B",
            textColorHex = "#FEF3C7",
            secondaryTextColorHex = "#D97706",
            glassOpacity = 0.88f,
            cornerRadiusDp = 26,
            borderWidthDp = 1.5f,
            isDarkTheme = true
        )

        val ROSE_QUARTZ = WidgetThemePreset(
            name = "ROSE_QUARTZ",
            displayName = "Rose Quartz",
            backgroundColorHex = "#261520",
            accentColorHex = "#FB7185",
            textColorHex = "#FFF1F2",
            secondaryTextColorHex = "#FDA4AF",
            glassOpacity = 0.85f,
            cornerRadiusDp = 26,
            borderWidthDp = 1.5f,
            isDarkTheme = true
        )

        val PIXEL_MATERIAL_YOU = WidgetThemePreset(
            name = "PIXEL_MATERIAL_YOU",
            displayName = "Google Pixel",
            backgroundColorHex = "#2B2930",
            accentColorHex = "#D0BCFF",
            textColorHex = "#FFFFFF",
            secondaryTextColorHex = "#CAC4D0",
            glassOpacity = 0.90f,
            cornerRadiusDp = 32,
            borderWidthDp = 1.5f,
            isDarkTheme = true
        )

        val NOTHING_DOT = WidgetThemePreset(
            name = "NOTHING_DOT",
            displayName = "Nothing OS",
            backgroundColorHex = "#000000",
            accentColorHex = "#E11D48",
            textColorHex = "#FFFFFF",
            secondaryTextColorHex = "#A1A1AA",
            glassOpacity = 0.98f,
            cornerRadiusDp = 24,
            borderWidthDp = 1.5f,
            isDarkTheme = true
        )

        val ONE_UI = WidgetThemePreset(
            name = "ONE_UI",
            displayName = "Samsung One UI",
            backgroundColorHex = "#1E2433",
            accentColorHex = "#38BDF8",
            textColorHex = "#FFFFFF",
            secondaryTextColorHex = "#94A3B8",
            glassOpacity = 0.90f,
            cornerRadiusDp = 26,
            borderWidthDp = 1.0f,
            isDarkTheme = true
        )

        val allPresets = listOf(
            OLIVE_AND_CREAM,
            MINT_AND_LAVENDER,
            FROSTED_GLASS,
            PIXEL_MATERIAL_YOU,
            NOTHING_DOT,
            ONE_UI,
            CLEAR_TRANSLUCENT,
            AMOLED_NEON,
            DEEP_OCEAN,
            SUNSET_AMBER,
            ROSE_QUARTZ
        )

        fun findByName(name: String): WidgetThemePreset {
            return allPresets.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: OLIVE_AND_CREAM
        }
    }
}

/**
 * Instance-specific customization settings saved for each individual widget (by appWidgetId).
 */
data class WidgetConfiguration(
    val instanceId: String,
    val widgetId: String,
    val sizePreset: WidgetSizePreset = WidgetSizePreset.MEDIUM_4X2,
    val themePresetName: String = WidgetThemePreset.OLIVE_AND_CREAM.name,
    val backgroundColorHex: String = WidgetThemePreset.OLIVE_AND_CREAM.backgroundColorHex,
    val accentColorHex: String = WidgetThemePreset.OLIVE_AND_CREAM.accentColorHex,
    val textColorHex: String = WidgetThemePreset.OLIVE_AND_CREAM.textColorHex,
    val secondaryTextColorHex: String = WidgetThemePreset.OLIVE_AND_CREAM.secondaryTextColorHex,
    val glassOpacity: Float = 0.85f,
    val cornerRadiusDp: Int = 26,
    val borderWidthDp: Float = 1.5f,
    val showSeconds: Boolean = false,
    val is24Hour: Boolean = false,
    val dateFormat: String = "EEEE, MMMM d",
    val customTitle: String = "",
    val customQuoteText: String = "Design your life with calm intention.",
    val customQuoteAuthor: String = "Personal Hub",
    val imageUri: String? = null,
    val waterTargetMl: Int = 2500,
    val dailyBudget: Double = 100.0,
    val screenTimeLimitMinutes: Int = 240,
    val isFavorite: Boolean = false,
    val lastModified: Long = System.currentTimeMillis()
) {
    fun getThemePreset(): WidgetThemePreset = WidgetThemePreset.findByName(themePresetName)
}

/**
 * Complete metadata blueprint for a widget design in the library.
 */
data class WidgetDefinition(
    val id: String,
    val name: String,
    val description: String,
    val category: WidgetCategory,
    val defaultSize: WidgetSizePreset,
    val supportedSizes: List<WidgetSizePreset> = listOf(defaultSize),
    val defaultTheme: WidgetThemePreset = WidgetThemePreset.OLIVE_AND_CREAM,
    val capabilities: Set<WidgetCapability> = emptySet(),
    val requiredPermission: WidgetPermissionRequirement = WidgetPermissionRequirement.NONE,
    val status: WidgetStatus = WidgetStatus.IMPLEMENTED,
    val providerClassName: String? = null,
    val isFavorite: Boolean = false
)
