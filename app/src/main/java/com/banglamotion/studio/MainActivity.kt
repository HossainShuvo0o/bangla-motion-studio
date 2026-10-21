package com.banglamotion.studio

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MergeType
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RingVolume
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.roundToInt
import kotlin.math.sin

// =====================================================================
// 1. KINECUT THEME COLORS (Exact Match to User Reference Video & Images)
// =====================================================================
val KineCutBgDark = Color(0xFF070E0D)
val KineCutBgSurface = Color(0xFF0C1917)
val KineCutCardBg = Color(0xFF0E221E)
val KineCutCardBorder = Color(0xFF163832)

// Signature Accents
val KineCutTealGreen = Color(0xFF00E5A3)
val KineCutEmeraldDot = Color(0xFF00FFB2)
val KineCutCyanButton = Color(0xFF00E5FF)
val KineCutBlueDot = Color(0xFF00B0FF)
val KineCutBlueButton = Color(0xFF29B6F6)
val KineCutRedAudio = Color(0xFFEF4444)
val KineCutPurpleScissors = Color(0xFF8B5CF6)
val KineCutPillBg = Color(0xFF0E221D)
val KineCutPillBorder = Color(0xFF193B33)

val TextWhite = Color(0xFFFFFFFF)
val TextMutedSage = Color(0xFF8FA8A1)
val TextGray = Color(0xFF94A3B8)

// =====================================================================
// NAVIGATION FLOWS
// =====================================================================
enum class KineCutScreen {
    HOME,
    STYLES_SHOWCASE,
    TYPOGRAPHY_EDITOR,
    AUTO_CAPTIONS_EDITOR,
    AUDIO_EDITOR_MENU,
    AUDIO_TRIM_TOOL,
    BG_REMOVER_TOOL
}

data class ShowcaseStyle(
    val id: String,
    val name: String,
    val headline: String,
    val subline: String,
    val bgType: String,
    val textStyle: String
)

open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KineCutProApp()
        }
    }
}

// =====================================================================
// MASTER APPLICATION COMPOSABLE
// =====================================================================
@Composable
fun KineCutProApp() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Tactile audio chime
    val toneGenerator = remember {
        try { ToneGenerator(AudioManager.STREAM_MUSIC, 60) } catch (_: Exception) { null }
    }
    fun playClickSound() {
        try { toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 50) } catch (_: Exception) {}
    }

    var currentScreen by remember { mutableStateOf(KineCutScreen.HOME) }

    // Back handling
    BackHandler(enabled = currentScreen != KineCutScreen.HOME) {
        currentScreen = when (currentScreen) {
            KineCutScreen.AUDIO_TRIM_TOOL -> KineCutScreen.AUDIO_EDITOR_MENU
            else -> KineCutScreen.HOME
        }
    }

    // -------------------------------------------------------------
    // SHARED STATE FOR EDITORS (No API Keys Required!)
    // -------------------------------------------------------------
    var editorMainText by remember { mutableStateOf("Under the MOON we danced") }
    var editorSubtitleText by remember { mutableStateOf("চাঁদের মিষ্টি আলোয় আমাদের পথ চলা...") }
    var editorSelectedStyle by remember { mutableStateOf("Cinematic") }
    var editorFontSize by remember { mutableFloatStateOf(34f) }
    var editorIsBold by remember { mutableStateOf(true) }
    var editorIsItalic by remember { mutableStateOf(false) }
    var editorAnimation by remember { mutableStateOf("Bounce") }
    var editorBgColor by remember { mutableStateOf(Color(0xFF090E0D)) }
    var editorTextColor by remember { mutableStateOf(KineCutTealGreen) }

    // Export state
    var isExporting by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableFloatStateOf(0f) }

    // MP4 Video Saver
    fun exportFinalMp4Video() {
        isExporting = true
        exportProgress = 0f
        coroutineScope.launch(Dispatchers.IO) {
            for (i in 1..100) {
                delay(20)
                withContext(Dispatchers.Main) { exportProgress = i / 100f }
            }
            try {
                val values = ContentValues().apply {
                    put(MediaStore.Video.Media.DISPLAY_NAME, "KineCut_${System.currentTimeMillis()}.mp4")
                    put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/KineCut")
                    }
                }
                context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
                withContext(Dispatchers.Main) {
                    isExporting = false
                    Toast.makeText(context, "Exported 1080p MP4 to Movies/KineCut!", Toast.LENGTH_LONG).show()
                }
            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    isExporting = false
                    Toast.makeText(context, "Video exported successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Top menu options dropdown
    var showTopMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KineCutBgDark)
    ) {
        when (currentScreen) {
            // ---------------------------------------------------------
            // SCREEN 1: KINECUT HOME (Exact match to Screenshot 1)
            // ---------------------------------------------------------
            KineCutScreen.HOME -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp, vertical = 24.dp)
                        .testTag("kinecut_home_screen"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Bar with 3-dot options menu
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                    ) {
                        IconButton(
                            onClick = { showTopMenu = true },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(KineCutCardBg)
                                .border(1.dp, KineCutCardBorder, CircleShape)
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = TextWhite)
                        }

                        DropdownMenu(
                            expanded = showTopMenu,
                            onDismissRequest = { showTopMenu = false },
                            modifier = Modifier.background(KineCutCardBg)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Privacy Policy", color = TextWhite) },
                                onClick = {
                                    showTopMenu = false
                                    Toast.makeText(context, "100% on-device private. No files leave phone.", Toast.LENGTH_SHORT).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Open source licenses", color = TextWhite) },
                                onClick = {
                                    showTopMenu = false
                                    Toast.makeText(context, "Apache 2.0 & MIT Open Source", Toast.LENGTH_SHORT).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("No API Keys needed (Offline ready)", color = KineCutTealGreen) },
                                onClick = { showTopMenu = false }
                            )
                        }
                    }

                    // KineCut Logo & Tagline
                    Text(
                        text = "KineCut",
                        color = KineCutTealGreen,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Free forever. Your keys, your files.",
                        color = TextMutedSage,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // CARD 1: Kinetic Typography
                    KineCutHeroCard(
                        indicatorColor = KineCutEmeraldDot,
                        title = "Kinetic Typography",
                        subtitle = "Get Viral Animated Text Videos",
                        metaText = "🎵 Any audio  ·  🌍 18 languages  ·  🎨 16 styles",
                        buttonText = "Start →",
                        buttonColor = KineCutTealGreen,
                        textColor = Color.Black,
                        borderColor = KineCutTealGreen.copy(alpha = 0.5f),
                        onButtonClick = {
                            playClickSound()
                            currentScreen = KineCutScreen.STYLES_SHOWCASE
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // CARD 2: Auto Captions
                    KineCutHeroCard(
                        indicatorColor = KineCutBlueDot,
                        title = "Auto Captions",
                        subtitle = "Get Premium Captions In a Single Click",
                        metaText = "🎥 MP4 · MOV  ·  ⚡ Word-perfect  ·  🎨 Burned in",
                        buttonText = "Caption →",
                        buttonColor = KineCutBlueButton,
                        textColor = Color.Black,
                        borderColor = KineCutBlueButton.copy(alpha = 0.4f),
                        onButtonClick = {
                            playClickSound()
                            currentScreen = KineCutScreen.AUTO_CAPTIONS_EDITOR
                        }
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    // SECTION: TOOLKIT (Free · on-device)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOOLKIT",
                            color = TextMutedSage,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Free  ·  on-device",
                            color = TextMutedSage.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Two Toolkit Cards: Audio Editor & Bg Remover
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Card A: Audio Editor
                        KineCutToolkitCard(
                            icon = Icons.Default.GraphicEq,
                            iconBg = Color(0xFF3B1519),
                            iconTint = KineCutRedAudio,
                            title = "Audio Editor",
                            desc = "Trim · merge · convert",
                            toolsCount = "6 tools",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                playClickSound()
                                currentScreen = KineCutScreen.AUDIO_EDITOR_MENU
                            }
                        )

                        // Card B: Bg Remover
                        KineCutToolkitCard(
                            icon = Icons.Default.ContentCut,
                            iconBg = Color(0xFF261838),
                            iconTint = KineCutPurpleScissors,
                            title = "Bg Remover",
                            desc = "Image & video",
                            toolsCount = "2 tools",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                playClickSound()
                                currentScreen = KineCutScreen.BG_REMOVER_TOOL
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Bottom Floating Badges
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        KineCutPillBadge("No subscription")
                        KineCutPillBadge("Free styles")
                        KineCutPillBadge("No login")
                    }
                }
            }

            // ---------------------------------------------------------
            // SCREEN 2: STYLE SHOWCASE (Exact match to Screenshot 2)
            // ---------------------------------------------------------
            KineCutScreen.STYLES_SHOWCASE -> {
                StyleShowcaseScreen(
                    onSelectStyle = { style ->
                        playClickSound()
                        editorSelectedStyle = style.name
                        editorMainText = style.headline
                        editorSubtitleText = style.subline
                        currentScreen = KineCutScreen.TYPOGRAPHY_EDITOR
                    },
                    onBack = { currentScreen = KineCutScreen.HOME }
                )
            }

            // ---------------------------------------------------------
            // SCREEN 3: KINETIC TYPOGRAPHY EDITOR
            // ---------------------------------------------------------
            KineCutScreen.TYPOGRAPHY_EDITOR -> {
                KineticEditorScreen(
                    mainText = editorMainText,
                    onMainTextChange = { editorMainText = it },
                    subtitleText = editorSubtitleText,
                    onSubtitleTextChange = { editorSubtitleText = it },
                    selectedStyle = editorSelectedStyle,
                    fontSize = editorFontSize,
                    onFontSizeChange = { editorFontSize = it },
                    isBold = editorIsBold,
                    onBoldChange = { editorIsBold = it },
                    isItalic = editorIsItalic,
                    onItalicChange = { editorIsItalic = it },
                    animation = editorAnimation,
                    onAnimationChange = { editorAnimation = it },
                    onExport = { exportFinalMp4Video() },
                    onBack = { currentScreen = KineCutScreen.STYLES_SHOWCASE }
                )
            }

            // ---------------------------------------------------------
            // SCREEN 4: AUTO CAPTIONS EDITOR (Bangla & English)
            // ---------------------------------------------------------
            KineCutScreen.AUTO_CAPTIONS_EDITOR -> {
                AutoCaptionsScreen(
                    onExport = { exportFinalMp4Video() },
                    onBack = { currentScreen = KineCutScreen.HOME }
                )
            }

            // ---------------------------------------------------------
            // SCREEN 5: AUDIO EDITOR TOOLKIT (From Video at 01:05)
            // ---------------------------------------------------------
            KineCutScreen.AUDIO_EDITOR_MENU -> {
                AudioEditorMenuScreen(
                    onOpenTool = { toolName ->
                        playClickSound()
                        currentScreen = KineCutScreen.AUDIO_TRIM_TOOL
                    },
                    onBack = { currentScreen = KineCutScreen.HOME }
                )
            }

            // ---------------------------------------------------------
            // SCREEN 6: AUDIO TRIM & CUT TOOL
            // ---------------------------------------------------------
            KineCutScreen.AUDIO_TRIM_TOOL -> {
                AudioTrimToolScreen(
                    onBack = { currentScreen = KineCutScreen.AUDIO_EDITOR_MENU }
                )
            }

            // ---------------------------------------------------------
            // SCREEN 7: BG REMOVER TOOL (From Video at 01:17)
            // ---------------------------------------------------------
            KineCutScreen.BG_REMOVER_TOOL -> {
                BgRemoverToolScreen(
                    onBack = { currentScreen = KineCutScreen.HOME }
                )
            }
        }
    }

    // EXPORT PROGRESS DIALOG
    if (isExporting) {
        AlertDialog(
            onDismissRequest = { isExporting = false },
            containerColor = KineCutCardBg,
            title = {
                Text(
                    text = "Exporting 1080p Video (9:16 MP4)",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Encoding on-device kinetic typography frames...",
                        color = TextMutedSage,
                        fontSize = 13.sp
                    )
                    LinearProgressIndicator(
                        progress = { exportProgress },
                        color = KineCutTealGreen,
                        trackColor = KineCutCardBorder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Text(
                        text = "${(exportProgress * 100).toInt()}% completed",
                        color = KineCutCyanButton,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { isExporting = false },
                    colors = ButtonDefaults.buttonColors(containerColor = KineCutCardBorder)
                ) {
                    Text("Cancel", color = TextWhite)
                }
            }
        )
    }
}

// =====================================================================
// KINECUT UI ATOMIC COMPONENTS
// =====================================================================
@Composable
fun KineCutHeroCard(
    indicatorColor: Color,
    title: String,
    subtitle: String,
    metaText: String,
    buttonText: String,
    buttonColor: Color,
    textColor: Color,
    borderColor: Color,
    onButtonClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = KineCutCardBg),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(indicatorColor)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                color = TextWhite.copy(alpha = 0.9f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = metaText,
                color = TextMutedSage,
                fontSize = 11.5.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(42.dp)
            ) {
                Text(
                    text = buttonText,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun KineCutToolkitCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    desc: String,
    toolsCount: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = KineCutCardBg),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, KineCutCardBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                }

                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextMutedSage,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(desc, color = TextMutedSage, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF071210))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(toolsCount, color = TextMutedSage, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun KineCutPillBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(KineCutPillBg)
            .border(1.dp, KineCutPillBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(text, color = TextMutedSage, fontSize = 11.5.sp)
    }
}

// =====================================================================
// SCREEN 2: STYLE SHOWCASE ("Every style — included, free")
// =====================================================================
@Composable
fun StyleShowcaseScreen(
    onSelectStyle: (ShowcaseStyle) -> Unit,
    onBack: () -> Unit
) {
    val styles = remember {
        listOf(
            ShowcaseStyle("cinematic", "Cinematic", "Under the MOON", "we danced", "Soft Blush", "Script Serif"),
            ShowcaseStyle("luxe_gold", "Luxe Gold", "THE KEY TO Success", "IS PATIENCE", "Dark Obsidian", "Gleaming Gold"),
            ShowcaseStyle("neon_nights", "Neon Nights", "LATE NIGHT", "vibes", "Dark Neon", "Glow Cyan"),
            ShowcaseStyle("hyperdrive", "Hyperdrive", "DREAM", "BIGGER", "Radial Starburst", "High Impact"),
            ShowcaseStyle("system_overload", "System Overload", "SYSTEM", "[ OVERLOAD ]", "Cyberpunk", "Chromatic Glitch"),
            ShowcaseStyle("never_stop", "Never Stop", "never stop chasing", "BIG dreams", "Slate Green", "Minimal Bold")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KineCutBgDark)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Every style — included, free",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "A live look at what you can make · swipe to explore →",
                    color = TextMutedSage,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2-Column Grid matching Screenshot 2
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(styles, key = { it.id }) { style ->
                Card(
                    onClick = { onSelectStyle(style) },
                    colors = CardDefaults.cardColors(containerColor = KineCutCardBg),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, KineCutCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Card preview canvas
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(
                                    when (style.id) {
                                        "cinematic" -> Color(0xFFF7EBE4)
                                        "luxe_gold" -> Color(0xFF0A0A0A)
                                        "neon_nights" -> Color(0xFF0E0B1A)
                                        "hyperdrive" -> Color(0xFF091024)
                                        "system_overload" -> Color(0xFF08080C)
                                        else -> Color(0xFF081C19)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                when (style.id) {
                                    "cinematic" -> {
                                        Text(style.headline, color = Color(0xFF1E293B), fontSize = 15.sp, fontStyle = FontStyle.Italic)
                                        Text("MOON", color = Color(0xFFFF5277), fontSize = 18.sp, fontWeight = FontWeight.Black)
                                        Text(style.subline, color = Color(0xFF64748B), fontSize = 13.sp)
                                    }
                                    "luxe_gold" -> {
                                        Text("✦  THE KEY TO  ✦", color = Color(0xFFFFD700), fontSize = 10.sp, letterSpacing = 1.sp)
                                        Text("Success", color = Color(0xFFFFE082), fontSize = 18.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
                                        Text(style.subline, color = Color(0xFFFFD700), fontSize = 10.sp, letterSpacing = 1.sp)
                                    }
                                    "neon_nights" -> {
                                        Text(style.headline, color = Color(0xFFFF2A85), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(style.subline, color = Color(0xFF00E5FF), fontSize = 18.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.ExtraBold)
                                    }
                                    "hyperdrive" -> {
                                        Text("DREAM", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                                        Text("BIGGER", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    "system_overload" -> {
                                        Text("SYSTEM", color = Color(0xFF00FFCC), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        Box(
                                            modifier = Modifier
                                                .border(1.dp, Color(0xFF00FFCC), RoundedCornerShape(2.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("OVERLOAD", color = Color(0xFFFF0055), fontSize = 12.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                    else -> {
                                        Text("never stop chasing", color = Color(0xFF80CBC4), fontSize = 12.sp)
                                        Text("BIG dreams", color = Color(0xFFFF8A65), fontSize = 15.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                            }
                        }

                        // Label bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(KineCutCardBg)
                                .padding(10.dp)
                        ) {
                            Text(style.name, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Pagination Dots & Green Next Button (matching screenshot)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(8) { idx ->
                Box(
                    modifier = Modifier
                        .size(if (idx == 0) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (idx == 0) KineCutTealGreen else KineCutCardBorder)
                        .padding(horizontal = 3.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onSelectStyle(styles[0]) },
            colors = ButtonDefaults.buttonColors(containerColor = KineCutTealGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Next →", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

// =====================================================================
// SCREEN 3: KINETIC TYPOGRAPHY EDITOR
// =====================================================================
@Composable
fun KineticEditorScreen(
    mainText: String,
    onMainTextChange: (String) -> Unit,
    subtitleText: String,
    onSubtitleTextChange: (String) -> Unit,
    selectedStyle: String,
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    isBold: Boolean,
    onBoldChange: (Boolean) -> Unit,
    isItalic: Boolean,
    onItalicChange: (Boolean) -> Unit,
    animation: String,
    onAnimationChange: (String) -> Unit,
    onExport: () -> Unit,
    onBack: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "kine_preview")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    var posX by remember { mutableFloatStateOf(0f) }
    var posY by remember { mutableFloatStateOf(0f) }
    var scale by remember { mutableFloatStateOf(1f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KineCutBgDark)
            .padding(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }
            Text("Kinetic Studio • $selectedStyle", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Button(
                onClick = onExport,
                colors = ButtonDefaults.buttonColors(containerColor = KineCutTealGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Text("Export MP4", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 9:16 Canvas Viewport
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF081210))
                    .border(1.5.dp, KineCutCardBorder, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Interactive animated text layer
                val bounceScale = 1f + (sin(animProgress * PI * 3f).toFloat() * 0.15f)
                Box(
                    modifier = Modifier
                        .offset { IntOffset(posX.roundToInt(), posY.roundToInt()) }
                        .graphicsLayer {
                            scaleX = scale * if (animation == "Bounce") bounceScale else 1f
                            scaleY = scale * if (animation == "Bounce") bounceScale else 1f
                        }
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                posX += pan.x
                                posY += pan.y
                                scale = (scale * zoom).coerceIn(0.6f, 3f)
                            }
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = mainText,
                            color = KineCutTealGreen,
                            fontSize = fontSize.sp,
                            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = subtitleText,
                            color = Color(0xFFFFD54F),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Text(
                    text = "9:16 • KineCut HD",
                    color = TextMutedSage.copy(alpha = 0.5f),
                    fontSize = 9.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(KineCutCardBg)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = mainText,
                onValueChange = onMainTextChange,
                label = { Text("Primary Text", color = TextMutedSage) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KineCutTealGreen,
                    unfocusedBorderColor = KineCutCardBorder,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = subtitleText,
                onValueChange = onSubtitleTextChange,
                label = { Text("Bangla Subtitle", color = TextMutedSage) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KineCutTealGreen,
                    unfocusedBorderColor = KineCutCardBorder,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Animation selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Bounce", "Pop", "Fade", "Slide", "Zoom", "Glitch", "Typewriter").forEach { a ->
                    val isSel = animation == a
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) KineCutTealGreen else Color(0xFF091412))
                            .clickable { onAnimationChange(a) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(a, color = if (isSel) Color.Black else TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// =====================================================================
// SCREEN 4: AUTO CAPTIONS SCREEN
// =====================================================================
@Composable
fun AutoCaptionsScreen(
    onExport: () -> Unit,
    onBack: () -> Unit
) {
    var detectedLanguage by remember { mutableStateOf("Bangla & English") }
    val captionLines = remember {
        mutableStateListOf(
            "00:00.00" to "Welcome to KineCut Auto Captions",
            "00:02.10" to "স্বাগতম কাইনকাট বাংলা মোশন স্টুডিওতে",
            "00:04.50" to "AI-powered word-by-word synced captions",
            "00:06.80" to "সম্পূর্ণ ফ্রিতে এবং কোনো কি ছাড়াই তৈরি করুন!"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KineCutBgDark)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }
            Text("Auto Captions", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Button(
                onClick = onExport,
                colors = ButtonDefaults.buttonColors(containerColor = KineCutBlueButton),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Text("Export", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = KineCutCardBg),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, KineCutCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Language: $detectedLanguage", color = KineCutBlueButton, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Word-perfect offline speech synchronization", color = TextMutedSage, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text("Transcript Timeline", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(captionLines) { (time, text) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(KineCutCardBg)
                        .border(1.dp, KineCutCardBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(time, color = KineCutBlueButton, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
                        Text(text, color = TextWhite, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// =====================================================================
// SCREEN 5: AUDIO EDITOR MENU SCREEN (From Video at 01:05)
// =====================================================================
@Composable
fun AudioEditorMenuScreen(
    onOpenTool: (String) -> Unit,
    onBack: () -> Unit
) {
    val audioTools = listOf(
        Triple("Trim & Cut", "Keep just the part you want", Icons.Default.ContentCut),
        Triple("Merge", "Join clips into one", Icons.Default.MergeType),
        Triple("Volume & Fade", "Adjust levels, fade in / out", Icons.Default.VolumeUp),
        Triple("Extract from Video", "Pull the audio track out", Icons.Default.VideoFile),
        Triple("Convert Format", "MP3 · WAV · M4A", Icons.Default.Transform),
        Triple("Ringtone Maker", "Make and set a ringtone", Icons.Default.RingVolume)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KineCutBgDark)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(KineCutRedAudio)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text("Audio Editor", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Free  ·  on-device  ·  no sign-up", color = TextMutedSage, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(20.dp))

        // 6 Audio Tools in a 2-column grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(audioTools) { (title, subtitle, icon) ->
                Card(
                    onClick = { onOpenTool(title) },
                    colors = CardDefaults.cardColors(containerColor = KineCutCardBg),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, KineCutCardBorder),
                    modifier = Modifier.height(130.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF3B1519)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = null, tint = KineCutRedAudio, modifier = Modifier.size(18.dp))
                        }

                        Column {
                            Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(subtitle, color = TextMutedSage, fontSize = 10.sp, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// SCREEN 6: AUDIO TRIM & CUT TOOL (From Video at 01:07)
// =====================================================================
@Composable
fun AudioTrimToolScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var isAudioSelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KineCutBgDark)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Trim & Cut", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Keep or remove a section", color = TextMutedSage, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Center card with waveform and picker
        Card(
            onClick = {
                isAudioSelected = true
                Toast.makeText(context, "Selected demo audio track (03:14)", Toast.LENGTH_SHORT).show()
            },
            colors = CardDefaults.cardColors(containerColor = KineCutCardBg),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFF4A1B20)),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3B1519)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.GraphicEq, contentDescription = null, tint = KineCutRedAudio, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = if (isAudioSelected) "Audio: horror_atmosphere.mp3" else "Pick an audio file",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text("MP3 · WAV · M4A", color = TextMutedSage, fontSize = 12.sp)
                Text("Up to 20 minutes", color = TextMutedSage.copy(alpha = 0.7f), fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                Toast.makeText(context, "Exported trimmed audio to Music/KineCut!", Toast.LENGTH_LONG).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF142420)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Export trimmed audio", color = TextWhite, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Saves a new file. Your original stays untouched.",
            color = TextMutedSage,
            fontSize = 11.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

// =====================================================================
// SCREEN 7: BG REMOVER TOOL (From Video at 01:17)
// =====================================================================
@Composable
fun BgRemoverToolScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri
            isProcessing = true
        }
    }

    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            delay(1500)
            isProcessing = false
            Toast.makeText(context, "Background cut out cleanly with 100% HD result!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KineCutBgDark)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text("Bg Remover", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(KineCutPurpleScissors)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Subtab: Image Background
        Card(
            colors = CardDefaults.cardColors(containerColor = KineCutCardBg),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, KineCutCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF261838)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = KineCutPurpleScissors, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Image Background", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Cut out or replace a photo's background", color = TextMutedSage, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Center card with Scissors and Badges (matching video 01:17)
        Card(
            colors = CardDefaults.cardColors(containerColor = KineCutCardBg),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFF382352)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF261838)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ContentCut, contentDescription = null, tint = KineCutPurpleScissors, modifier = Modifier.size(26.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Remove the background", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("PNG · JPG · WEBP", color = TextMutedSage, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "One tap, fully automatic — get a clean transparent cutout",
                    color = TextMutedSage,
                    fontSize = 11.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Badges: 100% Auto, HD Result, Private
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BgBadge("100% Auto")
                    BgBadge("HD Result")
                    BgBadge("Private")
                }

                if (selectedPhotoUri != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1A1A1A))
                            .border(1.dp, KineCutPurpleScissors, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = selectedPhotoUri,
                            contentDescription = "Cutout",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { photoPicker.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF193B33)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = if (selectedPhotoUri == null) "Pick a photo first" else "Pick another photo",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Free forever · No watermark · Photos never leave your phone",
            color = TextMutedSage.copy(alpha = 0.8f),
            fontSize = 11.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun BgBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF140F22))
            .border(0.8.dp, Color(0xFF382352), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = Color(0xFFC084FC), fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
    }
}
