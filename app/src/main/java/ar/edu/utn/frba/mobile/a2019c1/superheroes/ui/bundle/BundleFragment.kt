package ar.edu.utn.frba.mobile.a2019c1.superheroes.ui.bundle

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ar.edu.utn.frba.mobile.a2019c1.superheroes.R
import ar.edu.utn.frba.mobile.a2019c1.superheroes.services.SessionsService
import kotlinx.android.synthetic.main.fragment_bundle.view.*
import java.util.*

private const val TIMER_LENGTH = 30000L
private const val COUNTDOWN_INTERVAL = 1000L

class BundleFragment : Fragment() {

	private val sessionService by lazy { SessionsService(context!!) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_bundle, container, false)
		val button = view!!.btn_get_card
		val now = Date().time
		val currTimer = sessionService.getCurrentTimer()
		val remainingTime = currTimer - now
		val buttonText = context!!.getString(R.string.get_card)

		button.setOnClickListener {
			sessionService.storeTimer(Date().time + TIMER_LENGTH)
			this.getCards()
			startCountdown(TIMER_LENGTH, button, buttonText)
		}

		if (remainingTime > 0) {
			button.isEnabled = false
			startCountdown(remainingTime, button, buttonText)
		}

		return view
	}

	override fun onResume() {
		super.onResume()
	}

	private fun startCountdown(length: Long, button: Button, onFinishText: String) {
		button.isEnabled = false
		object : CountDownTimer(length, COUNTDOWN_INTERVAL) {
			override fun onTick(millisUntilFinished: Long) {
				button.text = "${millisUntilFinished / COUNTDOWN_INTERVAL}"
			}

			override fun onFinish() {
				button.isEnabled = true
				button.text = onFinishText
			}
		}.start()
	}

	private fun getCards() {
		val intent = Intent(context!!, ShowCards::class.java)
		startActivity(intent)
	}

	companion object {
		@JvmStatic
		fun newInstance() = BundleFragment()
	}

}
