package app.domain.usecase

import app.domain.model.ValidationResult
import org.jetbrains.compose.resources.StringResource


class ValidateCommonNumericFieldUseCase {
    operator fun invoke(
        input: Int?,
        errorMessageResource: StringResource,
        isRequired: Boolean = true
    ): ValidationResult {
        return if (isRequired) {
            if (input == null || input < 0) {
                ValidationResult(
                    successful = false,
                    errorMessageResource = errorMessageResource
                )
            } else {
                ValidationResult(successful = true)
            }
        } else {
            ValidationResult(successful = true)
        }
    }

    operator fun invoke(
        input: Long?,
        errorMessageResource: StringResource,
        isRequired: Boolean = true
    ): ValidationResult {
        return if (isRequired) {
            if (input == null || input < 0) {
                ValidationResult(
                    successful = false,
                    errorMessageResource = errorMessageResource
                )
            } else {
                ValidationResult(successful = true)
            }
        } else {
            ValidationResult(successful = true)
        }
    }
}