package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val isDataValid: Boolean = false
)
