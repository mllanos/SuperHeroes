package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R

class FightFragment : Fragment(), ShakeEventManager.ShakeListener {

	private val shakeEventManager = ShakeEventManager()
	private var shakeEnabled = false

	val handler = Handler()


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		shakeEventManager.setListener(this)
		shakeEventManager.init(context!!)
		handler.postDelayed({ enableShake() }, 500)
		return inflater.inflate(R.layout.fragment_fight, container, false)
	}

	override fun onShake() {


		if(shakeEnabled){
			shakeEnabled = false
			handler.postDelayed({ enableShake() }, 2000) //delay to prevent multiple shakes
			val intent = Intent(activity, FightSearchActivity::class.java)
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
			activity?.startActivity(intent)
		}

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

	fun enableShake(){
		shakeEnabled = true
	}

}
