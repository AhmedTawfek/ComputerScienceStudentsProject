package com.example.computerscienceproject.core.presentation.utils

import java.text.DecimalFormat
import java.util.regex.Pattern

fun validateFullName(fullName: String): Boolean {
    // Check if the name is empty or blank
    if (fullName.isBlank()) {
        return false
    }

    // Split the full name into parts and check if there are at least two parts
    val nameParts = fullName.trim().split("\\s+".toRegex())
    if (nameParts.size < 2) {
        return false
    }

    // Check that each part of the name has at least 2 characters
    if (nameParts.any { it.length < 2 }) {
        return false
    }

    // Trim the full name before applying the pattern
    val trimmedFullName = fullName.trim()

    // Pattern to allow both Latin and Arabic characters, along with spaces, hyphens, and apostrophes
    val namePattern = "^[a-zA-Z\\p{InArabic}À-ÖØ-öø-ÿ]+([\\s'-][a-zA-Z\\p{InArabic}À-ÖØ-öø-ÿ]+)*\$".toRegex()

    return namePattern.matches(trimmedFullName)
}

fun isEmailCorrect(email: String): Boolean {
    if (email.isEmpty()){
        return false
    }

    val emailRegex = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
    val pattern = Pattern.compile(emailRegex)

    return pattern.matcher(email).matches()
}

fun formatNumberWithCommas(numberStr: String): String {
    return try {
        val number = numberStr.toDouble()
        val formatter = DecimalFormat("#,###")
        formatter.format(number)
    } catch (e: NumberFormatException) {
        // Return original string if it's not a valid number
        numberStr
    }
}