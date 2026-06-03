package ru.itis.pddtrainerpractice.feature.scanner.impl.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.screen.Screen
import ru.itis.pddtrainerpractice.core.common.navigation.LocalRootNavigator
import ru.itis.pddtrainerpractice.feature.questions.impl.presentation.testing.screen.TestingScreen


class ScannerScreen : Screen {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        var hasCameraPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            )
        }

        // Запрашиваем разрешение на использование камеры
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted -> hasCameraPermission = isGranted }
        )

        LaunchedEffect(Unit) {
            if (!hasCameraPermission) {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        if (hasCameraPermission) {
            CameraPreview()
        } else {
            // Экран-заглушка, если нет разрешения
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Для сканирования знаков необходим доступ к камере")
            }
        }
    }
}

data class ClassificationResult(
    val label: String,
    val confidence: Float
)

@Composable
private fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val rootNavigator = LocalRootNavigator.current

    // Состояние хранит лучший результат
    var result by remember { mutableStateOf<ClassificationResult?>(null) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().apply {
                        setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                TrafficSignAnalyzer(context) { label, confidence ->
                                    result = if (label.isNotEmpty()) {
                                        ClassificationResult(label, confidence)
                                    } else null
                                }
                            )
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) { e.printStackTrace() }
                }, ContextCompat.getMainExecutor(context))
                previewView
            }
        )

        // 2. Рисуем статичный "Прицел" (Видоискатель)
        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.Center)
                .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Text(
                "Наведите на знак",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }

        // 3. Плашка с результатом внизу (AR-режим подсказки)
        result?.let { res ->
            if (res.confidence > 0.6f) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Распознан знак:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = res.label,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${(res.confidence * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // Переход на экран билета, передаем название знака для поиска
                                rootNavigator.push(TestingScreen(searchQuery = res.label))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Вопросы с этим знаком")
                        }
                    }
                }
            }
        }
    }
}
