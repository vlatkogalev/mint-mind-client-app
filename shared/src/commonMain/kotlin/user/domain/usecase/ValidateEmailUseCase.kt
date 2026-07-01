package user.domain.usecase

import app.domain.model.ValidationResult
import mintmind.shared.generated.resources.Res
import mintmind.shared.generated.resources.error_field_empty
import mintmind.shared.generated.resources.user_register_email_invalid_error

class ValidateEmailUseCase {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessageResource = Res.string.error_field_empty
            )
        }

        @Suppress("RegExpRedundantEscape", "RegExpDuplicateCharacterInClass")
        val emailAddressRegex = Regex(
            pattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        if (!email.matches(emailAddressRegex)) {
            return ValidationResult(
                successful = false,
                errorMessageResource = Res.string.user_register_email_invalid_error
            )
        }

        return ValidationResult(successful = true)
    }
}