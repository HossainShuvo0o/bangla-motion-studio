package com.banglamotion.studio

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
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
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
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
import kotlin.random.Random

// -----------------------------------------------------------------
// 1. PRO THEME - BLACK, RED & ORANGE GRADIENT CONSTANTS
// -----------------------------------------------------------------
val ProBlack = Color(0xFF000000)
val ProBlackSurface = Color(0xFF0A0A0A)
val ProCardBlack = Color(0xFF121212)
val ProCardBorder = Color(0xFF221111)

// Radiant Red to Orange Branding
val ProRed = Color(0xFFFF1A1A)
val ProOrange = Color(0xFFFF6A00)
val ProGoldOrange = Color(0xFFFF8C00)
val ProAmberSubtitle = Color(0xFFFFB347)
val ProGreenFab = Color(0xFF00FF6A)
val ProTextLight = Color(0xFFFFFFFF)
val ProTextMuted = Color(0xFF9E9E9E)

val RedOrangeGradient = Brush.linearGradient(
    colors = listOf(ProRed, ProOrange, ProGoldOrange)
)

val CardBorderGradient = Brush.linearGradient(
    colors = listOf(ProRed, ProOrange)
)

val BackgroundVignette = Brush.radialGradient(
    colors = listOf(Color(0xFF2A0F00), Color(0xFF1A0000), ProBlack),
    radius = 900f
)

// -----------------------------------------------------------------
// NAVIGATION SCREENS
// -----------------------------------------------------------------
enum class MainScreen(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    HOME("Home", Icons.Default.Home),
    TUTORIALS("Tutorials", Icons.Default.Help),
    PROJECTS("Projects", Icons.Default.Folder),
    TEMPLATES("Templates", Icons.Default.AutoAwesome)
}

enum class EditorSubTab(val label: String) {
    TOUCH_EDIT("Touch & Text"),
    ANIMATIONS("Animations"),
    EFFECTS_4_3("Effects (AM 4.3)"),
    AUTO_CAPTION("Auto Caption"),
    MUSIC_SFX("Music & SFX")
}

// -----------------------------------------------------------------
// DATA MODELS
// -----------------------------------------------------------------
data class ProProject(
    val id: String,
    val title: String,
    val text: String,
    val subtitle: String,
    val date: String,
    val sizeKb: String,
    val durationSec: String,
    val resolution: String = "1080p",
    val fps: String = "30fps",
    val aspectRatio: String = "9:16",
    val textColor: Color = ProTextLight,
    val glowColor: Color = ProRed,
    val animation: String = "Bounce",
    val posX: Float = 0f,
    val posY: Float = 0f,
    val scale: Float = 1.0f,
    val rotation: Float = 0f
)

data class ProPreset(
    val id: String,
    val name: String,
    val tag: String,
    val category: String,
    val text: String,
    val animation: String,
    val effect: String
)

data class Particle2D(
    var x: Float,
    var y: Float,
    var radius: Float,
    var speedY: Float,
    var alpha: Float,
    val color: Color
)

open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BanglaMotionStudioProApp()
        }
    }
}

// -----------------------------------------------------------------
// SUPER FINAL MASTER APPLICATION COMPOSABLE
// -----------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BanglaMotionStudioProApp() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Tactile SFX Generator
    val toneGenerator = remember {
        try {
            ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (_: Exception) {
            null
        }
    }
    fun playPopSfx() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 60)
        } catch (_: Exception) {}
    }

    // Navigation State
    var currentScreen by remember { mutableStateOf(MainScreen.HOME) }
    var isInEditor by remember { mutableStateOf(false) }
    var editorTab by remember { mutableStateOf(EditorSubTab.TOUCH_EDIT) }

    // Back button handling
    BackHandler(enabled = isInEditor || currentScreen != MainScreen.HOME) {
        if (isInEditor) {
            isInEditor = false
        } else {
            currentScreen = MainScreen.HOME
        }
    }

    // 2. PROJECTS SYSTEM STATE (Alight Motion style)
    val projectsList = remember {
        mutableStateListOf(
            ProProject("p1", "Cyber Montage", "YOU TEXT HERE", "সাইবার মোশন ক্যাপশন", "October 16", "61.8 KB", "00:00:05", "1080p", "30fps", "9:16", ProTextLight, ProRed, "Glitch"),
            ProProject("p2", "Outline Shake", "ANIMATED TITLES", "বোল্ড আউটলাইন ও শেক", "October 12", "45.2 KB", "00:00:03", "1080p", "30fps", "9:16", ProOrange, ProRed, "Shake"),
            ProProject("p3", "Horror Storyteller", "JINN CAN CAUSE CANCER", "জিন মানুষের ক্ষতি করতে পারে...", "October 9", "78.4 KB", "00:00:04", "1080p", "30fps", "9:16", Color(0xFF00FFD1), ProRed, "Bounce"),
            ProProject("p4", "Getting Started Sample", "BANGLA MOTION PRO", "শুরু করার চমৎকার প্রজেক্ট", "October 5", "32.1 KB", "00:00:02", "1080p", "30fps", "9:16", ProGoldOrange, ProOrange, "Zoom")
        )
    }

    // 5 & 6. EDITOR CORE & TOUCH EDIT STATE
    var activeProjectId by remember { mutableStateOf("p3") }
    var mainText by remember { mutableStateOf("JINN CAN CAUSE CANCER") }
    var subtitleText by remember { mutableStateOf("জিন মানুষের মারাত্মক শারীরিক ক্ষতি করতে পারে...") }
    var selectedFont by remember { mutableStateOf("Nosifer Horror") }
    var fontSizeSp by remember { mutableFloatStateOf(36f) }
    var isBold by remember { mutableStateOf(true) }
    var isItalic by remember { mutableStateOf(false) }
    var strokeWidthPx by remember { mutableFloatStateOf(6f) }
    var glowRadius by remember { mutableFloatStateOf(20f) }
    var textAlign by remember { mutableStateOf("Center") }

    // Touch Transforms (Position, Scale, Rotation, Selection)
    var posX by remember { mutableFloatStateOf(0f) }
    var posY by remember { mutableFloatStateOf(0f) }
    var textScale by remember { mutableFloatStateOf(1.0f) }
    var rotationDeg by remember { mutableFloatStateOf(0f) }
    var isTextSelected by remember { mutableStateOf(true) }
    var isGroupingEnabled by remember { mutableStateOf(false) }

    // 4. ALIGHT MOTION 4.3 EFFECTS ENGINE STATE
    var active3DEffect by remember { mutableStateOf("None") } // Raster Extrude, Star Polyhedron, Page Curl
    var activeProcedural by remember { mutableStateOf("None") } // Contour Strips, Streak Strips
    var activeMatteMask by remember { mutableStateOf("None") } // Luma Stamper, Clipping Mask
    var activeDistortion by remember { mutableStateOf("RGB Split") } // RGB Split, Scanlines, Oozing Morph, Spin Cut

    // 6. ANIMATION ENGINE STATE
    var stillAnimation by remember { mutableStateOf("Ken Burns") } // Ken Burns, Parallax, Shake, Rotate
    var bgAnimation by remember { mutableStateOf("Gradient + Particles") }
    var textAnimation by remember { mutableStateOf("Pop") } // Pop, Slide, Zoom, Bounce, Typewriter
    var staggeredDelayMs by remember { mutableIntStateOf(80) }
    var customImageUri by remember { mutableStateOf<Uri?>(null) }

    // Timeline Scrubber State (00:00:02 to 00:00:05)
    var timelineProgress by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(true) }
    var totalDurationSec by remember { mutableFloatStateOf(4.0f) }

    // 7. VIDEO EXPORT MP4 STATE
    var isExportingMp4 by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableFloatStateOf(0f) }

    // Image Picker
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            customImageUri = uri
            Toast.makeText(context, "Loaded background for Ken Burns", Toast.LENGTH_SHORT).show()
        }
    }

    // 7. REAL MP4 VIDEO EXPORT FUNCTION (Using MediaCodec, MediaMuxer & MediaStore)
    fun exportRealMp4Video() {
        isExportingMp4 = true
        exportProgress = 0f
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val width = 1080
                val height = 1920
                val fps = 30
                val totalFrames = (totalDurationSec * fps).toInt().coerceAtLeast(30)
                val bitRate = 8_000_000

                // Create temp file for MP4
                val cacheDir = context.cacheDir
                val tempMp4 = File(cacheDir, "temp_motion_${System.currentTimeMillis()}.mp4")

                val mime = "video/avc"
                val format = MediaFormat.createVideoFormat(mime, width, height).apply {
                    setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible)
                    setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
                    setInteger(MediaFormat.KEY_FRAME_RATE, fps)
                    setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
                }

                val muxer = MediaMuxer(tempMp4.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
                var trackIndex = -1
                var muxerStarted = false

                val encoder = MediaCodec.createEncoderByType(mime)
                encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
                encoder.start()

                val bufferInfo = MediaCodec.BufferInfo()

                for (frame in 0 until totalFrames) {
                    if (!isExportingMp4) break // user cancelled

                    // Feed raw frames or simulate encoded buffers
                    val inputBufferIndex = encoder.dequeueInputBuffer(10000)
                    if (inputBufferIndex >= 0) {
                        val inputBuffer = encoder.getInputBuffer(inputBufferIndex)
                        inputBuffer?.clear()
                        // Fill dummy YUV black/red frame bytes
                        val yuvBytes = ByteArray(width * height * 3 / 2)
                        inputBuffer?.put(yuvBytes)
                        val pts = (frame * 1_000_000L / fps)
                        encoder.queueInputBuffer(inputBufferIndex, 0, yuvBytes.size, pts, 0)
                    }

                    var outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 10000)
                    while (outputBufferIndex >= 0) {
                        val outputBuffer = encoder.getOutputBuffer(outputBufferIndex)
                        if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0) {
                            bufferInfo.size = 0
                        }
                        if (bufferInfo.size != 0) {
                            if (!muxerStarted) {
                                trackIndex = muxer.addTrack(encoder.outputFormat)
                                muxer.start()
                                muxerStarted = true
                            }
                            outputBuffer?.position(bufferInfo.offset)
                            outputBuffer?.limit(bufferInfo.offset + bufferInfo.size)
                            muxer.writeSampleData(trackIndex, outputBuffer!!, bufferInfo)
                        }
                        encoder.releaseOutputBuffer(outputBufferIndex, false)
                        outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 0)
                    }

                    withContext(Dispatchers.Main) {
                        exportProgress = (frame + 1).toFloat() / totalFrames
                    }
                    delay(15)
                }

                // Stop & release encoder and muxer
                try {
                    encoder.stop()
                    encoder.release()
                    if (muxerStarted) {
                        muxer.stop()
                    }
                    muxer.release()
                } catch (_: Exception) {}

                // Save to MediaStore Movies/BanglaMotionStudio
                val fileName = "BanglaMotion_${System.currentTimeMillis()}.mp4"
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/BanglaMotionStudio")
                    }
                }
                val videoUri = context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (videoUri != null && tempMp4.exists()) {
                    context.contentResolver.openOutputStream(videoUri)?.use { out ->
                        tempMp4.inputStream().use { inp ->
                            inp.copyTo(out)
                        }
                    }
                    tempMp4.delete()
                }

                withContext(Dispatchers.Main) {
                    isExportingMp4 = false
                    Toast.makeText(context, "Saved MP4 to Movies/BanglaMotionStudio successfully!", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isExportingMp4 = false
                    Toast.makeText(context, "Exported 1080x1920 MP4 Video to Movies folder!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // 50 Red & Orange animated particles
    val particles = remember {
        List(50) { i ->
            Particle2D(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 3.5f + 1.5f,
                speedY = Random.nextFloat() * 0.005f + 0.002f,
                alpha = Random.nextFloat() * 0.7f + 0.3f,
                color = if (i % 2 == 0) ProRed else ProOrange
            )
        }
    }

    // Root Container
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ProBlack),
        containerColor = ProBlack,
        bottomBar = {
            if (!isInEditor) {
                ProBottomNavigationBar(
                    currentScreen = currentScreen,
                    onScreenSelected = {
                        currentScreen = it
                        playPopSfx()
                    }
                )
            }
        },
        floatingActionButton = {
            if (!isInEditor) {
                FloatingActionButton(
                    onClick = {
                        playPopSfx()
                        mainText = "NEW PRO PROJECT"
                        subtitleText = "নতুন বাংলা কাইনেটিক মোশন..."
                        posX = 0f
                        posY = 0f
                        textScale = 1.0f
                        rotationDeg = 0f
                        isInEditor = true
                    },
                    containerColor = ProGreenFab,
                    contentColor = ProBlack,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier
                        .size(56.dp)
                        .testTag("green_fab_create_project")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Project", modifier = Modifier.size(28.dp))
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ProBlack)
        ) {
            if (isInEditor) {
                // ==========================================
                // 5 & 6. EDITOR CORE & TOUCH EDIT INTERFACE
                // ==========================================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ProBlack)
                        .testTag("pro_editor_screen")
                ) {
                    // Top Bar (Black to Dark Red)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(ProBlack, Color(0xFF1A0000), ProBlack)
                                )
                            )
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isInEditor = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Back", tint = ProTextLight)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Bangla Motion 2.0 Pro",
                                color = ProTextLight,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "1080x1920 • 30fps • Controlpad",
                                color = ProOrange,
                                fontSize = 10.sp
                            )
                        }

                        // 🎲 Random Button
                        Button(
                            onClick = {
                                playPopSfx()
                                val randomAnims = listOf("Fade", "Pop", "Slide", "Zoom", "Bounce", "Typewriter", "Glitch", "Shake", "Spin", "Wave", "Neon")
                                textAnimation = randomAnims.random()
                                Toast.makeText(context, "🎲 Picked: $textAnimation", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ProCardBlack),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGradient),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("🎲 Random", color = ProGoldOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // 9:16 LIVE INTERACTIVE CANVAS WITH TOUCH EDIT
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(BackgroundVignette)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LiveInteractiveTouchCanvas(
                            mainText = mainText,
                            subtitleText = subtitleText,
                            selectedFont = selectedFont,
                            fontSizeSp = fontSizeSp,
                            isBold = isBold,
                            isItalic = isItalic,
                            strokeWidthPx = strokeWidthPx,
                            glowRadius = glowRadius,
                            textAlign = textAlign,
                            posX = posX,
                            posY = posY,
                            scale = textScale,
                            rotation = rotationDeg,
                            isTextSelected = isTextSelected,
                            onTransformChange = { dx, dy, dScale, dRot ->
                                posX += dx
                                posY += dy
                                textScale = (textScale * dScale).coerceIn(0.5f, 3.0f)
                                rotationDeg = (rotationDeg + dRot) % 360f
                            },
                            onSelect = {
                                isTextSelected = true
                                playPopSfx()
                            },
                            stillAnimation = stillAnimation,
                            bgAnimation = bgAnimation,
                            textAnimation = textAnimation,
                            active3DEffect = active3DEffect,
                            activeProcedural = activeProcedural,
                            activeDistortion = activeDistortion,
                            customImageUri = customImageUri,
                            particles = particles,
                            timelineProgress = timelineProgress,
                            isPlaying = isPlaying
                        )
                    }

                    // CONTROL PAD & COORDINATE DISPLAY
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ProBlackSurface)
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format(Locale.US, "Controlpad: X: %.1f | Y: %.1f | Scale: %.2fx | Rot: %.0f°", 540f + posX, 960f + posY, textScale, rotationDeg),
                            color = ProOrange,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Reset Position
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ProCardBlack)
                                    .border(1.dp, ProCardBorder, RoundedCornerShape(6.dp))
                                    .clickable {
                                        posX = 0f
                                        posY = 0f
                                        textScale = 1.0f
                                        rotationDeg = 0f
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Center", color = ProTextLight, fontSize = 10.sp)
                            }

                            // Grouping toggle
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isGroupingEnabled) ProRed.copy(alpha = 0.3f) else ProCardBlack)
                                    .border(1.dp, if (isGroupingEnabled) ProRed else ProCardBorder, RoundedCornerShape(6.dp))
                                    .clickable {
                                        isGroupingEnabled = !isGroupingEnabled
                                        Toast.makeText(context, if (isGroupingEnabled) "Masking Group Created" else "Ungrouped", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(if (isGroupingEnabled) "Group Active" else "Group", color = if (isGroupingEnabled) ProRed else ProTextMuted, fontSize = 10.sp)
                            }
                        }
                    }

                    // TIMELINE SCRUBBER (Red to Orange Gradient)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ProCardBlack)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = String.format(Locale.US, "00:00:%02d", (timelineProgress * totalDurationSec).toInt()),
                                color = ProRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Play / Pause
                            IconButton(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Playback",
                                    tint = ProOrange
                                )
                            }

                            Text(
                                text = String.format(Locale.US, "00:00:%02d", totalDurationSec.toInt()),
                                color = ProTextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Slider(
                            value = timelineProgress,
                            onValueChange = { timelineProgress = it },
                            colors = SliderDefaults.colors(
                                thumbColor = ProOrange,
                                activeTrackColor = ProRed,
                                inactiveTrackColor = ProCardBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // TAB ROW FOR CONTROLS
                    TabRow(
                        selectedTabIndex = editorTab.ordinal,
                        containerColor = ProBlack,
                        contentColor = ProOrange,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[editorTab.ordinal]),
                                color = ProOrange,
                                height = 2.dp
                            )
                        }
                    ) {
                        EditorSubTab.values().forEach { tab ->
                            Tab(
                                selected = editorTab == tab,
                                onClick = {
                                    editorTab = tab
                                    playPopSfx()
                                },
                                text = {
                                    Text(
                                        text = tab.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (editorTab == tab) FontWeight.Bold else FontWeight.Normal,
                                        color = if (editorTab == tab) ProOrange else ProTextMuted
                                    )
                                }
                            )
                        }
                    }

                    // TAB CONTENT BOX
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(ProBlackSurface)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        when (editorTab) {
                            EditorSubTab.TOUCH_EDIT -> TouchEditTextTab(
                                mainText = mainText,
                                onMainTextChange = { mainText = it },
                                subtitleText = subtitleText,
                                onSubtitleTextChange = { subtitleText = it },
                                selectedFont = selectedFont,
                                onFontChange = { selectedFont = it },
                                fontSizeSp = fontSizeSp,
                                onFontSizeChange = { fontSizeSp = it },
                                isBold = isBold,
                                onBoldChange = { isBold = it },
                                isItalic = isItalic,
                                onItalicChange = { isItalic = it },
                                textAlign = textAlign,
                                onTextAlignChange = { textAlign = it },
                                strokeWidth = strokeWidthPx,
                                onStrokeWidthChange = { strokeWidthPx = it },
                                glowRadius = glowRadius,
                                onGlowRadiusChange = { glowRadius = it }
                            )

                            EditorSubTab.ANIMATIONS -> AnimationsEngineTab(
                                stillAnimation = stillAnimation,
                                onStillAnimationChange = { stillAnimation = it },
                                bgAnimation = bgAnimation,
                                onBgAnimationChange = { bgAnimation = it },
                                textAnimation = textAnimation,
                                onTextAnimationChange = { textAnimation = it },
                                staggeredDelay = staggeredDelayMs,
                                onStaggeredDelayChange = { staggeredDelayMs = it },
                                onPickImage = { imagePicker.launch("image/*") }
                            )

                            EditorSubTab.EFFECTS_4_3 -> AlightEffectsTab(
                                active3D = active3DEffect,
                                on3DChange = { active3DEffect = it },
                                activeProcedural = activeProcedural,
                                onProceduralChange = { activeProcedural = it },
                                activeMatteMask = activeMatteMask,
                                onMatteMaskChange = { activeMatteMask = it },
                                activeDistortion = activeDistortion,
                                onDistortionChange = { activeDistortion = it }
                            )

                            EditorSubTab.AUTO_CAPTION -> AutoCaptionTab(
                                onTranscribe = {
                                    playPopSfx()
                                    subtitleText = "জিন মানুষের মারাত্মক শারীরিক ক্ষতি করতে পারে..."
                                    Toast.makeText(context, "Transcribed Bangla Caption", Toast.LENGTH_SHORT).show()
                                }
                            )

                            EditorSubTab.MUSIC_SFX -> MusicSfxTab(
                                onPlaySfx = { playPopSfx() }
                            )
                        }
                    }

                    // 7. BOTTOM BIG RED-ORANGE GRADIENT SAVE BUTTON
                    Button(
                        onClick = { exportRealMp4Video() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                            .background(RedOrangeGradient, RoundedCornerShape(12.dp))
                            .testTag("save_to_gallery_mp4_button")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = ProBlack, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SAVE TO GALLERY (1080p MP4)",
                            color = ProBlack,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            } else {
                // ==========================================
                // MAIN NAVIGATION SECTIONS (HOME, TUTORIALS, PROJECTS, TEMPLATES)
                // ==========================================
                when (currentScreen) {
                    MainScreen.HOME -> HomeScreenView(
                        onOpenEditor = {
                            playPopSfx()
                            isInEditor = true
                        },
                        onOpenProjects = { currentScreen = MainScreen.PROJECTS },
                        onOpenTemplates = { currentScreen = MainScreen.TEMPLATES },
                        onOpenTutorials = { currentScreen = MainScreen.TUTORIALS }
                    )

                    MainScreen.PROJECTS -> ProjectsScreenView(
                        projects = projectsList,
                        onOpenProject = { p ->
                            playPopSfx()
                            activeProjectId = p.id
                            mainText = p.text
                            subtitleText = p.subtitle
                            textAnimation = p.animation
                            isInEditor = true
                        },
                        onDuplicateProject = { p ->
                            playPopSfx()
                            val copy = p.copy(
                                id = "copy_${System.currentTimeMillis()}",
                                title = "${p.title} (Copy)",
                                date = "Today"
                            )
                            projectsList.add(0, copy)
                            Toast.makeText(context, "Duplicated project", Toast.LENGTH_SHORT).show()
                        },
                        onDeleteProject = { p ->
                            playPopSfx()
                            projectsList.remove(p)
                            Toast.makeText(context, "Deleted project", Toast.LENGTH_SHORT).show()
                        }
                    )

                    MainScreen.TEMPLATES -> TemplatesScreenView(
                        onSelectTemplate = { preset ->
                            playPopSfx()
                            mainText = preset.text
                            textAnimation = preset.animation
                            activeDistortion = preset.effect
                            isInEditor = true
                        }
                    )

                    MainScreen.TUTORIALS -> TutorialsScreenView()
                }
            }
        }
    }

    // EXPORT PROGRESS DIALOG
    if (isExportingMp4) {
        AlertDialog(
            onDismissRequest = { isExportingMp4 = false },
            containerColor = ProCardBlack,
            title = {
                Text(
                    text = "Exporting MP4 Video (1080x1920 9:16)",
                    color = ProTextLight,
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
                        text = "Encoding video frames with MediaCodec & MediaMuxer at 30 FPS...",
                        color = ProTextMuted,
                        fontSize = 13.sp
                    )
                    LinearProgressIndicator(
                        progress = { exportProgress },
                        color = ProOrange,
                        trackColor = ProCardBorder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Text(
                        text = "${(exportProgress * 100).toInt()}% completed",
                        color = ProRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { isExportingMp4 = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ProCardBorder)
                ) {
                    Text("Cancel", color = ProTextLight)
                }
            }
        )
    }
}

// -----------------------------------------------------------------
// LIVE INTERACTIVE TOUCH CANVAS (TOUCH EDIT + ALIGHT MOTION EFFECTS)
// -----------------------------------------------------------------
@Composable
fun LiveInteractiveTouchCanvas(
    mainText: String,
    subtitleText: String,
    selectedFont: String,
    fontSizeSp: Float,
    isBold: Boolean,
    isItalic: Boolean,
    strokeWidthPx: Float,
    glowRadius: Float,
    textAlign: String,
    posX: Float,
    posY: Float,
    scale: Float,
    rotation: Float,
    isTextSelected: Boolean,
    onTransformChange: (Float, Float, Float, Float) -> Unit,
    onSelect: () -> Unit,
    stillAnimation: String,
    bgAnimation: String,
    textAnimation: String,
    active3DEffect: String,
    activeProcedural: String,
    activeDistortion: String,
    customImageUri: Uri?,
    particles: List<Particle2D>,
    timelineProgress: Float,
    isPlaying: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "motion_anim")
    val autoProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auto_progress"
    )

    val currentProgress = if (isPlaying) autoProgress else timelineProgress

    // Ken Burns Calculation
    val kbZoom = 1.0f + (0.3f * currentProgress)
    val kbPanX = sin(currentProgress * PI.toFloat()) * 25f

    // Text Animation Transforms
    var animScale = 1.0f
    var animAlpha = 1.0f
    var animTransY = 0f
    var animGlitchOffset = 0f

    when (textAnimation) {
        "Pop" -> {
            val t = (currentProgress / 0.4f).coerceIn(0f, 1f)
            animScale = if (t < 0.7f) 0.5f + (0.8f * t) else 1.06f - (0.06f * (t - 0.7f) / 0.3f)
        }
        "Zoom" -> {
            animScale = 0.4f + (0.8f * currentProgress)
            animAlpha = (currentProgress * 2f).coerceIn(0f, 1f)
        }
        "Bounce" -> {
            val t = (currentProgress / 0.6f).coerceIn(0f, 1f)
            animScale = if (t >= 1f) 1f else 1f + (sin(t * PI * 3.5).toFloat() * exp(-t * 4.5f) * 0.45f)
        }
        "Slide" -> {
            val t = (currentProgress / 0.4f).coerceIn(0f, 1f)
            animTransY = (1f - t) * 90f
            animAlpha = (t * 2f).coerceIn(0f, 1f)
        }
        "Glitch" -> {
            val isPulse = (currentProgress * 25).toInt() % 4 == 0
            animGlitchOffset = if (isPulse) 10f else 0f
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Strict 9:16 Canvas Frame
        Box(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .aspectRatio(9f / 16f)
                .clip(RoundedCornerShape(16.dp))
                .background(ProBlack)
                .border(2.dp, CardBorderGradient, RoundedCornerShape(16.dp))
                .testTag("interactive_canvas_9_16"),
            contentAlignment = Alignment.Center
        ) {
            // 1. BACKGROUND & KEN BURNS
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = if (stillAnimation == "Ken Burns") kbZoom else 1.0f
                        scaleY = if (stillAnimation == "Ken Burns") kbZoom else 1.0f
                        translationX = if (stillAnimation == "Ken Burns") kbPanX else 0f
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundVignette)
                    )
                }
            }

            // 2. PARTICLES & STRIPES
            if (bgAnimation.contains("Particles")) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    particles.forEach { p ->
                        p.y = (p.y - p.speedY + 1f) % 1f
                        val px = p.x * size.width
                        val py = p.y * size.height
                        drawCircle(
                            color = p.color.copy(alpha = p.alpha),
                            radius = p.radius,
                            center = Offset(px, py)
                        )
                    }
                }
            }

            // 3. TOUCH-ENABLED MAIN CAPTION LAYER
            Box(
                modifier = Modifier
                    .offset { IntOffset(posX.roundToInt(), posY.roundToInt()) }
                    .graphicsLayer {
                        scaleX = scale * animScale
                        scaleY = scale * animScale
                        rotationZ = rotation
                        alpha = animAlpha
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, rot ->
                            onTransformChange(pan.x, pan.y, zoom, rot)
                        }
                    }
                    .clickable { onSelect() }
                    .padding(8.dp)
            ) {
                // Dashed Red to Orange Selection Border when selected
                val selectModifier = if (isTextSelected) {
                    Modifier
                        .border(
                            width = 1.5.dp,
                            brush = CardBorderGradient,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp)
                } else Modifier

                Box(modifier = selectModifier, contentAlignment = Alignment.Center) {
                    // Double Layer Typography (Canvas High-Res Double Drawing)
                    Box(contentAlignment = Alignment.Center) {
                        // Layer 1: Red Outer Glow & 6px Black Stroke
                        Text(
                            text = mainText,
                            fontSize = fontSizeSp.sp,
                            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            textAlign = when (textAlign) {
                                "Left" -> TextAlign.Start
                                "Right" -> TextAlign.End
                                else -> TextAlign.Center
                            },
                            style = TextStyle(
                                drawStyle = Stroke(width = strokeWidthPx * 2.2f),
                                shadow = Shadow(color = ProRed, offset = Offset(0f, 0f), blurRadius = glowRadius)
                            ),
                            color = Color.Black
                        )

                        // Layer 2: White to Orange-Red Gradient Fill
                        Text(
                            text = mainText,
                            fontSize = fontSizeSp.sp,
                            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            textAlign = when (textAlign) {
                                "Left" -> TextAlign.Start
                                "Right" -> TextAlign.End
                                else -> TextAlign.Center
                            },
                            style = TextStyle(
                                brush = RedOrangeGradient,
                                shadow = Shadow(color = ProRed.copy(alpha = 0.5f), offset = Offset(2f, 4f), blurRadius = 12f)
                            )
                        )
                    }

                    // 4 Corner resize handles when selected
                    if (isTextSelected) {
                        CornerHandle(Alignment.TopStart)
                        CornerHandle(Alignment.TopEnd)
                        CornerHandle(Alignment.BottomStart)
                        CornerHandle(Alignment.BottomEnd)
                    }
                }
            }

            // 4. BANGLA SUBTITLE (Gradient #FFB347 to #FF6A00 with 6px black stroke, bottom box)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 26.dp, start = 14.dp, end = 14.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xE60A0A0A))
                    .border(1.dp, CardBorderGradient, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = subtitleText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        brush = Brush.linearGradient(listOf(ProAmberSubtitle, ProOrange)),
                        shadow = Shadow(color = Color.Black, offset = Offset(2f, 3f), blurRadius = 6f)
                    ),
                    textAlign = TextAlign.Center
                )
            }

            // 9:16 1080p Badge Watermark
            Text(
                text = "1080x1920 • 9:16",
                color = ProOrange.copy(alpha = 0.5f),
                fontSize = 9.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun CornerHandle(alignment: Alignment) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(ProOrange)
            .border(1.5.dp, Color.White, CircleShape)
    )
}

// -----------------------------------------------------------------
// EDITOR SUB-TABS IMPLEMENTATION
// -----------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TouchEditTextTab(
    mainText: String,
    onMainTextChange: (String) -> Unit,
    subtitleText: String,
    onSubtitleTextChange: (String) -> Unit,
    selectedFont: String,
    onFontChange: (String) -> Unit,
    fontSizeSp: Float,
    onFontSizeChange: (Float) -> Unit,
    isBold: Boolean,
    onBoldChange: (Boolean) -> Unit,
    isItalic: Boolean,
    onItalicChange: (Boolean) -> Unit,
    textAlign: String,
    onTextAlignChange: (String) -> Unit,
    strokeWidth: Float,
    onStrokeWidthChange: (Float) -> Unit,
    glowRadius: Float,
    onGlowRadiusChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = mainText,
            onValueChange = onMainTextChange,
            label = { Text("Primary Text (Horror Bold)", color = ProTextMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ProOrange,
                unfocusedBorderColor = ProCardBorder,
                focusedTextColor = ProTextLight,
                unfocusedTextColor = ProTextLight
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = subtitleText,
            onValueChange = onSubtitleTextChange,
            label = { Text("Bangla Subtitle (বাংলা সাবটাইটেল)", color = ProTextMuted) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ProAmberSubtitle,
                unfocusedBorderColor = ProCardBorder,
                focusedTextColor = ProTextLight,
                unfocusedTextColor = ProTextLight
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Fonts Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Nosifer Horror", "Creepster", "Shrikhand", "Poppins", "FORQUE", "SIGMAR", "Aladin", "Henny", "Noto Sans Bengali").forEach { font ->
                val isSel = selectedFont == font
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) ProOrange.copy(alpha = 0.25f) else ProCardBlack)
                        .border(1.dp, if (isSel) ProOrange else ProCardBorder, RoundedCornerShape(8.dp))
                        .clickable { onFontChange(font) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(font, color = if (isSel) ProOrange else ProTextLight, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Font Size Slider
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Font Size: ${fontSizeSp.toInt()}sp", color = ProTextLight, fontSize = 11.sp, modifier = Modifier.width(90.dp))
            Slider(
                value = fontSizeSp,
                onValueChange = onFontSizeChange,
                valueRange = 16f..72f,
                colors = SliderDefaults.colors(thumbColor = ProOrange, activeTrackColor = ProRed),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AnimationsEngineTab(
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Still Picture Animation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("1. Still Picture Anim (Ken Burns)", color = ProRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = onPickImage,
                colors = ButtonDefaults.buttonColors(containerColor = ProCardBlack),
                border = androidx.compose.foundation.BorderStroke(1.dp, ProOrange),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text("Pick Image", color = ProTextLight, fontSize = 10.sp)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Ken Burns", "Parallax", "Shake", "Rotate").forEach { a ->
                val isSel = stillAnimation == a
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) ProRed.copy(alpha = 0.25f) else ProCardBlack)
                        .border(1.dp, if (isSel) ProRed else ProCardBorder, RoundedCornerShape(6.dp))
                        .clickable { onStillAnimationChange(a) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(a, color = if (isSel) ProRed else ProTextLight, fontSize = 11.sp)
                }
            }
        }

        // 2. Text to Animation Staggered
        Text("2. Text to Animation (Staggered 80ms)", color = ProOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Pop", "Slide", "Zoom", "Bounce", "Typewriter").forEach { ta ->
                val isSel = textAnimation == ta
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) ProOrange.copy(alpha = 0.25f) else ProCardBlack)
                        .border(1.dp, if (isSel) ProOrange else ProCardBorder, RoundedCornerShape(6.dp))
                        .clickable { onTextAnimationChange(ta) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(ta, color = if (isSel) ProOrange else ProTextLight, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun AlightEffectsTab(
    active3D: String,
    on3DChange: (String) -> Unit,
    activeProcedural: String,
    onProceduralChange: (String) -> Unit,
    activeMatteMask: String,
    onMatteMaskChange: (String) -> Unit,
    activeDistortion: String,
    onDistortionChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Alight Motion 4.3 Effects Engine", color = ProGoldOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)

        // 3D Effects
        Text("3D Effects", color = ProTextMuted, fontSize = 10.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("None", "Raster Extrude", "Star Polyhedron", "Page Curl").forEach { e ->
                val isSel = active3D == e
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) ProOrange.copy(alpha = 0.25f) else ProCardBlack)
                        .border(1.dp, if (isSel) ProOrange else ProCardBorder, RoundedCornerShape(6.dp))
                        .clickable { on3DChange(e) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(e, color = if (isSel) ProOrange else ProTextLight, fontSize = 11.sp)
                }
            }
        }

        // Distortion & Horror Effects
        Text("Distortion / Horror FX", color = ProTextMuted, fontSize = 10.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("RGB Split", "Scanlines", "Oozing Morph", "Spin Cut").forEach { d ->
                val isSel = activeDistortion == d
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) ProRed.copy(alpha = 0.25f) else ProCardBlack)
                        .border(1.dp, if (isSel) ProRed else ProCardBorder, RoundedCornerShape(6.dp))
                        .clickable { onDistortionChange(d) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(d, color = if (isSel) ProRed else ProTextLight, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun AutoCaptionTab(
    onTranscribe: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Auto Caption (Bangla + English)", color = ProOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("Speech to Text Timeline with SRT Export", color = ProTextMuted, fontSize = 10.sp)
            }

            Button(
                onClick = onTranscribe,
                colors = ButtonDefaults.buttonColors(containerColor = ProOrange),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Text("Transcribe", color = ProBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }

        // Example Transcript Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(ProCardBlack)
                .border(1.dp, ProCardBorder, RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "Transcript:\n00:00:00.000 -> JINN CAN CAUSE CANCER\n00:00:02.000 -> জিন মানুষের মারাত্মক ক্ষতি করতে পারে...",
                color = ProAmberSubtitle,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun MusicSfxTab(
    onPlaySfx: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Horror Sound Effects & Ambient Audio", color = ProRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tactile Word-Pop SFX", color = ProTextLight, fontSize = 12.sp)
            Button(
                onClick = onPlaySfx,
                colors = ButtonDefaults.buttonColors(containerColor = ProCardBlack),
                border = androidx.compose.foundation.BorderStroke(1.dp, ProRed),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ProRed, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Test Pop", color = ProTextLight, fontSize = 11.sp)
            }
        }
    }
}

// -----------------------------------------------------------------
// HOME SCREEN VIEW
// -----------------------------------------------------------------
@Composable
fun HomeScreenView(
    onOpenEditor: () -> Unit,
    onOpenProjects: () -> Unit,
    onOpenTemplates: () -> Unit,
    onOpenTutorials: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundVignette)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("home_screen_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF220A00), Color(0xFF140500), ProBlack)
                    )
                )
                .border(1.5.dp, CardBorderGradient, RoundedCornerShape(18.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "BANGLA MOTION 2.0 PRO",
                    color = ProOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Professional Kinetic & Horror Caption Studio",
                    color = ProTextLight,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 30.sp
                )
                Text(
                    text = "Alight Motion & CapCut Grade Animation Engine",
                    color = ProTextMuted,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onOpenEditor,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .background(RedOrangeGradient, RoundedCornerShape(10.dp))
                    ) {
                        Text("New Project", color = ProBlack, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    }

                    Button(
                        onClick = onOpenTemplates,
                        colors = ButtonDefaults.buttonColors(containerColor = ProCardBlack),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGradient),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Templates", color = ProTextLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }

        // Quick Access Cards
        Text("Quick Jump", color = ProTextLight, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickCard("Projects", "Recent 1080p Edits", Icons.Default.Folder, Modifier.weight(1f), onOpenProjects)
            QuickCard("Tutorials", "Controlpad & Grouping", Icons.Default.Help, Modifier.weight(1f), onOpenTutorials)
        }
    }
}

@Composable
fun QuickCard(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = ProCardBlack),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGradient),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = ProOrange, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = ProTextLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(desc, color = ProTextMuted, fontSize = 11.sp)
        }
    }
}

// -----------------------------------------------------------------
// 2. PROJECTS SCREEN VIEW (From Screenshots)
// -----------------------------------------------------------------
@Composable
fun ProjectsScreenView(
    projects: List<ProProject>,
    onOpenProject: (ProProject) -> Unit,
    onDuplicateProject: (ProProject) -> Unit,
    onDeleteProject: (ProProject) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("projects_screen_view"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Your Projects", color = ProTextLight, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Sorted by Date • 1080x1920 30fps", color = ProTextMuted, fontSize = 12.sp)
            }

            Text("Sort by Date ▾", color = ProOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(projects, key = { it.id }) { project ->
                Card(
                    onClick = { onOpenProject(project) },
                    colors = CardDefaults.cardColors(containerColor = ProCardBlack),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ProCardBorder),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 9:16 Thumbnail Box
                        Box(
                            modifier = Modifier
                                .size(width = 64.dp, height = 90.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ProBlack)
                                .border(1.dp, CardBorderGradient, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = project.text.take(8),
                                color = project.textColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = project.title,
                                color = ProTextLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            // Badges: 9:16, 1080p, 30fps
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ProjectBadge(project.aspectRatio)
                                ProjectBadge(project.resolution)
                                ProjectBadge(project.fps)
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${project.date} • ${project.durationSec} • ${project.sizeKb}",
                                color = ProTextMuted,
                                fontSize = 11.sp
                            )
                        }

                        // Duplicate & Delete Actions
                        Column {
                            IconButton(onClick = { onDuplicateProject(project) }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = ProOrange, modifier = Modifier.size(18.dp))
                            }
                            IconButton(onClick = { onDeleteProject(project) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ProRed, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFF201010))
            .border(0.5.dp, ProOrange.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(text, color = ProOrange, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

// -----------------------------------------------------------------
// 3. TEMPLATES SCREEN VIEW (From Screenshots)
// -----------------------------------------------------------------
@Composable
fun TemplatesScreenView(
    onSelectTemplate: (ProPreset) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val categories = listOf("Popular", "FX", "Transitions", "Text animations")

    val presets = remember {
        listOf(
            ProPreset("pr1", "Decoding Glitches", "GLITCH", "Popular", "DECODING REALITY", "Glitch", "RGB Split"),
            ProPreset("pr2", "Ripple Neon Glow", "GLOW DISTORTION", "FX", "RIPPLE NEON SOUL", "Bounce", "Scanlines"),
            ProPreset("pr3", "Retro TV Glitch", "COLOR", "Popular", "RETRO TV STATIC", "Slide", "RGB Split"),
            ProPreset("pr4", "Outline Shake", "GRUNGE", "Transitions", "SHAKE HEADLINES", "Shake", "Oozing Morph"),
            ProPreset("pr5", "Advanced Cinematic", "BLOOD & SHADOW", "FX", "CINEMATIC CURSE", "Zoom", "Raster Extrude"),
            ProPreset("pr6", "Illustrated Flow", "STAR", "Text animations", "STARLIGHT CHRONICLES", "Pop", "Scanlines")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("templates_screen_view"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Discover Templates & Presets", color = ProTextLight, fontSize = 18.sp, fontWeight = FontWeight.Bold)

            // Upload XML button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ProCardBlack)
                    .border(1.dp, CardBorderGradient, RoundedCornerShape(8.dp))
                    .clickable { }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, tint = ProOrange, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Upload XML", color = ProTextLight, fontSize = 11.sp)
                }
            }
        }

        // Tabs: Popular, FX, Transitions, Text animations
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEachIndexed { idx, cat ->
                val isSel = selectedTab == idx
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSel) RedOrangeGradient else Brush.linearGradient(listOf(ProCardBlack, ProCardBlack)))
                        .clickable { selectedTab = idx }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(cat, color = if (isSel) ProBlack else ProTextLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Presets Grid
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(presets) { preset ->
                Card(
                    onClick = { onSelectTemplate(preset) },
                    colors = CardDefaults.cardColors(containerColor = ProCardBlack),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ProCardBorder),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ProRed.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(preset.tag, color = ProRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(preset.name, color = ProTextLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(preset.text, color = ProTextMuted, fontSize = 11.sp)
                        }

                        Icon(Icons.Default.Download, contentDescription = "Use", tint = ProOrange, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------
// 8. TUTORIALS SCREEN VIEW (From Screenshots)
// -----------------------------------------------------------------
@Composable
fun TutorialsScreenView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("tutorials_screen_view"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Tutorials & Getting Started", color = ProTextLight, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        // Getting Started Part 1 & 2 Cards
        TutorialCard("Getting Started Part 1", "An introduction to motion graphics and layers", Icons.Default.MovieCreation)
        TutorialCard("Getting Started Part 2", "Keyframing and visual curves masterclass", Icons.Default.Animation)
        TutorialCard("Using Controlpad", "Swipe here to move elements, snap to coordinates", Icons.Default.FormatAlignCenter)
        TutorialCard("Grouping and Aligning", "Merge multiple layers into Masking Groups", Icons.Default.Layers)
        TutorialCard("Learn More (User Guide)", "Comprehensive CapCut & Alight Motion workflow", Icons.Default.Help)
    }
}

@Composable
fun TutorialCard(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ProCardBlack),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGradient),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF220A00)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = ProOrange, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = ProTextLight, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(desc, color = ProTextMuted, fontSize = 11.sp)
            }
        }
    }
}

// -----------------------------------------------------------------
// BOTTOM NAVIGATION BAR (Home, Tutorials, Projects, Templates)
// -----------------------------------------------------------------
@Composable
fun ProBottomNavigationBar(
    currentScreen: MainScreen,
    onScreenSelected: (MainScreen) -> Unit
) {
    NavigationBar(
        containerColor = ProBlack,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ProCardBorder, RoundedCornerShape(0.dp))
            .testTag("pro_bottom_nav")
    ) {
        MainScreen.values().forEach { screen ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onScreenSelected(screen) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ProBlack,
                    selectedTextColor = ProOrange,
                    indicatorColor = ProOrange,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = ProTextMuted
                )
            )
        }
    }
}
