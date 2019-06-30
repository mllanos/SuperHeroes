package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Fight
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Geolocation
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_fight_search.*
import java.lang.Thread.sleep

class FightSearchActivity : AppCompatActivity() {

	private val sessionService by lazy { SessionsService(this) }
	private val apiService by lazy { ApiService(this) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_fight_search)
		searchOpponent()
	}

	@SuppressLint("MissingPermission")
	private fun searchOpponent() {
		val geolocation = intent.getSerializableExtra("geolocation") as Geolocation
		sessionService.getLoggedUser()?.let { user ->
			apiService.startFight(user.id, geolocation, { response ->
				processResult(response)
				finish()
			}, { error ->
				gerErrorMessage("Failed to start fight", error)
				handleOpponentNotFound()
			})
		} ?: handleUserNotLogged()
	}

	private fun processResult(result: Fight) {
		val intent = Intent(this, FightResultActivity::class.java)
		intent.putExtra("fightResult", result)
		startActivity(intent)
	}

	private fun handleOpponentNotFound() {
		title_search_fight.text = getString(R.string.title_fight_notfound)
		loading_figth_icon.visibility = View.INVISIBLE
		sleep(5000)
		finish()
	}

	private fun handleUserNotLogged() {
		println("Failed to get logged user in cards fragment")
		val intent = Intent(this, RegistrationActivity::class.java)
		startActivity(intent)
		setResult(Activity.RESULT_OK)
		finish()
	}

	private fun gerErrorMessage(message: String, error: VolleyError) {
		val statusCode = error.networkResponse.statusCode
		val data = String(error.networkResponse.data, Charsets.UTF_8)
		println("$message - statusCode: $statusCode - data: $data")
	}

}
