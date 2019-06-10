package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.cards.CardsFragment

class FightSearchFragment : Fragment(){

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
		inflater.inflate(R.layout.fragment_fight_search, container, false)



	companion object {
		@JvmStatic
		fun newInstance() = FightSearchFragment()
	}
}

