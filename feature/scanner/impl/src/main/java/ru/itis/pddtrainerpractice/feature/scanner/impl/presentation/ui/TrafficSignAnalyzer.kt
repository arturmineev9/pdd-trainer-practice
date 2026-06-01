package ru.itis.pddtrainerpractice.feature.scanner.impl.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.nio.MappedByteBuffer

class TrafficSignAnalyzer(
    context: Context,
    private val onResult: (String, Float) -> Unit
) : ImageAnalysis.Analyzer {

    private val modelBuffer: MappedByteBuffer =
        FileUtil.loadMappedFile(context, "traffic_signs.tflite")
    private val interpreter = Interpreter(modelBuffer)

    // Используем ResizeOp.Method.BILINEAR напрямую
    private val imageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(30, 30, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(0f, 255f)) // Нормализация для Float32 [0, 1]
        .build()

    private val classes = arrayOf(
        "Ограничение 20 км/ч",
        "Ограничение 30 км/ч",
        "Ограничение 50 км/ч",
        "Ограничение 60 км/ч",
        "Ограничение 70 км/ч",
        "Ограничение 80 км/ч",
        "Конец ограничения 80 км/ч",
        "Ограничение 100 км/ч",
        "Ограничение 120 км/ч",
        "Обгон запрещен",
        "Обгон грузовым запрещен",
        "Перекресток",
        "Главная дорога",
        "Уступи дорогу",
        "Стоп",
        "Движение запрещено",
        "Грузовым запрещено",
        "Кирпич",
        "Внимание",
        "Опасный поворот налево",
        "Опасный поворот направо",
        "Извилистая дорога",
        "Неровная дорога",
        "Скользкая дорога",
        "Сужение дороги справа",
        "Дорожные работы",
        "Светофор",
        "Пешеходы",
        "Дети",
        "Велосипеды",
        "Снег/лед",
        "Дикие животные",
        "Конец всех ограничений",
        "Поворот направо",
        "Поворот налево",
        "Только прямо",
        "Прямо или направо",
        "Прямо или налево",
        "Держись правее",
        "Держись левее",
        "Круговое движение",
        "Конец запрета обгона",
        "Конец запрета обгона грузовым"
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        try {
            val fullBitmap = imageProxy.toBitmap() ?: return

            // --- НОВАЯ МАГИЯ: ВЫРЕЗАЕМ ЦЕНТР ---
            // Берем размер прицела (например, 400x400 пикселей)
            // Но чтобы не выйти за края камеры, берем половину от меньшей стороны кадра
            val cropSize = minOf(fullBitmap.width, fullBitmap.height) / 2

            val startX = (fullBitmap.width - cropSize) / 2
            val startY = (fullBitmap.height - cropSize) / 2

            // Вырезаем квадратную картинку из центра
            val croppedBitmap = Bitmap.createBitmap(fullBitmap, startX, startY, cropSize, cropSize)
            // -----------------------------------

            // Подготавливаем TensorImage, загружая ОБРЕЗАННУЮ картинку
            var tensorImage = TensorImage(org.tensorflow.lite.DataType.FLOAT32)
            tensorImage.load(croppedBitmap)

            // Применяем ресайз (теперь он сожмет только знак, а не всё небо) и нормализацию
            tensorImage = imageProcessor.process(tensorImage)

            // Выходной буфер
            val output = Array(1) { FloatArray(43) }

            // Запуск нейросети
            interpreter.run(tensorImage.buffer, output)

            val probabilities = output[0]
            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
            val confidence = if (maxIndex != -1) probabilities[maxIndex] else 0f

            if (maxIndex != -1 && confidence > 0.6f) {
                onResult(classes[maxIndex], confidence)
            } else {
                onResult("", 0f)
            }
        } catch (e: Exception) {
            Log.e("SCANNER_DEBUG", "Ошибка анализа: ${e.message}")
        } finally {
            imageProxy.close()
        }
    }
}
