package utils

import org.joda.time.DateTime
import org.joda.time.DateTimeFieldType
import java.awt.Color
import java.util.*


/**
 * Generate Random Color
 * @return Color
 */
fun randomColor(): Color {
    val colorpicker = Random()
    val red = colorpicker.nextInt(255) + 1
    val green = colorpicker.nextInt(255) + 1
    val blue = colorpicker.nextInt(255) + 1
    return Color(red, green, blue)
}

fun cleanupCode(content: String): String {
    return if (content.startsWith("```") and content.endsWith("```")) {
        val cleanedContent = content.split("\n").subList(1, -1)
        cleanedContent.joinToString(separator = "\n")
    } else {
        content.replace("` \n", "")
    }
}

fun todayToTomorrow(): Pair<DateTime, DateTime> {
    val today = DateTime.now()
    val startTime = today.minusHours(today.hourOfDay)
    val tomorrow = DateTime.now()
    val endTime = tomorrow.plusHours((24 - tomorrow.get(DateTimeFieldType.hourOfDay())))
    return startTime to endTime
}

fun medalList(): List<String> {
    return listOf(
        Emoji.FIRST_PLACE,
        Emoji.SECOND_PLACE,
        Emoji.THIRD_PLACE,
        Emoji.MEDAL
    )
}