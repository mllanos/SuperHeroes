package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R

class FightFragment : Fragment(), ShakeEventManager.ShakeListener {

	private val shakeEventManager = ShakeEventManager()
	private var falsePositiveShake = true
	private var shakeCount = 0

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		shakeEventManager.setListener(this)
		shakeEventManager.init(context!!)
		return inflater.inflate(R.layout.fragment_fight, container, false)
	}

	override fun onShake() {

		if(falsePositiveShake) //the first two shakes are false positive
		{
			shakeCount++
			if(shakeCount==2)
				falsePositiveShake = false
		}
		else{
			//Toast.makeText(activity, "Device Shaked", Toast.LENGTH_LONG).show()
			val intent = Intent(activity, FightSearchActivity::class.java)
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

}
