package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TableRow.LayoutParams
import android.widget.TableRow.LayoutParams.MATCH_PARENT
import android.widget.TableRow.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity
import kotlinx.android.synthetic.main.fragment_cards.*


class CardsFragment : Fragment() {

	private val apiService by lazy { ApiService(context!!) }
	private val sessionService by lazy { SessionsService(context!!) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
		inflater.inflate(R.layout.fragment_cards, container, false)

	override fun onResume() {
		super.onResume()
		getUserAvailableCards()
		getUserTeam()
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

	private fun getUserTeam() {
		val table = view!!.findViewById<TableLayout>(R.id.table_user_team)
		val row = TableRow(context)
		val rowLayoutParams = LayoutParams(MATCH_PARENT, 0, 1f)
		row.layoutParams = rowLayoutParams
		for (i in 0..3) {
			val tvSuperhero = TextView(context)
			tvSuperhero.layoutParams = LayoutParams(0, WRAP_CONTENT, 0.25f)
			tvSuperhero.gravity = CENTER
			tvSuperhero.text = "superhero $i"
			row.addView(tvSuperhero)
		}
		table.addView(row, 0)
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
