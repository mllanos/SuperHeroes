package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import kotlinx.android.synthetic.main.fragment_fight.*
import kotlinx.android.synthetic.main.fragment_fight.view.*

class FightFragment : Fragment(){

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

		val view= inflater.inflate(R.layout.fragment_fight, container, false)

		view.btn_fight.setOnClickListener {

			this.searchFight()
		}

		return view
	}

	companion object {
		@JvmStatic
		fun newInstance() = FightFragment()
	}

	private fun searchFight(){
		val intent = Intent(context!!, FightSearchActivity::class.java)
		startActivity(intent)
	}

}
