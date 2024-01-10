package com.carissa.intermediate.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TimePost {
    companion object {
        fun formatDatabaseDate(inputDate: String?, outputPattern: String): String {
            try {
                val inputDateFormat = SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale("en", "US")
                )

                inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")

                val outputDateFormat = SimpleDateFormat(outputPattern, Locale("en", "US"))

                val date = inputDateFormat.parse(inputDate)
                return outputDateFormat.format(date)
            } catch (e: Exception) {
                return ""
            }
        }
    }
}
