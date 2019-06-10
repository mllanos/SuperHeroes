package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.bundle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity
import kotlinx.android.synthetic.main.activity_show_cards.*

class ShowCards : AppCompatActivity() {

	private val apiService by lazy { ApiService(this) }
	private val sessionService by lazy { SessionsService(this) }
	private lateinit var cardsAdapter: CardsAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_show_cards)
		initializeRecyclerView()
		showCards()
		buttonClose()
	}

	private fun buttonClose() {
		val buttonClose = findViewById<Button>(R.id.btn_close_show_cards)
		buttonClose.setOnClickListener{
			finish()
		}
	}

	private fun initializeRecyclerView() {
		cardsAdapter = CardsAdapter()
		rvBundle.layoutManager = GridLayoutManager(this, 3)
		rvBundle.adapter = cardsAdapter
	}

	private fun showCards() {
		sessionService.getLoggedUser()?.let { loggedUser ->
			apiService.openBundle(loggedUser, { cards ->
				cardsAdapter.replaceItems(cards)
				rvBundle.adapter = cardsAdapter
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
