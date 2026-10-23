# Widget Development Guide — Personal Hub

This guide explains how to add new widgets to the **Personal Hub Android Widget Studio**. The system utilizes a unified configuration engine, a high-fidelity Liquid Glass in-app preview renderer, and native Android `AppWidgetProvider` implementations.

---

## 1. Architectural Architecture

Every widget follows a strict separation of concerns across 4 layers:

```text
┌────────────────────────────────────────────────────────┐
│               1. Catalog & Registry                    │
│   WidgetRegistry.kt: Metadata, Sizes, Permissions,     │
│             Capabilities, and Status                   │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│            2. Per-Instance Configuration               │
│   WidgetConfiguration: Theme, Colors, Opacity, Bounds,  │
│      persisted per instanceId / appWidgetId            │
└─────────────┬────────────────────────────┬─────────────┘
              │                            │
┌─────────────▼──────────────┐ ┌───────────▼─────────────┐
│ 3. In-App Preview Engine   │ │ 4. Home-Screen Provider │
│    WidgetPreviewRenderer   │ │    AppWidgetProvider    │
│  Compose + Liquid Glass UI │ │ RemoteViews + Broadcast │
└────────────────────────────┘ └─────────────────────────┘
```

---

## 2. Step-by-Step Guide to Adding a New Widget

### Step 1: Define Metadata in `WidgetRegistry.kt`

Add your widget definition to `:core:widgetengine/src/main/kotlin/.../WidgetRegistry.kt`:

```kotlin
WidgetDefinition(
    id = "clock_binary_matrix",
    name = "Binary Matrix Clock",
    description = "Displays live hours and minutes encoded in illuminated binary matrix dots.",
    category = WidgetCategory.CLOCK,
    defaultSize = WidgetSizePreset.SQUARE_2X2,
    supportedSizes = listOf(WidgetSizePreset.SQUARE_2X2, WidgetSizePreset.MEDIUM_4X2),
    defaultTheme = WidgetThemePreset.AMOLED_NEON,
    capabilities = setOf(WidgetCapability.REALTIME_CLOCK, WidgetCapability.CUSTOM_ACCENT_COLOR),
    requiredPermission = WidgetPermissionRequirement.NONE,
    status = WidgetStatus.VERIFIED,
    providerClassName = "com.personallife.app.widget.BinaryClockWidgetProvider"
)
```

### Step 2: Implement In-App Preview in `WidgetPreviewRenderer.kt`

In `:core:widgetpreview`, add your Composable inside `WidgetPreviewRenderer`:

```kotlin
when (definition.category) {
    WidgetCategory.CLOCK -> {
        if (definition.id == "clock_binary_matrix") {
            BinaryClockPreview(configuration, accentColor, textColor)
        } else {
            ClockPreviewContent(...)
        }
    }
    // ...
}
```

Use `LiquidGlassCard`, `LiquidGlassChip`, and `LiquidGlassButton` for unified aesthetics.

### Step 3: Implement Home-Screen Layout & Provider

1. **Create the XML layout** in `app/src/main/res/layout/widget_binary_clock.xml` using `widget_glass_bg` as the background.
2. **Create the AppWidgetProvider XML** in `app/src/main/res/xml/widget_binary_clock_info.xml`:
   ```xml
   <appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
       android:minWidth="110dp"
       android:minHeight="110dp"
       android:targetCellWidth="2"
       android:targetCellHeight="2"
       android:updatePeriodMillis="1800000"
       android:initialLayout="@layout/widget_binary_clock"
       android:widgetCategory="home_screen"
       android:description="@string/widget_binary_desc" />
   ```
3. **Create the Kotlin Provider** in `app/src/main/kotlin/.../widget/BinaryClockWidgetProvider.kt`:
   ```kotlin
   class BinaryClockWidgetProvider : AppWidgetProvider() {
       override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
           for (appWidgetId in appWidgetIds) {
               val views = RemoteViews(context.packageName, R.layout.widget_binary_clock)
               // Populate views
               appWidgetManager.updateAppWidget(appWidgetId, views)
           }
       }
   }
   ```
4. **Register the receiver** in `app/src/main/AndroidManifest.xml`.

---

## 3. Per-Instance Customization Handling

When two instances of the same widget are placed on the home screen, each has a distinct `appWidgetId`.
To retrieve or save settings for an instance:

```kotlin
@Inject lateinit var repository: WidgetConfigurationRepository

// Read instance configuration
val config = repository.getConfigurationForAppWidget(appWidgetId)

// Save modified configuration
repository.saveConfiguration(config.copy(glassOpacity = 0.90f))
```

Configurations are stored independently, ensuring changes to one widget never overwrite another.

---

## 4. Battery & Performance Guidelines

1. **Avoid continuous background polling**:
   - Clocks: Use system `TextClock` where possible (self-updating via internal system ticks with 0 CPU overhead).
   - Battery: Read battery state via passive sticky broadcast receiver (`Intent.ACTION_BATTERY_CHANGED`) with 0 wakeups.
   - Music: React to `MediaSession` playback state change callbacks.
2. **Min/Max Resize Configuration**:
   - Always define `minResizeWidth` and `minResizeHeight` to protect the layout from extreme launcher squash.
3. **Launcher Compatibility**:
   - Support both automatic pinning (`AppWidgetManager.requestPinAppWidget`) and manual drag-and-drop launcher addition via `ManualAddDialog`.
