package user.domain.usecase

import app.domain.model.ValidationResult
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.error_field_empty
import mintmind.shared.generated.resources.user_register_conf_password_matching_error

class ValidateConfirmPasswordUseCase {
    operator fun invoke(confPassword: String, password: String): ValidationResult {
        if (confPassword.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessageResource = Res.string.error_field_empty
            )
        }

        if (confPassword != password) {
            return ValidationResult(
                successful = false,
                errorMessageResource = Res.string.user_register_conf_password_matching_error
            )
        }

        return ValidationResult(successful = true)
    }
}