package app.domain.usecase

import app.domain.model.ValidationResult
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource


class ValidateCommonFieldUseCase {
    @OptIn(ExperimentalResourceApi::class)
    operator fun invoke(
        input: String?,
        range: IntRange = 0..0,
        errorMessageResource: StringResource,
        lengthErrorMessageResource: StringResource? = null,
    ): ValidationResult {
        if (input.isNullOrBlank()) {
            return ValidationResult(
                successful = false,
                errorMessageResource = errorMessageResource
            )
        }

        if (range != 0..0) {
            if (input.trim().length in range) {
                return ValidationResult(successful = true)
            }

            return ValidationResult(
                successful = false,
                errorMessageResource = lengthErrorMessageResource
            )
        }

        return ValidationResult(successful = true)
    }
}