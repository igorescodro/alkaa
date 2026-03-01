package com.escodro.designsystem.theme

import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// Seed & error references
// Primary seed lifted from the redesign's --blue-primary / --blue-accent pair.
// Error anchored to the redesign's --red token, adjusted for M3 tonal pairing.
// ─────────────────────────────────────────────────────────────────────────────
val seed = Color(0xFF1A6FD4)
val error = Color(0xFFD93025)

// ─────────────────────────────────────────────────────────────────────────────
// LIGHT THEME
// Background : #F0F4FA  — soft blue-grey page wash
// Surface    : #FFFFFF  — pure white cards
// Primary    : #1A6FD4  — deep action blue (--blue-primary light)
// Secondary  : #4A6587  — mid-tone slate (--text-secondary light)
// Tertiary   : #7C52D4  — purple accent (--purple light)
// ─────────────────────────────────────────────────────────────────────────────

// Primary
val md_theme_light_primary = Color(0xFF1A6FD4)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFD6E8FF)
val md_theme_light_onPrimaryContainer = Color(0xFF002750)

// Secondary  (slate-blue — list subtitles, secondary actions)
val md_theme_light_secondary = Color(0xFF4A6587)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFD8E6F5)
val md_theme_light_onSecondaryContainer = Color(0xFF0D1F33)

// Tertiary  (purple — starred tasks, "All Tasks" smart card, assignee avatar)
val md_theme_light_tertiary = Color(0xFF7C52D4)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFEADDFF)
val md_theme_light_onTertiaryContainer = Color(0xFF250060)

// Error  (--red token)
val md_theme_light_error = Color(0xFFD93025)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)

// Background / Surface
val md_theme_light_background = Color(0xFFF0F4FA)
val md_theme_light_onBackground = Color(0xFF0E1F35)
val md_theme_light_surface = Color(0xFFFFFFFF)
val md_theme_light_onSurface = Color(0xFF0E1F35)

// Surface variant  (--bg-input: #EEF2F9 — used for chips, input fills)
val md_theme_light_surfaceVariant = Color(0xFFEEF2F9)
val md_theme_light_onSurfaceVariant = Color(0xFF4A6587)

// Outline  (--border subtle: rgba blue ~15% on white ≈ #C8D8ED)
val md_theme_light_outline = Color(0xFFC8D8ED)

// Inverse  (dark surface for snackbars / tooltips on light theme)
val md_theme_light_inverseOnSurface = Color(0xFFF0F4FA)
val md_theme_light_inverseSurface = Color(0xFF1A2D45)

// ─────────────────────────────────────────────────────────────────────────────
// DARK THEME
// Background : #0F1B2D  — deep navy page (--bg dark)
// Surface    : #162236  — slightly lighter panel (--bg-panel dark)
// Primary    : #5BADFF  — lightened blue for dark contrast (--blue-light dark)
// Secondary  : #8AA5C8  — muted steel blue (--text-secondary dark)
// Tertiary   : #C4AAFF  — lightened purple for dark contrast
// ─────────────────────────────────────────────────────────────────────────────

// Primary
val md_theme_dark_primary = Color(0xFF5BADFF)
val md_theme_dark_onPrimary = Color(0xFF002F65)
val md_theme_dark_primaryContainer = Color(0xFF0D52A8)
val md_theme_dark_onPrimaryContainer = Color(0xFFD6E8FF)

// Secondary
val md_theme_dark_secondary = Color(0xFF8AA5C8)
val md_theme_dark_onSecondary = Color(0xFF0D1F33)
val md_theme_dark_secondaryContainer = Color(0xFF1E3451)
val md_theme_dark_onSecondaryContainer = Color(0xFFD8E6F5)

// Tertiary
val md_theme_dark_tertiary = Color(0xFFC4AAFF)
val md_theme_dark_onTertiary = Color(0xFF3B0090)
val md_theme_dark_tertiaryContainer = Color(0xFF5A2FA0)
val md_theme_dark_onTertiaryContainer = Color(0xFFEADDFF)

// Error
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF8C1D18)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)

// Background / Surface
val md_theme_dark_background = Color(0xFF0F1B2D)
val md_theme_dark_onBackground = Color(0xFFE8F1FF)
val md_theme_dark_surface = Color(0xFF162236)
val md_theme_dark_onSurface = Color(0xFFE8F1FF)

// Surface variant  (--bg-card: #1A2D45 — elevated cards, list rows)
val md_theme_dark_surfaceVariant = Color(0xFF1A2D45)
val md_theme_dark_onSurfaceVariant = Color(0xFF8AA5C8)

// Outline  (--border: rgba blue-primary ~15% on dark ≈ #2B4A6E)
val md_theme_dark_outline = Color(0xFF2B4A6E)

// Inverse  (light surface for snackbars / tooltips on dark theme)
val md_theme_dark_inverseOnSurface = Color(0xFF0F1B2D)
val md_theme_dark_inverseSurface = Color(0xFFE8F1FF)
