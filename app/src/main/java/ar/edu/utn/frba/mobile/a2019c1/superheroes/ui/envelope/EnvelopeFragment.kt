package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.envelope


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import kotlinx.android.synthetic.main.fragment_envelope.view.*

class EnvelopeFragment : Fragment() {

	private val apiService by lazy { ApiService(context!!) }
	private val sessionService by lazy { SessionsService(context!!) }

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view: View = inflater.inflate(R.layout.fragment_envelope, container, false)

		view.btn_get_card.setOnClickListener {
			this.getCards()
		}
		return view
	}

	private fun getCards(){
		apiService.openBundle(sessionService.getLoggedUser()!!,
				{ cards ->
					val gridView = view!!.findViewById(R.id.cards_grid_view_2) as GridView
					gridView.adapter = CardsAdapter(cards, context!!)
					gridView.visibility = View.VISIBLE
				}, { error ->
					Toast.makeText(context!!, error.toString(), Toast.LENGTH_SHORT).show()
				}
		)
	}

	companion object {
		fun newInstance(): EnvelopeFragment {
			val fragment = EnvelopeFragment()
			return fragment
		}
	}

}
