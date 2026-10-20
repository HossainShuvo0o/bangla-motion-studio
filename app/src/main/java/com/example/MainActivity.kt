package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

// Dark Theme Colors
val DarkAppBackground = Color(0xFF0A0A0B)
val DarkSurfaceCard = Color(0xFF131418)
val DarkSurfaceCardHigh = Color(0xFF1C1E24)
val DarkBorderColor = Color(0xFF282A34)
val NeonCyan = Color(0xFF00F0FF)
val ElectricViolet = Color(0xFFA855F7)
val GoldenSunset = Color(0xFFFFD700)
val NeonPink = Color(0xFFEC4899)
val CrimsonRed = Color(0xFFEF4444)
val TextLight = Color(0xFFF3F4F6)
val TextMutedGray = Color(0xFF9CA3AF)

enum class AppScreen {
    HOME,
    TEMPLATES,
    EDITOR
}

data class MotionTemplateItem(
    val id: String,
    val name: String,
    val banglaName: String,
    val description: String,
    val text: String,
    val textColor: Color,
    val canvasBg: Color,
    val fontSize: Float,
    val isBold: Boolean,
    val isItalic: Boolean,
    val hasBadge: Boolean,
    val badgeColor: Color,
    val animation: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BanglaMotionStudioApp()
        }
    }
}

@Composable
fun BanglaMotionStudioApp() {
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }

    // Editor Shared State
    var editorText by remember { mutableStateOf("বাংলা মোশন স্টুডিও") }
    var editorFontSize by remember { mutableFloatStateOf(32f) }
    var editorIsBold by remember { mutableStateOf(true) }
    var editorIsItalic by remember { mutableStateOf(false) }
    var editorTextColor by remember { mutableStateOf(NeonCyan) }
    var editorCanvasBg by remember { mutableStateOf(DarkAppBackground) }
    var editorAnimation by remember { mutableStateOf("Bounce") }
    var editorHasBadge by remember { mutableStateOf(false) }
    var editorBadgeColor by remember { mutableStateOf(Color(0xFF1E2433)) }
    var editorTextAlign by remember { mutableStateOf("Center") }

    // Back handler: return to Home if on Editor or Templates
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        currentScreen = AppScreen.HOME
    }

    // 8 Predefined Templates
    val templates = remember {
        listOf(
            MotionTemplateItem(
                id = "cinematic",
                name = "Cinematic",
                banglaName = "সিনেমাটিক গোল্ড",
                description = "Golden dramatic title with slow cinematic zoom",
                text = "আমার ভাইয়ের রক্তে রাঙানো একুশে ফেব্রুয়ারি",
                textColor = GoldenSunset,
                canvasBg = Color(0xFF070709),
                fontSize = 28f,
                isBold = true,
                isItalic = false,
                hasBadge = false,
                badgeColor = Color.Transparent,
                animation = "Zoom"
            ),
            MotionTemplateItem(
                id = "bold",
                name = "Bold",
                banglaName = "বোল্ড ইমপ্যাক্ট",
                description = "High energy typography with dynamic bounce punch",
                text = "KEEP MOVING এগিয়ে চলো",
                textColor = Color(0xFFFB923C),
                canvasBg = Color(0xFF0F121C),
                fontSize = 34f,
                isBold = true,
                isItalic = false,
                hasBadge = false,
                badgeColor = Color.Transparent,
                animation = "Bounce"
            ),
            MotionTemplateItem(
                id = "minimal",
                name = "Minimal",
                banglaName = "মিনিমালিস্ট",
                description = "Soft serene pastel teal with breathing fade",
                text = "নতুন ভোরের মিষ্টি আলো",
                textColor = Color(0xFF5EEAD4),
                canvasBg = Color(0xFF0D1117),
                fontSize = 30f,
                isBold = false,
                isItalic = false,
                hasBadge = false,
                badgeColor = Color.Transparent,
                animation = "Fade"
            ),
            MotionTemplateItem(
                id = "neon",
                name = "Neon",
                banglaName = "সাইবার নিয়ন",
                description = "Electric cyan & violet glow with kinetic glitch",
                text = "CYBER গতি 2088",
                textColor = NeonCyan,
                canvasBg = Color(0xFF050508),
                fontSize = 36f,
                isBold = true,
                isItalic = false,
                hasBadge = false,
                badgeColor = Color.Transparent,
                animation = "Glitch"
            ),
            MotionTemplateItem(
                id = "social",
                name = "Social",
                banglaName = "ভাইরাল ক্যাপশন",
                description = "Yellow text with black badge designed for reels",
                text = "আজকের সেরা মুহূর্ত! মিস করবেন না",
                textColor = Color(0xFFFFEB3B),
                canvasBg = Color(0xFF0A0F1D),
                fontSize = 28f,
                isBold = true,
                isItalic = false,
                hasBadge = true,
                badgeColor = Color(0xFF000000),
                animation = "Bounce"
            ),
            MotionTemplateItem(
                id = "news",
                name = "News",
                banglaName = "ব্রেকিং নিউজ",
                description = "Urgent news headline with crimson banner slide",
                text = "ব্রেকিং: ঐতিহাসিক আন্তর্জাতিক সাফল্য!",
                textColor = Color.White,
                canvasBg = DarkAppBackground,
                fontSize = 26f,
                isBold = true,
                isItalic = false,
                hasBadge = true,
                badgeColor = CrimsonRed,
                animation = "Slide"
            ),
            MotionTemplateItem(
                id = "wedding",
                name = "Wedding",
                banglaName = "শুভ বিবাহ",
                description = "Royal rose gold elegance with gentle pulse",
                text = "শুভ পরিণয় ও প্রীতিভোজ",
                textColor = Color(0xFFFFC0CB),
                canvasBg = Color(0xFF160A13),
                fontSize = 32f,
                isBold = true,
                isItalic = true,
                hasBadge = false,
                badgeColor = Color.Transparent,
                animation = "Zoom"
            ),
            MotionTemplateItem(
                id = "glitch",
                name = "Glitch",
                banglaName = "ডিজিটাল গ্লিচ",
                description = "Cyberpunk chromatic aberration split effect",
                text = "ভবিষ্যতের নতুন রূপরেখা",
                textColor = NeonPink,
                canvasBg = Color(0xFF08060E),
                fontSize = 30f,
                isBold = true,
                isItalic = false,
                hasBadge = false,
                badgeColor = Color.Transparent,
                animation = "Glitch"
            )
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkAppBackground),
        containerColor = DarkAppBackground,
        bottomBar = {
            StudioBottomNav(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkAppBackground)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(
                    onNewProjectClick = {
                        editorText = "বাংলা মোশন স্টুডিও"
                        editorFontSize = 32f
                        editorIsBold = true
                        editorIsItalic = false
                        editorTextColor = NeonCyan
                        editorCanvasBg = DarkAppBackground
                        editorAnimation = "Bounce"
                        editorHasBadge = false
                        currentScreen = AppScreen.EDITOR
                    },
                    onTemplatesClick = {
                        currentScreen = AppScreen.TEMPLATES
                    },
                    onSelectPreset = { preset ->
                        editorText = preset.text
                        editorFontSize = preset.fontSize
                        editorIsBold = preset.isBold
                        editorIsItalic = preset.isItalic
                        editorTextColor = preset.textColor
                        editorCanvasBg = preset.canvasBg
                        editorAnimation = preset.animation
                        editorHasBadge = preset.hasBadge
                        editorBadgeColor = preset.badgeColor
                        currentScreen = AppScreen.EDITOR
                    }
                )

                AppScreen.TEMPLATES -> TemplatesScreen(
                    templates = templates,
                    onApplyTemplate = { selected ->
                        editorText = selected.text
                        editorFontSize = selected.fontSize
                        editorIsBold = selected.isBold
                        editorIsItalic = selected.isItalic
                        editorTextColor = selected.textColor
                        editorCanvasBg = selected.canvasBg
                        editorAnimation = selected.animation
                        editorHasBadge = selected.hasBadge
                        editorBadgeColor = selected.badgeColor
                        currentScreen = AppScreen.EDITOR
                    }
                )

                AppScreen.EDITOR -> EditorScreen(
                    text = editorText,
                    onTextChange = { editorText = it },
                    fontSize = editorFontSize,
                    onFontSizeChange = { editorFontSize = it },
                    isBold = editorIsBold,
                    onBoldChange = { editorIsBold = it },
                    isItalic = editorIsItalic,
                    onItalicChange = { editorIsItalic = it },
                    textColor = editorTextColor,
                    onTextColorChange = { editorTextColor = it },
                    canvasBg = editorCanvasBg,
                    onCanvasBgChange = { editorCanvasBg = it },
                    animation = editorAnimation,
                    onAnimationChange = { editorAnimation = it },
                    hasBadge = editorHasBadge,
                    onHasBadgeChange = { editorHasBadge = it },
                    badgeColor = editorBadgeColor,
                    onBadgeColorChange = { editorBadgeColor = it },
                    textAlign = editorTextAlign,
                    onTextAlignChange = { editorTextAlign = it }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 1. HOME SCREEN
// -------------------------------------------------------------

@Composable
fun HomeScreen(
    onNewProjectClick: () -> Unit,
    onTemplatesClick: () -> Unit,
    onSelectPreset: (MotionTemplateItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
            .testTag("home_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF141724), DarkSurfaceCard)
                    )
                )
                .border(1.dp, DarkBorderColor, RoundedCornerShape(20.dp))
                .padding(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(NeonCyan)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "KINETIC TYPOGRAPHY",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = "Bangla Motion Studio",
                    color = TextLight,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 34.sp
                )

                Text(
                    text = "বাংলা ও ইংরেজি টাইপোগ্রাফি অ্যানিমেশন এবং মোশন ক্যাপশন এডিটর।",
                    color = TextMutedGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // The 2 Main Requested Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Button 1: New Project
                    Button(
                        onClick = onNewProjectClick,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("new_project_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "New Project",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Button 2: Templates
                    Button(
                        onClick = onTemplatesClick,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCardHigh),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("templates_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = ElectricViolet,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Templates",
                            color = TextLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Feature Highlights
        Text(
            text = "স্টুডিও ইঞ্জিন ফিচারস",
            color = TextLight,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FeatureItem(
                title = "Bangla Unicode",
                desc = "যুক্তাক্ষর সুরক্ষিত ইঞ্জিন",
                color = NeonCyan,
                modifier = Modifier.weight(1f)
            )
            FeatureItem(
                title = "9:16 Canvas",
                desc = "রিলস ও শর্টসের জন্য তৈরি",
                color = ElectricViolet,
                modifier = Modifier.weight(1f)
            )
        }

        // Quick Bengali Phrases to Test
        Text(
            text = "জনপ্রিয় বাংলা বাক্য (এক ট্যাপে টেস্ট করুন)",
            color = TextLight,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "বাংলা মোশন স্টুডিও",
                "আমার সোনার বাংলা",
                "অমর একুশে",
                "বিজয় নিশান উড়ছে",
                "স্বপ্নের ঠিকানা",
                "ব্রেকিং নিউজ"
            ).forEach { phrase ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkSurfaceCard)
                        .border(1.dp, DarkBorderColor, RoundedCornerShape(16.dp))
                        .clickable {
                            onSelectPreset(
                                MotionTemplateItem(
                                    id = "quick",
                                    name = "Quick",
                                    banglaName = phrase,
                                    description = "",
                                    text = phrase,
                                    textColor = NeonCyan,
                                    canvasBg = DarkAppBackground,
                                    fontSize = 32f,
                                    isBold = true,
                                    isItalic = false,
                                    hasBadge = false,
                                    badgeColor = Color.Transparent,
                                    animation = "Bounce"
                                )
                            )
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(text = phrase, color = TextLight, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun FeatureItem(
    title: String,
    desc: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceCard)
            .border(1.dp, DarkBorderColor, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(
                text = title,
                color = color,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                color = TextMutedGray,
                fontSize = 11.sp
            )
        }
    }
}

// -------------------------------------------------------------
// 2. EDITOR SCREEN
// -------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditorScreen(
    text: String,
    onTextChange: (String) -> Unit,
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    isBold: Boolean,
    onBoldChange: (Boolean) -> Unit,
    isItalic: Boolean,
    onItalicChange: (Boolean) -> Unit,
    textColor: Color,
    onTextColorChange: (Color) -> Unit,
    canvasBg: Color,
    onCanvasBgChange: (Color) -> Unit,
    animation: String,
    onAnimationChange: (String) -> Unit,
    hasBadge: Boolean,
    onHasBadgeChange: (Boolean) -> Unit,
    badgeColor: Color,
    onBadgeColorChange: (Color) -> Unit,
    textAlign: String,
    onTextAlignChange: (String) -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("editor_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Editor Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "মোশন স্টুডিও এডিটর",
                color = TextLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Play / Pause toggle
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isPlaying) NeonCyan else DarkSurfaceCardHigh)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Toggle Animation Playback",
                    tint = if (isPlaying) Color.Black else TextLight,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 9:16 Canvas Preview
        CanvasPreview916(
            text = text,
            fontSize = fontSize,
            isBold = isBold,
            isItalic = isItalic,
            textColor = textColor,
            canvasBg = canvasBg,
            animation = animation,
            hasBadge = hasBadge,
            badgeColor = badgeColor,
            textAlign = textAlign,
            isPlaying = isPlaying
        )

        // Text Input Field (Bengali & English)
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text("বাংলা বা ইংরেজি টেক্সট লিখুন", color = TextMutedGray) },
            singleLine = false,
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = DarkBorderColor,
                focusedTextColor = TextLight,
                unfocusedTextColor = TextLight,
                cursorColor = NeonCyan
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("editor_text_input")
        )

        // Animation Type Selector Pills
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "অ্যানিমেশন ইফেক্ট: $animation",
                color = TextLight,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Bounce", "Zoom", "Fade", "Slide", "Shake", "Glitch", "Typewriter").forEach { anim ->
                    val isSelected = animation == anim
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else DarkSurfaceCard)
                            .border(
                                1.dp,
                                if (isSelected) NeonCyan else DarkBorderColor,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onAnimationChange(anim) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("anim_btn_$anim")
                    ) {
                        Text(
                            text = anim,
                            color = if (isSelected) NeonCyan else TextLight,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Font Size Slider
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "ফন্ট সাইজ (Font Size)", color = TextLight, fontSize = 13.sp)
                Text(text = "${fontSize.toInt()} sp", color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = fontSize,
                onValueChange = onFontSizeChange,
                valueRange = 16f..56f,
                colors = SliderDefaults.colors(
                    thumbColor = NeonCyan,
                    activeTrackColor = NeonCyan,
                    inactiveTrackColor = DarkBorderColor
                ),
                modifier = Modifier.testTag("font_size_slider")
            )
        }

        // Bold, Italic & Text Alignment Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bold / Italic
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isBold) NeonCyan.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isBold) NeonCyan else DarkBorderColor, RoundedCornerShape(8.dp))
                        .clickable { onBoldChange(!isBold) }
                        .testTag("bold_toggle"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatBold,
                        contentDescription = "Bold",
                        tint = if (isBold) NeonCyan else TextMutedGray
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isItalic) NeonCyan.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isItalic) NeonCyan else DarkBorderColor, RoundedCornerShape(8.dp))
                        .clickable { onItalicChange(!isItalic) }
                        .testTag("italic_toggle"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatItalic,
                        contentDescription = "Italic",
                        tint = if (isItalic) NeonCyan else TextMutedGray
                    )
                }
            }

            // Alignment
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(DarkSurfaceCard)
                    .border(1.dp, DarkBorderColor, RoundedCornerShape(8.dp))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                listOf(
                    Triple("Left", Icons.Default.FormatAlignLeft, "Align Left"),
                    Triple("Center", Icons.Default.FormatAlignCenter, "Align Center"),
                    Triple("Right", Icons.Default.FormatAlignRight, "Align Right")
                ).forEach { (align, icon, desc) ->
                    val isSelected = textAlign == align
                    IconButton(
                        onClick = { onTextAlignChange(align) },
                        modifier = Modifier
                            .size(38.dp)
                            .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = desc,
                            tint = if (isSelected) NeonCyan else TextMutedGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Text Color Palette
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "টেক্সটের রং", color = TextLight, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    NeonCyan,
                    ElectricViolet,
                    GoldenSunset,
                    Color.White,
                    NeonPink,
                    Color(0xFF22C55E),
                    Color(0xFFFB923C),
                    CrimsonRed
                ).forEach { c ->
                    val isSelected = textColor == c
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(c)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color.White else DarkBorderColor,
                                shape = CircleShape
                            )
                            .clickable { onTextColorChange(c) }
                    )
                }
            }
        }

        // Text Badge Highlight Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "টেক্সট হাইলাইট ব্যাজ", color = TextLight, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "সোশ্যাল মিডিয়া রিলস ক্যাপশন স্টাইল", color = TextMutedGray, fontSize = 11.sp)
            }
            Switch(
                checked = hasBadge,
                onCheckedChange = onHasBadgeChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonCyan,
                    checkedTrackColor = NeonCyan.copy(alpha = 0.4f),
                    uncheckedTrackColor = DarkBorderColor
                )
            )
        }

        if (hasBadge) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Color.Black,
                    CrimsonRed,
                    Color(0xFF1E2433),
                    Color(0xFF0F172A),
                    Color(0xFF581C87)
                ).forEach { bc ->
                    val isSelected = badgeColor == bc
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(bc)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) NeonCyan else DarkBorderColor,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { onBadgeColorChange(bc) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

// -------------------------------------------------------------
// 9:16 CANVAS PREVIEW COMPONENT
// -------------------------------------------------------------

@Composable
fun CanvasPreview916(
    text: String,
    fontSize: Float,
    isBold: Boolean,
    isItalic: Boolean,
    textColor: Color,
    canvasBg: Color,
    animation: String,
    hasBadge: Boolean,
    badgeColor: Color,
    textAlign: String,
    isPlaying: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "motion_transition")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "anim_progress"
    )

    val currentProgress = if (isPlaying) animationProgress else 0.5f

    // Transform calculations for the 9:16 canvas
    var scale = 1.0f
    var alpha = 1.0f
    var translationX = 0f
    var translationY = 0f
    var rotation = 0f
    var glitchOffset = 0f

    when (animation) {
        "Bounce" -> {
            val t = (currentProgress / 0.6f).coerceIn(0f, 1f)
            scale = if (t >= 1f) 1f else {
                1f + (sin(t * PI * 3.5).toFloat() * exp(-t * 4.5f) * 0.4f)
            }
        }
        "Zoom" -> {
            val t = (currentProgress / 0.5f).coerceIn(0f, 1f)
            scale = 0.3f + (0.7f * t)
            alpha = (t * 2f).coerceIn(0f, 1f)
        }
        "Fade" -> {
            alpha = if (currentProgress < 0.2f) {
                currentProgress / 0.2f
            } else if (currentProgress > 0.85f) {
                (1f - currentProgress) / 0.15f
            } else 1f
        }
        "Slide" -> {
            val t = (currentProgress / 0.4f).coerceIn(0f, 1f)
            translationY = (1f - t) * 100f
            alpha = (t * 2f).coerceIn(0f, 1f)
        }
        "Shake" -> {
            val cycle = (currentProgress % 0.5f) / 0.5f
            val decay = (1f - cycle).coerceIn(0f, 1f)
            translationX = sin(cycle * PI.toFloat() * 12f) * 14f * decay
            rotation = sin(cycle * PI.toFloat() * 8f) * 4f * decay
        }
        "Glitch" -> {
            val isPulse = (currentProgress * 20).toInt() % 4 == 0
            glitchOffset = if (isPulse) 8f else 1f
            translationX = if (isPulse) sin(currentProgress * 80f) * 10f else 0f
        }
        "Typewriter" -> {
            // Handled via substring below
        }
    }

    val displayText = if (animation == "Typewriter") {
        val count = (text.length * (currentProgress / 0.8f).coerceIn(0f, 1f)).toInt()
        val sub = text.take(count)
        sub + if ((currentProgress * 10).toInt() % 2 == 0) "|" else ""
    } else {
        text
    }

    val composeTextAlign = when (textAlign) {
        "Left" -> TextAlign.Start
        "Right" -> TextAlign.End
        else -> TextAlign.Center
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("canvas_9_16_container"),
        contentAlignment = Alignment.Center
    ) {
        // Strict 9:16 Aspect Ratio Frame
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .aspectRatio(9f / 16f)
                .clip(RoundedCornerShape(16.dp))
                .background(canvasBg)
                .border(1.5.dp, DarkBorderColor, RoundedCornerShape(16.dp))
                .testTag("canvas_9_16_preview"),
            contentAlignment = Alignment.Center
        ) {
            // Animated Text Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                        this.translationX = translationX
                        this.translationY = translationY
                        rotationZ = rotation
                    },
                contentAlignment = Alignment.Center
            ) {
                val badgeModifier = if (hasBadge) {
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                } else {
                    Modifier
                }

                Box(
                    modifier = badgeModifier,
                    contentAlignment = Alignment.Center
                ) {
                    if (animation == "Glitch" && glitchOffset > 0f) {
                        // Cyan glitch shadow
                        Text(
                            text = displayText,
                            fontSize = fontSize.sp,
                            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            textAlign = composeTextAlign,
                            color = NeonCyan.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer { this.translationX = glitchOffset }
                        )
                        // Magenta glitch shadow
                        Text(
                            text = displayText,
                            fontSize = fontSize.sp,
                            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            textAlign = composeTextAlign,
                            color = NeonPink.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer { this.translationX = -glitchOffset }
                        )
                    }

                    // Foreground Primary Text
                    Text(
                        text = displayText,
                        fontSize = fontSize.sp,
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                        fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                        textAlign = composeTextAlign,
                        color = textColor,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color(0xAA000000),
                                offset = Offset(2f, 4f),
                                blurRadius = 8f
                            )
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 9:16 Watermark Indicator
            Text(
                text = "9:16 • Reels Canvas",
                color = TextMutedGray.copy(alpha = 0.4f),
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }
    }
}

// -------------------------------------------------------------
// 3. TEMPLATES SCREEN
// -------------------------------------------------------------

@Composable
fun TemplatesScreen(
    templates: List<MotionTemplateItem>,
    onApplyTemplate: (MotionTemplateItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("templates_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text(
                text = "মোশন টেমপ্লেট সংগ্রহ",
                color = TextLight,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "৮টি প্রি-ডিজাইন করা সিনেমাটিক ও ক্যাপশন স্টাইল",
                color = TextMutedGray,
                fontSize = 13.sp
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(templates, key = { it.id }) { template ->
                Card(
                    onClick = { onApplyTemplate(template) },
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("template_card_${template.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(NeonCyan.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = template.name,
                                        color = NeonCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = template.banglaName,
                                    color = TextLight,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = template.animation,
                                color = ElectricViolet,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Preview box showing text appearance
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(template.canvasBg)
                                .border(1.dp, DarkBorderColor, RoundedCornerShape(10.dp))
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = template.text,
                                color = template.textColor,
                                fontSize = 15.sp,
                                fontWeight = if (template.isBold) FontWeight.Bold else FontWeight.Normal,
                                fontStyle = if (template.isItalic) FontStyle.Italic else FontStyle.Normal,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = template.description,
                                color = TextMutedGray,
                                fontSize = 11.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Button(
                                onClick = { onApplyTemplate(template) },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(36.dp)
                                    .padding(start = 8.dp)
                            ) {
                                Text(
                                    text = "Edit",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. BOTTOM NAVIGATION
// -------------------------------------------------------------

@Composable
fun StudioBottomNav(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    NavigationBar(
        containerColor = DarkSurfaceCard,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkBorderColor, RoundedCornerShape(0.dp))
            .testTag("bottom_nav_bar")
    ) {
        NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { onScreenSelected(AppScreen.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen == AppScreen.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = NeonCyan,
                indicatorColor = NeonCyan,
                unselectedIconColor = TextMutedGray,
                unselectedTextColor = TextMutedGray
            ),
            modifier = Modifier.testTag("nav_item_home")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.TEMPLATES,
            onClick = { onScreenSelected(AppScreen.TEMPLATES) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Dashboard,
                    contentDescription = "Templates"
                )
            },
            label = {
                Text(
                    text = "Templates",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen == AppScreen.TEMPLATES) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = NeonCyan,
                indicatorColor = NeonCyan,
                unselectedIconColor = TextMutedGray,
                unselectedTextColor = TextMutedGray
            ),
            modifier = Modifier.testTag("nav_item_templates")
        )

        NavigationBarItem(
            selected = currentScreen == AppScreen.EDITOR,
            onClick = { onScreenSelected(AppScreen.EDITOR) },
            icon = {
                Icon(
                    imageVector = Icons.Default.MovieCreation,
                    contentDescription = "Editor"
                )
            },
            label = {
                Text(
                    text = "Editor",
                    fontSize = 11.sp,
                    fontWeight = if (currentScreen == AppScreen.EDITOR) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = NeonCyan,
                indicatorColor = NeonCyan,
                unselectedIconColor = TextMutedGray,
                unselectedTextColor = TextMutedGray
            ),
            modifier = Modifier.testTag("nav_item_editor")
        )
    }
}
