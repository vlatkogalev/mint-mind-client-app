package user.domain.usecase

import app.domain.model.ValidationResult
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.error_field_empty
import mintmind.shared.generated.resources.user_register_password_invalid_error
import mintmind.shared.generated.resources.user_register_password_length_error

class ValidatePasswordUseCase {
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessageResource = Res.string.error_field_empty
            )
        }

        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessageResource = Res.string.user_register_password_length_error
            )
        }

        val specialChars = "!#$%&'()*+,-.:;<=>?@^_`{|}~"
        val isValidPassword = password.any { it.isUpperCase() }
                && password.any { it.isLowerCase() }
                && password.any { it.isDigit() }
                && password.any { it in specialChars }

        if (!isValidPassword) {
            return ValidationResult(
                successful = false,
                errorMessageResource = Res.string.user_register_password_invalid_error
            )
        }

        return ValidationResult(successful = true)
    }
}