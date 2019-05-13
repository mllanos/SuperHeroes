package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import ar.edu.utn.frba.mobile.a2019c1.superheroes.MainActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.DefaultRetryPolicy
import org.json.JSONObject

import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val login = findViewById<Button>(R.id.login)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString()
            )
        }

        login.setOnClickListener {
			val progressbar = this.progressBar3
			val user = username.text.toString()
			val queue = Volley.newRequestQueue(this)
			val url = "http://localhost:8080/superheroes/users"
			val params = HashMap<String,String>()
			params["nickname"] = user
			val jsonObject = JSONObject(params)

			val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
				Response.Listener { response ->
					val strResp = response.toString()
					val jsonObj = JSONObject(strResp)
					val id = jsonObj.get("id").toString()
					val intent = Intent(this@LoginActivity, MainActivity::class.java)
					Toast.makeText(this, "Logged in successfully. User: $user, id: $id.", Toast.LENGTH_LONG).show()
					intent.putExtra("username", user)
					intent.putExtra("id", id)
					startActivity(intent)
					progressbar.visibility = View.GONE
					window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
				}, Response.ErrorListener{ error ->
					Toast.makeText(this, "Error fetching resource: $error", Toast.LENGTH_LONG).show()
					progressbar.visibility = View.INVISIBLE
					window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
				})

			progressbar.visibility = View.VISIBLE
			window.setFlags(
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
			queue.add(request)

			// Volley request policy, only one time request to avoid duplicate transaction
			request.retryPolicy = DefaultRetryPolicy(
				DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
				// 0 means no retry
				0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
				1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
			)

		}
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
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
