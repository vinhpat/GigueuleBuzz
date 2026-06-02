package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// --- BUBBLY & GOOFY CARTOONY PALETTE ---
// Handpicked warm, cheerful cartoon paint colors
val CuteCocoaCharcoal = Color(0xFF2C1A11) // Warm rich chocolate brown for text & outlines
val CuteVanillaCream = Color(0xFFFFFDF4) // Soft, cozy marshmallow background
val CuteCloudWhite = Color(0xFFFFFFFF) // High-contrast clean card surfaces
val CuteSunnyYellow = Color(0xFFFFD633) // Primary yellow: cheerful, sweet, energetic
val CuteMangoOrange = Color(0xFFFF9E1B) // Warm rich orange accents
val CuteBubblegumPink = Color(0xFFFF6694) // Sweet berry pink for secondary nodes
val CuteSkyBlue = Color(0xFF4CC9F0) // Refreshing water bubble blue for connection lines
val CuteLimeSoda = Color(0xFF6EDC14) // Fizzy green for active states/buses
val CuteCherryRed = Color(0xFFFF4757) // Bright goofy red for errors/aborts

// Mapping to Sleek design tokens for compatibility
val PrimarySleek = CuteSunnyYellow
val OnPrimarySleek = CuteCocoaCharcoal
val PrimaryContainerSleek = CuteCloudWhite
val OnPrimaryContainerSleek = CuteCocoaCharcoal

val SecondarySleek = CuteBubblegumPink
val OnSecondarySleek = CuteCloudWhite
val SecondaryContainerSleek = CuteCloudWhite
val OnSecondaryContainerSleek = CuteCocoaCharcoal

val BackgroundSleek = CuteVanillaCream
val SurfaceSleek = CuteCloudWhite

// Compatibility with slate variables used in other files
val Slate100 = CuteVanillaCream
val Slate400 = CuteCocoaCharcoal.copy(alpha = 0.4f)
val Slate500 = CuteCocoaCharcoal.copy(alpha = 0.6f)
val Slate600 = CuteCocoaCharcoal.copy(alpha = 0.8f)
val Slate700 = CuteCocoaCharcoal
val Slate900 = CuteCocoaCharcoal

val ErrorSleek = CuteCherryRed
val OnErrorSleek = CuteCloudWhite
