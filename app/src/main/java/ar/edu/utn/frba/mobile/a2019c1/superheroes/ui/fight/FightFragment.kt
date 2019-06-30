package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import kotlinx.android.synthetic.main.fragment_fight.view.*

class FightFragment : Fragment(), ShakeEventManager.ShakeListener {

	private val shakeEventManager = ShakeEventManager()
	private val handler = Handler()
	private var shakeEnabled = false

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_fight, container, false)
		view.btn_fight.setOnClickListener { searchFight() }
		return view
	}

	override fun onResume() {
		super.onResume()
		shakeEventManager.setListener(this)
		shakeEventManager.init(context!!)
		handler.postDelayed({
			shakeEnabled = true
		}, 500)
	}

	override fun onStop() {
		super.onStop()
		shakeEventManager.setListener(null)
	}

	private fun searchFight() {
		val intent = Intent(context!!, FightSearchActivity::class.java)
		startActivity(intent)
	}

	override fun onShake() {
		if (shakeEnabled) {
			shakeEnabled = false
			searchFight()
		}
	}

	companion object {
		@JvmStatic
		fun newInstance() = FightFragment()
	}
}
