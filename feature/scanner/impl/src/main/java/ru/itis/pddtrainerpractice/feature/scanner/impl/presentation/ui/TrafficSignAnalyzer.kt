package ru.itis.pddtrainerpractice.feature.scanner.impl.presentation.ui

import android.graphics.Rect
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions

class TrafficSignAnalyzer(
    private val onResult: (String, Float) -> Unit
) : ImageAnalysis.Analyzer {

    private val localModel = LocalModel.Builder()
        .setAssetFilePath("traffic_signs.tflite")
        .build()

    // Используем ImageLabeling вместо ObjectDetection
    private val options = CustomImageLabelerOptions.Builder(localModel)
        .setConfidenceThreshold(0.5f) // Показывать только если уверенность > 50%
        .setMaxResultCount(1)
        .build()

    private val labeler = ImageLabeling.getClient(options)

    // Список классов GTSRB (43 штуки)
    private val classes = arrayOf(
        "Ограничение 20 км/ч", "Ограничение 30 км/ч", "Ограничение 50 км/ч",
        "Ограничение 60 км/ч", "Ограничение 70 км/ч", "Ограничение 80 км/ч",
        "Конец ограничения 80 км/ч", "Ограничение 100 км/ч", "Ограничение 120 км/ч",
        "Обгон запрещен", "Обгон грузовым запрещен", "Перекресток", "Главная дорога",
        "Уступи дорогу", "Стоп", "Движение запрещено", "Грузовым запрещено",
        "Кирпич", "Внимание", "Опасный поворот налево", "Опасный поворот направо",
        "Извилистая дорога", "Неровная дорога", "Скользкая дорога", "Сужение дороги справа",
        "Дорожные работы", "Светофор", "Пешеходы", "Дети", "Велосипеды", "Снег/лед",
        "Дикие животные", "Конец всех ограничений", "Поворот направо", "Поворот налево",
        "Только прямо", "Прямо или направо", "Прямо или налево", "Держись правее",
        "Держись левее", "Круговое движение", "Конец запрета обгона", "Конец запрета обгона грузовым"
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    if (labels.isNotEmpty()) {
                        val firstLabel = labels[0]
                        // Получаем название из нашего массива по индексу, который вернула нейросеть
                        val signName = classes.getOrElse(firstLabel.index) { "Неизвестный знак" }
                        onResult(signName, firstLabel.confidence)
                    } else {
                        onResult("", 0f)
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
