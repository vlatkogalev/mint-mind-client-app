package user.domain.usecase

import app.domain.model.ValidationResult
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.error_field_empty
import mintmind.shared.generated.resources.user_register_conf_email_matching_error


class ValidateConfirmEmailUseCase {
    operator fun invoke(confEmail: String, email: String): ValidationResult {
        if (confEmail.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessageResource = Res.string.error_field_empty
            )
        }

        if (confEmail != email) {
            return ValidationResult(
                successful = false,
                errorMessageResource = Res.string.user_register_conf_email_matching_error
            )
        }

        return ValidationResult(successful = true)
    }
}