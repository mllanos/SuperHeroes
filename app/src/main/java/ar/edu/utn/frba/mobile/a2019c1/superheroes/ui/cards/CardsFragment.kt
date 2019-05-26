package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R.id.cards_grid_view
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService

class CardsFragment : Fragment() {

	private val apiService by lazy { ApiService(context!!) }

	private val sessionService by lazy { SessionsService(context!!) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
		inflater.inflate(R.layout.fragment_cards, container, false)

	override fun onResume() {
		super.onResume()
		getUserAvailableCards()
	}

	private fun getUserAvailableCards() {
		sessionService.getLoggedUser()?.let { loggedUser ->
			apiService.getUserAvailableCards(loggedUser, { cards ->
				val gridView = view!!.findViewById(cards_grid_view) as GridView
				val cardsAdapter = CardsAdapter(cards, context!!)
				gridView.adapter = cardsAdapter
			}, { error ->
				println("Failed to get user cards - error: $error")
			})
		} ?: println("Failed to get logged user")

	}

	companion object {
		@JvmStatic
		fun newInstance() = CardsFragment()
	}

}
