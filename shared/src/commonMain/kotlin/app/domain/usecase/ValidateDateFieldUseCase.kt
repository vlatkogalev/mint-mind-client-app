package app.domain.usecase

import app.domain.model.ValidationResult
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ValidateDateFieldUseCase {
    @OptIn(ExperimentalTime::class)
    operator fun invoke(
        input: String?,
        requiredErrorMessageResource: StringResource,
        invalidErrorMessageResource: StringResource,
        isRequired: Boolean = true
    ): ValidationResult {
        if (input.isNullOrBlank()) {
            return if (isRequired) {
                ValidationResult(
                    successful = false,
                    errorMessageResource = requiredErrorMessageResource
                )
            } else {
                ValidationResult(successful = true)
            }
        }

        val parts = input.split("/")
        if (parts.size != 3 || parts[0].length != 2 || parts[1].length != 2 || parts[2].length != 4) {
            return ValidationResult(
                successful = false,
                errorMessageResource = invalidErrorMessageResource
            )
        }

        val day = parts[0].toIntOrNull()
        val month = parts[1].toIntOrNull()
        val year = parts[2].toIntOrNull()

        if (day == null || month == null || year == null) {
            return ValidationResult(
                successful = false,
                errorMessageResource = invalidErrorMessageResource
            )
        }

        val parsedDate = try {
            LocalDate(year, month, day)
        } catch (_: IllegalArgumentException) {
            null
        }

        if (parsedDate == null) {
            return ValidationResult(
                successful = false,
                errorMessageResource = invalidErrorMessageResource
            )
        }

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (parsedDate > today) {
            return ValidationResult(
                successful = false,
                errorMessageResource = invalidErrorMessageResource
            )
        }

        return ValidationResult(successful = true)
    }
}