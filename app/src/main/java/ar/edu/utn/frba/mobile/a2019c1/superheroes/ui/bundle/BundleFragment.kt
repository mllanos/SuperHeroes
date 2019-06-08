package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.bundle


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.adapters.CardsAdapter
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.ApiService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.registration.RegistrationActivity
import kotlinx.android.synthetic.main.fragment_bundle.view.*
import android.os.CountDownTimer
import java.util.*


class BundleFragment : Fragment() {

	private val apiService by lazy { ApiService(context!!) }
	private val sessionService by lazy { SessionsService(context!!) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_bundle, container, false)
		val button = view.btn_get_card
		button.setOnClickListener {
			sessionService.storeTimer(Date().time + 30000)
			this.getCards()
			button.isEnabled = false
			object : CountDownTimer(30000, 1000) {
				override fun onTick(millisUntilFinished: Long) {
					button.text = "seconds remaining: " + millisUntilFinished / 1000
				}

				override fun onFinish() {
					button.isEnabled = true
					button.text = "CLICK ME"
				}
			}.start()
		}
		return view
	}

	override fun onResume() {
		super.onResume()
		val button = view!!.btn_get_card
		val now = Date().time
		val currTimer = sessionService.getCurrentTimer()
		val remainingTime = currTimer - now
		if (remainingTime > 0) {
			button.isEnabled = false
			object : CountDownTimer(remainingTime, 1000) {
				override fun onTick(millisUntilFinished: Long) {
					button.text = "seconds remaining: " + (millisUntilFinished / 1000)
				}

				override fun onFinish() {
					button.isEnabled = true
					button.text = "CLICK ME"
				}
			}.start()
		}
	}

	private fun getCards() {
		sessionService.getLoggedUser()?.let { loggedUser ->
			apiService.openBundle(loggedUser, { cards ->
				val gridView = view!!.findViewById(R.id.bundle_grid_view) as GridView
				gridView.adapter = CardsAdapter(cards, context!!)
				gridView.visibility = View.VISIBLE
			}, { error ->
				Toast.makeText(context!!, error.toString(), Toast.LENGTH_SHORT).show()
			})
		} ?: handleUserNotLogged()
	}

	private fun handleUserNotLogged() {
		println("Failed to get logged user in bundle fragment")
		val intent = Intent(activity, RegistrationActivity::class.java)
		startActivity(intent)
		activity!!.setResult(Activity.RESULT_OK)
		activity!!.finish()
	}

	companion object {
		@JvmStatic
		fun newInstance() = BundleFragment()
	}

}
