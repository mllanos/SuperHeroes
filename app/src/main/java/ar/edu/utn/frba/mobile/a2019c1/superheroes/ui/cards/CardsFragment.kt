package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.UserTeamAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Card
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.fragment_cards.*
import kotlinx.android.synthetic.main.linearlayout_card.view.*
import kotlin.text.Charsets.UTF_8


class CardsFragment : Fragment() {

	private val apiService by lazy { ApiService(context!!) }
	private val sessionService by lazy { SessionsService(context!!) }
	private var cardsAdapter = CardsAdapter(
		{ card -> onCardClick(card) },
		{ card, view -> onLongCardClick(card, view) })
	private var userTeamAdapter = UserTeamAdapter()
	private var selectedCards = mutableListOf<Card>()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
		inflater.inflate(R.layout.fragment_cards, container, false)

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		getUserAvailableCards()
		getUserTeam()
		onCreateTeamButtonClick()
		handleElementsVisibility()
		refreshCardsSelection()
	}

	private fun handleElementsVisibility() {
		if (!cardsAdapter.hasCards()) {
			linearlayout_cards_nocards.visibility = VISIBLE
		} else {
			btn_cards_createteam.visibility = VISIBLE
		}
	}

	private fun getUserAvailableCards() {
		recyclerview_cards_available.layoutManager = GridLayoutManager(context, 3)
		recyclerview_cards_available.itemAnimator = DefaultItemAnimator()
		recyclerview_cards_available.adapter = cardsAdapter
		val spinner = progressbar_cards_spinner.apply { visibility = View.VISIBLE }
		sessionService.getLoggedUser()?.let { loggedUser ->
			apiService.getUserAvailableCards(loggedUser, { cards ->
				cardsAdapter.replaceItems(cards)
				spinner.visibility = GONE
				if (cardsAdapter.hasCards()) {
					linearlayout_cards_nocards.visibility = GONE
					btn_cards_createteam.visibility = VISIBLE
				}
			}, { error ->
				gerErrorMessage("Failed to get user cards", error)
				spinner.visibility = GONE
			})
		} ?: handleUserNotLogged()
	}

	private fun getUserTeam() {
		recyclerview_cards_userteam.layoutManager =
			object : GridLayoutManager(context, 4, RecyclerView.VERTICAL, false) {
				override fun canScrollVertically() = false
			}
		recyclerview_cards_userteam.adapter = userTeamAdapter
		sessionService.getLoggedUserTeamId()?.also { id ->
			val spinner = progressbar_cards_spinner.apply { visibility = View.VISIBLE }
			apiService.getTeam(id, { team ->
				userTeamAdapter.addTeamCards(team.superheroes)
				spinner.visibility = GONE
			}, { error ->
				gerErrorMessage("Failed to get user team", error)
				spinner.visibility = GONE
			})
		} ?: userTeamAdapter.setPlaceHolders()
	}

	private fun onLongCardClick(card: Card, view: View): Boolean {
		selectedCards.find { it.id == card.id }?.also {
			selectedCards.remove(card)
			view.view_card_selectedoverlay.visibility = INVISIBLE
		} ?: run {
			if (selectedCards.size < 4) {
				selectedCards.add(card)
				view.view_card_selectedoverlay.visibility = VISIBLE
			}
		}
		if (selectedCards.size <= 4) {
			btn_cards_createteam.text = getString(R.string.btn_create_team_counter, selectedCards.size)
		}
		return true
	}

	private fun onCardClick(card: Card) {
		val intent = Intent(context!!, ShowSingleCard::class.java).apply {
			putExtra("name", card.name)
			putExtra("description", card.description)
			putExtra("id", card.id)
			putExtra("power", card.power.toString())
			putExtra("thumbnail", card.thumbnail)
		}
		startActivity(intent)
	}

	private fun onCreateTeamButtonClick() {
		btn_cards_createteam.setOnClickListener {
			if (selectedCards.size == 4) {
				val spinner = progressbar_cards_spinner.apply { visibility = View.VISIBLE }
				val user = sessionService.getLoggedUser()!!
				apiService.createTeam(user, selectedCards, { teamId ->
					sessionService.storeLoggedUserTeamId(teamId)
					userTeamAdapter.addTeamCards(selectedCards.toList())
					resetTeamCreationButton(btn_cards_createteam)
					spinner.visibility = GONE
				}, { error ->
					gerErrorMessage("Failed to create team", error)
					spinner.visibility = GONE
				})
			} else {
				Toast.makeText(context, "You must select 4 cards", Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun resetTeamCreationButton(button: Button) {
		for (i in 0 until recyclerview_cards_available.childCount) {
			val holder = recyclerview_cards_available
				.getChildViewHolder(recyclerview_cards_available.getChildAt(i))
			holder.itemView.view_card_selectedoverlay.visibility = INVISIBLE
		}
		selectedCards.clear()
		button.text = getString(R.string.btn_create_team)
	}

	private fun refreshCardsSelection() = selectedCards.clear()

	private fun handleUserNotLogged() {
		println("Failed to get logged user in cards fragment")
		val intent = Intent(activity, RegistrationActivity::class.java)
		startActivity(intent)
		activity!!.setResult(Activity.RESULT_OK)
		activity!!.finish()
	}

	private fun gerErrorMessage(message: String, error: VolleyError) {
		val statusCode = error.networkResponse.statusCode
		val data = String(error.networkResponse.data, UTF_8)
		println("$message - statusCode: $statusCode - data: $data")
	}

	companion object {
		@JvmStatic
		fun newInstance() = CardsFragment()
	}

}
