package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R

class RegistrationViewModel : ViewModel() {

	private val _registrationForm = MutableLiveData<RegistrationFormState>()
	val registrationFormState: LiveData<RegistrationFormState> = _registrationForm

	private val _registrationResult = MutableLiveData<RegistrationResult>()
	val registrationResult: LiveData<RegistrationResult> = _registrationResult

	fun successfulRegistration(nickname: String) {
		_registrationResult.value = RegistrationResult(displayNickname = nickname)
	}

	fun failedRegistration() {
		_registrationResult.value = RegistrationResult(error = R.string.registration_failed)
	}

	fun registrationDataChanged(username: String) {
		if (!isNicknameValid(username)) {
			_registrationForm.value = RegistrationFormState(nicknameError = R.string.invalid_nickname)
		} else {
			_registrationForm.value = RegistrationFormState(isDataValid = true)
		}
	}

	private fun isNicknameValid(nickname: String) = nickname.length >= 5

}

data class RegistrationFormState(val nicknameError: Int? = null, val isDataValid: Boolean = false)

data class RegistrationResult(val displayNickname: String? = null, val error: Int? = null)
