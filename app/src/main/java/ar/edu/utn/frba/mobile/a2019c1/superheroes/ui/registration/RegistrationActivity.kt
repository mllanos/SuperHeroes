package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.User
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

	private val apiService by lazy { ApiService(this) }

	private lateinit var registrationViewModel: RegistrationViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_registration)
		val nicknameText = findViewById<EditText>(R.id.nickname)
		val registrationButton = findViewById<Button>(R.id.registration)

		registrationViewModel = ViewModelProviders
			.of(this)
			.get(RegistrationViewModel::class.java)

		registrationViewModel.registrationFormState.observe(this@RegistrationActivity, Observer {
			val registrationState = it ?: return@Observer

			// disable registration button unless both username is valid
			registrationButton.isEnabled = registrationState.isDataValid

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

		nicknameText.afterTextChanged { registrationViewModel.registrationDataChanged(nicknameText.text.toString()) }

		registrationButton.setOnClickListener {
			val progressbar = this.progressBar3.apply { visibility = View.VISIBLE }
			window.setFlags(
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
			)
			lateinit var user: User
			val nickname = nicknameText.text.toString()
			apiService.createUser(nickname,
				{ userCreated ->
					user = userCreated
					val intent = Intent(this@RegistrationActivity, MainActivity::class.java).apply {
						putExtra("user", user)
					}
					startActivity(intent)
					progressbar.visibility = View.GONE
					window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
					registrationViewModel.successfulRegistration(nickname)
				}, { error ->
					println("Failed to create user - error: ${error.message}")
					progressbar.visibility = View.INVISIBLE
					window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
					registrationViewModel.failedRegistration()
				})
		}
	}

	private fun showRegistrationFailed(@StringRes errorString: Int) =
		Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
	this.addTextChangedListener(object : TextWatcher {
		override fun afterTextChanged(editable: Editable?) {
			afterTextChanged.invoke(editable.toString())
		}

		override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

		override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
	})
}
