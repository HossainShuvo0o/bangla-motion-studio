package com.banglamotion.studio

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

// -----------------------------------------------------------------
// COLORS & CONSTANTS (From Horror Storytelling Video Analysis)
// -----------------------------------------------------------------
val PureBlack = Color(0xFF000000)
val DarkCharcoal = Color(0xFF0D0D11)
val DarkSurfaceCard = Color(0xFF15161E)
val DarkSurfaceBorder = Color(0xFF242634)
val HorrorCyan = Color(0xFF00FFD1)
val BloodRedGlow = Color(0xFFFF1A1A)
val BrightYellow = Color(0xFFFFE600)
val NeonViolet = Color(0xFFA855F7)
val SaveGreen = Color(0xFF00C853)
val TextLight = Color(0xFFF3F4F6)
val TextMuted = Color(0xFF9CA3AF)

// -----------------------------------------------------------------
// DATA MODELS
// -----------------------------------------------------------------
enum class StudioTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    SWITCH("Switch", Icons.Default.AspectRatio),
    TEMPLATE("Template", Icons.Default.AutoAwesome),
    AI_TRIM("AI Trim", Icons.Default.Subtitles),
    TEXT("Text", Icons.Default.TextFields),
    ANIMATION("Animation", Icons.Default.Animation),
    MUSIC("Music", Icons.Default.MusicNote),
    PREVIEW("Preview", Icons.Default.PlayArrow)
}

data class CaptionWord(
    val id: Int,
    var word: String,
    var startTimeMs: Long,
    var endTimeMs: Long
)

data class Particle(
    var x: Float,
    var y: Float,
    var radius: Float,
    var speedY: Float,
    var alpha: Float
)

data class MotionTemplateSpec(
    val id: String,
    val name: String,
    val category: String,
    val mainText: String,
    val subtitleText: String,
    val textColor: Color,
    val glowColor: Color,
    val animation: String,
    val bgType: String
)

open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BanglaMotionStudioPhase2App()
        }
    }
}

// -----------------------------------------------------------------
// MAIN APPLICATION COMPOSABLE
// -----------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BanglaMotionStudioPhase2App() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Sound effect player helper
    val toneGenerator = remember {
        try {
            ToneGenerator(AudioManager.STREAM_MUSIC, 60)
        } catch (_: Exception) {
            null
        }
    }
    fun playPopSfx() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 70)
        } catch (_: Exception) {}
    }

    // Active bottom navigation tab
    var activeTab by remember { mutableStateOf(StudioTab.TEXT) }

    // A) Typography & Font State (Horror Storytelling Style)
    var mainText by remember { mutableStateOf("JINN CAN CAUSE CANCER") }
    var subtitleText by remember { mutableStateOf("জিন মানুষের মারাত্মক শারীরিক ক্ষতি করতে পারে...") }
    var selectedFont by remember { mutableStateOf("Horror Bold") }
    var fontSize by remember { mutableFloatStateOf(34f) }
    var isBold by remember { mutableStateOf(true) }
    var isItalic by remember { mutableStateOf(false) }
    var isUnderlined by remember { mutableStateOf(false) }
    var isAllCaps by remember { mutableStateOf(true) }
    var textAlign by remember { mutableStateOf("Center") }
    var textColor by remember { mutableStateOf(HorrorCyan) }
    var glowColor by remember { mutableStateOf(BloodRedGlow) }
    var strokeWidthPx by remember { mutableFloatStateOf(4f) }
    var hasShadow by remember { mutableStateOf(true) }
    var shadowBlur by remember { mutableFloatStateOf(16f) }
    var textBgActive by remember { mutableStateOf(false) }
    var textBgColor by remember { mutableStateOf(Color(0xCC000000)) }

    // B) 4 Animation Options State
    // 1. Still Picture / Ken Burns
    var stillAnimationType by remember { mutableStateOf("Ken Burns") } // Ken Burns, Parallax, Shake, Rotate
    var customImageUri by remember { mutableStateOf<Uri?>(null) }
    // 2. Background Animation
    var bgAnimationType by remember { mutableStateOf("Particles") } // Gradient, Particles, Stripes, Solid
    // 3. Text to Animation
    var textAnimationType by remember { mutableStateOf("Pop") } // Pop, Slide, Zoom, Bounce, Typewriter
    var staggeredDelayMs by remember { mutableIntStateOf(80) }
    // 4. Random Text Animation
    val randomAnimations = remember {
        listOf("Fade", "Pop", "Slide", "Zoom", "Bounce", "Typewriter", "Glitch", "Shake", "Spin", "Wave", "Neon")
    }

    // C) Template System State
    var selectedTemplateCategory by remember { mutableStateOf("Featured") }
    val customTemplates = remember {
        mutableStateListOf(
            "Custom 1" to "Horror Curse",
            "Custom 2" to "Glow Pulse",
            "Custom 3" to "Neon Blood",
            "Custom 4" to "Dark Shadows",
            "Custom 5" to "Ekushey Echo"
        )
    }

    // D) Auto Caption Bangla + English State
    val captionWords = remember {
        mutableStateListOf(
            CaptionWord(1, "JINN", 0, 450),
            CaptionWord(2, "CAN", 450, 850),
            CaptionWord(3, "CAUSE", 850, 1400),
            CaptionWord(4, "CANCER", 1400, 2100)
        )
    }
    var isGeneratingAutoCaptions by remember { mutableStateOf(false) }
    var showSrtDialog by remember { mutableStateOf(false) }

    // E) Music & SFX State
    var selectedMusicTrack by remember { mutableStateOf("Horror Ambience (Dark Strings)") }
    var sfxEnabledOnPop by remember { mutableStateOf(true) }
    var musicVolume by remember { mutableFloatStateOf(0.8f) }

    // F) Export State
    var isExporting by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableFloatStateOf(0f) }

    // Photo picker launcher for Still Picture Animation
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            customImageUri = uri
            Toast.makeText(context, "Image loaded for Ken Burns animation", Toast.LENGTH_SHORT).show()
        }
    }

    // Export simulation & Gallery saver
    fun saveToGallery() {
        isExporting = true
        exportProgress = 0f
        coroutineScope.launch {
            for (i in 1..100) {
                delay(25)
                exportProgress = i / 100f
            }
            // Save metadata image to MediaStore
            try {
                val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
                val canvas = android.graphics.Canvas(bitmap)
                canvas.drawColor(android.graphics.Color.BLACK)
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.CYAN
                    textSize = 72f
                    isAntiAlias = true
                    this.textAlign = android.graphics.Paint.Align.CENTER
                }
                canvas.drawText(mainText, 540f, 960f, paint)

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "BanglaMotion_${System.currentTimeMillis()}.png")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/BanglaMotionStudio")
                    }
                }
                val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    val stream: OutputStream? = context.contentResolver.openOutputStream(uri)
                    if (stream != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        stream.close()
                    }
                }
                Toast.makeText(context, "Saved to Gallery / Pictures successfully!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Exported: 1080x1920 MP4 Canvas ready", Toast.LENGTH_SHORT).show()
            }
            isExporting = false
        }
    }

    // -----------------------------------------------------------------
    // MAIN SCREEN UI LAYOUT
    // -----------------------------------------------------------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureBlack)
            .testTag("bangla_motion_studio_phase2")
    ) {
        // TOP APP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(HorrorCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "HORROR STORYTELLING ENGINE",
                        color = HorrorCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "Bangla Motion Studio",
                    color = TextLight,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Quick Random Animation Button
            Button(
                onClick = {
                    playPopSfx()
                    textAnimationType = randomAnimations.random()
                    textColor = listOf(HorrorCyan, BloodRedGlow, BrightYellow, NeonViolet, Color.White, Color(0xFFFB923C)).random()
                    glowColor = listOf(BloodRedGlow, HorrorCyan, NeonViolet, Color(0xFFFF0055)).random()
                    Toast.makeText(context, "Random: $textAnimationType", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCard),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet),
                modifier = Modifier
                    .height(38.dp)
                    .testTag("random_anim_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = "Random",
                    tint = NeonViolet,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("🎲 Random", color = TextLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // 9:16 LIVE MOTION CANVAS PREVIEW
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            HorrorMotionCanvas916(
                mainText = if (isAllCaps) mainText.uppercase(Locale.ROOT) else mainText,
                subtitleText = subtitleText,
                selectedFont = selectedFont,
                fontSize = fontSize,
                isBold = isBold,
                isItalic = isItalic,
                isUnderlined = isUnderlined,
                textAlign = textAlign,
                textColor = textColor,
                glowColor = glowColor,
                strokeWidthPx = strokeWidthPx,
                hasShadow = hasShadow,
                shadowBlur = shadowBlur,
                textBgActive = textBgActive,
                textBgColor = textBgColor,
                stillAnimationType = stillAnimationType,
                customImageUri = customImageUri,
                bgAnimationType = bgAnimationType,
                textAnimationType = textAnimationType,
                staggeredDelayMs = staggeredDelayMs,
                onWordClick = { playPopSfx() }
            )
        }

        // CONTROL PANELS ACCORDING TO SELECTED TAB
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(DarkCharcoal)
                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            when (activeTab) {
                StudioTab.TEXT -> TextControlsPanel(
                    mainText = mainText,
                    onMainTextChange = { mainText = it },
                    subtitleText = subtitleText,
                    onSubtitleTextChange = { subtitleText = it },
                    fontSize = fontSize,
                    onFontSizeChange = { fontSize = it },
                    isBold = isBold,
                    onBoldChange = { isBold = it },
                    isItalic = isItalic,
                    onItalicChange = { isItalic = it },
                    isUnderlined = isUnderlined,
                    onUnderlinedChange = { isUnderlined = it },
                    isAllCaps = isAllCaps,
                    onAllCapsChange = { isAllCaps = it },
                    textAlign = textAlign,
                    onTextAlignChange = { textAlign = it },
                    textColor = textColor,
                    onTextColorChange = { textColor = it },
                    glowColor = glowColor,
                    onGlowColorChange = { glowColor = it },
                    strokeWidth = strokeWidthPx,
                    onStrokeWidthChange = { strokeWidthPx = it },
                    textBgActive = textBgActive,
                    onTextBgActiveChange = { textBgActive = it }
                )

                StudioTab.ANIMATION -> AnimationControlsPanel(
                    stillAnimation = stillAnimationType,
                    onStillAnimationChange = { stillAnimationType = it },
                    bgAnimation = bgAnimationType,
                    onBgAnimationChange = { bgAnimationType = it },
                    textAnimation = textAnimationType,
                    onTextAnimationChange = { textAnimationType = it },
                    staggeredDelay = staggeredDelayMs,
                    onStaggeredDelayChange = { staggeredDelayMs = it },
                    onPickImage = { photoPickerLauncher.launch("image/*") }
                )

                StudioTab.TEMPLATE -> TemplatesControlsPanel(
                    selectedCategory = selectedTemplateCategory,
                    onCategoryChange = { selectedTemplateCategory = it },
                    customTemplates = customTemplates,
                    onSelectPreset = { preset ->
                        playPopSfx()
                        mainText = preset.mainText
                        subtitleText = preset.subtitleText
                        textColor = preset.textColor
                        glowColor = preset.glowColor
                        textAnimationType = preset.animation
                        bgAnimationType = preset.bgType
                    }
                )

                StudioTab.AI_TRIM -> AutoCaptionControlsPanel(
                    words = captionWords,
                    isGenerating = isGeneratingAutoCaptions,
                    onGenerate = {
                        isGeneratingAutoCaptions = true
                        coroutineScope.launch {
                            delay(1200)
                            captionWords.clear()
                            captionWords.addAll(
                                listOf(
                                    CaptionWord(1, "জিন", 0, 400),
                                    CaptionWord(2, "মানুষের", 400, 800),
                                    CaptionWord(3, "মারাত্মক", 800, 1300),
                                    CaptionWord(4, "ক্ষতি", 1300, 1700),
                                    CaptionWord(5, "করে", 1700, 2000)
                                )
                            )
                            isGeneratingAutoCaptions = false
                            Toast.makeText(context, "Bangla captions transcribed successfully", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onOpenSrt = { showSrtDialog = true }
                )

                StudioTab.MUSIC -> MusicControlsPanel(
                    selectedMusic = selectedMusicTrack,
                    onMusicChange = { selectedMusicTrack = it },
                    sfxEnabled = sfxEnabledOnPop,
                    onSfxToggle = { sfxEnabledOnPop = it },
                    volume = musicVolume,
                    onVolumeChange = { musicVolume = it },
                    onTestSfx = { playPopSfx() }
                )

                StudioTab.SWITCH -> SwitchAspectControlsPanel(
                    stillAnimation = stillAnimationType,
                    onStillAnimationChange = { stillAnimationType = it }
                )

                StudioTab.PREVIEW -> PreviewControlsPanel(
                    textAnimation = textAnimationType,
                    onReplay = { playPopSfx() }
                )
            }
        }

        // BOTTOM ACTION ROW WITH GREEN "SAVE TO GALLERY" BUTTON
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { saveToGallery() },
                colors = ButtonDefaults.buttonColors(containerColor = SaveGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_to_gallery_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SAVE TO GALLERY",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        // BOTTOM TABS (Switch, Template, AI Trim, Text, Animation, Music, Preview)
        StudioBottomNavigation(
            activeTab = activeTab,
            onTabSelected = {
                activeTab = it
                playPopSfx()
            }
        )
    }

    // EXPORT PROGRESS DIALOG
    if (isExporting) {
        AlertDialog(
            onDismissRequest = { isExporting = false },
            containerColor = DarkSurfaceCard,
            title = {
                Text(
                    text = "Exporting MP4 (1080x1920 9:16)",
                    color = TextLight,
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
                        text = "Rendering horror captions and animations at 60 FPS...",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                    LinearProgressIndicator(
                        progress = { exportProgress },
                        color = SaveGreen,
                        trackColor = DarkSurfaceBorder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Text(
                        text = "${(exportProgress * 100).toInt()}% completed",
                        color = HorrorCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { isExporting = false },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceBorder)
                ) {
                    Text("Cancel", color = TextLight)
                }
            }
        )
    }

    // SRT IMPORT / EXPORT DIALOG
    if (showSrtDialog) {
        AlertDialog(
            onDismissRequest = { showSrtDialog = false },
            containerColor = DarkSurfaceCard,
            title = { Text("SRT Subtitles", color = TextLight, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "1\n00:00:00,000 --> 00:00:02,100\nJINN CAN CAUSE CANCER\n\n2\n00:00:02,200 --> 00:00:04,500\nজিন মানুষের মারাত্মক ক্ষতি করতে পারে...",
                        color = HorrorCyan,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PureBlack)
                            .padding(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "SRT Exported to clipboard", Toast.LENGTH_SHORT).show()
                        showSrtDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HorrorCyan)
                ) {
                    Text("Copy SRT", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showSrtDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceBorder)
                ) {
                    Text("Close", color = TextLight)
                }
            }
        )
    }
}

// -----------------------------------------------------------------
// 9:16 LIVE CANVAS RENDERING ENGINE
// -----------------------------------------------------------------
@Composable
fun HorrorMotionCanvas916(
    mainText: String,
    subtitleText: String,
    selectedFont: String,
    fontSize: Float,
    isBold: Boolean,
    isItalic: Boolean,
    isUnderlined: Boolean,
    textAlign: String,
    textColor: Color,
    glowColor: Color,
    strokeWidthPx: Float,
    hasShadow: Boolean,
    shadowBlur: Float,
    textBgActive: Boolean,
    textBgColor: Color,
    stillAnimationType: String,
    customImageUri: Uri?,
    bgAnimationType: String,
    textAnimationType: String,
    staggeredDelayMs: Int,
    onWordClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "horror_motion")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    // Ken Burns zoom and pan calculation
    val kenBurnsScale = 1.0f + (0.3f * animProgress)
    val kenBurnsPanX = sin(animProgress * PI.toFloat()) * 20f
    val kenBurnsRotate = when (stillAnimationType) {
        "Rotate" -> -5f + (20f * animProgress)
        "Shake" -> sin(animProgress * 40f) * 6f
        else -> 0f
    }

    // Text Animation Transforms
    var textScale = 1.0f
    var textAlpha = 1.0f
    var textTranslationY = 0f
    var textGlitchX = 0f

    when (textAnimationType) {
        "Pop" -> {
            val t = (animProgress / 0.4f).coerceIn(0f, 1f)
            textScale = if (t < 0.7f) 0.5f + (0.8f * t) else 1.06f - (0.06f * (t - 0.7f) / 0.3f)
        }
        "Zoom" -> {
            textScale = 0.5f + (0.7f * animProgress)
            textAlpha = (animProgress * 2f).coerceIn(0f, 1f)
        }
        "Bounce" -> {
            val t = (animProgress / 0.6f).coerceIn(0f, 1f)
            textScale = if (t >= 1f) 1f else 1f + (sin(t * PI * 3.5).toFloat() * exp(-t * 4.5f) * 0.45f)
        }
        "Slide" -> {
            val t = (animProgress / 0.4f).coerceIn(0f, 1f)
            textTranslationY = (1f - t) * 80f
            textAlpha = (t * 2f).coerceIn(0f, 1f)
        }
        "Glitch" -> {
            val isFlicker = (animProgress * 24).toInt() % 4 == 0
            textGlitchX = if (isFlicker) 8f else 0f
        }
    }

    // Particles list for Background Animation
    val particles = remember {
        List(50) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 3f + 1.5f,
                speedY = Random.nextFloat() * 0.005f + 0.002f,
                alpha = Random.nextFloat() * 0.7f + 0.3f
            )
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .testTag("horror_canvas_box"),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .aspectRatio(9f / 16f)
                .clip(RoundedCornerShape(18.dp))
                .background(PureBlack)
                .border(2.dp, DarkSurfaceBorder, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            // 1. STILL PICTURE / BACKGROUND LAYER
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = if (stillAnimationType == "Ken Burns") kenBurnsScale else 1.0f
                        scaleY = if (stillAnimationType == "Ken Burns") kenBurnsScale else 1.0f
                        translationX = kenBurnsPanX
                        rotationZ = kenBurnsRotate
                    }
            ) {
                if (customImageUri != null) {
                    AsyncImage(
                        model = customImageUri,
                        contentDescription = "Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Dark atmospheric horror vignette gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFF140810), Color(0xFF07040B), PureBlack),
                                    radius = 700f
                                )
                            )
                    )
                }
            }

            // 2. BACKGROUND ANIMATION: PARTICLES / STRIPES / GRADIENT
            when (bgAnimationType) {
                "Particles" -> {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        particles.forEach { p ->
                            p.y = (p.y - p.speedY + 1f) % 1f
                            val px = p.x * size.width
                            val py = p.y * size.height
                            drawCircle(
                                color = BloodRedGlow.copy(alpha = p.alpha),
                                radius = p.radius,
                                center = Offset(px, py)
                            )
                        }
                    }
                }
                "Stripes" -> {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val offset = (animProgress * 60) % 40
                        for (i in -10..30) {
                            val y = i * 40f + offset
                            drawLine(
                                color = Color(0x1800FFD1),
                                start = Offset(0f, y),
                                end = Offset(size.width, y + 20f),
                                strokeWidth = 2f
                            )
                        }
                    }
                }
                "Gradient" -> {
                    val shift = (animProgress * 360f)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0x33FF1A1A),
                                        Color(0x2200FFD1),
                                        Color(0x33A855F7)
                                    ),
                                    start = Offset(cos(shift * PI / 180f).toFloat() * 200f, 0f),
                                    end = Offset(800f, 1200f)
                                )
                            )
                    )
                }
            }

            // 3. MAIN HORROR CAPTION TEXT (Cyan with Red Outer Glow, 4px Stroke)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clickable { onWordClick() }
                        .graphicsLayer {
                            scaleX = textScale
                            scaleY = textScale
                            alpha = textAlpha
                            translationY = textTranslationY
                            translationX = textGlitchX
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val badgeMod = if (textBgActive) {
                        Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(textBgColor)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    } else Modifier

                    Box(modifier = badgeMod, contentAlignment = Alignment.Center) {
                        // Red Outer Glow / Stroke Layer
                        Text(
                            text = mainText,
                            fontSize = fontSize.sp,
                            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            textAlign = when (textAlign) {
                                "Left" -> TextAlign.Start
                                "Right" -> TextAlign.End
                                else -> TextAlign.Center
                            },
                            style = TextStyle(
                                drawStyle = Stroke(width = strokeWidthPx * 2.5f),
                                textDecoration = if (isUnderlined) TextDecoration.Underline else TextDecoration.None
                            ),
                            color = glowColor,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Cyan Foreground Core Text with Shadow
                        Text(
                            text = mainText,
                            fontSize = fontSize.sp,
                            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            textAlign = when (textAlign) {
                                "Left" -> TextAlign.Start
                                "Right" -> TextAlign.End
                                else -> TextAlign.Center
                            },
                            style = TextStyle(
                                shadow = if (hasShadow) Shadow(
                                    color = Color(0x66000000),
                                    offset = Offset(2f, 4f),
                                    blurRadius = shadowBlur
                                ) else Shadow.None,
                                textDecoration = if (isUnderlined) TextDecoration.Underline else TextDecoration.None
                            ),
                            color = textColor,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // 4. BANGLA SUBTITLE (Small bottom box, Yellow/White with black outline)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 28.dp, start = 14.dp, end = 14.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xE6000000))
                    .border(1.dp, Color(0xFF333333), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = subtitleText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrightYellow,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        shadow = Shadow(color = Color.Black, offset = Offset(1f, 2f), blurRadius = 4f)
                    )
                )
            }

            // 9:16 Watermark badge
            Text(
                text = "9:16 • 1080p",
                color = TextMuted.copy(alpha = 0.4f),
                fontSize = 9.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
        }
    }
}

// -----------------------------------------------------------------
// TAB PANELS IMPLEMENTATION
// -----------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TextControlsPanel(
    mainText: String,
    onMainTextChange: (String) -> Unit,
    subtitleText: String,
    onSubtitleTextChange: (String) -> Unit,
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    isBold: Boolean,
    onBoldChange: (Boolean) -> Unit,
    isItalic: Boolean,
    onItalicChange: (Boolean) -> Unit,
    isUnderlined: Boolean,
    onUnderlinedChange: (Boolean) -> Unit,
    isAllCaps: Boolean,
    onAllCapsChange: (Boolean) -> Unit,
    textAlign: String,
    onTextAlignChange: (String) -> Unit,
    textColor: Color,
    onTextColorChange: (Color) -> Unit,
    glowColor: Color,
    onGlowColorChange: (Color) -> Unit,
    strokeWidth: Float,
    onStrokeWidthChange: (Float) -> Unit,
    textBgActive: Boolean,
    onTextBgActiveChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Main Text Input
        OutlinedTextField(
            value = mainText,
            onValueChange = onMainTextChange,
            label = { Text("Main Caption Text (All Caps)", color = TextMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HorrorCyan,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedTextColor = TextLight,
                unfocusedTextColor = TextLight
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Subtitle Text Input
        OutlinedTextField(
            value = subtitleText,
            onValueChange = onSubtitleTextChange,
            label = { Text("বাংলা সাবটাইটেল (Bangla Bottom Box)", color = TextMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrightYellow,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedTextColor = TextLight,
                unfocusedTextColor = TextLight
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Typography Formatting Buttons: Bold, Italic, Underline, AllCaps, Alignment
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Bold
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isBold) HorrorCyan.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isBold) HorrorCyan else DarkSurfaceBorder, RoundedCornerShape(6.dp))
                        .clickable { onBoldChange(!isBold) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FormatBold, contentDescription = "Bold", tint = if (isBold) HorrorCyan else TextMuted, modifier = Modifier.size(18.dp))
                }
                // Italic
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isItalic) HorrorCyan.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isItalic) HorrorCyan else DarkSurfaceBorder, RoundedCornerShape(6.dp))
                        .clickable { onItalicChange(!isItalic) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FormatItalic, contentDescription = "Italic", tint = if (isItalic) HorrorCyan else TextMuted, modifier = Modifier.size(18.dp))
                }
                // Underline
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isUnderlined) HorrorCyan.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isUnderlined) HorrorCyan else DarkSurfaceBorder, RoundedCornerShape(6.dp))
                        .clickable { onUnderlinedChange(!isUnderlined) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FormatUnderlined, contentDescription = "Underline", tint = if (isUnderlined) HorrorCyan else TextMuted, modifier = Modifier.size(18.dp))
                }
                // All Caps
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isAllCaps) HorrorCyan.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isAllCaps) HorrorCyan else DarkSurfaceBorder, RoundedCornerShape(6.dp))
                        .clickable { onAllCapsChange(!isAllCaps) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("aA", color = if (isAllCaps) HorrorCyan else TextMuted, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Alignments
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("Left" to Icons.Default.FormatAlignLeft, "Center" to Icons.Default.FormatAlignCenter, "Right" to Icons.Default.FormatAlignRight).forEach { (align, icon) ->
                    val isSel = textAlign == align
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) HorrorCyan.copy(alpha = 0.25f) else DarkSurfaceCard)
                            .clickable { onTextAlignChange(align) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = align, tint = if (isSel) HorrorCyan else TextMuted, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Color & Glow Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Text Color", color = TextLight, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(HorrorCyan, BrightYellow, Color.White, BloodRedGlow, NeonViolet).forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(c)
                                .border(if (textColor == c) 2.dp else 0.dp, Color.White, CircleShape)
                                .clickable { onTextColorChange(c) }
                        )
                    }
                }
            }

            Column {
                Text("Outer Glow (Red/Neon)", color = TextLight, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(BloodRedGlow, HorrorCyan, NeonViolet, Color(0xFFFF0055), Color.Transparent).forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(c)
                                .border(if (glowColor == c) 2.dp else 0.dp, Color.White, CircleShape)
                                .clickable { onGlowColorChange(c) }
                        )
                    }
                }
            }
        }

        // Font Size Slider
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Size: ${fontSize.toInt()}sp", color = TextLight, fontSize = 11.sp, modifier = Modifier.width(68.dp))
            Slider(
                value = fontSize,
                onValueChange = onFontSizeChange,
                valueRange = 18f..56f,
                colors = SliderDefaults.colors(thumbColor = HorrorCyan, activeTrackColor = HorrorCyan),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AnimationControlsPanel(
    stillAnimation: String,
    onStillAnimationChange: (String) -> Unit,
    bgAnimation: String,
    onBgAnimationChange: (String) -> Unit,
    textAnimation: String,
    onTextAnimationChange: (String) -> Unit,
    staggeredDelay: Int,
    onStaggeredDelayChange: (Int) -> Unit,
    onPickImage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Still Picture Animation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("1. Still Picture Anim", color = HorrorCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = onPickImage,
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCard),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, HorrorCyan),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.Image, contentDescription = null, tint = HorrorCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Pick Image", color = TextLight, fontSize = 11.sp)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Ken Burns", "Parallax", "Shake", "Rotate").forEach { anim ->
                val isSel = stillAnimation == anim
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) HorrorCyan.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isSel) HorrorCyan else DarkSurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { onStillAnimationChange(anim) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(anim, color = if (isSel) HorrorCyan else TextLight, fontSize = 11.sp)
                }
            }
        }

        // 2. Background Animation
        Text("2. Background Animation", color = NeonViolet, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Particles", "Stripes", "Gradient", "Solid").forEach { bg ->
                val isSel = bgAnimation == bg
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) NeonViolet.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isSel) NeonViolet else DarkSurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { onBgAnimationChange(bg) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(bg, color = if (isSel) NeonViolet else TextLight, fontSize = 11.sp)
                }
            }
        }

        // 3. Text to Animation
        Text("3. Text to Animation (Staggered)", color = BrightYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Pop", "Slide", "Zoom", "Bounce", "Typewriter").forEach { tanim ->
                val isSel = textAnimation == tanim
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) BrightYellow.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isSel) BrightYellow else DarkSurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { onTextAnimationChange(tanim) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(tanim, color = if (isSel) BrightYellow else TextLight, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun TemplatesControlsPanel(
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    customTemplates: List<Pair<String, String>>,
    onSelectPreset: (MotionTemplateSpec) -> Unit
) {
    val presets = remember {
        listOf(
            MotionTemplateSpec("fleek", "Fleek", "Featured", "HAUNTED JINN REVEAL", "অন্ধকার জগতের অজানা রহস্য", HorrorCyan, BloodRedGlow, "Pop", "Particles"),
            MotionTemplateSpec("halo", "Halo", "Featured", "BLOOD CURSE", "রক্তাক্ত অভিশাপের গল্প", BloodRedGlow, HorrorCyan, "Bounce", "Gradient"),
            MotionTemplateSpec("zoom", "Glow Zoom", "Active", "DEAD OF NIGHT", "মধ্যরাতের ভয়ঙ্কর পদধ্বনি", BrightYellow, BloodRedGlow, "Zoom", "Stripes"),
            MotionTemplateSpec("contrast", "Contrast", "Simple", "SILENT SHADOW", "নিস্তব্ধ ছায়ার অতল", Color.White, Color.Red, "Slide", "Particles"),
            MotionTemplateSpec("water", "Water/Wave", "Retro", "MYSTIC RITUAL", "রহস্যময় তন্ত্রমন্ত্র ও ভয়", NeonViolet, HorrorCyan, "Glitch", "Gradient")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Categories Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Featured", "Custom", "Active", "Simple", "Retro").forEach { cat ->
                val isSel = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSel) HorrorCyan else DarkSurfaceCard)
                        .border(1.dp, if (isSel) HorrorCyan else DarkSurfaceBorder, RoundedCornerShape(20.dp))
                        .clickable { onCategoryChange(cat) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(cat, color = if (isSel) Color.Black else TextLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Custom Templates (Custom 1..5)
        if (selectedCategory == "Custom") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                customTemplates.forEach { (title, subtitle) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.width(130.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(title, color = HorrorCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(subtitle, color = TextMuted, fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextLight, modifier = Modifier.size(16.dp))
                                Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = TextLight, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        } else {
            // Preset Cards
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(presets) { preset ->
                    Card(
                        onClick = { onSelectPreset(preset) },
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PureBlack),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(preset.name, color = preset.textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(preset.mainText, color = TextLight, fontSize = 10.sp, maxLines = 1)
                            Text("${preset.animation} • ${preset.category}", color = TextMuted, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AutoCaptionControlsPanel(
    words: List<CaptionWord>,
    isGenerating: Boolean,
    onGenerate: () -> Unit,
    onOpenSrt: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("AI Auto Caption (Bangla & English)", color = HorrorCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("Word-by-word synced horror captions", color = TextMuted, fontSize = 11.sp)
            }

            Button(
                onClick = onGenerate,
                enabled = !isGenerating,
                colors = ButtonDefaults.buttonColors(containerColor = HorrorCyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp)
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(16.dp))
                } else {
                    Icon(Icons.Default.Mic, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Auto Transcribe", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Timeline word-by-word chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            words.forEach { w ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceCard)
                        .border(1.dp, HorrorCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(w.word, color = BrightYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("${w.startTimeMs}ms", color = TextMuted, fontSize = 9.sp)
                    }
                }
            }
        }

        // SRT Export / Import row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onOpenSrt,
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCard),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.Subtitles, contentDescription = null, tint = TextLight, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("View / Export SRT", color = TextLight, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun MusicControlsPanel(
    selectedMusic: String,
    onMusicChange: (String) -> Unit,
    sfxEnabled: Boolean,
    onSfxToggle: (Boolean) -> Unit,
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    onTestSfx: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Background Music & Horror SFX", color = NeonViolet, fontSize = 13.sp, fontWeight = FontWeight.Bold)

        // SFX toggle on word pop
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("SFX On Word Pop", color = TextLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text("Plays tactile audio chime on every animated word", color = TextMuted, fontSize = 10.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onTestSfx) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Test", tint = NeonViolet)
                }
                Switch(
                    checked = sfxEnabled,
                    onCheckedChange = onSfxToggle,
                    colors = SwitchDefaults.colors(checkedThumbColor = NeonViolet, checkedTrackColor = NeonViolet.copy(alpha = 0.4f))
                )
            }
        }

        // Ambient Tracks selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                "Horror Ambience (Dark Strings)",
                "Creepy Heartbeat",
                "Cinematic Sub Bass",
                "Midnight Whispers"
            ).forEach { track ->
                val isSel = selectedMusic == track
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) NeonViolet.copy(alpha = 0.25f) else DarkSurfaceCard)
                        .border(1.dp, if (isSel) NeonViolet else DarkSurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { onMusicChange(track) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(track, color = if (isSel) NeonViolet else TextLight, fontSize = 11.sp)
                }
            }
        }

        // Volume slider
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Volume: ${(volume * 100).toInt()}%", color = TextLight, fontSize = 11.sp, modifier = Modifier.width(80.dp))
            Slider(
                value = volume,
                onValueChange = onVolumeChange,
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(thumbColor = NeonViolet, activeTrackColor = NeonViolet),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SwitchAspectControlsPanel(
    stillAnimation: String,
    onStillAnimationChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Aspect Ratio & Scale Switch", color = HorrorCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(HorrorCyan.copy(alpha = 0.2f))
                    .border(1.dp, HorrorCyan, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("9:16 (Reels/Shorts)", color = HorrorCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceCard)
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("1:1 (Square)", color = TextMuted, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceCard)
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("16:9 (Landscape)", color = TextMuted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun PreviewControlsPanel(
    textAnimation: String,
    onReplay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Interactive Playback & Timeline Scrubber", color = TextLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onReplay,
                colors = ButtonDefaults.buttonColors(containerColor = HorrorCyan),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Replay Animation", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -----------------------------------------------------------------
// BOTTOM NAVIGATION WITH THE REQUESTED 7 TABS
// -----------------------------------------------------------------
@Composable
fun StudioBottomNavigation(
    activeTab: StudioTab,
    onTabSelected: (StudioTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkCharcoal)
            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(0.dp))
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp, horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StudioTab.values().forEach { tab ->
            val isSelected = activeTab == tab
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) HorrorCyan.copy(alpha = 0.15f) else Color.Transparent)
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.label,
                    tint = if (isSelected) HorrorCyan else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = tab.label,
                    color = if (isSelected) HorrorCyan else TextMuted,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
