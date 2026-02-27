package ru.itis.pddtrainerpractice

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.itis.pddtrainerpractice.core.database.dao.QuestionsDao
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var questionsDao: QuestionsDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            Log.d("DB_TEST", "Пытаемся пнуть базу данных...")
            try {
                val count = questionsDao.getCount()
                Log.d("DB_TEST", "Количество вопросов в базе: $count")
            } catch (e: Exception) {
                Log.e("DB_TEST", "Ошибка при запросе к базе", e)
            }
        }
    }
}
