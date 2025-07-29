package com.amarj.utility

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommonUtility {

    fun formatDate(date: Date?): String? {
        if (date == null) return null
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return outputFormat.format(date)
    }
}