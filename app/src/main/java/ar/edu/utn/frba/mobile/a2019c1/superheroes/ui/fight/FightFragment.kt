package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.fight


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast

import ar.edu.utn.frba.mobile.a2019c1.superheroes.R






class FightFragment : Fragment(), ShakeEventManager.ShakeListener {

	private val sd: ShakeEventManager = ShakeEventManager()

	override fun onCreateView(

		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		sd.setListener(this)
		sd.init(context!!)
		return inflater.inflate(R.layout.fragment_fight, container, false)
	}

	companion object {
		fun newInstance(): FightFragment {
			val fragment = FightFragment()
			return fragment
		}
	}

	override fun onShake(){
		Toast.makeText(activity,"Device Shaked",Toast.LENGTH_LONG).show()
	}

	override fun onResume(){
		super.onResume()
		sd.register()
	}

	override fun onPause(){
		super.onPause()
		sd.deregister()
	}

}
