package ru.itis.pddtrainerpractice.core.database

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.itis.pddtrainerpractice.core.database.dto.QuestionDto
import ru.itis.pddtrainerpractice.core.database.entity.QuestionEntity
import javax.inject.Inject
import javax.inject.Provider

class DatabaseInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val databaseProvider: Provider<AppDatabase>
) : RoomDatabase.Callback() {

    private val json = Json { ignoreUnknownKeys = true }
    private val TAG = "DB_TEST" // Тэг для поиска в Logcat

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        Log.d(TAG, "onCreate: База данных создается впервые. Запускаем загрузку...")

        CoroutineScope(Dispatchers.IO).launch {
            prePopulateDatabase()
        }
    }

    // Если база открывается, но не создается - onCreate не сработает.
    // Для отладки можно временно раскомментировать onOpen, но потом уберите!
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        Log.d(TAG, "onOpen: База открыта (уже существует).")
     }

    private suspend fun prePopulateDatabase() {
        try {
            val assetManager = context.assets
            // Проверяем папку
            val ticketFiles = assetManager.list("tickets") ?: emptyArray()
            Log.d(TAG, "Найдено файлов в папке tickets: ${ticketFiles.size}")

            if (ticketFiles.isEmpty()) {
                Log.e(TAG, "ОШИБКА: Папка tickets пуста или не найдена!")
                return
            }

            val allEntities = mutableListOf<QuestionEntity>()

            for (fileName in ticketFiles) {
                if (fileName.endsWith(".json")) {
                    Log.d(TAG, "Обработка файла: $fileName")

                    val jsonString = assetManager.open("tickets/$fileName")
                        .bufferedReader()
                        .use { it.readText() }

                    val dtoList = json.decodeFromString<List<QuestionDto>>(jsonString)

                    val entities = dtoList.map { dto ->
                        // ... ваш код маппинга (можно скопировать из предыдущего ответа) ...
                        // Для краткости я его свернул, но он должен быть тут
                        val ticketNum = dto.ticketNumber.filter { it.isDigit() }.toIntOrNull() ?: 0
                        val questionNum = dto.title.filter { it.isDigit() }.toIntOrNull() ?: 0
                        val rawImageName = dto.image?.substringAfterLast("/")
                        val finalImageName = if (rawImageName == "no_image.jpg") null else rawImageName
                        val optionsList = dto.answers.map { it.text }
                        val optionsJsonString = json.encodeToString(optionsList)
                        val correctIndex = dto.answers.indexOfFirst { it.isCorrect }

                        QuestionEntity(
                            ticketNumber = ticketNum,
                            questionNumber = questionNum,
                            text = dto.questionText,
                            imageName = finalImageName,
                            optionsJson = optionsJsonString,
                            correctOptionIndex = if (correctIndex != -1) correctIndex else 0,
                            comment = dto.answerTip,
                            topic = dto.topics.firstOrNull() ?: "Без темы"
                        )
                    }
                    allEntities.addAll(entities)
                }
            }

            Log.d(TAG, "Всего подготовлено вопросов для вставки: ${allEntities.size}")

            if (allEntities.isNotEmpty()) {
                databaseProvider.get().questionsDao().insertAll(allEntities)
                Log.d(TAG, "УСПЕХ! Данные вставлены в базу.")
            } else {
                Log.e(TAG, "Список сущностей пуст, вставлять нечего.")
            }

        } catch (e: Exception) {
            Log.e(TAG, "КРИТИЧЕСКАЯ ОШИБКА при загрузке базы:", e)
        }
    }
}