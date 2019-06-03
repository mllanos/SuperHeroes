package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.widget.GridView
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity
import kotlinx.android.synthetic.main.fragment_cards.*
import android.view.*


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
		val spinner = cards_spinner.apply { visibility = View.VISIBLE }
		sessionService.getLoggedUser()?.let { loggedUser ->
			apiService.getUserAvailableCards(loggedUser, { cards ->
				val gridView = view!!.findViewById(R.id.cards_grid_view) as GridView
				val cardsAdapter = CardsAdapter(cards, context!!)
				gridView.adapter = cardsAdapter
				spinner.visibility = GONE
			}, { error ->
				println("Failed to get user cards - error: $error")
				spinner.visibility = INVISIBLE
			})
		} ?: handleUserNotLogged()
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
