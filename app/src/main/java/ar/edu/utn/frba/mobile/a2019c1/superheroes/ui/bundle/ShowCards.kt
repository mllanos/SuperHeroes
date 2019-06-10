package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.bundle

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity

class ShowCards : AppCompatActivity() {

	private val apiService by lazy { ApiService(this) }
	private val sessionService by lazy { SessionsService(this) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_show_cards)
		val recyclerView = findViewById<RecyclerView>(R.id.bundle_recycler_view)

		sessionService.getLoggedUser()?.let { loggedUser ->
			apiService.openBundle(loggedUser, { cards ->
				recyclerView.adapter = CardsAdapter(cards, this)
				recyclerView.visibility = View.VISIBLE
			}, { error ->
				Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
			})
		} ?: handleUserNotLogged()
	}

	private fun handleUserNotLogged() {
		println("Failed to get logged user in bundle fragment")
		val intent = Intent(this, RegistrationActivity::class.java)
		startActivity(intent)
		this.setResult(Activity.RESULT_OK)
		this.finish()
	}
}
