package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R

class FightFragment : Fragment(), ShakeEventManager.ShakeListener {

	private val shakeEventManager = ShakeEventManager()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		shakeEventManager.setListener(this)
		shakeEventManager.init(context!!)
		return inflater.inflate(R.layout.fragment_fight, container, false)
	}

	override fun onShake() {
		Toast.makeText(activity, "Device Shaked", Toast.LENGTH_LONG).show()
	}

	override fun onResume() {
		super.onResume()
		shakeEventManager.register()
	}

	override fun onPause() {
		super.onPause()
		shakeEventManager.deregister()
	}

	companion object {
		@JvmStatic
		fun newInstance() = FightFragment()
	}

}
