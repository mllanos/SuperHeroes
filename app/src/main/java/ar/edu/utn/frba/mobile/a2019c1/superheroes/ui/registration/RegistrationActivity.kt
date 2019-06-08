package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ar.edu.utn.frba.mobile.a2019c1.superheroes.MainActivity
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {

	private val apiService by lazy { ApiService(this) }

	private val sessionService by lazy { SessionsService(this) }

	private lateinit var registrationViewModel: RegistrationViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_registration)
	}

	override fun onResume() {
		super.onResume()
		createUser()
	}

	private fun createUser() {
		if (sessionService.isUserLoggedIn()) {
			val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
			startActivity(intent)
			setResult(Activity.RESULT_OK)
			finish()
		}

		val nicknameText = findViewById<EditText>(R.id.et_nickname)
		val registrationButton = findViewById<Button>(R.id.b_registration)

		registrationViewModel = ViewModelProviders
			.of(this)
			.get(RegistrationViewModel::class.java)

		registrationViewModel.registrationFormState.observe(this@RegistrationActivity, Observer {
			val registrationState = it ?: return@Observer
			if (registrationState.nicknameError != null) {
				nicknameText.error = getString(registrationState.nicknameError)
			}
		})

		registrationViewModel.registrationResult.observe(this@RegistrationActivity, Observer {
			val registrationResult = it ?: return@Observer
			if (registrationResult.error != null) {
				showRegistrationFailed(registrationResult.error)
			}
			setResult(Activity.RESULT_OK)
			finish()
		})

		registrationButton.setOnClickListener {
			if (registrationViewModel.validateForm(nicknameText.text.toString())) {
				val spinner = this.registration_spinner.apply { visibility = VISIBLE }
				window.setFlags(
					WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
					WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				)
				val nickname = nicknameText.text.toString().trim()
				apiService.createUser(nickname,
					{ userCreated ->
						val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
						sessionService.createSession(userCreated)
						sessionService.storeTimer(Date().time)
						startActivity(intent)
						spinner.visibility = GONE
						window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
						registrationViewModel.successfulRegistration(nickname)
					}, { error ->
						println("Failed to create user - error: ${error.message}")
						spinner.visibility = GONE
						window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
						registrationViewModel.failedRegistration()
					})
			}
		}
	}

	private fun showRegistrationFailed(@StringRes errorString: Int) =
		Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()

}
