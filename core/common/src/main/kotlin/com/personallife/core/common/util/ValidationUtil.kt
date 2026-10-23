package com.personallife.core.common.util

/**
 * Validation result wrapper.
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

/**
 * Common input validation utilities.
 */
object ValidationUtil {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validateEmail(email: String): ValidationResult {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Email cannot be empty")
            !EMAIL_REGEX.matches(trimmed) -> ValidationResult(false, "Please enter a valid email address")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Password cannot be empty")
            password.length < 8 -> ValidationResult(false, "Password must be at least 8 characters long")
            !password.any { it.isDigit() } -> ValidationResult(false, "Password must contain at least one number")
            !password.any { it.isLetter() } -> ValidationResult(false, "Password must contain at least one letter")
            else -> ValidationResult(true)
        }
    }

    fun validateDisplayName(name: String): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "Name cannot be empty")
            trimmed.length < 2 -> ValidationResult(false, "Name must be at least 2 characters")
            else -> ValidationResult(true)
        }
    }
}
