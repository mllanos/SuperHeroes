package ar.edu.utn.frba.mobile.a2019c1.superheroes

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class CardsFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
		inflater.inflate(R.layout.fragment_cards, container, false)

	companion object {
		fun newInstance(): CardsFragment = CardsFragment()
	}
}
