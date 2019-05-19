package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ar.edu.utn.frba.mobile.a2019c1.superheroes.R

class CardsFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_cards, container, false)
	}

	companion object {
		fun newInstance(): CardsFragment {
			val fragment = CardsFragment()
			return fragment
		}
	}

}
