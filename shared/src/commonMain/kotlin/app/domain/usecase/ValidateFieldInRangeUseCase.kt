package app.domain.usecase

import app.domain.model.ValidationResult
import org.jetbrains.compose.resources.StringResource

class ValidateFieldInRangeUseCase {
    operator fun invoke(
        input: Int?,
        range: IntRange,
        errorMessageResource: StringResource,
        isRequired: Boolean = true
    ): ValidationResult {
        if (input != null && !isRequired && (input <= 0)) {
            return ValidationResult(successful = true)
        }

        return if (input != null && input in range) {
            ValidationResult(successful = true)
        } else {
            ValidationResult(
                successful = false,
                errorMessageResource = errorMessageResource
            )
        }
    }
}