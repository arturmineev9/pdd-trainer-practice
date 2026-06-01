package ru.itis.pddtrainerpractice.core.ui.utils

import android.content.Context
import androidx.annotation.DrawableRes
import android.util.Log

/**
 * Находит ID drawable ресурса по его строковому имени.
 * @param imageName имя файла без расширения (например, "ic_sign_stop")
 */
@DrawableRes
fun Context.getDrawableIdByName(imageName: String): Int {
    val cleanName = imageName.substringBeforeLast(".")

    val resId = this.resources.getIdentifier(cleanName, "drawable", this.packageName)

    if (resId == 0) {
        Log.e("ResourceHelper", "Картинка не найдена в drawable: $cleanName")
    }

    return resId
}
