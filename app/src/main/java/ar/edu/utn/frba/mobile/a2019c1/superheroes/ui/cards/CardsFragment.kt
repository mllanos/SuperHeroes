package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TableRow.LayoutParams
import android.widget.TableRow.LayoutParams.MATCH_PARENT
import android.widget.TableRow.LayoutParams.WRAP_CONTENT
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Card
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity
import kotlinx.android.synthetic.main.fragment_cards.*
import androidx.recyclerview.widget.LinearLayoutManager as LinearLayoutManager1


class CardsFragment : Fragment() {

	private val apiService by lazy { ApiService(context!!) }
	private val sessionService by lazy { SessionsService(context!!) }
	private var cardsSelectedCounter = 0
	private lateinit var cardsAdapter: CardsAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
		inflater.inflate(R.layout.fragment_cards, container, false)

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		getUserAvailableCards()
		getUserTeam()
		onCreateTeamButtonClick()
	}

	private fun getUserAvailableCards() {
		cardsAdapter = CardsAdapter({ card -> onCardClick(card) }, { card -> onLongCardClick(card) })
		recyclerview_cards_available.layoutManager = GridLayoutManager(context, 3)
		recyclerview_cards_available.adapter = cardsAdapter
		val spinner = progressbar_cards_spinner.apply { visibility = View.VISIBLE }
		sessionService.getLoggedUser()?.let { loggedUser ->
			apiService.getUserAvailableCards(loggedUser, { cards ->
				cardsAdapter.replaceItems(cards)
				recyclerview_cards_available.adapter = cardsAdapter
				spinner.visibility = GONE
			}, { error ->
				println("Failed to get user cards - error: $error")
				spinner.visibility = GONE
			})
		} ?: handleUserNotLogged()
	}

	private fun getUserTeam() {
		val row = TableRow(context)
		val rowLayoutParams = LayoutParams(MATCH_PARENT, 0, 1f)
		row.layoutParams = rowLayoutParams
		for (i in 0..3) {
			val ivSuperhero = ImageView(context)
			ivSuperhero.layoutParams = LayoutParams(0, WRAP_CONTENT, 0.25f)
			ivSuperhero.setImageResource(R.drawable.ic_launcher_foreground)
			row.addView(ivSuperhero)
		}
		tablelayout_cards_userteam.addView(row, 0)
	}

	private fun onLongCardClick(card: Card): Boolean {
		Toast.makeText(context, "Long clicked: ${card.name}", Toast.LENGTH_LONG).show()
		return true
	}

	private fun onCardClick(card: Card) {
		Toast.makeText(context, "Clicked: ${card.name}", Toast.LENGTH_LONG).show()
	}

	private fun onCreateTeamButtonClick() {
		btn_cards_createteam.let { button ->
			button.setOnClickListener {
				cardsSelectedCounter = 0
				button.text = getString(R.string.btn_create_team)
			}
		}
	}

	private fun handleUserNotLogged() {
		println("Failed to get logged user in cards fragment")
		val intent = Intent(activity, RegistrationActivity::class.java)
		startActivity(intent)
		activity!!.setResult(Activity.RESULT_OK)
		activity!!.finish()
	}

	companion object {
		@JvmStatic
		fun newInstance() = CardsFragment()
	}

}
