package app.domain.model

import org.jetbrains.compose.resources.StringResource

data class ValidationResult(
    val successful: Boolean,
    val errorMessageResource: StringResource? = null
)
